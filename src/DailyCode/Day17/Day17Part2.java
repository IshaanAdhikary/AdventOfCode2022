package DailyCode.Day17;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Day17Part2 {
    private static final int towerWidth = 7;

    // Starting position of rocks when a new rock is spawned
    private static final int leftStartGap = 2;
    private static final int upStartGap = 3;

    private static final long rockAmountInFinalStack = 1000000000000L;

    private static boolean rockIsMoving = true;

    public static Long findTowerHeight() throws FileNotFoundException {
        ArrayList<Rock> rocks = initializeRocks();
        String jetstream = initializeJetstream();

        long finalHeight = 0;

        // Each array should always be towerWidth-wide
        ArrayList<boolean[]> tower = new ArrayList<boolean[]>();

        int moveCount = 0;
        for (long rockCount = 0; rockCount < rockAmountInFinalStack; rockCount++) {
            Rock rock = rocks.get((int) (rockCount % rocks.size()));
            int leftGap = leftStartGap;
            int height = -(upStartGap + 1);
            
            while (rockIsMoving) {
                leftGap = moveRockJetstream(leftGap, height, rock, tower, jetstream, moveCount);
                height = moveRockDown(leftGap, height, rock, tower);
                moveCount = (moveCount + 1) % jetstream.length();
            }
            rockIsMoving = true; // Set the global variable for the next rock

            // When rock is done moving, we have the height and leftPos of the bottom left corner of the rock as two 0-indexed values. Rocks will not intersect each other.

            // Set every position in tower where rock should be to true, representing a full block.
            for (int j = rock.height - height - 1; j > 0; j--) {
                tower.add(0, new boolean[towerWidth]);
                height++;
            }
            ArrayList<Integer> levelsToCheck = new ArrayList<>();
            for (int i = rock.height - 1; i >= 0; i--) {
                for (int j = rock.width - 1; j >= 0; j--) {
                    levelsToCheck.add(i - (rock.height - 1) + height);
                    if (rock.shape[i][j]) tower.get(levelsToCheck.get(levelsToCheck.size() - 1))[j + leftGap] = true;
                }
            }

            // If a floor of blocks is completed by the newly placed block, remove it and all lines below it.
            for (int i = levelsToCheck.size() - 1; i >= 0; i--) {
                newTowerReturnType newTower =  updateFinalHeight(tower, finalHeight, levelsToCheck.get(i));
                finalHeight = newTower.newFinalTowerHeight;
                if (newTower.hasChanged) break;
            }

        }
        return finalHeight + tower.size();
    }

    private static newTowerReturnType updateFinalHeight(ArrayList<boolean[]> tower, long finalHeight, int levelToCheck) {
        boolean layerIsFilled = true;
        for (int i = 0; i < towerWidth; i++) {
            layerIsFilled = layerIsFilled && tower.get(levelToCheck)[i];
        }
        if (layerIsFilled) {
            finalHeight = finalHeight + tower.size() - levelToCheck;
            for (int i = tower.size() - 1; i >= levelToCheck; i--) tower.remove(i);
        }
        return new newTowerReturnType(finalHeight, layerIsFilled);
    }

    // Returns the new (or old) height. If moving down intersects and returns old height, sets Boolean to continue moving rock in while loop as false.
    private static int moveRockDown(int distFromLeft, int heightFromTop, Rock rock, ArrayList<boolean[]> tower) {
        int potentialHeight = heightFromTop + 1;

        // If height (index of bottom from top) is not overlapping with tower, no more checks matter, just move.
        if (potentialHeight < 0) {
            return potentialHeight;
        }

        // If you hit the floor of the tower, stop and don't move
        if (potentialHeight >= tower.size()) {
            rockIsMoving = false;
            return heightFromTop;
        }

        // If any collision happens, don't move the rock down and stop moving this rock (and trigger next rock)
        for (int i = rock.height - 1; i >= 0; i--) {
            int towerHeightCheck = potentialHeight - (rock.height - 1) + i;
            if (towerHeightCheck < 0) break;
            if (towerHeightCheck >= tower.size()) continue;
            for (int j = 0; j < rock.width; j++) {
                if (rock.shape[i][j] && tower.get(towerHeightCheck)[j + distFromLeft]) {
                    rockIsMoving = false;
                    return heightFromTop;
                }
            }
        }

        // Otherwise, move it
        return potentialHeight;
    }

    // Returns the new (or old) left gap
    private static int moveRockJetstream(int distFromLeft, int heightFromTop, Rock rock, ArrayList<boolean[]> tower, String jetstream, int moveCount) {
        int moveAsValue = 0;
        switch (jetstream.charAt(moveCount)) {
            case '>': moveAsValue = 1;
                break;
            case '<': moveAsValue = -1;
                break;
        }

        // If you overflow off the edge of the tower, don't move.
        int potentialDistFromLeft = distFromLeft + moveAsValue;
        if (potentialDistFromLeft < 0) return distFromLeft;
        else if (potentialDistFromLeft > towerWidth - rock.width) return distFromLeft;

        // If height (index of bottom from top) is not overlapping with tower, no more checks matter.
        if (heightFromTop < 0) return potentialDistFromLeft;

        // If any collision happens, don't move the rock jetstream
        for (int i = rock.height - 1; i >= 0; i--) {
            int towerHeightCheck = heightFromTop - (rock.height - 1) + i;
            if (towerHeightCheck < 0) break;
            if (towerHeightCheck >= tower.size()) continue;
            for (int j = 0; j < rock.width; j++) {
                if (rock.shape[i][j] && tower.get(towerHeightCheck)[j + potentialDistFromLeft]) return distFromLeft;
            }
        }

        // Otherwise, move it
        return potentialDistFromLeft;
    }

    private static String initializeJetstream() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day17\\Day17Input.txt");
        Scanner in = new Scanner(file);
        String jetstream = in.nextLine();
        in.close();
        return jetstream;
    }

    private static ArrayList<Rock> initializeRocks() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day17\\RocksInput.txt");
        Scanner in = new Scanner(file);
        ArrayList<Rock> rocks = new ArrayList<Rock>();

        ArrayList<String> currentRock = new ArrayList<>();
        while (in.hasNextLine()) {
            String line = in.nextLine();

            if (line.equals("")) {
                rocks.add(new Rock(currentRock));
                currentRock = new ArrayList<>();
                continue;
            }

            currentRock.add(line);
        }
        rocks.add(new Rock(currentRock));

        in.close();
        return rocks;
    }

    // private static void printTowerDebug(ArrayList<boolean[]> tower) {
    //     System.out.println("-------");
    //     for (boolean[] towerRow : tower) {
    //         for (boolean block : towerRow) {
    //             System.out.print(block ? '#' : '.');
    //         }
    //         System.out.println();
    //     }
    // }
}

class newTowerReturnType {
    long newFinalTowerHeight;
    boolean hasChanged;

    newTowerReturnType(long newFinalTowerHeight, boolean hasChanged) {
        this.newFinalTowerHeight = newFinalTowerHeight;
        this.hasChanged = hasChanged;
    }
}

class Rock2 {
    int height;
    int width;
    boolean[][] shape;

    public Rock2(ArrayList<String> rawShape) {
        height = rawShape.size();
        // Every single String should be the same length
        width = rawShape.get(0).length();
        shape = new boolean[height][width];
        for (int i = rawShape.size() - 1; i >= 0; i--) {
            for (int j = 0; j < rawShape.get(i).length(); j++) {
                if (rawShape.get(i).charAt(j) == '#') {
                    shape[i][j] = true;
                }
                else {
                    shape[i][j] = false;
                }
            }
        }
    }
}