package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.Tileset;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class RandomGenerator {
    TETile[][] world;
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    public static int avatarX;
    public static int avatarY;
    public final List<Room> rooms = new ArrayList<>();
    private final List<Hallway> hallways = new ArrayList<>();
    private List<int[]> floors = new ArrayList<>();
    private static Random r;
    public static int roomCount;

    public RandomGenerator(int specificSeed) {
        world = new TETile[WIDTH][HEIGHT];
        r = new Random(specificSeed);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    public TETile[][] generateWorld(int numberOfRooms) { // actually generates the world with stuff
            // Java Swing for Pop-Up window: https://stackoverflow.com/questions/8852560/how-to-make-popup-window-in-java

        String instructions = "Instructions:\n"
                    + "- Press W & S to move up and down\n"
                    + "- Press A & D to move left and right\n"
                    + "- Press F to turn a light ON and OFF \n"
                + "*** If you hit a wall more than 10x you lose! *****";

        JOptionPane.showMessageDialog(null, instructions, "Game Instructions", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, "It's time to sleep! You have 30 secs to turn off all the lights. \n PRESS OK to start game", "Welcome!", JOptionPane.INFORMATION_MESSAGE);

            roomCount = 0; // Counter for added rooms
            while (roomCount < numberOfRooms) {
                if (addRoom()) {
                    roomCount++;
                }
            }
            connectRooms();
            for (Room room : rooms) { // Iterate through the list of rooms
                fixRoomInterior(room);
                addTheLights(room);
                //addGradient(room);
            }
            for (Hallway hallway: hallways) { // Iterate through the list of rooms
                fixHallwayInterior(hallway);
            }
            addAvatar();

        return world;
    }

    public static boolean exitGame() {
        String input = "";

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                input += key;

                if (input.equals(":q") || input.equals(":Q")) {
                    return true;
                }

                if (input.length() > 2) {
                    input = "";
                }
            }
        }
    }


    public boolean addRoom() {
        int roomWidth = r.nextInt(10) + 5; // room width (4-13 tiles)
        int roomHeight = r.nextInt(10) + 5; // room height (4-13 tiles)
        int x = r.nextInt(WIDTH - roomWidth); // random x-coordinate
        // HEIGHT - 2 to account for Timer Display
        int y = r.nextInt(HEIGHT - 2 - roomHeight); // random y-coordinate

        // over-lap? nope
        for (int i = x; i < x + roomWidth; i++) {
            for (int j = y; j < y + roomHeight; j++) {
                if (world[i][j] != Tileset.NOTHING) {
                    return false; // overlap detected, don't put a room there
                }
            }
        }

        // place that  wall girl
        for (int i = x; i < x + roomWidth; i++) {
            world[i][y] = Tileset.WALL; // bottom wall
            world[i][y + roomHeight - 1] = Tileset.WALL; // top wall
        }
        for (int j = y; j < y + roomHeight; j++) {
            world[x][j] = Tileset.WALL; // left wall
            world[x + roomWidth - 1][j] = Tileset.WALL; // right wall
        }

        // fill inside room with floor tiles

        for (int i = x + 1; i < x + roomWidth - 1; i++) {
            for (int j = y + 1; j < y + roomHeight - 1; j++) {
                world[i][j] = Tileset.FLOOR;
                floors.add(new int[] {i,j});
            }
        }

        rooms.add(new Room(x, y, roomWidth, roomHeight, r));
        return true;
    }

    private void addAvatar() {

        int[] randomTile = floors.get(r.nextInt(floors.size()));
        avatarX = randomTile[0];
        avatarY = randomTile[1];
        world[avatarX][avatarY] = Tileset.AVATAR;
    }

    private void connectRooms() {
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room room1 = rooms.get(i); // get room
            Room room2 = rooms.get(i + 1); // get another room

            // get random exit and entry points for the rooms
            // lightPoint returns a coordinate like this [x,y]
            // index 0 being x and index 1 is y
            int[] exit1 = room1.lightPoint;
            int[] entry2 = room2.lightPoint;
            // checks to see if their exit points have the same x coordinate
            // these rooms have same x coordinate but different y coordinates
            // so they perfectly above/below each other
            // so just add a vertical hallway!
            if (exit1[0] == entry2[0]) {
                // vertical hallway
                addVerticalHallway(exit1[1], entry2[1], exit1[0]);
                // same logic for reverse. same y coordinate but different x coordinates. add a horizontal hallway
            } else if (exit1[1] == entry2[1]) {
                // horizontal hallway
                addHorizontalHallway(exit1[0], entry2[0], exit1[1]);
                // what if they have different x and y coordinates ?
                // in that case, we use L hallways
            } else {
                // vertical hallway first or horizontal hallway first?
                // must be random
                boolean horizontalFirst = r.nextBoolean(); // randomly decide order
                if (horizontalFirst) {
                    // adds a horizontal hallway first and then vertical
                    addHorizontalHallway(exit1[0], entry2[0], exit1[1]); // x
                    addVerticalHallway(exit1[1], entry2[1], entry2[0]);  // y
                } else {
                    // adds a vertical hallway first and then horizontal
                    addVerticalHallway(exit1[1], entry2[1], exit1[0]); // y
                    addHorizontalHallway(exit1[0], entry2[0], entry2[1]); // x
                }
            }
        }
    }

    // we don't check for overlaps because hallways can intersect with other hallway
    // hallways can also intersect/overlap room
    // rooms cannot overlap with other rooms
    private void addVerticalHallway(int yStart, int yEnd, int x) {
        for (int y = Math.min(yStart, yEnd); y <= Math.max(yStart, yEnd); y++) {
            world[x - 1][y] = Tileset.WALL; // left wall
            world[x][y] = Tileset.FLOOR;    // walkable space
            world[x + 1][y] = Tileset.WALL; // right wall
        }
        hallways.add(new Hallway(x, Math.min(yStart, yEnd), x, Math.max(yStart, yEnd))); // add hallway to list of hallways
    }

    private void addHorizontalHallway(int xStart, int xEnd, int y) {
        for (int x = Math.min(xStart, xEnd); x <= Math.max(xStart, xEnd); x++) {
            world[x][y - 1] = Tileset.WALL; // bottom wall
            world[x][y] = Tileset.FLOOR;    // walkable space
            world[x][y + 1] = Tileset.WALL; // top  wall
        }

        hallways.add(new Hallway(Math.min(xStart, xEnd), y, Math.max(xStart, xEnd), y)); // add to list

    }

    private void fixRoomInterior(Room room) {
        // for each room, we check if there are walls inside
        // if there is a wall, replace it with a floor
        for (int i = room.x + 1; i < room.x + room.width - 1; i++) { // excludes left/right walls
            for (int j = room.y + 1; j < room.y + room.height - 1; j++) { // excludes top/bottom walls (so just interior)
                if (world[i][j] == Tileset.WALL) {
                    world[i][j] = Tileset.FLOOR; // convert wall to floor
                }

            }
        }
    }

    private void fixHallwayInterior(Hallway hallway) {
        // iterates over all tiles in the rectangular area defined by hallway's start and end coordinates
        for (int x = Math.min(hallway.startX, hallway.endX); x <= Math.max(hallway.startX, hallway.endX); x++) {
            for (int y = Math.min(hallway.startY, hallway.endY); y <= Math.max(hallway.startY, hallway.endY); y++) {
                // If there is a wall inside the hallway, replace it with a floor
                if (world[x][y] == Tileset.WALL) {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    // we don't have to subtract bounds here like we did for the fixRoomInterior bc a wall is measured by
    // its walkable pathway
    // hai

    public void addTheLights(Room room) {
        int[] lightPoint = room.lightPoint; // get the rooms light point (also exit point)
        world[lightPoint[0]][lightPoint[1]] = Tileset.LIGHT; // set that point as a light

        int maxIntensity = 6;

        for (int x = room.x + 1; x < room.x + room.width - 1; x++) {
            for (int y = room.y + 1; y < room.y + room.height - 1; y++) {
                // @source: https://www.cuemath.com/distance-formula/
                // to find the distance between two points
                int distance = (int) Math.sqrt((double)(x - lightPoint[0])*(x - lightPoint[0]) + (y - lightPoint[1])*(y - lightPoint[1]));
                // determine tile intensity based on distance
                if (distance == 0) {
                    world[x][y] = Tileset.LIGHT; // light source itself
                } else if (distance <= maxIntensity) {
                    // adjust tile appearance on intensity level
                    world[x][y] = ambreTile(distance);
                }
            }
        }
    }

    public void toggleLight(Room room) {
        if (room.roomIsLit) {
            // turn off the light
            offTheLights(room);

        } else {
            // turn on the light
            addTheLights(room);
        }

        room.roomIsLit = !room.roomIsLit; // change light state
    }


    private TETile ambreTile(int intensity) {
         if (intensity==1) {
             return Tileset.FLOOR_SUPER_BRIGHT;
         }
        if (intensity==2) {

            return Tileset.FLOOR_BRIGHT;
        }
        if (intensity==3) {
            return Tileset.FLOOR_MEDIUM;

        }
        if (intensity==4) {
            return Tileset.FLOOR_NORMAL_LOW;

        }
        if (intensity==5) {

            return Tileset.FLOOR_MED_LOW;
        }
        if (intensity==6) {
            return Tileset.FLOOR_EXTRA_LOW;
        }
        return Tileset.FLOOR_EXTRA_LOW;
    }

    //turning the light off by making the ambre around the lights become floors
    public void offTheLights(Room room) {
        for (int x = room.x + 1; x < room.x + room.width - 1; x++) {
            for (int y = room.y + 1; y < room.y + room.height - 1; y++) {
                if (!(world[x][y]==Tileset.LIGHT || world[x][y]==Tileset.AVATAR)){
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }
}





