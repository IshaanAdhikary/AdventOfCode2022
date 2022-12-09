package DailyCode.Day9;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;

public class Day9Part2 {
    public static Integer findVisitedByTailAmount() throws FileNotFoundException {
        File file = new File("src\\DailyCode\\Day9\\Day9Input.txt");
		Scanner in = new Scanner(file);

        // The first knot (knot[0]) is the head, everything else is the following tails
        Cell[] knots = new Cell[10];
        for (int i = 0; i < knots.length; i++) knots[i] = new Cell(0, 0);
        HashSet<Cell> cellsVisitedByLastTail = new HashSet<Cell>();
        cellsVisitedByLastTail.add(knots[9]);

        while (in.hasNextLine()) {
            int horizontalStep, verticalStep, stepAmount;
            horizontalStep = verticalStep = 0;
            String[] line = in.nextLine().split(" ");
            stepAmount = Integer.parseInt(line[1]);
            switch(line[0]) {
                case "U":
                    verticalStep = -1;
                    break;
                case "D":
                    verticalStep = 1;
                    break;
                case "L":
                    horizontalStep = -1;
                    break;
                case "R":
                    horizontalStep = 1;
                    break;
                default: throw new IllegalArgumentException("Instructions do not refer to U, D, L, or R");
            }
            for (int step = 0; step < stepAmount; step++) {
                knots[0].move(horizontalStep, verticalStep);
                for(int i = 1; i < knots.length; i++) {
                    knots[i].moveTowards(knots[i - 1]);
                }
                cellsVisitedByLastTail.add(knots[9].duplicateCell());
            }
        }

        in.close();
        return cellsVisitedByLastTail.size();
    }
}

class Cell {
    private int x;
    private int y;

    public Cell(int setX, int setY) {
        x = setX;
        y = setY;
    }

    public Cell duplicateCell() {
        return new Cell(x, y);
    }

    public void move (int moveX, int moveY) {
        x += moveX;
        y += moveY;
    }

    public void moveTowards (Cell otherCell) {
        if (this.isAdjacent(otherCell)) return;
        this.move(Integer.signum(otherCell.x - x), Integer.signum(otherCell.y - y));
    }

    private boolean isAdjacent(Cell otherCell) {
        if (Math.abs(otherCell.x - x) <= 1 && Math.abs(otherCell.y - y) <= 1) return true;
        return false;
    }

    @Override
    public boolean equals(Object originalObject) {
        if (originalObject == this) return true;
        if (!(originalObject instanceof Cell)) return false;

        Cell object = (Cell) originalObject;

        return x == object.x && y == object.y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;
        hashCode = prime * hashCode + x;
        hashCode = prime * hashCode + y;
        return hashCode;
    }
}