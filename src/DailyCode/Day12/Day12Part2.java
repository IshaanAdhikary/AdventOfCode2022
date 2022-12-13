package DailyCode.Day12;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Day12Part2 {
    // Working backwards, the old endPos is the new startPos, and endPos is now only filled in for the return answer while solving.
    private static int[] startPos = new int[2];
    private static Integer[] endPos = new Integer[2];

    // Make the hills into a maze by finding which squares can go to which squares, then doing a breadth-first step-numbered flood fill until the end is reached.
    public static int findMinStepsTrail() throws FileNotFoundException {
        char[][] maze = createMaze();
        Integer[][] mazeSolved = solveMaze(maze);

        return mazeSolved[endPos[0]][endPos[1]];
    }

    private static Integer[][] solveMaze(char[][] maze) {
        Integer[][] mazeSolved = new Integer[maze.length][maze[0].length];
        Queue<int[]> pairsToTravel = new LinkedList<int[]>();
        pairsToTravel.add(new int[] {startPos[0], startPos[1]});

        int step = 0;
        while (endPos[0] == null && endPos[1] == null) {
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

    // Going backwards
    private static void takeStepInMaze(char[][] maze, Integer[][] mazeSolution, Queue<int[]> nextPairs, int x, int y, int step) {
        // If already filled in, skip it
        if (mazeSolution[x][y] != null) return;
        
        mazeSolution[x][y] = step;

        // If the maze in this position contains elevation 'a', then this step is the first to reach bottom elevation. If this path is reversed, it maintains the same length and meets all problem conditions. This pathLength of this position is the answers (even if multiple 'a's are hit at the same time), and we can save it as the endPos.
        if (maze[x][y] == 'a') {
            endPos[0] = x;
            endPos[1] = y;
            return;
        }

        // If not on top edge and (this time) cell above's elevation is either one lower, equal, or higher than this one, then add that above cell to the queue for the next step.
        // Repeat for all orthogonal cells (below, left, and right)
        if (x > 0 && maze[x - 1][y] >= maze[x][y] - 1) nextPairs.add(new int[] {x - 1, y});
        if (x < maze.length - 1 && maze[x + 1][y] >= maze[x][y] - 1) nextPairs.add(new int[] {x + 1, y});
        if (y > 0 && maze[x][y - 1] >= maze[x][y] - 1) nextPairs.add(new int[] {x, y - 1});
        if (y < maze[0].length - 1 && maze[x][y + 1] >= maze[x][y] - 1) nextPairs.add(new int[] {x, y + 1});
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
                    maze[row][column] = 'a';
                }
                else if (gridCharacter == 'E') {
                    startPos[0] = row;
                    startPos[1] = column;
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
