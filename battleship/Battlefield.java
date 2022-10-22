package battleship;

import java.util.Arrays;

public class Battlefield {

    private final char[][] visibleField;
    private final char[][] actualField;

    private final char[][] fogOfWarField;

    Battlefield(){
        this.visibleField = new char[10][10];

        for(char[] row : visibleField) {
            Arrays.fill(row, '~');
        }

        this.actualField = new char[10][10];

        for(char[] row : actualField) {
            Arrays.fill(row, '~');
        }

        this.fogOfWarField = new char[10][10];
        for(char[] row : fogOfWarField) {
            Arrays.fill(row, '~');
        }
    }

    char[][] getVisibleField() {
        return this.visibleField;
    }

    char[][] getActualField() {
        return this.actualField;
    }

    char[][] getFogOfWarField() { return this.fogOfWarField; }

    void updateVisibleField(int[][] coordinates) {
        for (int row = coordinates[0][0]; row <= coordinates[1][0]; row++) {
            for (int column = coordinates[0][1]; column <= coordinates[1][1]; column++) {
                visibleField[row][column] = 'O';
            }
        }
    }

    public void updateActualField(int[][] c) {
        // protect against out of bounds error
        if (c[0][0] == 0) c[0][0]++;
        if (c[0][1] == 0) c[0][1]++;
        if (c[1][0] == 9) c[1][0]--;
        if (c[1][1] == 9) c[1][1]--;

        // padding ship location by 1 in all directions
        for (int row = c[0][0] - 1; row <= c[1][0] + 1; row++) {
            for (int column = c[0][1] - 1; column <= c[1][1] + 1; column++) {
                actualField[row][column] = 'O';
            }
        }
    }

    public boolean areCellsFree(int[][] coordinates) {
        for (int row = coordinates[0][0]; row <= coordinates[1][0]; row++) {
            for (int column = coordinates[0][1]; column <= coordinates[1][1]; column++) {
                if (actualField[row][column] == 'O') return false;
            }
        }
        return true;
    }

    public void printField(char[][] field) {
        char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        System.out.print("  ");
        for (int num = 0; num <= 9; num++) {
            System.out.print(numbers[num] + " ");
        }
        System.out.println();

        for (int row = 0; row < field.length; row++) {
            System.out.print(letters[row] + " ");
            for (int cell = 0; cell < field[row].length; cell++) {
                System.out.print(field[row][cell] + " ");
            }
            System.out.println();
        }
    }

    public void placeShot(int[] coordinate) {
        int row = coordinate[0];
        int col = coordinate[1];

        if (visibleField[row][col] == 'O' || visibleField[row][col] == 'X') {
            visibleField[row][col] = 'X';
            fogOfWarField[row][col] = 'X';
        } else {
            visibleField[row][col] = 'M';
            fogOfWarField[row][col] = 'M';
        }
    }
}
