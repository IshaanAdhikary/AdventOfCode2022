package DailyCode.Day6;

import java.io.*;
import java.util.Scanner;

public class Day6Part2 {
    public static Integer findStartOfMessageIndex() throws Exception {
        File file = new File("src\\DailyCode\\Day6\\Day6Input.txt");
		Scanner in = new Scanner(file);

        String line = in.nextLine();
        in.close();
        char[] lastFourteenChars = new char[14];

        if (line.length() < 14) throw new IllegalArgumentException("Datastream has less then four characters");
        for (int i = 0; i < 14; i++) lastFourteenChars[i] = line.charAt(i);
        if (!doAnyElementsMatch(lastFourteenChars)) return 14;

        for (int i = 14; i < line.length(); i++) {
            lastFourteenChars[i % 14] = line.charAt(i);
            if (!doAnyElementsMatch(lastFourteenChars)) return i + 1;
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
