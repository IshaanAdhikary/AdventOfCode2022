package DailyCode.Day10;

import java.io.*;
import java.util.Scanner;

public class Day10Part2 {
    public static String CRTScreenAsString() throws FileNotFoundException {
        int cycle = 0;
        int registerX = 1;
        char[][] screenAsArray = new char[6][40];

        File file = new File("src\\DailyCode\\Day10\\Day10Input.txt");
		Scanner in = new Scanner(file);

        while (in.hasNextLine()) {
            String line = in.nextLine();
            
            // Happens whether no operation or adding to x
            cycle++;
            drawCharToScreen(cycle, registerX, screenAsArray);

            if (line.equals("noop")) continue;

            // If there is an operation, it is an addX command, and the value is the int cast of the command after the space.
            int addValue = Integer.parseInt(line.split(" ")[1]);

            cycle++;
            drawCharToScreen(cycle, registerX, screenAsArray);
            registerX += addValue;
            if (cycle >= 240) break;
        }

        in.close();

        String toDraw = "";
        for (int i = 0; i < screenAsArray.length; i++) {
            toDraw += "\n";
            toDraw += new String(screenAsArray[i]);
        }

        return toDraw;
    }

    private static void drawCharToScreen(int cycle, int registerX, char[][] screenAsArray) {
        cycle--;
        int row = cycle / 40, column = cycle % 40;
        char charToDraw = '.';
        // If the difference between the register and target column is 0 or 1, then the target is inside of the sprite.
        if (Math.abs(registerX - column) <= 1) charToDraw = '#';
        screenAsArray[row][column] = charToDraw;
    }
}