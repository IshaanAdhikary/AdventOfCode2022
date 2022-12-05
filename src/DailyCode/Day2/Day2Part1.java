package DailyCode.Day2;

import java.io.*;
import java.util.Scanner;

public class Day2Part1 {
    public static Integer findRockPaperScissorsScore() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day2\\Day2Input.txt");
		Scanner in = new Scanner(file);
		int score = 0;

		while (in.hasNextLine()) {
			String[] moves = in.nextLine().split(" ", 2);
			int opponentMoveVal = (int) moves[0].charAt(0) - 65;
			int myMoveVal = (int) moves[1].charAt(0) - 88;
			
			if ((opponentMoveVal == myMoveVal)) score += 3;
			else if ((opponentMoveVal + 1) % 3 == myMoveVal) score += 6;
			score += (myMoveVal + 1);
		}

		in.close();
		return score;
    }
}
