package DailyCode.Day14;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;

public class Day14Part2 {
    private static int lowestWallPoint = 0;

    public static Integer findMaxSand() throws FileNotFoundException {
        HashSet<Cell2> filledCells = buildWalls();

        return fillSand(filledCells);
    }

    // Fills the hashset with sand, but also returns whichever number sand is the last to come to rest, indexed from one
    private static int fillSand(HashSet<Cell2> filledCells) {
        int amountOfSand = 0;
        while (!filledCells.contains(new Cell2(500, 0))) {
            addUnitOfSand(filledCells);
            amountOfSand++;
        }
        return amountOfSand;
    }

    // Returns true if the sand stops and false if it goes infinitely
    private static void addUnitOfSand(HashSet<Cell2> filledCells) {
        Cell2 sand = Cell2.spawnSand();
        sand.moveSandDown(filledCells, lowestWallPoint);
        filledCells.add(sand);
    }

    private static HashSet<Cell2> buildWalls() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day14\\Day14Input.txt");
        Scanner in = new Scanner(file);

        HashSet<Cell2> filledCells = new HashSet<Cell2>();

        while (in.hasNextLine()) {
            // Makes a string of just numbers: [0] = cell1x, [1] = cell1y, [2] = cell2x, etc.
            String[] cellNumbersAsStrings = in.nextLine().split(",|( -> )");
            int[] cellNumbers = new int[cellNumbersAsStrings.length];
            for (int i = 0; i < cellNumbersAsStrings.length; i++) cellNumbers[i] = Integer.parseInt(cellNumbersAsStrings[i]);

            // From each listed cell, draw a straight line to the next listed cell, except for the last cell (-2 represents, which has no next cell to draw to)
            // i is the index of the xpos of each cell, except for last
            for (int i = 0; i < cellNumbers.length - 2; i += 2) {
                // If either endpoint has the lowest y so far, it's the lowest elevation so far.
                lowestWallPoint = cellNumbers[i + 1] > lowestWallPoint ? cellNumbers[i + 1] : lowestWallPoint;
                lowestWallPoint = cellNumbers[i + 3] > lowestWallPoint ? cellNumbers[i + 3] : lowestWallPoint;

                // Draw line between other two points
                if (cellNumbers[i] == cellNumbers[i + 2]) {
                    // j is the y pos of cells to be added. Start at start point, move towards the new y pos adding each cell along the way
                    int direction = Integer.signum(cellNumbers[i + 3] - cellNumbers[i + 1]);
                    for (int j = cellNumbers[i + 1]; j != cellNumbers[i + 3] + direction; j += direction) {
                        filledCells.add(new Cell2(cellNumbers[i], j));
                    }
                }
                else if (cellNumbers[i + 1] == cellNumbers[i + 3]) {
                    // j is the x pos of cells to be added. Start at start point, move towards the new x pos adding each cell along the way
                    int direction = Integer.signum(cellNumbers[i + 2] - cellNumbers[i]);
                    for (int j = cellNumbers[i]; j != cellNumbers[i + 2] + direction; j += direction) {
                        filledCells.add(new Cell2(j, cellNumbers[i + 1]));
                    }
                }
                else {
                    in.close();
                    throw new IllegalArgumentException("No straight line can be drawn between those two points");
                }
            }
        }

        in.close();
        return filledCells;
    }
}

class Cell2 {
    private int x;
    private int y;

    public Cell2(int setX, int setY) {
        x = setX;
        y = setY;
    }

    // No more need for booleans
    public void moveSandDown(HashSet<Cell2> filledCells, int lowestWallPoint) {
        while (true) {
            // Cell2 will stop at floorHeight - 1 elevation, and since floorHeight is maxHeight + 2, we stop at maxHeight + 1 elevation.
            if (y >= lowestWallPoint + 1) return;
            else if (!filledCells.contains(new Cell2(x, y + 1))) {
                y++;
            }
            else if (!filledCells.contains(new Cell2(x - 1, y + 1))) {
                x--;
                y++;
            }
            else if (!filledCells.contains(new Cell2(x + 1, y + 1))) {
                x++;
                y++;
            }
            // All 3 slots below the cell are filled, it comes to a stop
            else {
                return;
            }
        }
    }
    
    public int getXPos() {
        return x;
    }
    
    public int getYPos() {
        return y;
    }

    public static Cell2 spawnSand() {
        final int sandStartX = 500, sandStartY = 0;
        return new Cell2(sandStartX, sandStartY);
    }
    
    @Override
    public boolean equals(Object originalObject) {
        if (originalObject == this) return true;
        if (!(originalObject instanceof Cell2)) return false;

        Cell2 object = (Cell2) originalObject;

        return x == object.x && y == object.y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;
        hashCode = prime * hashCode + x;
        hashCode = prime * hashCode + y;
        return hashCode;
    }
}