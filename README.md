# World Exploration Engine

## Description
The **World Exploration Engine** is a Software Engineering project that implements a 2D tile-based world, where users can explore a maze-like environment as an avatar. This competitive, timer-based game allows the user to interact with various objects in the world, navigating through the maze and experiencing dynamic environmental changes.

## Features
1. **Random World Generation**: The game generates unique worlds based on a seed entered by the user, ensuring a fresh experience each time the game is played.
   
2. **Main Menu**: The user is greeted with a main menu, offering options to:
   - Start a new game
   - Load a previously saved game
   - Quit the game

3. **Persistent Progress**: When a user loads a previous game, the world and the avatar's position are restored exactly as they were when the game was last saved.

4. **Dynamic Lighting**: The world contains light sources that can be toggled on or off by a keypress. The intensity of the light diminishes in a gradient as the distance from the source increases, affecting how the world is rendered.

5. **Music Theme**: The game features a dynamic music theme that adds to the atmosphere, immersing the player in the world exploration experience.

6. **Point System & Game Over Conditions**: The game uses a point system to track progress. The player earns points by navigating the maze and interacting with objects. The game ends when:
   - The timer runs out
   - The avatar runs out of strikes

## Installation
To install and run the **World Exploration Engine**, clone the repository and follow the instructions below:

```bash
git clone https://github.com/yourusername/world-exploration-engine.git
cd world-exploration-engine
# Follow the setup instructions specific to your platform
