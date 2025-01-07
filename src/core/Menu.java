// Sources:
// Java Swing for Pop-Up window: https://stackoverflow.com/questions/8852560/how-to-make-popup-window-in-java


package core;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import java.awt.Font;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Menu {
    private static Clip clip;
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final int roomNum = 19;

    public static void playMenu() throws IOException {
        displayMenu();
        char input = getInput();

        if (input == 'N' || input == 'n') {

            String seedString = getSeedInput();
            int seed = Integer.parseInt(seedString);

            TERenderer ter = new TERenderer();
            ter.initialize(WIDTH, HEIGHT);
            RandomGenerator generatedWorld = new RandomGenerator(seed);

            TETile[][] world = generatedWorld.generateWorld(roomNum);

            AvatarInteractor avatar = new AvatarInteractor(world, generatedWorld, 10); //updated constructor that uses
            // randomGenerator
            avatar.playAvatar(RandomGenerator.avatarX, RandomGenerator.avatarY, seed);
        }

        if (input == 'Q' || input == 'q') {
            System.exit(0);
        }

        if (input == 'L' || input == 'l') {

            // Get seed from previously saved world
            List<String> lines;
            try {
                lines = Files.readAllLines(Paths.get("save.txt"));
            } catch (IOException e) {
                System.out.println("Failed to load save file.");
                return;
            }

            int seed = Integer.parseInt(lines.get(0));

            // Get avatar's x-coord from previously saved world
            int Xcoord = Integer.parseInt(lines.get(1));

            // Get avatar's y-coord from previously saved world
            int Ycoord = Integer.parseInt(lines.get(2));
            int score = Integer.parseInt(lines.get(3));

            TERenderer ter = new TERenderer();
            ter.initialize(WIDTH, HEIGHT);
            JOptionPane.showMessageDialog(null, "Your previous game's score was: " + score, "WELCOME BACK!", JOptionPane.INFORMATION_MESSAGE);

            RandomGenerator generatedWorld = new RandomGenerator(seed);

            // room number stays the same for all worlds; thus global
            TETile[][] world = generatedWorld.generateWorld(roomNum);

            // remove original starting point to ensure avatar ONLY exists at its immediate previous world's position
            world[RandomGenerator.avatarX][RandomGenerator.avatarY] = Tileset.FLOOR;
            world[Xcoord][Ycoord] = Tileset.AVATAR;

            AvatarInteractor avatar = new AvatarInteractor(world, generatedWorld, score);
            avatar.playAvatar(Xcoord, Ycoord, seed);
        }

    }

    public static void displayMenu() {
        playMusic();
        StdDraw.clear();

            StdDraw.setCanvasSize(800, 800);

            // Adjust the coordinate scale to suit your menu layout
            StdDraw.setXscale(0, 1);
            StdDraw.setYscale(0, 1);

            Font font = new Font("Arial", Font.ITALIC, 35);
            StdDraw.setFont(font);

            // Clear the canvas to white
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(0.5, 0.8, "(N) New Game");
            StdDraw.text(0.5, 0.5, "(L) Load Game");
            StdDraw.text(0.5, 0.2, "(Q) Quit Game");
            StdDraw.show();
        }

    public static char getInput() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                return StdDraw.nextKeyTyped();
            }
        }

    }

    public static String getSeedInput() {
        String seed = "";
        while (true) {
            displaySeedEntryScreen(seed);
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();

                if (Character.isDigit(key)) {
                    seed += key; // append  digit to the seed
                } else if ((key == 'S' || key == 's') && !seed.isEmpty()) {
                    break; // Return the seed entered
                }
            }
        }
        return seed;
    }

    private static void displaySeedEntryScreen(String seedSoFar) {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.5, 0.7, "Enter Seed Followed by S");
        StdDraw.text(0.5, 0.5, seedSoFar); // Show the seed as it's being typed
        StdDraw.show();
    }

    private static void playMusic() {

        // https://stackoverflow.com/questions/55533682/how-do-i-access-and-play-an-audio-file-from-a-java-resource-class-folder/55535509#55535509
        URL audioUrl = Menu.class.getResource("/Resources/backgroundMusic.wav");
        if (audioUrl == null) {
            System.out.println("Audio file not found");
        } else {
            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioUrl);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }
}

