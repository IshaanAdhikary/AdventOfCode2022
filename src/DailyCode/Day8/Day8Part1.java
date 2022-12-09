package DailyCode.Day8;

import java.io.*;
import java.util.Scanner;

public class Day8Part1 {
    public static Integer amountOfVisibleTrees() throws FileNotFoundException {
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

        // Find the amount of trees visible using array
        int amount = 0;
        for (int vertical = 0; vertical < gridSize; vertical++) {
            for (int horizontal = 0; horizontal < gridSize; horizontal++) {
                boolean[] isVisible = new boolean[4];
                for (int i = 0; i < 4; i++) isVisible[i] = true;
                for (int i = 0; i < gridSize; i++) {
                    // Check up and down for any cell greater or equal to current tree height
                    if (i < horizontal && tree2DArray[vertical][i] >= tree2DArray[vertical][horizontal]) isVisible[0] = false; 
                    if (i > horizontal && tree2DArray[vertical][i] >= tree2DArray[vertical][horizontal]) isVisible[1] = false; 
                    // Check left and right for any cell greater or equal to current tree height
                    if (i < vertical && tree2DArray[i][horizontal] >= tree2DArray[vertical][horizontal]) isVisible[2] = false; 
                    if (i > vertical && tree2DArray[i][horizontal] >= tree2DArray[vertical][horizontal]) isVisible[3] = false; 
                }
                // If tree is visible from any direction, the entire tree is visible, and adds one more to the total.
                if (isTreeVisible(isVisible)) amount++;
            }
        }
        
        return amount;
    }

    private static boolean isTreeVisible(boolean[] isVisibleArray) {
        for (int i = 0; i < isVisibleArray.length; i++) {
            if (isVisibleArray[i]) return true;
        }
        return false;
    }
}
