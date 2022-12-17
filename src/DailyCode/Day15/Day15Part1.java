package DailyCode.Day15;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;

public class Day15Part1 {
    private static final int row = 2000000;

    public static Integer findPositionsWithoutBeacon() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day15\\Day15Input.txt");
        Scanner in = new Scanner(file);

        ArrayList<Range> ranges = new ArrayList<Range>();
        HashSet<Integer> beaconsOnRow = new HashSet<Integer>();

        while (in.hasNextLine()) {
            String line = in.nextLine();
            // Array of: Sensor X, Sensor Y, Beacon X, Beacon Y
            int[] positions = inputToPositionArray(line);
            // If a beacon already lies in a position, 
            if (positions[3] == row) beaconsOnRow.add(positions[2]);
            int manhattanDistance = Math.abs(positions[0] - positions[2]) + Math.abs(positions[1] - positions[3]);
            // Horizontal distance at row - Freedom to move left and right once at row/remaining moves after neccesary vertical movement to get to row
            int horizontalDistance = manhattanDistance - Math.abs(positions[1] - row);
            if (horizontalDistance >= 0) {
                ranges.add(new Range(positions[0] - horizontalDistance, positions[0] + horizontalDistance));
            }
        }
        in.close();

        Integer totalImpossibleForBeacon = findTotalFromRanges(ranges) - beaconsOnRow.size();
        return totalImpossibleForBeacon;
    }

    private static Integer findTotalFromRanges(ArrayList<Range> ranges) {
        quickSort(ranges, 0, ranges.size() - 1);

        Stack<Range> finalRanges = new Stack<>();
        finalRanges.push(ranges.get(0));

        for (int i = 1; i < ranges.size(); i++) {
            Range recentRange = finalRanges.peek();

            if (recentRange.end < ranges.get(i).start) {
                finalRanges.push(ranges.get(i));
            }
            else if (recentRange.end < ranges.get(i).end) {
                recentRange.end = ranges.get(i).end;
                finalRanges.pop();
                finalRanges.push(recentRange);
            }
        }

        Integer total = 0;
        while (!finalRanges.isEmpty()) {
            Range range = finalRanges.pop();
            total += range.end - range.start + 1;
        }
        return total;
    }

    private static void quickSort(ArrayList<Range> ranges, int start, int end) {
        // Base case: start == end
        // if (start >= end) return;
        if (start < end) {
            int pivotIndex = seperateAtPivot(ranges, start, end);
            quickSort(ranges, start, pivotIndex - 1);
            quickSort(ranges, pivotIndex + 1, end);
        }
    }

    // Seperates at the pivot and returns the pivot index
    private static int seperateAtPivot(ArrayList<Range> ranges, int start, int end) {
        Range pivot = ranges.get(end);
        // Start off since we increment in loop
        int i = start - 1;

        for (int j = start; j < end; j++) {
            // If j comes before the pivot when ordered, increment i and swap items [i] and [j].
            if(ranges.get(j).start < pivot.start) {
                i++;
                Range temp = ranges.get(i);
                ranges.set(i, ranges.get(j));
                ranges.set(j, temp);
            }
        }
        i++;
        Range temp = ranges.get(i);
        ranges.set(i, ranges.get(end));
        ranges.set(end, temp);
        return i;
    }

    // Returns an array of: Sensor X, Sensor Y, Beacon X, Beacon Y
    private static int[] inputToPositionArray(String line) {
        int[] positions = new int[4];
        int currentPos = 0;
        String currentNumber = "";
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (Character.isDigit(c) || c == '-') {
                currentNumber += c;
            }
            else {
                if (currentNumber.equals("")) continue;
                positions[currentPos] = Integer.parseInt(currentNumber);
                currentPos++;
                currentNumber = "";
            }
        }
        positions[currentPos] = Integer.parseInt(currentNumber);
        return positions;
    }
}

// Inclusive
class Range {
    public int start;
    public int end;

    public Range(int startInput, int endInput) {
        start = startInput;
        end = endInput;
    }
}