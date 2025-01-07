package core;

import javax.swing.JOptionPane;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import java.io.IOException;
import java.nio.file.Files;
import java.awt.Font;

import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static core.RandomGenerator.*;


public class AvatarInteractor {
    AutograderBuddy helpingOut = new AutograderBuddy();

    TETile[][] avatar;
    RandomGenerator randomGenerator;
    public static int totalScore;
    private boolean wallHitDisplayed = false; // prevents repeated messages bc of while loop (there for timer)


    //I added RandomGenerator into the constructor of the AvatarInteractor to be able to access
    // the rooms list from AvatarInteractor and important methods like offTheLights and addTheLights
    // why is it important?
    // toggleLight uses those functions to turn off and on light
    // and toggleLight is used when the user presses F or f

    public AvatarInteractor(TETile[][] avatar, RandomGenerator randomGenerator, int totalScore) {
        this.avatar = avatar;
        this.randomGenerator =  randomGenerator;
        this.totalScore = totalScore;
    }

    private boolean isPlayerInRoom(int avatarX, int avatarY, Room room) {
        return avatarX > room.x && avatarX < room.x + room.width - 1 &&
                avatarY > room.y && avatarY < room.y + room.height - 1;
    }

    private void toggleLight(Room room) {
        if (room.roomIsLit) {
            // Turn off the light
            randomGenerator.offTheLights(room);
        } else {
            // Turn on the light
            randomGenerator.addTheLights(room);
        }

        room.roomIsLit = !room.roomIsLit; // Toggle the light state
    }

    public void playAvatar(int avatar_x, int avatar_y, int seed) throws IOException {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile previousTile = Tileset.FLOOR;

        avatar[avatar_x][avatar_y] = Tileset.AVATAR;
        ter.renderFrame(avatar);

        String input = "";

        // Timer settings
        long startTime = System.currentTimeMillis();
        int maxTimeInSeconds = 30; // Set the maximum time allowed, e.g., 5 minutes (300 seconds)

        // Game loop
        while (true) {
            // Calculate elapsed time
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            int timeRemaining = maxTimeInSeconds - (int) elapsedTime;
            StdDraw.setPenColor(StdDraw.BLACK); // Match the background color
            StdDraw.filledRectangle(WIDTH / 2.0, HEIGHT - 2, 2, 2);

            if (roomCount <= 0) {
                JOptionPane.showMessageDialog(null, "You've turned off all the lights. You win!", "Click to play again", JOptionPane.INFORMATION_MESSAGE);
                Menu.playMenu(); // Change this to your game-over handling method
                return;
            }

            if (totalScore <= 0) {
                JOptionPane.showMessageDialog(null, "You've hit too many walls. You lose!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                Menu.playMenu(); // Change this to your game-over handling method
                return;
            }


            StdDraw.setFont(new Font("Arial", Font.BOLD, 15));
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(WIDTH / 2.0, HEIGHT - 2, "Time: " + timeRemaining + "s");
            StdDraw.show();

            // Check if user has entered a key
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                //wallHitDisplayed = false;
                input += key;

                if (input.length() >= 2 && (input.endsWith(":Q") || input.endsWith(":q"))) {
                    String seedString = Integer.toString(seed);
                    String avatarXString = Integer.toString(avatar_x);
                    String avatarYString = Integer.toString(avatar_y);
                    String totalScoreString = Integer.toString(totalScore);

                    // Writing Strings into txt file
                    try {
                        Files.write(Paths.get("save.txt"), ("").getBytes()); // Clear data from any previous worlds
                        Files.write(Paths.get("save.txt"), (seedString + "\n").getBytes(), StandardOpenOption.APPEND);
                        Files.write(Paths.get("save.txt"), (avatarXString + "\n").getBytes(), StandardOpenOption.APPEND);
                        Files.write(Paths.get("save.txt"), (avatarYString + "\n").getBytes(), StandardOpenOption.APPEND);
                        Files.write(Paths.get("save.txt"), (totalScoreString + "\n").getBytes(), StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        System.out.println("Error");
                    }
                    System.exit(0);
                }

                // Avatar movement and interaction logic
                if (key == 'W' || key == 'w') { // Move up
                    if (avatar_y + 1 < HEIGHT && helpingOut.isGroundTile(avatar[avatar_x][avatar_y + 1])) {
                        avatar[avatar_x][avatar_y] = previousTile;
                        previousTile = avatar[avatar_x][avatar_y + 1];
                        avatar_y++;
                        avatar[avatar_x][avatar_y] = Tileset.AVATAR;
                        ter.renderFrame(avatar);
                        wallHitDisplayed = false; // reset from previous wall hit
                    } else {
                        totalScore--;
                        if (totalScore <= 0) {
                            JOptionPane.showMessageDialog(null, "Score: 0", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
                            Menu.playMenu();
                            return;
                        } else {
                            hitWall();
                            wallHitDisplayed = true;
                        }
                    }
                }


                if (key == 'A' || key == 'a') { // Move left
                    if (avatar_x - 1 >= 0 && helpingOut.isGroundTile(avatar[avatar_x-1][avatar_y])) {
                        avatar[avatar_x][avatar_y] = previousTile;
                        previousTile = avatar[avatar_x - 1][avatar_y];
                        avatar_x--;
                        avatar[avatar_x][avatar_y] = Tileset.AVATAR;
                        ter.renderFrame(avatar);
                        wallHitDisplayed = false; // reset from previous wall hit
                    } else {
                        totalScore--;
                        if (totalScore <= 0) {
                            JOptionPane.showMessageDialog(null, "Score: 0", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
                            Menu.playMenu();
                        } else {
                            if (!wallHitDisplayed) {
                                hitWall();
                                wallHitDisplayed = true; // reset display to prevent repeated msgs
                            }
                        }
                    }
                }


                if (key == 'S' || key == 's') { // Move down
                    if (avatar_y - 1 >= 0 && helpingOut.isGroundTile(avatar[avatar_x][avatar_y - 1])) {
                        avatar[avatar_x][avatar_y] = previousTile;
                        previousTile = avatar[avatar_x][avatar_y - 1];
                        avatar_y--;
                        avatar[avatar_x][avatar_y] = Tileset.AVATAR;
                        ter.renderFrame(avatar);
                        wallHitDisplayed = false; // reset from previous wall hit
                    } else {
                        totalScore--;
                        if (totalScore <= 0) {
                            JOptionPane.showMessageDialog(null, "Score: 0", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
                            Menu.playMenu();
                        } else {
                            if (!wallHitDisplayed) {
                                hitWall();
                                wallHitDisplayed = true; // reset display to prevent repeated msgs
                            }
                        }
                    }
                }

                if (key == 'D' || key == 'd') { // Move right
                    if (avatar_x + 1 < WIDTH && helpingOut.isGroundTile(avatar[avatar_x + 1][avatar_y])) {
                        avatar[avatar_x][avatar_y] = previousTile;
                        previousTile = avatar[avatar_x + 1][avatar_y];
                        avatar_x++;
                        avatar[avatar_x][avatar_y] = Tileset.AVATAR;
                        ter.renderFrame(avatar);
                        wallHitDisplayed = false; // reset from previous wall hit
                    } else {
                        totalScore--;
                        if (totalScore <= 0) {
                            JOptionPane.showMessageDialog(null, "Score: 0", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
                            Menu.playMenu();
                        } else {
                            if (!wallHitDisplayed) {
                                hitWall();
                                wallHitDisplayed = true; // reset display to prevent repeated msgs
                            }
                        }
                    }
                }

                // (Repeat similar logic for 'A', 'S', 'D' keys)

                if (key == 'F' || key == 'f') {
                    for (Room room : randomGenerator.rooms) {
                        if (isPlayerInRoom(avatar_x, avatar_y, room)) {
                            int[] lightPoint = room.lightPoint;
                            if (avatar_x == lightPoint[0] && avatar_y == lightPoint[1]) {
                                toggleLight(room);
                                roomCount--;// Toggle lights
                                ter.renderFrame(avatar);
                            }
                        }
                    }
                }
            }
        }
    }




    public void hitWall() {
        String message =
                "YOU'VE HIT A WALL! \n"
                + "      -1 POINTS \n"
                + "Total Score Remaining: " + totalScore;
        JOptionPane.showMessageDialog(null, message, "WARNING", JOptionPane.WARNING_MESSAGE);
    }
}