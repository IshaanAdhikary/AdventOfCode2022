package DailyCode.Day3;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;

public class Day3Part1 {
    public static Integer findPrioritySum() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day3\\Day3Input.txt");
		Scanner in = new Scanner(file);
        int prioritySum = 0;

        while (in.hasNextLine()) {
            String bagContents = in.nextLine();
            int compartmentSize = bagContents.length()/2;
            HashSet<Character> compartment1 = new HashSet<Character>(compartmentSize);
            HashSet<Character> compartment2 = new HashSet<Character>(compartmentSize);
            for (int i = 0; i < compartmentSize; i++) {
                compartment1.add(bagContents.charAt(i));
                compartment2.add(bagContents.charAt(i + compartmentSize));
            }
            
            // Finds the (Guaranteed by problem restrictions) one character in both compartments of the bag
            compartment1.retainAll(compartment2);
            Character item = compartment1.iterator().next();

            // Uses ASCII values of characters to find their priority
            if (Character.isLowerCase(item)) prioritySum += (int) item - 96;
            else prioritySum += (int) item - 38;
        }
        
        in.close();
        return prioritySum;
    }
}
