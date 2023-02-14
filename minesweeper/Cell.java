package minesweeper;

public class Cell {
    public boolean isMarked = false;
    public boolean isMine = false;

    public boolean isFreed = false;
    public int numOfNearbyMines = 0;
}
