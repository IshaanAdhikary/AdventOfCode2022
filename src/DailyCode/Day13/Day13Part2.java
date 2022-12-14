package DailyCode.Day13;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Day13Part2 {

    public static int test() {
        String string1 = "[[[[10,9]],3,[[0,3],0,[6,7],[3,8,5,0],[1,6]],1],[[],7,2],[[[8],[9],10,7],1],[[[1,6,5,1,9],5,[2],[2,3,1,2,8]],5]]";
        String string2 = "[[[[10,3,1,1,0],6,[4,4,10,2],6],1,[],5]]";

        return comparePackets(string1, string2);
    }

    public static Integer findDecoderKey() throws FileNotFoundException {
        final String dividerPacket1 = "[[2]]", dividerPacket2 = "[[6]]";

        File file = new File("src\\DailyCode\\Day13\\Day13Input.txt");
		Scanner in = new Scanner(file);
        
        ArrayList<String> packets = new ArrayList<String>();
        
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.equals("")) continue;
            packets.add(line);
        }
        
        packets.add(dividerPacket1);
        packets.add(dividerPacket2);

        in.close();

        quickSort(packets, 0, packets.size() - 1);

        int answer = (packets.indexOf(dividerPacket1) + 1) * (packets.indexOf(dividerPacket2) + 1);
        return answer;
    }

    private static void quickSort(ArrayList<String> packets, int start, int end) {
        // Base case: start == end
        // if (start >= end) return;
        if (start < end) {
            int pivotIndex = seperateAtPivot(packets, start, end);
            quickSort(packets, start, pivotIndex - 1);
            quickSort(packets, pivotIndex + 1, end);
        }
    }

    // Seperates at the pivot and returns the pivot index
    private static int seperateAtPivot(ArrayList<String> packets, int start, int end) {
        String pivot = packets.get(end);
        // Start off since we increment in loop
        int i = start - 1;

        for (int j = start; j < end; j++) {
            // If j comes before the pivot when ordered, increment i and swap items [i] and [j].
            if(comparePackets(packets.get(j), pivot) == 1) {
                i++;
                String temp = packets.get(i);
                packets.set(i, packets.get(j));
                packets.set(j, temp);
            }
        }
        i++;
        String temp = packets.get(i);
        packets.set(i, packets.get(end));
        packets.set(end, temp);
        return i;
    }

    // 1 if packet1 comes before packet2, 0 if any order works, and -1 if packet2 comes before packet1. Recursive function
    private static int comparePackets(String packet1, String packet2) {
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
            int orderValue = comparePackets(dataInPacket1.get(i), dataInPacket2.get(i));
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
