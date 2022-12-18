package DailyCode.Day16;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;

public class Day16Part1 {
    public static Integer maxPressureReleased() throws FileNotFoundException{
        File file = new File("src\\DailyCode\\Day16\\Day16Input.txt");
        Scanner in = new Scanner(file);

        Valve startValve = null;
        HashMap<String, Valve> valvesByName = new HashMap<>();

        while (in.hasNextLine()) {
            String[] lineArray = in.nextLine().split(", |; | |=");

            ArrayList<String> neighborValvesStrings = new ArrayList<>();
            for (int i = 10; i < lineArray.length; i++) {
                neighborValvesStrings.add(lineArray[i]);
            }
            
            Valve newValve = new Valve(lineArray[1], Integer.parseInt(lineArray[5]), neighborValvesStrings);
            if (lineArray[1].equals("AA")) startValve = newValve;

            valvesByName.put(lineArray[1], newValve);
        }
        in.close();
        for (Valve valve : valvesByName.values()) {
            valve.fillNeighborValves(valvesByName);
        }

        if (startValve == null) throw new IllegalArgumentException("The input as no valve \"AA\" to start from");
        int bestPathPressureValue = findBestPathPressureValue(valvesByName, startValve);

        return bestPathPressureValue;
    }

    private static int findBestPathPressureValue(HashMap<String, Valve> valves, Valve startValve) {
        Stack<Path> pathsToExplore = new Stack<>();
        Path bestPath = new Path();
        
        pathsToExplore.push(new Path(null, startValve, 1, 0, new HashSet<Valve>(), new HashSet<Valve>(valves.values())));
        while (!pathsToExplore.isEmpty()) {
            Path lastPath = pathsToExplore.pop();
            if (lastPath.getSteps() == 30) {
                bestPath = bestPath.getValue() > lastPath.getValue() ? bestPath : lastPath;
            }
            ArrayList<Path> nextPaths = lastPath.findNextPaths(bestPath);
            for (Path path : nextPaths) {
                pathsToExplore.push(path);
            }
        }

        return bestPath.getValue();
    }
}

class Path {
    private HashSet<Valve> closedValves;
    private HashSet<Valve> openValves;
    private Valve lastVisitedValve;
    private Valve currentValve;
    private int currentValue;
    private int steps;
    private final int stepsAllowed = 30;

    public Path() {
        currentValue = 0;
    }

    public Path(Valve lastVisitedValve, Valve currentValve, int step, int currentValue, HashSet<Valve> openValves, HashSet<Valve> closedValves) {
        this.currentValue = currentValue;
        this.lastVisitedValve = lastVisitedValve;
        this.steps = step;
        this.currentValve = currentValve;
        this.openValves = openValves;
        this.closedValves = closedValves;
    }

    public ArrayList<Path> findNextPaths(Path bestPath) {
        ArrayList<Path> nextPaths = new ArrayList<>();

        if (steps == 30) return nextPaths;
        
        for (Valve nextValve : currentValve.getNeighbors()) {
            if (nextValve == lastVisitedValve) continue;
            Path newPath = new Path(currentValve, nextValve, steps + 1, currentValue, openValves, closedValves);
            // int generousLimit = currentValue;
            // for (Valve canBeOpened : closedValves) {
            //     generousLimit += canBeOpened.getFlowRate() * (stepsAllowed - steps);
            // }
            // if (generousLimit < bestPath.currentValue) {
            //     continue;
            // }
            nextPaths.add(newPath);
        }
        
        // If not already closed and actually matters, also create a path where this valve is opened
        if (closedValves.contains(currentValve) && currentValve.getFlowRate() != 0) {
            Path newPath = new Path(null, currentValve, steps + 1, currentValue, openValves, closedValves);
            newPath.updateOpenValves(currentValve);
            newPath.updateValue(currentValve.getFlowRate());
            nextPaths.add(newPath);
        }
        
        return nextPaths;
    }

    private void updateOpenValves(Valve valve) {
        closedValves.remove(valve);
        openValves.add(valve);
    }

    private void updateValue(int flowRate) {
        currentValue += flowRate * (stepsAllowed - steps);
    }

    public int getSteps() {
        return steps;
    }

    public int getValue() {
        return currentValue;
    }
}

class Valve {
    private int flowRate;
    private ArrayList<String> neighborValvesStrings;
    private ArrayList<Valve> neighborValves;

    // private String label; // Delete later, only for debugging, completely unneccesary

    public Valve(String label, int flowRate, ArrayList<String> neighborValvesStrings) {
        // this.label = label;
        this.flowRate = flowRate;
        this.neighborValvesStrings = neighborValvesStrings;
    }

    public ArrayList<Valve> getNeighbors() {
        return neighborValves;
    }

    public int getFlowRate() {
        return flowRate;
    }

    public void fillNeighborValves(HashMap<String, Valve> valves) {
        neighborValves = new ArrayList<Valve>();
        for (String neighbor : neighborValvesStrings) {
            neighborValves.add(valves.get(neighbor));
        }
    }
}