package minesweeper;

import java.util.Random;

public class Minefield {
    public Cell[][] cells = new Cell[9][9];

    public Minefield() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell();
            }
        }
    }
    public void placeMines(int numberOfMines) {
        Random random = new Random();
        for (int i = 0; i < numberOfMines; i++) {
            while (true) {
                int row = random.nextInt(0, 9);
                int col = random.nextInt(0, 9);
                if (cells[row][col].isMine) continue;
                cells[row][col].isMine = true;
                break;
            }
        }
    }

    public void printMineField(){
        System.out.println(" |123456789|");
        System.out.println("-|---------|");

        for (int i = 0; i < cells.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(i + 1);
            sb.append("|");
            for (Cell cell : cells[i]) {
                if (cell.isMarked) {
                    sb.append("*");
                }
                else if (!cell.isFreed) {
                    sb.append(".");
                }
                else if (cell.isMine) {
                    sb.append("X");
                }
                else if (cell.numOfNearbyMines > 0) {
                    sb.append(cell.numOfNearbyMines);
                }
                else if (cell.numOfNearbyMines == 0) {
                    sb.append("/");
                }
                else {
                    // sb.append("X");
                    continue;
                }
            }
            sb.append("|");
            String output = sb.toString();
            System.out.println(output);
        }
        System.out.println("-|---------|");
    }

    public void calculateNearbyMines() {
        for (int i = 0; i < cells.length; i++){
            for (int j = 0; j < cells[i].length; j++){
                if (cells[i][j].isMine) {
                    continue;
                }
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0) {
                            continue;
                        }
                        if (dx + i < 0 || dy + j < 0 || dx + i > cells[i].length - 1 || dy + j > cells[i].length - 1) {
                            continue;
                        }
                        if (cells[dx + i][dy + j].isMine) {
                            cells[i][j].numOfNearbyMines++;
                        }
                    }
                }
            }
        }
    }

    public boolean markCellForMine(int x, int y) {
        if (cells[x][y].isFreed) {
            return false;
        }

        cells[x][y].isMarked = !cells[x][y].isMarked;
        return true;
    }

    public boolean freeCell(int x, int y) {
        if (cells[x][y].isFreed) {
            return false;
        }
        if (cells[x][y].numOfNearbyMines > 0)
        {
            cells[x][y].isFreed = true;
            cells[x][y].isMarked = false;
            return true;
        }
        if (cells[x][y].numOfNearbyMines == 0)
        {
            cells[x][y].isFreed = true;
            cells[x][y].isMarked = false;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) {
                        continue;
                    }
                    if (dx + x < 0 || dy + y < 0 || dx + x > cells[x].length - 1 || dy + y > cells[x].length - 1) {
                        continue;
                    }
                    freeCell(x + dx, y + dy);
                }
            }
        }
        return true;
    }
    public boolean allMinesDiscovered() {
        for (Cell[] line : cells) {
            for (Cell cell : line) {
                if (cell.isMine && !cell.isMarked) {
                    return false;
                }
                if (!cell.isMine && cell.isMarked) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean steppedOnMine() {
        for (Cell[] line : cells) {
            for (Cell cell : line) {
                if (cell.isMine && cell.isFreed) {
                    freeAllMineCells();
                    return true;
                }
            }
        }
        return false;
    }

    public void freeAllMineCells() {
        for (Cell[] line : cells) {
            for (Cell cell : line) {
                if (cell.isMine) {
                    cell.isFreed = true;
                }
            }
        }
    }
}
