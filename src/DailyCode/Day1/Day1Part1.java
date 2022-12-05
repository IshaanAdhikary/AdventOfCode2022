package DailyCode.Day1;

import java.io.*;
import java.util.Scanner;

public class Day1Part1 {
    public static Integer findTopElfCalorieTotal() throws FileNotFoundException{
        File file = new File("src\\DailyCode\\Day1\\Day1Input.txt");
		Scanner in = new Scanner(file);
		int currentTotal = 0;
		int maximum = 0;

		while (in.hasNextLine()) {
			String amountString = in.nextLine();

			if (amountString.equals("")) {
				maximum = currentTotal > maximum ? currentTotal : maximum;
				currentTotal = 0;
			}
			else currentTotal += Integer.parseInt(amountString);
		}
		
		in.close();
		return maximum;
    }
}
