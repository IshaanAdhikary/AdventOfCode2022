package DailyCode.Day15;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Day15Part2 {
    private static final long searchLimitMin = 0;
    private static final long searchLimitMax = 4000000; // CHANGE
    private static final long multiplicandTuningFrequency = 4000000;

    public static Long findBeacon() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day15\\Day15Input.txt");
        Scanner in = new Scanner(file);

        ArrayList<Diamond> ranges = new ArrayList<Diamond>();

        while (in.hasNextLine()) {
            String line = in.nextLine();
            // Array of: Sensor X, Sensor Y, Beacon X, Beacon Y
            int[] positions = inputToPositionArray(line);
            int manhattanDistance = Math.abs(positions[0] - positions[2]) + Math.abs(positions[1] - positions[3]);
            ranges.add(new Diamond(manhattanDistance, positions[0], positions[1]));
        }
        in.close();

        Integer[] position = getPositionOfBeacon(ranges);
        if (position[0] == null) throw new IllegalArgumentException("No beacon source possible!");
        return position[0] * multiplicandTuningFrequency + position[1];
    }

    private static Integer[] getPositionOfBeacon(ArrayList<Diamond> ranges) {
        Integer[] position = new Integer[2];
        for (Diamond sensor : ranges) {
            int newDistance = sensor.manhattanDistance + 1;
            testPointForBeacon(sensor.startX - newDistance, sensor.startY, ranges, sensor, position);
            testPointForBeacon(sensor.startX + newDistance, sensor.startY, ranges, sensor, position);
            // Points witht the same x coordinate
            for (int i = -newDistance + 1; i < newDistance; i++) {
                testPointForBeacon(sensor.startX + i, sensor.startY + newDistance - Math.abs(i), ranges, sensor, position);
                testPointForBeacon(sensor.startX + i, sensor.startY - newDistance + Math.abs(i), ranges, sensor, position);
            }
        }
        return position;
    }

    private static void testPointForBeacon(int x, int y, ArrayList<Diamond> ranges, Diamond toSkip, Integer[] positionResult) {
        if (x > searchLimitMax || x < searchLimitMin || y > searchLimitMax || y < searchLimitMin) return;
        for (Diamond otherSensor : ranges) {
            if (otherSensor == toSkip) continue;
            // If manhattan distance to sensor is less or equal to manhattan distance of sensor, then this square is within the sensor's range.
            if (Math.abs(otherSensor.startX - x) + Math.abs(otherSensor.startY - y) <= otherSensor.manhattanDistance) return;
        }
        // If out of range of every sensor, return the x and y as a position
        positionResult[0] = x;
        positionResult[1] = y;
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
class Diamond {
    public int manhattanDistance;
    // X then Y (sensor location)
    public int startX;
    public int startY;

    public Diamond(int manhattanDistanceInput, int startingX, int startingY) {
        manhattanDistance = manhattanDistanceInput;
        startX = startingX;
        startY = startingY;
    }
}