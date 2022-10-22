package battleship;

public class Ship {

    private boolean isAlive;
    private boolean gotShot;
    private int[] coordinates;
    private int[] shotAt;
    private int size;

    public Ship (int[] coordinates, int size){
        this.isAlive = true;
        this.coordinates = coordinates;
        this.shotAt = new int[size];
        this.size = size;
    }

    public boolean repeatedShot(int coordinate) {
        for (int i = 0; i < this.size; i++) {
            if (this.coordinates[i] == coordinate) {
                if (this.shotAt[i] == -1) return true;
            }
        }
        return false;
    }
    public boolean gotShot(int coordinate) {
        // reinitialize values
        this.gotShot = false;

        // check whether got shot at and whether still alive
        for (int i = 0; i < this.size; i++) {
            if (this.coordinates[i] == coordinate) {
                this.shotAt[i] = -1;
                this.gotShot = true;
            }
        }
        return this.gotShot;
    }

    public boolean isAlive() {
        this.isAlive = false;
        for (int i = 0; i < this.size; i++) {
            if (this.shotAt[i] != -1) {
                isAlive = true;
            }
        }
        return isAlive;
    }

}
