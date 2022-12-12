package DailyCode.Day11;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Day11Part1 {
    private static final int maxRounds = 20;

    public static Integer findMonkeyBusiness() throws FileNotFoundException{
        Monkey[] monkeysArray = makeMonkeys();

        for (int round = 1; round <= maxRounds; round++) {
            for (Monkey monkey : monkeysArray) {
                monkey.takeTurn(monkeysArray);
            }
        }

        int[] mostInspections = new int[2];

        for (Monkey monkey : monkeysArray) {
            int inspections = monkey.getInspectionCount();
            for (int i = 0; i < mostInspections.length; i++) {
                if (inspections > mostInspections[i] && (i == mostInspections.length - 1 || inspections <= mostInspections[i + 1])) {
                    for (int j = i - 1; j >= 0; j--) mostInspections[j] = mostInspections[j + 1];
                    mostInspections[i] = inspections;
                }
            }
        }

        int answer = 1;
        for (int i = 0; i < mostInspections.length; i++) answer *= mostInspections[i];
        return answer;
    }

    private static Monkey[] makeMonkeys() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day11\\Day11Input.txt");
		Scanner in = new Scanner(file);

        ArrayList<Monkey> monkeyList = new ArrayList<Monkey>();

        while (in.hasNextLine()) {
            in.nextLine(); // Pass over Monkey {number} header
            // Numbers start at char[18] (manually counted)
            String[] itemsAsStrings = in.nextLine().substring(18).split(", ");
            ArrayList<Integer> items = new ArrayList<Integer>(itemsAsStrings.length);
            for (int i = 0; i < itemsAsStrings.length; i++) {
                items.add(Integer.parseInt(itemsAsStrings[i]));
            }

            // Actual expression (ignoring "new =") starts at char[19] (manually counted)
            String operationLine = in.nextLine().substring(19);

            int divisibilityTestNumber = Integer.parseInt(in.nextLine().substring(21));
            
            // If true then, then If false then
            int[] targetMonkeys = new int[2];
            // Manually counted, only the number monkeys to throw to
            targetMonkeys[0] = Integer.parseInt(in.nextLine().substring(29));
            targetMonkeys[1] = Integer.parseInt(in.nextLine().substring(30));

            monkeyList.add(new Monkey(items, divisibilityTestNumber, operationLine, targetMonkeys));
            if (in.hasNextLine()) in.nextLine(); // Pass over blank line in between monkeys
        }

        in.close();

        Monkey[] monkeysArray = new Monkey[monkeyList.size()];
        monkeysArray = monkeyList.toArray(monkeysArray);
        return monkeysArray;
    }
}

class Monkey {
    // Items identified on worry level, just like input. Items do NOT have to be distinguishable from each other (so it's fine if they happen to equal each other).
    private ArrayList<Integer> items;
    private int divisibilityTestNumber = 0;
    private String operation;
    // If true then, then If false then
    private int[] targetMonkeys = new int[2];

    private int inspectionCount = 0;

    public Monkey(ArrayList<Integer> startingItems, int divisibilityTestNumberInput, String operationInput, int[] targetMonkeysInput) {
        items = startingItems;
        divisibilityTestNumber = divisibilityTestNumberInput;
        operation = operationInput;
        targetMonkeys = targetMonkeysInput;
    }

    public int getInspectionCount() {
        return inspectionCount;
    }

    public void catchItem(int item) {
        items.add(item);
    }

    public void takeTurn(Monkey[] monkeys) {
        // Start from end since items are removed if they match the divisibility condition
        for (int i = items.size() - 1; i >= 0; i--) {
            inspectionCount++;
            items.set(i, inspectItem(i));
            throwItem(i, monkeys);
        }
    }

    // Does both inspection worry raise using operation, and division of worry caused by relief of item being undamaged
    private int inspectItem(int itemIndex) {
        // Operations are always only one operation with two terms all seperated by spaces. [0] num, [1] op, [2] num.
        String[] operationTerms = operation.split(" ");

        int t1, t2;
        if (operationTerms[0].matches("old")) t1 = items.get(itemIndex);
        else t1 = Integer.parseInt(operationTerms[0]);
        if (operationTerms[2].matches("old")) t2 = items.get(itemIndex);
        else t2 = Integer.parseInt(operationTerms[2]);

        int answer;

        switch (operationTerms[1]) {
            case "+":
                answer = t1 + t2;
                break;
            case "-":
                answer = t1 - t2;
                break;
            case "*":
                answer = t1 * t2;
                break;
            case "/":
                answer = t1 / t2;
                break;
            default: throw new IllegalArgumentException("Invalid operation while inspecting item using {" + operation + "}");
        }

        // Worry lowers
        return (int) answer / 3;
    }

    private void throwItem(int itemIndex, Monkey[] monkeys) {
        Integer item = items.get(itemIndex);
        int target = item % divisibilityTestNumber == 0 ? targetMonkeys[0] : targetMonkeys[1];
        monkeys[target].catchItem(item);
        items.remove(itemIndex);
    }
}