package DailyCode.Day4;

import java.io.*;
import java.util.Scanner;

public class Day4Part2 {
    public static Integer findAnyOverlap() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day4\\Day4Input.txt");
		Scanner in = new Scanner(file);
        int amount = 0;

        while (in.hasNextLine()) {
            String elfPair = in.nextLine();
            // Elf 1 start, elf 1 end, elf 2 start, elf 2 end
            String[] elfIDRangesString = elfPair.split(",|-", 4);
            int[] elfIDRanges = new int[4];
            for (int i = 0; i < 4; i++) elfIDRanges[i] = Integer.parseInt(elfIDRangesString[i]);

            if (elfIDRanges[0] <= elfIDRanges[3] && elfIDRanges[1] >= elfIDRanges[2]) amount++;
            else if (elfIDRanges[2] <= elfIDRanges[1] && elfIDRanges[3] >= elfIDRanges[0]) amount++;
        }

        in.close();
        return amount;
    }
}
