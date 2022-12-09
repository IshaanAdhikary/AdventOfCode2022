package DailyCode.Day8;

import java.io.*;
import java.util.Scanner;

public class Day8Part2 {
    public static Integer maxScenicScore() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day8\\Day8Input.txt");
		Scanner in = new Scanner(file);

        // Create 2D tree array using input
        String line = in.nextLine();
        int gridSize = line.length();
        int[][] tree2DArray = new int[gridSize][gridSize];
        for (int vertical = 0; vertical < gridSize; vertical++) {
            for (int horizontal = 0; horizontal < gridSize; horizontal++) {
                tree2DArray[vertical][horizontal] = (int) line.charAt(horizontal) - (int) '0';
            }
            if (in.hasNextLine()) line = in.nextLine();
        }
        in.close();

        // Find the maximum scenic score
        int maximum = 0;
        for (int vertical = 0; vertical < gridSize; vertical++) {
            for (int horizontal = 0; horizontal < gridSize; horizontal++) {
                int score = getScenicScore(vertical, horizontal, tree2DArray, gridSize);
                maximum = score > maximum ? score : maximum;
            }
        }
        
        return maximum;
    }

    private static Integer getScenicScore(int vertical, int horizontal, int[][] tree2DArray, int gridSize) {
        // Booleans and integers to represent when to exit the loop and amount of trees visible in each direction
        boolean[] shouldContinueDistance = new boolean[4];
        for (int i = 0; i < 4; i++) shouldContinueDistance[i] = true;
        int[] directionalDistance = new int[4];
        for (int i = 0; i < 4; i++) directionalDistance[i] = 0;

        // See how many trees it takes in each direction until view is blocked
        // End the loop if every tree search has been stopped
        boolean shouldContinue = true;
        for (int i = 1; shouldContinue; i++) {
            shouldContinue = false;
            for (int j = 0; j < 4; j++) {
                if (shouldContinueDistance[j]) {
                    shouldContinue = true;
                }
            }
            if (shouldContinueDistance[0]) {
                if (vertical - i < 0) shouldContinueDistance[0] = false;
                else if (tree2DArray[vertical - i][horizontal] >= tree2DArray[vertical][horizontal]) {
                    directionalDistance[0]++;
                    shouldContinueDistance[0] = false;
                }
                else directionalDistance[0]++;
            }
            if (shouldContinueDistance[1]) {
                if (vertical + i >= gridSize) shouldContinueDistance[1] = false;
                else if (tree2DArray[vertical + i][horizontal] >= tree2DArray[vertical][horizontal]) {
                    directionalDistance[1]++;
                    shouldContinueDistance[1] = false;
                }
                else directionalDistance[1]++;
            }
            if (shouldContinueDistance[2]) {
                if (horizontal - i < 0) shouldContinueDistance[2] = false;
                else if (tree2DArray[vertical][horizontal - i] >= tree2DArray[vertical][horizontal]) {
                    directionalDistance[2]++;
                    shouldContinueDistance[2] = false;
                }
                else directionalDistance[2]++;
            }
            if (shouldContinueDistance[3]) {
                if (horizontal + i >= gridSize) shouldContinueDistance[3] = false;
                else if (tree2DArray[vertical][horizontal + i] >= tree2DArray[vertical][horizontal]) {
                    directionalDistance[3]++;
                    shouldContinueDistance[3] = false;
                }
                else directionalDistance[3]++;
            }
        }
        
        // Calculate scenic score by multiplying distances
        int scenicScore = 1;
        for (int i = 0; i < 4; i++) {
            scenicScore *= directionalDistance[i];
        }

        return scenicScore;
    }
}
