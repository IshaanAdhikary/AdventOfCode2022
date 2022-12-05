package DailyCode.Day3;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;

public class Day3Part2 {
    public static Integer findGroupPrioritySum() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day3\\Day3Input.txt");
		Scanner in = new Scanner(file);
        int prioritySum = 0;

        while (in.hasNextLine()) {
            String[] bagsAsStrings = new String[] {in.nextLine(), in.nextLine(), in.nextLine()};
            HashSet<Character> bag1 = new HashSet<Character>(bagsAsStrings[0].length());
            HashSet<Character> bag2 = new HashSet<Character>(bagsAsStrings[1].length());
            HashSet<Character> bag3 = new HashSet<Character>(bagsAsStrings[2].length());
            for (int i = 0; i < bagsAsStrings[0].length(); i++) bag1.add(bagsAsStrings[0].charAt(i));
            for (int i = 0; i < bagsAsStrings[1].length(); i++) bag2.add(bagsAsStrings[1].charAt(i));
            for (int i = 0; i < bagsAsStrings[2].length(); i++) bag3.add(bagsAsStrings[2].charAt(i));

            // Finds the (Guaranteed by problem restrictions) one character in all three bags
            bag1.retainAll(bag2);
            bag1.retainAll(bag3);
            Character item = bag1.iterator().next();

            // Uses ASCII values of characters to find their priority
            if (Character.isLowerCase(item)) prioritySum += (int) item - 96;
            else prioritySum += (int) item - 38;
        }
        
        in.close();
        return prioritySum;
    }
}
