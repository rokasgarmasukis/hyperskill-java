package battleship;

import java.util.Scanner;

public class Game {
    private final Battlefield battlefield;
    private final Ship[] ships;

    Game() {
        this.battlefield = new Battlefield();
        this.ships = new Ship[5];
    }

    public void Run() {
        battlefield.printField(battlefield.getVisibleField());

        ships[0] = initShip(5, "Aircraft Carrier");
        ships[1] = initShip(4, "Battleship");
        ships[2] = initShip(3, "Submarine");
        ships[3] = initShip(3, "Cruiser");
        ships[4] = initShip(2, "Destroyer");

        System.out.println("The game starts!");
        battlefield.printField(battlefield.getFogOfWarField());
        System.out.println("Take a shot!");
        while (true) {
            int[] shotCoordinates = takeAShot();
            battlefield.placeShot(shotCoordinates);
            battlefield.printField(battlefield.getFogOfWarField());
            System.out.println(checkShot(shotCoordinates));

            if(isGameOver()) {
                System.out.println("You sank the last ship. You won. Congratulations!");
                break;
            }
        }
    }

    public String checkShot(int[] shotCoordinates) {
        String hitMessage = "You hit a ship! Try again:";
        String sinkMessage = "You sank a ship! Specify a new target:";
        String missedMessage = "You missed. Try again:";
        String alreadyShotMessage = "You hit a ship!\nYou missed!";

        int shot = shotCoordinates[0] * 10 + shotCoordinates[1];

        for (Ship ship : ships) {
            if (ship.repeatedShot(shot)) return alreadyShotMessage;
            if (ship.gotShot(shot)) {
                if (ship.isAlive()) {
                    return hitMessage;
                } else {
                    return sinkMessage;
                }
            }
        }
        return missedMessage;
    }

    private boolean isGameOver() {
        for (Ship ship : ships) {
            if (ship.isAlive()) return false;
        }
        return true;
    }


    public Ship initShip(int shipLength, String shipType) {
        int[][] coordinates = placeShip(battlefield, shipLength, shipType);
        battlefield.updateVisibleField(coordinates);

        // make a copy because function "updateActualField" changes the array
        int[][] copy = copyArray(coordinates);
        battlefield.updateActualField(copy);

        battlefield.printField(battlefield.getVisibleField());
        return new Ship(flattenArray(coordinates, shipLength), shipLength);
    }

    private int[][] copyArray(int[][] array) {
        int[][] copy = new int[2][2];
        copy[0] = array[0].clone();
        copy[1] = array[1].clone();
        return copy;
    }

    private int[] flattenArray(int[][] coordinates, int size) {
        int[] flattenedArray = new int[size];
        // if rows are equal, increase column
        int row = coordinates[0][0];
        int col = coordinates[0][1];
        if (coordinates[0][0] == coordinates[1][0]) {
            for (int i = 0; i < size; i++) {
                flattenedArray[i] = row * 10 + col;
                col++;
            }
            // else, increase row and keep column the same
        } else {
            for (int i = 0; i < size; i++) {
                flattenedArray[i] = row * 10 + col;
                row++;
            }
        }
        return flattenedArray;
    }

    private int[] takeAShot() {
        String message = "Take a shot!";
        String wrongCoordinatesMessage = "Error! You entered the wrong coordinates! Try again:";

        //System.out.println(message);
        String input = getInput();
        while (true) {
            if (!isInputInvalid(input)) {
                System.out.println(wrongCoordinatesMessage);
                input = getInput();
                continue;
            }
            break;
        }

        return parseCoordinate(input);
    }

    public boolean isInputInvalid(String coordinate) {
        String letters = "ABCDEFGHIJ";
        // check for length
        if (coordinate.length() < 2 || coordinate.length() > 3) {
            return false;
        }
        // check whether first character is a letter
        char coordinateLetter = coordinate.charAt(0);
        if (letters.indexOf(coordinateLetter) == -1) {
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
        return coordinateNumber >= 1 && coordinateNumber <= 10;
    }

    public int[][] placeShip(Battlefield battlefield, int shipLength, String shipType) {
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
            if (letterIndexDiff == 0 && numIndexDiff + 1 != shipLength
                    || numIndexDiff == 0 && letterIndexDiff + 1 != shipLength) {
                System.out.println(messageWrongLength);
                continue;
            }

            // compute indexes, sort in case needed - works, because either letter or number has to be equal
            if (coordinateOne[0] < coordinateTwo[0] || coordinateOne[1] < coordinateTwo[1]) {
                coordinates[0] = coordinateOne;
                coordinates[1] = coordinateTwo;
            } else {
                coordinates[1] = coordinateOne;
                coordinates[0] = coordinateTwo;
            }

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

    public String getInput() {
        Scanner scanner = new Scanner(System.in);
        String coordinate = scanner.next();
        return coordinate;
    }

    public int[] parseCoordinate(String coordinate) {
        String letters = "ABCDEFGHIJ";
        char coordinateLetter = coordinate.charAt(0);
        int indexCoordinateLetter = letters.indexOf(coordinateLetter);
        int coordinateNumber = Integer.parseInt(coordinate.substring(1)) - 1;
        return new int[]{indexCoordinateLetter, coordinateNumber};
    }
}
