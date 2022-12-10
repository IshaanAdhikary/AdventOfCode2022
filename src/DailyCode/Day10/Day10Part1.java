package DailyCode.Day10;

import java.io.*;
import java.util.Scanner;

public class Day10Part1 {
    private static final int firstCycle = 20;
    private static final int cycleRepeat = 40;
    private static final int maxTermsToSum = 6;

    public static Integer signalStrengthSums() throws FileNotFoundException {
        int signal = 0;
        int cycle = 0;
        int registerX = 1;

        File file = new File("src\\DailyCode\\Day10\\Day10Input.txt");
		Scanner in = new Scanner(file);

        while (in.hasNextLine()) {
            String line = in.nextLine();

            // Happens whether no operation or adding to x
            cycle++;
            signal = signalValue(cycle, registerX, signal);

            if (line.equals("noop")) continue;

            // If there is an operation, it is an addX command, and the value is the int cast of the command after the space.
            int addValue = Integer.parseInt(line.split(" ")[1]);

            cycle++;
            signal = signalValue(cycle, registerX, signal);
            registerX += addValue;
            if (cycle >= firstCycle + cycleRepeat * maxTermsToSum) break;
        }

        in.close();
        return signal;
    }

    private static int signalValue(int cycle, int registerX, int signal) {
        if ((cycle - firstCycle) % cycleRepeat == 0) signal += cycle * registerX;
        return signal;
    }
}