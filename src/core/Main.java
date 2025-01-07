package core;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import static core.RandomGenerator.HEIGHT;
import static core.RandomGenerator.WIDTH;

public class Main {


    public static void main(String[] args) throws IOException {

        Menu theMenu = new Menu();
        theMenu.playMenu();
    }
}







