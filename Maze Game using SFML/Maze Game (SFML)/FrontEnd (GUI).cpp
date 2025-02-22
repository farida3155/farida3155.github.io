#include <SFML/Graphics.hpp>
#include <SFML/Window.hpp>
#include <iostream>
#include <string>
#include <unordered_map>
#include "mymaze.h"

using namespace std;
mymaze mazeGame;

class Button {
public:
    sf::RectangleShape shape;
    sf::Text text;

    bool isClicked(sf::Vector2i mousePos) {
        return shape.getGlobalBounds().contains(sf::Vector2f(mousePos));
    }
};

void displayMainMenu(sf::RenderWindow& window, Button buttons[], int numButtons) {
    window.clear(sf::Color(240,234,214));

    for (int i = 0; i < numButtons; ++i) {
        window.draw(buttons[i].shape);
        window.draw(buttons[i].text);
    }

    window.display();
}


void initializeButtons(Button buttons[], int& numButtons, sf::Font& font) {
    // Button for Random Game
    buttons[0].shape.setSize(sf::Vector2f(350, 100));
    buttons[0].shape.setPosition(435, 200);
    buttons[0].shape.setFillColor(sf::Color(196,184,154));
    buttons[0].text.setFont(font);
    buttons[0].text.setString("Random Game");
    buttons[0].text.setCharacterSize(30);
    buttons[0].text.setStyle(sf::Text::Bold);
    buttons[0].text.setFillColor(sf::Color::White);
    buttons[0].text.setPosition(520, 225);

    // Button for Levels
    buttons[1].shape.setSize(sf::Vector2f(350, 100));
    buttons[1].shape.setPosition(435, 350);
    buttons[1].shape.setFillColor(sf::Color(135,206,235));
    buttons[1].text.setFont(font);
    buttons[1].text.setString("Levels");
    buttons[1].text.setCharacterSize(30);
    buttons[1].text.setStyle(sf::Text::Bold);
    buttons[1].text.setFillColor(sf::Color::White);
    buttons[1].text.setPosition(550, 375);

    // Button for Create Game
    buttons[2].shape.setSize(sf::Vector2f(350, 100));
    buttons[2].shape.setPosition(435, 500);
    buttons[2].shape.setFillColor(sf::Color(196,184,154));
    buttons[2].text.setFont(font);
    buttons[2].text.setString("Create Game");
    buttons[2].text.setCharacterSize(30);
    buttons[2].text.setStyle(sf::Text::Bold);
    buttons[2].text.setFillColor(sf::Color::White);
    buttons[2].text.setPosition(525, 525);

    // Button for Exit
    buttons[3].shape.setSize(sf::Vector2f(350, 100));
    buttons[3].shape.setPosition(435, 650);
    buttons[3].shape.setFillColor(sf::Color(128,0,32));
    buttons[3].text.setFont(font);
    buttons[3].text.setString("Exit");
    buttons[3].text.setCharacterSize(30);
    buttons[3].text.setStyle(sf::Text::Bold);
    buttons[3].text.setFillColor(sf::Color::White);
    buttons[3].text.setPosition(570, 675);

    numButtons = 4;
}

void displayEntryPage(sf::RenderWindow& window, sf::Text& logoText, Button& playButton) {
    window.clear(sf::Color(240, 234, 214));

    window.draw(logoText);
    window.draw(playButton.shape);
    window.draw(playButton.text);

    window.display();
}

void initializeEntryPage(sf::Font& font, sf::Text& logoText, Button& playButton) {
    // Logo Text
    logoText.setFont(font);
    logoText.setString("THE MAZE");
    logoText.setStyle(sf::Text::Bold);
    logoText.setCharacterSize(80);
    logoText.setFillColor(sf::Color::Black);
    logoText.setPosition(435, 200);

    // Play Button
    playButton.shape.setSize(sf::Vector2f(200, 100));
    playButton.shape.setPosition(515, 370);
    playButton.shape.setFillColor(sf::Color(0,100,0));
    playButton.text.setFont(font);
    playButton.text.setString("Play");
    playButton.text.setStyle(sf::Text::Bold);
    playButton.text.setCharacterSize(40);
    playButton.text.setFillColor(sf::Color::White);
    playButton.text.setPosition(575, 390);
}
void mymaze::editMazeGUI(sf::RenderWindow& window, unordered_map<mymaze::Point, char, mymaze::PointHash>& maze, int w, int h, mymaze::Point& S, mymaze::Point& P, mymaze::Point& E, int cellSize) {
    bool editing = true;

    sf::Font font;
    if (!font.loadFromFile("C:/Users/habib/OneDrive/Documents/SFML-2.6.2-windows-vc17-64-bit/SFML-2.6.2/examples/island/resources/tuffy.ttf")) {
        std::cerr << "Error loading font\n";
        return;
    }

    sf::Text editText;
    editText.setFont(font);
    editText.setCharacterSize(20);
    editText.setFillColor(sf::Color::White);
    editText.setString("Click on the maze to edit walls. Press Enter when done.");
    editText.setPosition(10, 10);

    // Calculate the size of the maze in pixels
    int mazePixelWidth = w * cellSize;
    int mazePixelHeight = h * cellSize;

    // Calculate the offsets to center the maze
    int offsetX = (window.getSize().x - mazePixelWidth) / 2;
    int offsetY = (window.getSize().y - mazePixelHeight) / 2;

    while (editing && window.isOpen()) {
        sf::Event event;
        bool redraw = false;

        // Handle events
        while (window.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                window.close();
                return;
            }
            mazeGame.displayMazeGUI(window, maze, w, h, P, S, E, nullptr, 0);
            if (event.type == sf::Event::MouseButtonPressed) {
                if (event.mouseButton.button == sf::Mouse::Left) {
                    int x = (event.mouseButton.x - offsetX) / cellSize;
                    int y = (event.mouseButton.y - offsetY) / cellSize;
                    if (x > 0 && x < w - 1 && y > 0 && y < h - 1) {
                        maze[{x, y}] = (maze[{x, y}] == '#') ? '.' : '#';
                        redraw = true; // Trigger redraw for the updated maze
                    }
                }
            }
            else if (event.type == sf::Event::KeyPressed && event.key.code == sf::Keyboard::Enter) {
                editing = false;
            }
        }

        // Redraw the maze only when needed
        if (redraw) {
            window.clear();
            mazeGame.displayMazeGUI(window, maze, w, h, P, S, E, nullptr, 0);
            window.draw(editText);
            window.display();
            redraw = false; // Reset redraw flag
        }
    }

    // Ensure the final state is displayed after editing is complete
    window.clear();
    mazeGame.displayMazeGUI(window, maze, w, h, P, S, E, nullptr, 0);
    window.draw(editText);
    window.display();
}



void handleMainMenuClick(sf::RenderWindow& window, Button buttons[], int numButtons) {
    unordered_map<mymaze::Point, char, mymaze::PointHash> maze, mazeCopy;
    mymaze::Point S, P, E;
    int w = 15, h = 15;
    mymaze::Point* path = nullptr;
    int pathLength = 0;
    mymaze::Level* levels = nullptr;
    int numLevels = 0;
    bool mazeValid = false;
    sf::Text promptText;
    sf::Font font;
    sf::Vector2i mousePos = sf::Mouse::getPosition(window);
  
    for (int i = 0; i < numButtons; ++i) {
        
  
        if (buttons[i].isClicked(mousePos)) {
            switch (i) {
            case 0: { // Random Game
                std::cout << "Random Game selected" << std::endl;

                // Keep generating the maze until a valid path is found
                do {
                    mazeGame.generateMaze(maze, w, h, S, P, E);
                    mazeCopy = maze; // Create a copy of the maze for pathfinding
                    delete[] path; // Clean up previous path if any
                    path = mazeGame.findPathBFS(mazeCopy, w, h, S, E, pathLength);
                } while (path == nullptr);

                mazeGame.displayMazeGUI(window, maze, w, h, P, S, E, nullptr, 0); // Display maze without the path

                bool playing = true;
                bool won = false;
                while (playing && window.isOpen()) {
                    sf::Event event;
                    while (window.pollEvent(event)) {
                        if (event.type == sf::Event::Closed) {
                            window.close();
                        }
                        if (event.type == sf::Event::KeyPressed) {
                            mymaze::Point newPosition = P;

                            // Player movement controls
                            if (event.key.code == sf::Keyboard::Up) newPosition.y -= 1;
                            else if (event.key.code == sf::Keyboard::Down) newPosition.y += 1;
                            else if (event.key.code == sf::Keyboard::Left) newPosition.x -= 1;
                            else if (event.key.code == sf::Keyboard::Right) newPosition.x += 1;
                            else if (event.key.code == sf::Keyboard::Escape) playing = false;

                            // Check if the new position is within bounds and is an empty space
                            if (mazeGame.isValid(newPosition, w, h, maze)) {
                                P = newPosition; // Move the player to the new position
                                if (P == E) {
                                    won = true;
                                    playing = false;
                                }
                            }

                            mazeGame.displayMazeGUI(window, maze, w, h, P, S, E, nullptr, 0); // Redraw the maze without the path
                        }
                    }
                }
                if (won) {
                    std::cout << "YOU WON!" << std::endl;
                    // Display a message on the screen
                    sf::Text winText;
                    sf::Font font;
                    if (!font.loadFromFile("C:/Users/habib/OneDrive/Documents/SFML-2.6.2-windows-vc17-64-bit/SFML-2.6.2/examples/island/resources/tuffy.ttf")) {
                        std::cerr << "Error loading font\n";
                        break;
                    }
                    winText.setFont(font);
                    winText.setCharacterSize(60);
                    winText.setFillColor(sf::Color::White);
                    winText.setStyle(sf::Text::Bold);
                    winText.setString("                  YOU WON!!!\n Press Enter to return to Main Menu.");
                    winText.setPosition(200, 350);
                    window.clear(sf::Color(196, 184, 154));
                    window.draw(winText);
                    window.display();

                    // Wait for user to press Enter to play again
                    bool waitForEnter = true;
                    while (waitForEnter && window.isOpen()) {
                        sf::Event event;
                        while (window.pollEvent(event)) {
                            if (event.type == sf::Event::Closed) {
                                window.close();
                                waitForEnter = false;
                            }
                            else if (event.type == sf::Event::KeyPressed && event.key.code == sf::Keyboard::Enter) {
                                waitForEnter = false;
                            }
                        }
                    }
                }
                delete[] path; // Clean up when done
                break;
            }
            case 1: { // Levels
                std::cout << "Levels selected" << std::endl;

                mazeGame.loadLevels(levels, numLevels);
                std::cout << "Number of levels loaded: " << numLevels << std::endl; // Debugging output

                bool playAgain = true;

                while (playAgain && window.isOpen()) {
                    for (int currentLevel = 0; currentLevel < numLevels; ++currentLevel) {
                        std::cout << "Playing level " << (currentLevel + 1) << std::endl; // Debugging output

                        mymaze::Point P = { 1, 1 };
                        mymaze::Point S = { 1, 0 };
                        mymaze::Point E = mazeGame.findEnd(levels[currentLevel]);
                        int pathLength = 0;
                        mymaze::Point* path = nullptr;

                        mazeGame.displayLevelGUI(window, levels[currentLevel], P, S, E, currentLevel + 1);

                        bool giveUp = false;
                        bool levelCompleted = false;
                        while (window.isOpen() && !levelCompleted) {
                            sf::Event event;
                            while (window.pollEvent(event)) {
                                if (event.type == sf::Event::Closed) {
                                    window.close();
                                    return;
                                }

                                if (event.type == sf::Event::KeyPressed) {
                                    mymaze::Point next = P;

                                    // Player movement controls
                                    if (event.key.code == sf::Keyboard::Up) next.y -= 1;
                                    else if (event.key.code == sf::Keyboard::Down) next.y += 1;
                                    else if (event.key.code == sf::Keyboard::Left) next.x -= 1;
                                    else if (event.key.code == sf::Keyboard::Right) next.x += 1;
                                    else if (event.key.code == sf::Keyboard::Escape) {
                                        playAgain = false;
                                        window.clear();
                                        // Return to main menu by breaking out of all loops
                                        return;
                                    }
                                    else if (event.key.code == sf::Keyboard::Enter) {
                                        levelCompleted = true;
                                        break;
                                    }

                                    if (mazeGame.isValidLevel(next, levels[currentLevel])) {
                                        P = next;
                                        mazeGame.displayLevelGUI(window, levels[currentLevel], P, S, E, currentLevel + 1);
                                    }

                                    if (P == E) {
                                        levelCompleted = true;
                                        std::cout << "YOU WIN!!!!\n";
                                    }
                                }
                            }
                        }

                        if (!playAgain) return; // Exit the level loop if the user pressed Escape

                        if (giveUp) {
                            path = mazeGame.findLevelPath(levels[currentLevel], S, E, pathLength);
                            if (path) {
                                mazeGame.displayLevelAnswerGUI(window, levels[currentLevel], path, pathLength);
                                delete[] path;
                            }
                            else {
                                std::cout << "No solution found!\n";
                            }
                            break;
                        }

                        if (currentLevel == numLevels - 1) {
                            std::cout << "CONGRATULATIONS! YOU COMPLETED ALL LEVELS!\n";
                        }
                        else {
                            // Display message to proceed to next level
                            sf::Text nextLevelText;
                            sf::Font font;
                            if (!font.loadFromFile("C:/Users/habib/OneDrive/Documents/SFML-2.6.2-windows-vc17-64-bit/SFML-2.6.2/examples/island/resources/tuffy.ttf")) {
                                std::cerr << "Error loading font\n";
                                break;
                            }
                            nextLevelText.setFont(font);
                            nextLevelText.setCharacterSize(60);
                            nextLevelText.setStyle(sf::Text::Bold);
                            nextLevelText.setFillColor(sf::Color::White);
                            nextLevelText.setString("       YOU WON!!! \nPress Enter to Proceed :D!\nPress Escape to Exit :( ..");
                            nextLevelText.setPosition(350, 350);
                            window.clear(sf::Color(135, 206, 225));
                            window.draw(nextLevelText);
                            window.display();

                            // Wait for user input to proceed or exit
                            bool waitForInput = true;
                            while (waitForInput && window.isOpen()) {
                                sf::Event event;
                                while (window.pollEvent(event)) {
                                    if (event.type == sf::Event::Closed) {
                                        window.close();
                                        return;
                                    }
                                    else if (event.type == sf::Event::KeyPressed) {
                                        if (event.key.code == sf::Keyboard::Escape) {
                                            playAgain = false;
                                            waitForInput = false;
                                            return; // Exit to main menu
                                        }
                                        else if (event.key.code == sf::Keyboard::Enter) {
                                            waitForInput = false;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    sf::Event event;
                    bool validInput = false;
                    while (!validInput && window.isOpen()) {
                        while (window.pollEvent(event)) {
                            if (event.type == sf::Event::Closed) {
                                window.close();
                                return;
                            }
                            if (event.type == sf::Event::KeyPressed) {
                                if (event.key.code == sf::Keyboard::Y) {
                                    playAgain = true;
                                    validInput = true;
                                }
                                else if (event.key.code == sf::Keyboard::N) {
                                    playAgain = false;
                                    validInput = true;
                                }
                            }
                        }
                    }
                }

                delete[] levels; // Clean up when done
                break;
            }

            case 2: { // Create Game
                std::cout << "Create Game selected" << std::endl;

                mazeValid = false;
                mazeGame.generateMaze(maze, w, h, S, P, E);

                sf::Font font;
                if (!font.loadFromFile("C:/Users/habib/OneDrive/Documents/SFML-2.6.2-windows-vc17-64-bit/SFML-2.6.2/examples/island/resources/tuffy.ttf")) {
                    std::cerr << "Error loading font\n";
                    break;
                }

                while (window.isOpen()) {
                    mazeGame.editMazeGUI(window, maze, w, h, S, P, E, 50);

                    std::cout << "Checking if the maze is valid..." << std::endl;
                    path = mazeGame.findPathBFS(maze, w, h, S, E, pathLength);

                    sf::Text promptText;
                    promptText.setFont(font);
                    promptText.setCharacterSize(40);
                    promptText.setPosition(10, h * 50 + 120); // Display the text below the maze

                    if (path != nullptr) {
                        mazeValid = true;
                        promptText.setFillColor(sf::Color(0,100,0));
                        promptText.setString("Maze is valid! Press Enter to start the game.");
                        delete[] path;
                        path = nullptr;
                    }
                    else {
                        mazeValid = false;
                        promptText.setFillColor(sf::Color(128,0,32));
                        promptText.setString("Maze is invalid! Do you want to edit again? Press Y for Yes or N for No.");
                    }

                    window.clear();
                    mazeGame.displayMazeGUI(window, maze, w, h, P, S, E, nullptr, 0);
                    window.draw(promptText);
                    window.display();

                    bool choiceMade = false;
                    while (!choiceMade && window.isOpen()) {
                        sf::Event event;
                        while (window.pollEvent(event)) {
                            if (event.type == sf::Event::Closed) {
                                window.close();
                                return;
                            }
                            else if (event.type == sf::Event::KeyPressed) {
                                if (!mazeValid) {
                                    if (event.key.code == sf::Keyboard::Y) {
                                        choiceMade = true;
                                        mazeGame.editMazeGUI(window, maze, w, h, S, P, E, 50);
                                    }
                                    else if (event.key.code == sf::Keyboard::N) {
                                        choiceMade = true;
                                        return;
                                    }
                                }
                                else {
                                    if (event.key.code == sf::Keyboard::Enter) {
                                        choiceMade = true;
                                    }
                                }
                            }
                        }
                    }

                    bool playing = true;
                    while (playing && window.isOpen()) {
                        sf::Event event;
                        while (window.pollEvent(event)) {
                            if (event.type == sf::Event::Closed) {
                                window.close();
                                return;
                            }
                            if (event.type == sf::Event::KeyPressed) {
                                mymaze::Point newPosition = P;
                                if (event.key.code == sf::Keyboard::Up) newPosition.y -= 1;
                                else if (event.key.code == sf::Keyboard::Down) newPosition.y += 1;
                                else if (event.key.code == sf::Keyboard::Left) newPosition.x -= 1;
                                else if (event.key.code == sf::Keyboard::Right) newPosition.x += 1;
                                else if (event.key.code == sf::Keyboard::Escape) playing = false;

                                if (mazeGame.isValid(newPosition, w, h, maze)) {
                                    P = newPosition;
                                    if (P == E) {
                                        playing = false;
                                        promptText.setFillColor(sf::Color(0,100,0));
                                        promptText.setString("YOU WON!!! Press Enter to play again or M to return to the main menu.");
                                        window.clear();
                                        mazeGame.displayMazeGUI(window, maze, w, h, P, S, E, nullptr, 0);
                                        window.draw(promptText);
                                        window.display();

                                        // Wait for user input to decide the next action
                                        bool waitForChoice = true;
                                        while (waitForChoice && window.isOpen()) {
                                            sf::Event event;
                                            while (window.pollEvent(event)) {
                                                if (event.type == sf::Event::Closed) {
                                                    window.close();
                                                    return;
                                                }
                                                else if (event.type == sf::Event::KeyPressed) {
                                                    if (event.key.code == sf::Keyboard::Enter) {
                                                        waitForChoice = false; // Generate a new game
                                                        mazeGame.generateMaze(maze, w, h, S, P, E);
                                                        playing = true; // Restart the gameplay loop
                                                    }
                                                    else if (event.key.code == sf::Keyboard::M) {
                                                        waitForChoice = false; // Return to the main menu
                                                        return;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            mazeGame.displayMazeGUI(window, maze, w, h, P, S, E, nullptr, 0);
                        }
                    }
                }
                break;
            }

            case 3: // Exit
                std::cout << "Exit selected" << std::endl;
                window.close();
                break;

                return; // Ensure only one button is processed

            }
        }
    }
}

int main() {
    sf::RenderWindow window(sf::VideoMode(1280, 960), "Maze Game");

    sf::Font font;
    if (!font.loadFromFile("C:/Users/habib/OneDrive/Documents/SFML-2.6.2-windows-vc17-64-bit/SFML-2.6.2/examples/island/resources/tuffy.ttf")) {
        std::cerr << "Error loading font\n";
        return -1;
    }

    // Initialize entry page
    sf::Text logoText;
    Button playButton;
    initializeEntryPage(font, logoText, playButton);

    // Main menu buttons
    Button buttons[4];
    int numButtons;
    initializeButtons(buttons, numButtons, font);

    bool showMainMenu = false;

    while (window.isOpen()) {
        sf::Event event;
        while (window.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                window.close();
            }
        }

        if (showMainMenu) {
            displayMainMenu(window, buttons, numButtons);
            if (sf::Mouse::isButtonPressed(sf::Mouse::Left)) {
                handleMainMenuClick(window, buttons, numButtons);
            }
        }
        else {
            displayEntryPage(window, logoText, playButton);
            if (sf::Mouse::isButtonPressed(sf::Mouse::Left) && playButton.isClicked(sf::Mouse::getPosition(window))) {
                showMainMenu = true;
            }
        }
    }

    return 0;
}
