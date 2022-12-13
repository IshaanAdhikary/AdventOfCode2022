package DailyCode.Day12;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Day12Part1 {
    private static int[] startPos = new int[2];
    private static int[] endPos = new int[2];

    // Make the hills into a maze by finding which squares can go to which squares, then doing a breadth-first step-numbered flood fill until the end is reached.
    public static int findMinSteps() throws FileNotFoundException {
        char[][] maze = createMaze();
        Integer[][] mazeSolved = solveMaze(maze);

        return mazeSolved[endPos[0]][endPos[1]];
    }

    private static Integer[][] solveMaze(char[][] maze) {
        Integer[][] mazeSolved = new Integer[maze.length][maze[0].length];
        Queue<int[]> pairsToTravel = new LinkedList<int[]>();
        pairsToTravel.add(new int[] {startPos[0], startPos[1]});

        int step = 0;
        while (mazeSolved[endPos[0]][endPos[1]] == null) {
            Queue<int[]> nextPairsToTravel = new LinkedList<int[]>();
            while (!pairsToTravel.isEmpty()) {
                int[] pair = pairsToTravel.poll();
                takeStepInMaze(maze, mazeSolved, nextPairsToTravel, pair[0], pair[1], step);
            }
            step++;
            pairsToTravel = nextPairsToTravel;
        }

        return mazeSolved;
    }

    private static void takeStepInMaze(char[][] maze, Integer[][] mazeSolution, Queue<int[]> nextPairs, int x, int y, int step) {
        // If already filled in, skip it
        if (mazeSolution[x][y] != null) return;

        mazeSolution[x][y] = step;
        // If not on top edge and cell above is either one greater than, equal to, or less, then add that above cell to the queue for the next step.
        // Repeat for all orthogonal cells (below, left, and right)
        if (x > 0 && maze[x - 1][y] <= maze[x][y] + 1) nextPairs.add(new int[] {x - 1, y});
        if (x < maze.length - 1 && maze[x + 1][y] <= maze[x][y] + 1) nextPairs.add(new int[] {x + 1, y});
        if (y > 0 && maze[x][y - 1] <= maze[x][y] + 1) nextPairs.add(new int[] {x, y - 1});
        if (y < maze[0].length - 1 && maze[x][y + 1] <= maze[x][y] + 1) nextPairs.add(new int[] {x, y + 1});
    }

    private static char[][] createMaze() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day12\\Day12Input.txt");
		Scanner in = new Scanner(file);

        ArrayList<String> lines = new ArrayList<String>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }

        char[][] maze = new char[lines.size()][lines.get(0).length()];
        for (int row = 0; row < maze.length; row++) {
            for (int column = 0; column < maze[0].length; column++) {
                char gridCharacter = lines.get(row).charAt(column);
                if (gridCharacter == 'S') {
                    startPos[0] = row;
                    startPos[1] = column;
                    maze[row][column] = 'a';
                }
                else if (gridCharacter == 'E') {
                    endPos[0] = row;
                    endPos[1] = column;
                    maze[row][column] = 'z';
                }
                else {
                    maze[row][column] = gridCharacter;
                }
            }
        }
        
        in.close();
        return maze;
    }
}
