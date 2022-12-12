package DailyCode.Day11;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day11Part2 {
    private static final int maxRounds = 10000;

    public static Long findMonkeyBusiness() throws FileNotFoundException{
        Monkey2[] monkeysArray = makeMonkeys();

        HashMap<Integer, Integer> LCMAsMap = new HashMap<Integer, Integer>();
        for (Monkey2 monkey : monkeysArray) {
            int divisibilityTest = monkey.getDivisibilityTest();
            for (int factor = 2; factor <= divisibilityTest; factor++) {
                int goesIntoAmount = 0;
                while (divisibilityTest % factor == 0) {
                    divisibilityTest /= factor;
                    goesIntoAmount++;
                }
                if (goesIntoAmount <= 0) continue;
                Integer prevFactorAmount = LCMAsMap.get(factor);
                if (prevFactorAmount == null || goesIntoAmount > prevFactorAmount) LCMAsMap.put(factor, goesIntoAmount);
                if (divisibilityTest == 1) break;
            }
        }
        int LCMOfMonkeyTests = 1;
        for (Map.Entry<Integer, Integer> factor : LCMAsMap.entrySet()) {
            for (int i = 0; i < factor.getValue(); i++) {
                LCMOfMonkeyTests *= factor.getKey();
            }
        }

        for (int round = 1; round <= maxRounds; round++) {
            for (Monkey2 monkey : monkeysArray) {
                monkey.takeTurn(monkeysArray, LCMOfMonkeyTests);
            }
        }

        int[] mostInspections = new int[2];

        for (Monkey2 monkey : monkeysArray) {
            int inspections = monkey.getInspectionCount();
            for (int i = 0; i < mostInspections.length; i++) {
                if (inspections > mostInspections[i] && (i == mostInspections.length - 1 || inspections <= mostInspections[i + 1])) {
                    for (int j = i - 1; j >= 0; j--) mostInspections[j] = mostInspections[j + 1];
                    mostInspections[i] = inspections;
                }
            }
        }

        long answer = 1;
        for (int i = 0; i < mostInspections.length; i++) answer *= mostInspections[i];
        return answer;
    }

    private static Monkey2[] makeMonkeys() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day11\\Day11Input.txt");
		Scanner in = new Scanner(file);

        ArrayList<Monkey2> monkeyList = new ArrayList<Monkey2>();

        while (in.hasNextLine()) {
            in.nextLine(); // Pass over Monkey {number} header
            // Numbers start at char[18] (manually counted)
            String[] itemsAsStrings = in.nextLine().substring(18).split(", ");
            ArrayList<Long> items = new ArrayList<Long>(itemsAsStrings.length);
            for (int i = 0; i < itemsAsStrings.length; i++) {
                items.add(Long.valueOf(Integer.parseInt(itemsAsStrings[i])));
            }

            // Actual expression (ignoring "new =") starts at char[19] (manually counted)
            String operationLine = in.nextLine().substring(19);

            int divisibilityTestNumber = Integer.parseInt(in.nextLine().substring(21));
            
            // If true then, then If false then
            int[] targetMonkeys = new int[2];
            // Manually counted, only the number monkeys to throw to
            targetMonkeys[0] = Integer.parseInt(in.nextLine().substring(29));
            targetMonkeys[1] = Integer.parseInt(in.nextLine().substring(30));

            monkeyList.add(new Monkey2(items, divisibilityTestNumber, operationLine, targetMonkeys));
            if (in.hasNextLine()) in.nextLine(); // Pass over blank line in between monkeys
        }

        in.close();

        Monkey2[] monkeysArray = new Monkey2[monkeyList.size()];
        monkeysArray = monkeyList.toArray(monkeysArray);
        return monkeysArray;
    }
}

class Monkey2 {
    // Items identified on worry level, just like input. Items do NOT have to be distinguishable from each other (so it's fine if they happen to equal each other).
    private ArrayList<Long> items;
    private int divisibilityTestNumber = 0;
    private String operation;
    // If true then, then If false then
    private int[] targetMonkeys = new int[2];

    private int inspectionCount = 0;

    public Monkey2(ArrayList<Long> startingItems, int divisibilityTestNumberInput, String operationInput, int[] targetMonkeysInput) {
        items = startingItems;
        divisibilityTestNumber = divisibilityTestNumberInput;
        operation = operationInput;
        targetMonkeys = targetMonkeysInput;
    }

    public int getDivisibilityTest() {
        return divisibilityTestNumber;
    }

    public int getInspectionCount() {
        return inspectionCount;
    }

    public void catchItem(Long item) {
        items.add(item);
    }

    public void takeTurn(Monkey2[] monkeys, int LCMOfMonkeyTests) {
        // Start from end since items are removed if they match the divisibility condition
        for (int i = items.size() - 1; i >= 0; i--) {
            inspectionCount++;
            items.set(i, inspectItem(i, LCMOfMonkeyTests));
            throwItem(i, monkeys);
        }
    }

    private long inspectItem(int itemIndex, int LCMOfMonkeyTests) {
        // Operations are always only one operation with two terms all seperated by spaces. [0] num, [1] op, [2] num.
        String[] operationTerms = operation.split(" ");

        long t1, t2;
        if (operationTerms[0].matches("old")) t1 = items.get(itemIndex) % LCMOfMonkeyTests;
        else t1 = Integer.parseInt(operationTerms[0]);
        if (operationTerms[2].matches("old")) t2 = items.get(itemIndex) % LCMOfMonkeyTests;
        else t2 = Integer.parseInt(operationTerms[2]);

        long answer;

        switch (operationTerms[1]) {
            case "+":
                answer = (t1 + t2) % LCMOfMonkeyTests;
                break;
            case "-":
                answer = (t1 - t2) % LCMOfMonkeyTests;
                break;
            case "*":
                answer = (t1 * t2) % LCMOfMonkeyTests;
                break;
            case "/":
                answer = (t1 / t2) % LCMOfMonkeyTests;
                break;
            default: throw new IllegalArgumentException("Invalid operation while inspecting item using {" + operation + "}");
        }

        return answer;
    }

    private void throwItem(int itemIndex, Monkey2[] monkeys) {
        Long item = items.get(itemIndex);
        int target = item % divisibilityTestNumber == 0 ? targetMonkeys[0] : targetMonkeys[1];
        monkeys[target].catchItem(item);
        items.remove(itemIndex);
    }
}