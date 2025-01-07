package core;
import java.util.Random;

class Room {
    int x, y; //bottom left corner, remember
    int width, height;
    boolean roomIsLit;
    int[] lightPoint;

    public Room(int x, int y, int width, int height, Random r) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.roomIsLit = true;
        this.lightPoint = getExitPoint(r);

    }

    // randomly generate an exit point for the room
    // we want our hallways to connect to exit points
    // exit is the final destination a hallway wants to reach to
    // randomly placed so that it's not systematic
    public int[] getExitPoint(Random r) {
        // randomly select any floor tile inside the room
        int xPoint = x + 1 + r.nextInt(width - 2); // random x-coordinate inside the room
        int yPoint = y + 1 + r.nextInt(height - 2); // random y-coordinate inside the room
        return new int[]{xPoint, yPoint};
    }

     // y + 1/ x + 1  bc it slides the random point up one tile
    // without this a random point can be ON the room's perimeter
    //we do not watch that, can cause a floor tile to be exposed to black tiles
    // - 2 ensures that the selection stops one tile before the right/left and top/bottom walls.







}
