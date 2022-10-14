package cinema;

import java.util.Scanner;

public class Cinema {

    public static void showOptions() {
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
    }

    public static void showStatistics(int purchasedTickets, int totalSeats, int currentIncome, int totalIncome) {
        float percentage = (float)purchasedTickets / totalSeats * 100;
        System.out.println("Number of purchased tickets: " + purchasedTickets);
        System.out.printf("Percentage: %.2f%%%n", percentage);
        System.out.println("Current income: $" + currentIncome);
        System.out.println("Total income: $" + totalIncome);
        System.out.println();
    }

    public static int getOption() {
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        System.out.println();
        return choice;
    }

    public static void showCinema(String[][] seatMatrix) {
        System.out.println("Cinema:");

        System.out.print("  ");
        for (int i = 1; i <= seatMatrix[0].length; i++) {
            System.out.print(i + " ");
        }

        System.out.println();

        for(int i = 0; i < seatMatrix.length; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < seatMatrix[i].length; j++) {
                System.out.print(seatMatrix[i][j]);
            }
            System.out.println();
        }

        System.out.println();
    }

    public static String[][] createMatrix(int rows, int seats){
        String[][] matrix = new String[rows][seats];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < seats; j++) {
                matrix[i][j] = "S ";
            }
        }
        return matrix;
    }

    public static int[] getSeat(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a row number:");
        int row = scanner.nextInt();

        System.out.println("Enter a seat number in that row:");
        int seat = scanner.nextInt();

        int[] rowSeat = new int[] {row, seat};
        return rowSeat;
    }
    public static void buyTicket(String[][] seatMatrix, int row, int seat, int price){

        seatMatrix[row-1][seat-1] = "B ";

        System.out.println();
        System.out.println("Ticket price: $" + price);
        System.out.println();
    }

    public static int getSeatPrice(int rows, int totalSeats, int row) {
        int price;
        if (totalSeats <= 60) {
            price = 10;
        } else {
            int frontRows = rows / 2;
            if (row <= frontRows) {
                price = 10;
            } else {
                price = 8;
            }
        }
        return price;
    }

    public static int getTotalIncome(int rows, int seats, int totalSeats) {
        int totalPrice;
        if (totalSeats <= 60) {
            totalPrice = totalSeats *  10;
        } else {
            int frontRows = rows / 2;
            int backRows= rows - frontRows;
            totalPrice = frontRows * 10 * seats + backRows * 8 * seats;
        }
        return totalPrice;
    }

    private static boolean isValidSeat(String[][] cinemaMatrix, int row, int seat) {
        if (row > cinemaMatrix.length || seat > cinemaMatrix[0].length){
            System.out.println();
            System.out.println("Wrong input!");
            System.out.println();
            return false;
        }
        if (cinemaMatrix[row-1][seat-1] == "B ") {
            System.out.println();
            System.out.println("That ticket has already been purchased!");
            System.out.println();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        // Write your code here
        System.out.println("Enter the number of rows:");
        int rows = scanner.nextInt();

        System.out.println("Enter the number of seats in each row:");
        int seats = scanner.nextInt();

        int purchasedTickets = 0;
        int totalSeats = rows * seats;
        int totalIncome = getTotalIncome(rows, seats, totalSeats);
        int currentIncome = 0;

        System.out.println();
        String[][] cinemaMatrix = createMatrix(rows, seats);



        while (true) {
            showOptions();
            int option = getOption();

            if (option == 0) break;

            if (option == 1) showCinema(cinemaMatrix);

            if (option == 2) {
                boolean valid = true;
                int row;
                int seat;

                while (true) {
                    int[] rowSeat = getSeat();
                    row = rowSeat[0];
                    seat = rowSeat[1];

                    valid = isValidSeat(cinemaMatrix, row, seat);

                    if (valid) break;
                }

                purchasedTickets++;

                int seatPrice = getSeatPrice(rows, totalSeats, row);

                currentIncome = currentIncome + seatPrice;

                buyTicket(cinemaMatrix, row, seat, seatPrice);
            }

            if (option == 3) showStatistics(purchasedTickets, totalSeats, currentIncome, totalIncome);
        }

    }
}
