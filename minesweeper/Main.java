package minesweeper;

import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Minefield field = new Minefield();

        System.out.print("How many mines do you want on the field? > ");
        Scanner scanner = new Scanner(System.in);
        int numberOfMines = scanner.nextInt();

        field.placeMines(numberOfMines);
        field.calculateNearbyMines();
        field.printMineField();

        String[] input;
        boolean goodCoordinates;

        while (!field.allMinesDiscovered() && !field.steppedOnMine()) {

            do {
                input = getInput();
                int yCoordinate = Integer.parseInt(input[0]) - 1;
                int xCoordinate = Integer.parseInt(input[1]) - 1;

                if (Objects.equals(input[2], "free")) {
                    goodCoordinates = field.freeCell(xCoordinate, yCoordinate);
                    if (!goodCoordinates) {
                        System.out.println("This cell is already freed!");
                    }
                }
                else {
                    goodCoordinates = field.markCellForMine(xCoordinate, yCoordinate);
                    if (!goodCoordinates) {
                        System.out.println("There is a number here!");
                    }
                }
            } while (!goodCoordinates);

            field.printMineField();
        }
        if (field.allMinesDiscovered()) {
            System.out.println("Congratulations! You found all the mines!");
        } else {
            System.out.println("You stepped on a mine and failed!");
        }

    }

    public static String[] getInput() {
        System.out.print("Set/unset mine marks or claim a cell as free: > ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input.split(" ");
    }
}
