package DailyCode.Day6;

import java.io.*;
import java.util.Scanner;

public class Day6Part1 {
    public static Integer findStartOfPacketIndex() throws Exception {
        File file = new File("src\\DailyCode\\Day6\\Day6Input.txt");
		Scanner in = new Scanner(file);

        String line = in.nextLine();
        in.close();
        char[] lastFourChars = new char[4];

        if (line.length() < 4) throw new IllegalArgumentException("Datastream has less then four characters");
        for (int i = 0; i < 4; i++) lastFourChars[i] = line.charAt(i);
        if (!doAnyElementsMatch(lastFourChars)) return 4;

        for (int i = 4; i < line.length(); i++) {
            lastFourChars[i % 4] = line.charAt(i);
            if (!doAnyElementsMatch(lastFourChars)) return i + 1;
        }
        throw new IllegalArgumentException("Datastream has no start-of-message");
    }

    private static boolean doAnyElementsMatch(char[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = arr.length - 1; j > i; j--) {
                if (arr[i] == arr[j]) return true;
            }
        }
        return false;
    }
}
