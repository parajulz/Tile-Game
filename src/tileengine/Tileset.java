package tileengine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you", 0);
    public static final TETile WALL = new TETile(' ', new Color(216, 128, 128), new Color(34, 29, 64),
            "wall", 1);
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black, "floor", 2);
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing", 3);
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass", 4);
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water", 5);
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower", 6);
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door", 7);
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door", 8);
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand", 9);
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain", 10);
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree", 11);

    public static final TETile CELL = new TETile('█', Color.white, Color.black, "cell", 12);
    public static final TETile LIGHT = new TETile('★', Color.white, Color.orange, "light", 13);
    public static final TETile FLOOR_EXTRA_LOW = new TETile('·', new Color(128, 192, 128), new Color(5, 19, 57), "floor2", 14);
    public static final TETile FLOOR_MED_LOW = new TETile('·', new Color(128, 192, 128), new Color(14, 35, 97), "floor3", 15);
    public static final TETile FLOOR_NORMAL_LOW = new TETile('·', new Color(128, 192, 128), new Color(31, 56, 133), "floor3", 15);
    public static final TETile FLOOR_MEDIUM = new TETile('·', new Color(128, 192, 128), new Color(52, 100, 189), "floor3", 15);
    public static final TETile FLOOR_BRIGHT = new TETile('·', new Color(128, 192, 128), new Color(71, 141, 220), "floor3", 15);
    public static final TETile FLOOR_SUPER_BRIGHT = new TETile('·', new Color(128, 192, 128), new Color(96, 168, 250), "floor3", 15);


}


