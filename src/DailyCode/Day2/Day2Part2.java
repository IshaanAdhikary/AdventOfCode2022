package DailyCode.Day2;

import java.io.FileNotFoundException;

import java.io.*;
import java.util.Scanner;

public class Day2Part2 {
    public static Integer findRockPaperScissorsScore() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day2\\Day2Input.txt");
		Scanner in = new Scanner(file);
		int score = 0;

		while (in.hasNextLine()) {
			String[] moves = in.nextLine().split(" ", 2);
			int opponentMoveVal = (int) moves[0].charAt(0) - 65;
			// - 1 if loss, 0 if tie, 1 if win
			int myGoal = (int) moves[1].charAt(0) - 89;

			switch (myGoal) {
				case 0: score += 3;
					break;
				case 1: score += 6;
					break;
			}

			score += ((opponentMoveVal + myGoal + 3) % 3) + 1;
		}
		
		in.close();
		return score;
    }
}
