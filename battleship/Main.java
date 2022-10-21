package battleship;

import java.util.Scanner;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // Write your code here

        Battlefield battlefield = new Battlefield();
        battlefield.printField(battlefield.getVisibleField());

        int[][] coordinates = placeShip(battlefield, 5, "Aircraft Carrier");
        battlefield.updateVisibleField(coordinates);
        battlefield.updateActualField(coordinates);
        battlefield.printField(battlefield.getVisibleField());

        coordinates = placeShip(battlefield, 4, "Battleship");
        battlefield.updateVisibleField(coordinates);
        battlefield.updateActualField(coordinates);
        battlefield.printField(battlefield.getVisibleField());

        coordinates = placeShip(battlefield, 3, "Submarine");
        battlefield.updateVisibleField(coordinates);
        battlefield.updateActualField(coordinates);
        battlefield.printField(battlefield.getVisibleField());

        coordinates = placeShip(battlefield, 3, "Cruiser");
        battlefield.updateVisibleField(coordinates);
        battlefield.updateActualField(coordinates);
        battlefield.printField(battlefield.getVisibleField());

        coordinates = placeShip(battlefield, 2, "Destroyer");
        battlefield.updateVisibleField(coordinates);
        battlefield.updateActualField(coordinates);
        battlefield.printField(battlefield.getVisibleField());

//      coordinates = getAndParseInput(freeCells, 2, "Destroyer");
        System.out.println("The game starts!");
        battlefield.printField(battlefield.getFogOfWarField());
        int[] shotCoordinates = takeAShot(battlefield);
        String message = battlefield.placeShot(shotCoordinates);
        battlefield.printField(battlefield.getFogOfWarField());
        System.out.println(message);
        battlefield.printField(battlefield.getVisibleField());
    }

    private static int[] takeAShot(Battlefield battlefield) {
        String message = "Take a shot!";
        String wrongCoordinatesMessage = "Error! You entered the wrong coordinates! Try again:";

        System.out.println(message);
        String input = getInput();
        while(true) {
            if (!isInputInvalid(input)) {
                System.out.println(wrongCoordinatesMessage);
                input = getInput();
                continue;
            }
            break;
        }

        return parseCoordinate(input);
    }

    public static boolean isInputInvalid(String coordinate) {
        String letters = "ABCDEFGHIJ";
        // check for length
        if (coordinate.length() < 2 || coordinate.length() > 3) {
            return false;
        }
        // check whether first character is a letter
        char coordinateLetter = coordinate.charAt(0);
        if(letters.indexOf(coordinateLetter) == -1) {
            return false;
        }

        // check whether next characters are a number
        int coordinateNumber;
        try {
            coordinateNumber = Integer.parseInt(coordinate.substring(1));
        } catch (NumberFormatException e) {
            return false;
        }

        // check for out of bounds coordinates
        if (coordinateNumber < 1 || coordinateNumber > 10) {
            return false;
        }

        return true;
    }

    public static int[][] placeShip(Battlefield battlefield, int shipLength, String shipType) {
        Scanner scanner = new Scanner(System.in);
        String messageWrongLocation = "Error! Wrong ship location! Try again:";
        String messageWrongLength = "Error! Wrong length of the " + shipType + "! Try again:";
        String messageTooClose = "Error! You placed it too close to another one. Try again:";

        System.out.printf("Enter the coordinates of the %s (%d cells):\n", shipType, shipLength);

        int[][] coordinates = new int[2][2];

        while (true) {
            String firstCoordinate = scanner.next();
            String secondCoordinate = scanner.next();

            if (!isInputInvalid(firstCoordinate) || !isInputInvalid(secondCoordinate)) {
                System.out.println(messageWrongLocation);
                continue;
            }

            int[] coordinateOne = parseCoordinate(firstCoordinate);
            int[] coordinateTwo = parseCoordinate(secondCoordinate);

            // check for diagonal ships
            if (coordinateOne[0] != coordinateTwo[0] && coordinateOne[1] != coordinateTwo[1]) {
                System.out.println(messageWrongLocation);
                continue;
            }

            // check for correct length of the chip
            int letterIndexDiff = Math.abs(coordinateOne[0] - coordinateTwo[0]);
            int numIndexDiff = Math.abs(coordinateOne[1] - coordinateTwo[1]);
            if ( letterIndexDiff == 0 && numIndexDiff + 1 != shipLength
                    || numIndexDiff == 0 && letterIndexDiff + 1 != shipLength) {
                System.out.println(messageWrongLength);
                continue;
            }

            // compute indexes, sort in case needed - works, because either letter or number has to be equal
            int indexFirstCoordLetter = Math.min(coordinateOne[0], coordinateTwo[0]);
            int indexFirstCoordNumber = Math.min(coordinateOne[1], coordinateTwo[1]);
            int indexSecondCoordLetter = Math.max(coordinateOne[0], coordinateTwo[0]);
            int indexSecondCoordNumber = Math.max(coordinateOne[1], coordinateTwo[1]);

            coordinates[0][0] = indexFirstCoordLetter;
            coordinates[0][1] = indexFirstCoordNumber;
            coordinates[1][0] = indexSecondCoordLetter;
            coordinates[1][1] = indexSecondCoordNumber;

            // check whether the new coordinates are not too close to another ships
            if (!battlefield.areCellsFree(coordinates)) {
                System.out.println(messageTooClose);
                continue;
            }

            // if everything's alright, break out of loop
            break;
        }

        return coordinates;
    }

    public static String getInput() {
        Scanner scanner = new Scanner(System.in);
        String coordinate = scanner.next();
        return coordinate;
    }

    public static int[] parseCoordinate(String coordinate) {
        String letters = "ABCDEFGHIJ";
        char coordinateLetter = coordinate.charAt(0);
        int indexCoordinateLetter = letters.indexOf(coordinateLetter);
        int coordinateNumber = Integer.parseInt(coordinate.substring(1)) - 1;
        return new int[] {indexCoordinateLetter, coordinateNumber};
    }
}
