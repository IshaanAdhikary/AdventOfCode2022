package DailyCode.Day13;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Day13Part1 {
    public static Integer findOrderedPacketIndexSum() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day13\\Day13Input.txt");
		Scanner in = new Scanner(file);

        int index = 1;
        int total = 0;
        while (in.hasNextLine()) {
            String packet1 = in.nextLine();
            String packet2 = in.nextLine();
            
            if (solvePacket(packet1, packet2) == 1) total = total + index;

            index++;
            // Skip empty lines
            if (in.hasNextLine()) in.nextLine();
        }

        in.close();
        return total;
    }

    // - 1: Wrong order, 0: Equal in order, 1 correct order. Recursive function
    private static int solvePacket(String packet1, String packet2) {
        // Base case, if two numbers, find which one is greater or less than, and return that.
        Integer packet1Integer = null, packet2Integer = null;
        if (packet1.matches("\\d+")) packet1Integer = Integer.parseInt(packet1);
        if (packet2.matches("\\d+")) packet2Integer = Integer.parseInt(packet2);
        // Ints only null if the elements are lists. If both numbers, compare them.
        if (packet1Integer != null && packet2Integer != null) {
            if (packet1Integer == packet2Integer) return 0;
            if (packet1Integer < packet2Integer) return 1;
            if (packet1Integer > packet2Integer) return -1;
        }

        // If an element is empty, it comes first.
        if (packet1.equals("") && packet2.equals("")) return 0; 
        if (packet1.equals("")) return 1;
        if (packet2.equals("")) return -1;

        // Remove starting and ending brackets of the lists to be parsed as their elements.
        // Single numbers are going to be treated as sets, so although they don't need brackets removed, they go through the rest normally.
        if (packet1Integer == null) {
            packet1 = packet1.substring(1, packet1.length() - 1);
        }
        if (packet2Integer == null) {
            packet2 = packet2.substring(1, packet2.length() - 1);
        }

        ArrayList<String> dataInPacket1 = listElementsAsStrings(packet1);
        ArrayList<String> dataInPacket2 = listElementsAsStrings(packet2);

        // The shorter length of the two and what to return if the end of the shorter array is hit without returning.
        int shorterLength, valueIfSame;
        if (dataInPacket1.size() < dataInPacket2.size()) {
            shorterLength = dataInPacket1.size();
            valueIfSame = 1;
        }
        else if (dataInPacket1.size() == dataInPacket2.size()) {
            shorterLength = dataInPacket1.size(); // Either would work, they're equal
            valueIfSame = 0;
        }
        else {
            shorterLength = dataInPacket2.size();
            valueIfSame = -1;
        }

        for (int i = 0; i < shorterLength; i++) {
            int orderValue = solvePacket(dataInPacket1.get(i), dataInPacket2.get(i));
            // If in-order or out of order, pass it up the stack.
            if (orderValue != 0) return orderValue;
        }
        // None of the terms in this List matched, so see which list is shorter, or if equal, look at the next term.
        return valueIfSame;
    }

    // Takes in without external brackets
    private static ArrayList<String> listElementsAsStrings(String packet) {
        ArrayList<String> elements = new ArrayList<String>();
        ArrayList<Character> toBeAdded = new ArrayList<Character>();

        int openBrackets = 0;
        for (int i = 0; i < packet.length(); i++) {
            char currentCharacter = packet.charAt(i);
            if (currentCharacter == '[') openBrackets++;
            else if (currentCharacter == ']') openBrackets--;
            else if (currentCharacter == ',' && openBrackets == 0) {
                elements.add(charListToString(toBeAdded));
                toBeAdded.clear();
                continue; // Don't add character to toBeAdded List
            }
            toBeAdded.add(currentCharacter);
        }

        // Add the last toBeAdded, since it was never followed by a comma to be done in the for loop.
        elements.add(charListToString(toBeAdded));
        return elements;
    }

    private static String charListToString(ArrayList<Character> list) {    
        StringBuilder stringVersion = new StringBuilder(list.size());

        for(Character c : list)
        {
            stringVersion.append(c);
        }

        return stringVersion.toString();
    }
}
