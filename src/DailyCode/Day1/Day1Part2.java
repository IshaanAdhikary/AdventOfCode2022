package DailyCode.Day1;

import java.io.*;
import java.util.Scanner;

public class Day1Part2 {
    public static Integer findTopThreeElvesCalorieTotal() throws FileNotFoundException{
        File file = new File("src\\DailyCode\\Day1\\Day1Input.txt");
		Scanner in = new Scanner(file);
		int currentTotal = 0;
		Integer[] topThree = new Integer[] {0, 0, 0};

		while (in.hasNextLine()) {
			String amountString = in.nextLine();
			if (amountString.equals("")) {
				for (int i = 0; i < 3; i++) {
					if (currentTotal > topThree[i] && (i == 2 || currentTotal <= topThree[i + 1])) topThree[i] = currentTotal;
				}
				currentTotal = 0;
			}
			else currentTotal += Integer.parseInt(amountString);
		}
		
		in.close();
		int answer = 0;
		for (int i = 0; i < 3; i++) {
			answer += topThree[i];
		}
		return answer;
    }
}
