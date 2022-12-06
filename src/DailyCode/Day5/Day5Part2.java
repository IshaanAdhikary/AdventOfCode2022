package DailyCode.Day5;

import java.io.*;
import java.util.*;

public class Day5Part2 {
    public static String findTopCrates() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day5\\Day5Input.txt");
		Scanner in = new Scanner(file);
        
        ArrayList<Deque<Character>> crateStacks = new ArrayList<Deque<Character>>(9);
        for (int i = 0; i < 9; i++) crateStacks.add(new LinkedList<Character>());
        
        // Initalize starting crate configuration
        while (in.hasNextLine()) {
            String crateRow = in.nextLine();
            
            if (crateRow.charAt(1) == '1') { in.nextLine(); break; }

            for (int i = 0; i < 9; i++) {
                char crate = crateRow.charAt(1 + 4 * i);
                if (Character.isWhitespace(crate)) continue;

                crateStacks.get(i).addLast(crate);
            }
        }
        // Modifies the crates based on instructions
        while (in.hasNextLine()) {
            String instruction = in.nextLine();
            // Forms blank string, amount of crates, origin stack, then target stack
            String[] instructionsBrokenDown = instruction.split("(move )|( from )|( to )");
            int amount = Integer.parseInt(instructionsBrokenDown[1]);
            int origin = Integer.parseInt(instructionsBrokenDown[2]) - 1;
            int target = Integer.parseInt(instructionsBrokenDown[3]) - 1;

            // Pops the top crate of the origin and pushes it to the target for the amount of times required, creates a temp stack so deques can still be used
            Deque<Character> tempStack = new LinkedList<Character>();
            for (int i = 0; i < amount; i++) tempStack.push(crateStacks.get(origin).pop());
            for (int i = 0; i < amount; i++) crateStacks.get(target).push(tempStack.pop());
        }

        // Prints top crates of stacks
        String answer = "";
        for (int i = 0; i < 9; i++) {
            answer += crateStacks.get(i).pop();
        }

        in.close();
        return answer;
    } 
}