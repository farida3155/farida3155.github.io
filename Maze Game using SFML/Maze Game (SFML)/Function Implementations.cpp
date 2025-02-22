#include <iostream>
#include <unordered_map>
#include <ctime>
#include <cstdlib>
#include <queue>
#include <cstring>
#include"mymaze.h"

using namespace std;
void mymaze::loadLevels(Level*& levels, int& numLevels) {
    numLevels = 10;
    levels = new Level[numLevels];

    const char* levelData[10][12] = {
        {
            "#S###################",
            "#P.................##",
            "#.################.##",
            "#.................#.#",
            "#########.#########.#",
            "#.................#.#",
            "#.###############.#.#",
            "#.................#.#",
            "#.#######.#########.#",
            "#########E###########"
        },
        {
            "#S####################",
            "#P.................###",
            "#.##################.#",
            "#.............########",
            "#######............###",
            "#.......#####........#",
            "#.....#######........#",
            "#.........#####......#",
            "#..............#######",
            "#.......##############",
            "#....................#",
            "##E###################"
        },
        {
            "#S#################",
            "#P...............##",
            "#.###############.#",
            "#.................#",
            "#####.#############",
            "#.................#",
            "#.###.###########.#",
            "#####E#############"
        },
        {
            "#S###################",
            "#P.................##",
            "#.#################.#",
            "#.................#.#",
            "#########.#########.#",
            "#.................#.#",
            "#.###############.#.#",
            "#.................#.#",
            "#.#######.#########.#",
            "#########E###########"
        },
        {
            "#S#################",
            "#P...............##",
            "#.###############.#",
            "#.................#",
            "#####.#############",
            "#.................#",
            "#.###############.#",
            "#.................#",
            "#####E#############"
        },
        {
            "#S###################",
            "#P.................##",
            "#.#################.#",
            "#.................#.#",
            "#########.#########.#",
            "#.................#.#",
            "#.###############.#.#",
            "#.................#.#",
            "#.#################.#",
            "#.................#.#",
            "#########E###########"
        },
        {
            "#S###################",
            "#P.................##",
            "#.#################.#",
            "#.................#.#",
            "#########.#########.#",
            "#.................#.#",
            "#.###############.#.#",
            "#.................#.#",
            "#.#######.#########.#",
            "#########E###########"
        },
        {
            "#S####################",
            "#P.................###",
            "#.##################.#",
            "#.............########",
            "#######............###",
            "#.......#####........#",
            "#.....#######........#",
            "#.........#####......#",
            "#..............#######",
            "#.......##############",
            "#....................#",
            "##E###################"
        },
        {
            "#S#################",
            "#P...............##",
            "#.###############.#",
            "#.................#",
            "#####.#############",
            "#.................#",
            "#.###############.#",
            "#.................#",
            "#####E#############"
        },
        {
            "#S###################",
            "#P.................##",
            "#.#################.#",
            "#.................#.#",
            "#########.#########.#",
            "#.................#.#",
            "#.###############.#.#",
            "#.................#.#",
            "#.#################.#",
            "#.................#.#",
            "#########E###########"
        }
    };

    for (int i = 0; i < numLevels; ++i) {
        levels[i].height = 0;
        levels[i].width = 0;
        for (int j = 0; j < 12; ++j) {
            if (levelData[i][j] == nullptr) break;
            levels[i].height++;
            levels[i].width = std::max(levels[i].width, static_cast<int>(strlen(levelData[i][j])));
        }
        for (int y = 0; y < levels[i].height; ++y) {
            for (int x = 0; x < levels[i].width; ++x) {
                levels[i].layout[{x, y}] = levelData[i][y][x];
            }
        }
    }
    std::cout << "Levels loaded successfully. Number of levels: " << numLevels << std::endl;
}


void mymaze::cleanupLevels(Level* levels, int numLevels) {
    delete[] levels;
}


bool mymaze:: isValid(const Point& p, int width, int height, const unordered_map<Point, char, PointHash>& maze) {
    if (p.x < 0 || p.x >= width || p.y < 0 || p.y >= height) {
        return false;
    }
    auto it = maze.find(p);
    return it != maze.end() && (it->second == '.' || it->second == 'E'); // Check for path or end
}
bool mymaze:: isValidLevel(Point p, const Level& level) {

    if (p.x < 0 || p.x >= level.width || p.y < 0 || p.y >= level.height)
        return false;


    return level.layout.at(p) != '#';
}

mymaze::Point* mymaze::findPathBFS(const unordered_map<Point, char, PointHash>& maze, int width, int height, const Point& start, const Point& end, int& pathLength) {
    unordered_map<Point, Point, PointHash> cameFrom;
    queue<Point> q;
    q.push(start);
    cameFrom[start] = start;

    Point directions[] = { {0, 1}, {1, 0}, {0, -1}, {-1, 0} };

    while (!q.empty()) {
        Point current = q.front();
        q.pop();

        if (current == end) {
            pathLength = 0;
            Point temp = end;
            while (temp != start) {
                pathLength++;
                temp = cameFrom[temp];
            }
            pathLength++;

            Point* path = new Point[pathLength];
            temp = end;
            for (int i = pathLength - 1; i >= 0; i--) {
                path[i] = temp;
                temp = cameFrom[temp];
            }
            return path;
        }

        for (const auto& dir : directions) {
            Point next = { current.x + dir.x, current.y + dir.y };
            if (isValid(next, width, height, maze) && cameFrom.find(next) == cameFrom.end()) {
                q.push(next);
                cameFrom[next] = current;
            }
        }
    }

    pathLength = 0;
    return nullptr;
}
mymaze::Point* mymaze:: findLevelPath(const Level& level, const Point& start, const Point& end, int& pathLength) {
    unordered_map<Point, Point, PointHash> cameFrom; // Tracks where each Point came from
    queue<Point> q;                                 // Queue for BFS
    q.push(start);                                  // Start BFS from the start position
    cameFrom[start] = start;                        // Start comes from itself


    Point directions[] = { {0, 1}, {1, 0}, {0, -1}, {-1, 0} };

    while (!q.empty()) {
        Point current = q.front();
        q.pop();


        if (current == end) {
            pathLength = 0;
            Point temp = end;


            while (temp != start) {
                pathLength++;
                temp = cameFrom[temp];
            }
            pathLength++;


            Point* path = new Point[pathLength];
            temp = end;

            for (int i = pathLength - 1; i >= 0; i--) {
                path[i] = temp;
                temp = cameFrom[temp];
            }
            return path;
        }


        for (const auto& dir : directions) {
            Point next = { current.x + dir.x, current.y + dir.y };


            if (isValidLevel(next, level) && cameFrom.find(next) == cameFrom.end()) {
                q.push(next);
                cameFrom[next] = current;
            }
        }
    }

    // No path found
    pathLength = 0;
    return nullptr;
}


void mymaze:: generateMaze(unordered_map<Point, char, PointHash>& maze, int width, int height, Point&start, Point& player, Point& end) {
    maze.clear();


    char** mazeLayout = new char* [height];
    for (int i = 0; i < height; i++) {
        mazeLayout[i] = new char[width];
        for (int j = 0; j < width; j++) {
            mazeLayout[i][j] = '#';
        }
    }

    srand((unsigned)time(0));


    int wallSideStart = rand() % 4;
    if (wallSideStart == 0) start = { rand() % (width - 2) + 1, 0 };
    else if (wallSideStart == 1) start = { width - 1, rand() % (height - 2) + 1 };
    else if (wallSideStart == 2) start = { rand() % (width - 2) + 1, height - 1 };
    else if (wallSideStart == 3) start = { 0, rand() % (height - 2) + 1 };

    player = start;


    if (wallSideStart == 0) player.y = 1;
    else if (wallSideStart == 1) player.x = width - 2;
    else if (wallSideStart == 2) player.y = height - 2;
    else if (wallSideStart == 3) player.x = 1;


    for (int y = 1; y < height - 1; ++y) {
        for (int x = 1; x < width - 1; ++x) {
            mazeLayout[y][x] = (rand() % 3 == 0) ? '#' : '.';
        }
    }


    Point directions[] = { {0, 1}, {1, 0}, {0, -1}, {-1, 0} };
    int wallsAroundPlayer = 0;

    for (const auto& dir : directions) {
        Point neighbor = { player.x + dir.x, player.y + dir.y };
        if (neighbor.x >= 0 && neighbor.x < width && neighbor.y >= 0 && neighbor.y < height &&
            mazeLayout[neighbor.y][neighbor.x] == '#') {
            ++wallsAroundPlayer;
        }
    }


    if (wallsAroundPlayer >= 3) {
        for (const auto& dir : directions) {
            Point neighbor = { player.x + dir.x, player.y + dir.y };
            if (neighbor.x >= 0 && neighbor.x < width && neighbor.y >= 0 && neighbor.y < height) {
                mazeLayout[neighbor.y][neighbor.x] = '.';
                break;
            }
        }
    }


    int wallSideEnd = rand() % 4;
    do {
        if (wallSideEnd == 0) end = { rand() % (width - 2) + 1, 0 };
        else if (wallSideEnd == 1) end = { width - 1, rand() % (height - 2) + 1 };
        else if (wallSideEnd == 2) end = { rand() % (width - 2) + 1, height - 1 };
        else if (wallSideEnd == 3) end = { 0, rand() % (height - 2) + 1 };
    } while (end == start);


    mazeLayout[start.y][start.x] = 'S';
    mazeLayout[end.y][end.x] = 'E';


    for (int y = 0; y < height; ++y) {
        for (int x = 0; x < width; ++x) {
            maze[{x, y}] = mazeLayout[y][x];
        }
    }


    for (int i = 0; i < height; i++) {
        delete[] mazeLayout[i];
    }
    delete[] mazeLayout;
}



void mymaze:: editMaze(unordered_map<Point, char, PointHash>& maze, int width, int height) {
    int x, y;
    char newElement;

    // Request input coordinates
    cout << "Enter coordinates to edit (x y): ";
    cin >> x >> y;

    // Debug print to check input coordinates
    cout << "Received coordinates: (" << x << ", " << y << ")" << endl;

    // Check if the coordinates are within valid bounds (i.e., not on the outer walls)
    if (x < 1 || x >= width - 1 || y < 1 || y >= height - 1) {
        cout << "Invalid coordinates! Must be within the maze boundaries (excluding outer walls)." << endl;
        return;
    }

    // Request the new element to replace at the coordinates
    cout << "Enter new element ('#' for wall, '.' for path): ";
    cin >> newElement;

    // Check for valid element input
    if (newElement == '#' || newElement == '.') {
        maze[{x, y}] = newElement;  // Update maze at given coordinates
        cout << "Maze updated!" << endl;
    }
    else {
        cout << "Invalid element! Please enter '#' or '.' only." << endl;
    }
}



void mymaze::displayMaze(const unordered_map<Point, char, PointHash>& maze, int width, int height, const Point& player, const Point& start, const Point& end, const Point* path, int pathLength) {
    for (int y = 0; y < height; ++y) {
        for (int x = 0; x < width; ++x) {
            Point current = { x, y };

            if (player == current) {
                cout << 'P' << ' ';
            }
            else if (start == current) {
                cout << 'S' << ' ';
            }
            else if (end == current) {
                cout << 'E' << ' ';
            }
            else {
                bool isPath = false;
                for (int i = 0; i < pathLength; i++) {
                    if (path[i] == current) {
                        isPath = true;
                        break;
                    }
                }
                if (isPath) {
                    cout << '*' << ' ';
                }
                else {
                    auto it = maze.find(current);
                    cout << (it != maze.end() ? it->second : '?') << ' ';
                }
            }
        }
        cout << endl;
    }
}
void mymaze:: displayLevel(const Level& level, Point P, Point S, Point E) {
    for (int y = 0; y < level.height; ++y) {
        for (int x = 0; x < level.width; ++x) {
            Point p = { x, y };
            if (p == P) cout << "P ";      // Player's current position
            else if (p == S) cout << "S "; // Start position
            else if (p == E) cout << "E "; // End position
            else cout << level.layout.at(p) << ' ';
        }
        cout << endl;
    }
    cout << endl;
}
void mymaze:: displayLevelAnswer(const Level& level, Point* path, int pathLength) {
    unordered_map<Point, char, PointHash> displayLayout = level.layout;

    // Overlay the path on the maze
    for (int i = 0; i < pathLength; ++i) {
        if (displayLayout[path[i]] != 'S' && displayLayout[path[i]] != 'E') {
            displayLayout[path[i]] = '*'; // Mark the path with '*'
        }
    }

    // Display the maze
    for (int y = 0; y < level.height; ++y) {
        for (int x = 0; x < level.width; ++x) {
            Point p = { x, y };
            auto it = displayLayout.find(p);
            if (it != displayLayout.end()) {
                cout << it->second << ' ';
            }
            else {
                cout << ' ' << ' '; // Empty space if not found
            }
        }
        cout << endl;
    }
    cout << endl;
}

mymaze::Point mymaze::findEnd(const Level& level) {
    for (auto& cell : level.layout) {
        if (cell.second == 'E') {
            return cell.first;
        }
    }
    return { -1, -1 }; // Return a default invalid point.
}



void mymaze::playLevels(Level* levels, int numLevels) {
    bool playAgain = true;

    while (playAgain) {
        int currentLevel = 0;

        while (currentLevel < numLevels) {
            cout << "            Level " << currentLevel + 1 << "\n";
            Point P = { 1, 1 };
            Point S = { 1, 0 };
            Point E = findEnd(levels[currentLevel]);
            int pathLength = 0;
            Point* path = nullptr;

            displayLevel(levels[currentLevel], P, S, E);

            bool giveUp = false;
            while (P != E) {
                int move;
                bool validInput = false;
                while (!validInput) {
                    cout << "1. UP" << endl;
                    cout << "2. DOWN" << endl;
                    cout << "3. LEFT" << endl;
                    cout << "4. RIGHT" << endl;
                    cout << "5. GIVE UP" << endl;

                    cin >> move;


                    if (move >= 1 && move <= 5) {
                        validInput = true;
                    }
                    else {
                        cout << "Invalid input! Please try again." << endl;
                        cin.clear();
                        cin.ignore(numeric_limits<streamsize>::max(), '\n');
                    }
                }
                Point next = P;
                if (move == 1) next.y -= 1;
                else if (move == 2) next.y += 1;
                else if (move == 3) next.x -= 1;
                else if (move == 4) next.x += 1;
                else if (move == 5) {
                    giveUp = true;
                    cout << "You gave up! Showing solution..." << endl;
                    path = findLevelPath(levels[currentLevel], S, E, pathLength);
                    if (path) {
                        displayLevelAnswer(levels[currentLevel], path, pathLength);
                        delete[] path;
                    }
                    else {
                        cout << "No solution found!" << endl;
                    }
                    break;
                }
                else {
                    cout << "Invalid move! Try again." << endl;
                    continue;
                }

                if (isValidLevel(next, levels[currentLevel])) {
                    levels[currentLevel].layout[{P.x, P.y}] = '*';
                    P = next;
                    displayLevel(levels[currentLevel], P, S, E);
                }
                else {
                    cout << "Invalid move! Try again." << endl;
                }

                if (P == E && !giveUp) {
                    cout << "YOU WIN!!!!\n";
                    break;
                }
            }

            if (giveUp) {
                break;
            }

            currentLevel++;
            if (currentLevel == numLevels) {
                cout << "CONGRATULATIONS! YOU COMPLETED ALL LEVELS!\n";
                break;
            }
        }

        int plAg;
        cout << "PLAY AGAIN?\n";
        cout << "1. YES   2. NO\n";
        cin >> plAg;
        if (plAg == 2)
            playAgain = false;
    }
}


void mymaze:: mainMenu() {
    cout << "-----MAZE-----" << endl;
    cout << "1.RANDOM GAME" << endl;
    cout << "2.LEVELS" << endl;
    cout << "3.CREATE GAME" << endl;
    cout << "4.EXIT\n";
}


void mymaze::displayMazeGUI(RenderWindow& window, const unordered_map<Point, char, PointHash>& maze, int width, int height, const Point& player, const Point& start, const Point& end, const Point* path, int pathLength) {
    window.clear(sf::Color(196, 184, 154)); // Darker cream color for the background

    // Calculate the size of the maze in pixels
    int mazePixelWidth = width * 50;
    int mazePixelHeight = height * 50;

    // Calculate the offsets to center the maze
    int offsetX = (window.getSize().x - mazePixelWidth) / 2;
    int offsetY = (window.getSize().y - mazePixelHeight) / 2;

    for (int y = 0; y < height; ++y) {
        for (int x = 0; x < width; ++x) {
            Point current = { x, y };
            RectangleShape cell(Vector2f(50, 50));
            cell.setPosition(offsetX + x * 50, offsetY + y * 50); // Apply the offsets

            if (player == current) {
                cell.setFillColor(Color(75, 46, 30)); // Darker brown color for the player
            }
            else if (start == current) {
                cell.setFillColor(Color::Black);
            }
            else if (end == current) {
                cell.setFillColor(Color(128, 0, 0)); // Dark red color for the end point
            }
            else {
                bool isPath = false;
                for (int i = 0; i < pathLength; ++i) {
                    if (path[i] == current) {
                        isPath = true;
                        break;
                    }
                }
                if (isPath) {
                    cell.setFillColor(Color::Yellow);
                }
                else {
                    auto it = maze.find(current);
                    cell.setFillColor(it != maze.end() && it->second == '#' ? Color(111, 78, 55) : Color(255, 253, 208)); // Wall color or cream color for the maze
                }
            }

            window.draw(cell);
        }
    }

    window.display();
}

void mymaze::displayLevelGUI(sf::RenderWindow& window, const Level& level, Point P, Point S, Point E, int levelNumber) {
    window.clear(sf::Color(135,206,225));

    sf::Font font;
    if (!font.loadFromFile("C:/Users/habib/OneDrive/Documents/SFML-2.6.2-windows-vc17-64-bit/SFML-2.6.2/examples/island/resources/tuffy.ttf")) {
        std::cerr << "Error loading font\n";
        return;
    }

    // Display Level Number
    sf::Text levelText;
    levelText.setFont(font);
    levelText.setCharacterSize(50);
    levelText.setFillColor(sf::Color:: Black);
    levelText.setString("Level " + std::to_string(levelNumber));
    levelText.setPosition(500, 20);
    window.draw(levelText);

    // Display the Maze
    sf::RectangleShape cell(sf::Vector2f(150, 150));
    cell.setOutlineThickness(5);
    cell.setOutlineColor(sf::Color::Black);

    for (int y = 0; y < level.height; ++y) {
        for (int x = 0; x < level.width; ++x) {
            Point p = { x, y };
            if (p == P) {
                cell.setFillColor(sf::Color(0,0,128));
            }
            else if (p == S) {
                cell.setFillColor(sf::Color::Black);
            }
          
            else if (p == E) {
                sf::Vector2f cellSize(150, 50);
                cell.setSize(cellSize);
              
                cell.setFillColor(sf::Color(128,0,0));
            }
            else {
                switch (level.layout.at(p)) {
                case '#':
                    cell.setFillColor(sf::Color(0,128,128));
                    break;
                case '.':
                    cell.setFillColor(sf::Color(245,245,220));
                    break;
                default:
                    cell.setFillColor(sf::Color::White);
                    break;
                }
            }

            cell.setPosition(x * 60, y * 50+100); // Adjust position to leave space for the title
            window.draw(cell);
        }
    }

    window.display();
}

void mymaze::displayLevelAnswerGUI(sf::RenderWindow& window, const Level& level, Point* path, int pathLength) {
    window.clear();

    sf::Font font;
    if (!font.loadFromFile("C:/Users/habib/OneDrive/Documents/SFML-2.6.2-windows-vc17-64-bit/SFML-2.6.2/examples/island/resources/tuffy.ttf")) {
        std::cerr << "Error loading font\n";
        return;
    }

    sf::RectangleShape cell(sf::Vector2f(20, 20));
    cell.setOutlineThickness(1);
    cell.setOutlineColor(sf::Color::Black);

    for (int y = 0; y < level.height; ++y) {
        for (int x = 0; x < level.width; ++x) {
            Point p = { x, y };
            if (level.layout.at(p) == 'P') cell.setFillColor(sf::Color::White);
            else {
                switch (level.layout.at(p)) {
                case '#':
                    cell.setFillColor(sf::Color::Black);
                    break;
                case '.':
                    cell.setFillColor(sf::Color::White);
                    break;
                default:
                    cell.setFillColor(sf::Color::White);
                    break;
                }
            }

            cell.setPosition(x * 20, y * 20 + 50); // Adjust position to leave space for the title
            window.draw(cell);
        }
    }

    for (int i = 0; i < pathLength; ++i) {
        cell.setFillColor(sf::Color::Cyan);
        cell.setPosition(path[i].x * 20, path[i].y * 20 + 50);
        window.draw(cell);
    }

    window.display();
}

/*void mymaze::playLevelsGUI(Level* levels, int numLevels, sf::RenderWindow& window) {
    bool playAgain = true;

    while (playAgain && window.isOpen()) {
        for (int currentLevel = 0; currentLevel < numLevels; ++currentLevel) {
            std::cout << "Playing level " << (currentLevel + 1) << std::endl; // Debugging output

            Point P = { 1, 1 };
            Point S = { 1, 0 };
            Point E = findEnd(levels[currentLevel]);
            int pathLength = 0;
            Point* path = nullptr;

            displayLevelGUI(window, levels[currentLevel], P, S, E, currentLevel + 1);

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
                        Point next = P;

                        // Player movement controls
                        if (event.key.code == sf::Keyboard::Up) next.y -= 1;
                        else if (event.key.code == sf::Keyboard::Down) next.y += 1;
                        else if (event.key.code == sf::Keyboard::Left) next.x -= 1;
                        else if (event.key.code == sf::Keyboard::Right) next.x += 1;
                        else if (event.key.code == sf::Keyboard::Escape) {
                          
                            playAgain = false;
                            window.close();

                            return;
                            
                        }
                        else if (event.key.code == sf::Keyboard::Enter) {
                            levelCompleted = true;
                            break;
                        }

                        if (isValidLevel(next, levels[currentLevel])) {
                            P = next;
                            displayLevelGUI(window, levels[currentLevel], P, S, E, currentLevel + 1);
                        }

                        if (P == E) {
                            levelCompleted = true;
                            std::cout << "YOU WIN!!!!\n";
                        }
                    }
                }
            }

            if (giveUp) {
                path = findLevelPath(levels[currentLevel], S, E, pathLength);
                if (path) {
                    displayLevelAnswerGUI(window, levels[currentLevel], path, pathLength);
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
                nextLevelText.setCharacterSize(30);
                nextLevelText.setFillColor(sf::Color::Yellow);
                nextLevelText.setString("YOU WIN!!!\n Press Enter to Proceed :D!\n Press Escape to Exit :( ..");
                nextLevelText.setPosition(500, 200);
                window.clear();
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
                                window.close();
                                return;
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
}
*/

/*void mymaze::editMaze(sf::RenderWindow& window, unordered_map<mymaze::Point, char, mymaze::PointHash>& maze, int w, int h, mymaze::Point& S, mymaze::Point& P, mymaze::Point& E) {
    bool editing = true;
    while (editing && window.isOpen()) {
        mazeGame.displayMazeGUI(window, maze, w, h, P, S, E, nullptr, 0);

        // Display editing instructions
        sf::Text editText;
        sf::Font font;
        if (!font.loadFromFile("C:/Users/habib/OneDrive/Documents/SFML-2.6.2-windows-vc17-64-bit/SFML-2.6.2/examples/island/resources/tuffy.ttf")) {
            std::cerr << "Error loading font\n";
            return;
        }
        editText.setFont(font);
        editText.setCharacterSize(20);
        editText.setFillColor(sf::Color::White);
        editText.setString("Click on the maze to edit walls. Press Enter when done.");
        editText.setPosition(10, 10);
        window.draw(editText);
        window.display();

        sf::Event event;
        while (window.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                window.close();
                return;
            }
            else if (event.type == sf::Event::MouseButtonPressed) {
                if (event.mouseButton.button == sf::Mouse::Left) {
                    int x = event.mouseButton.x / 20;
                    int y = event.mouseButton.y / 20;
                    if (x > 0 && x < w - 1 && y > 0 && y < h - 1) {
                        maze[{x, y}] = (maze[{x, y}] == '#') ? '.' : '#';
                    }
                }
            }
            else if (event.type == sf::Event::KeyPressed && event.key.code == sf::Keyboard::Enter) {
                editing = false;
            }
        }
    }
}*/
