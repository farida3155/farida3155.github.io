#include <iostream>
#include <unordered_map>
#include <ctime>
#include <cstdlib>
#include <queue>
#include <cstring>
#include"mymaze.h"
mymaze mazeGame;
  unordered_map<mymaze::Point, char, mymaze::PointHash> maze;
  unordered_map<mymaze::Point, char, mymaze::PointHash> mazeCopy;
int main() {
 
    while (true) {
        mazeGame.mainMenu();
        int choice;
        cin >> choice;

        switch (choice) {
        case 1: {
            bool playAgain = true;
            while (playAgain == true) {

                int w = 15, h = 15;
                mymaze::Point S, E, P;
                mymaze::Point* checkpath = nullptr;
                int pathL = 0;

                do {
                    mazeGame.generateMaze(maze, w, h, S, P, E);
                    mazeCopy = maze;
                    checkpath = mazeGame.findPathBFS(mazeCopy, w, h, S, E, pathL);
                } while (checkpath == nullptr);
                delete[] checkpath;
                mazeGame.displayMaze(maze, w, h, P, S, E, nullptr, 0);

                bool giveUp = false;
                while (P != E) {
                   mymaze:: Point* path = new mymaze::Point{ 0, 0 };
                    int move;
                    bool validInput = false;


                    while (!validInput) {
                        cout << "1. UP" << endl;
                        cout << "2. DOWN" << endl;
                        cout << "3. LEFT" << endl;
                        cout << "4. RIGHT" << endl;
                        cout << "5. EXIT TO MAIN MENU" << endl;

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
                    mymaze::Point next = P;
                    if (move == 1) next.y -= 1;
                    else if (move == 2) next.y += 1;
                    else if (move == 3) next.x -= 1;
                    else if (move == 4) next.x += 1;
                    else if (move == 5) {
                        cout << "Returning to Main Menu...\n";
                        playAgain = false;
                        break;
                    }
                    else {
                        cout << "Invalid input!" << endl;
                        break;
                    }

                    if (mazeGame.isValid(next, w, h, maze)) {
                        P = next;
                        mazeGame.displayMaze(maze, w, h, P, S, E, path, pathL);
                    }
                    else {
                        cout << "Invalid move!" << endl;
                        cout << "Give up?: " << endl;
                        cout << "1. YES" << endl;
                        cout << "2. NO" << endl;
                        int choice;
                        cin >> choice;
                        if (choice == 1) {
                            path = mazeGame.findPathBFS(maze, w, h, S, E, pathL);
                            mazeGame.displayMaze(maze, w, h, P, S, E, path, pathL);
                            giveUp = true;
                            delete path;
                            break;
                        }
                        else if (choice == 2) {
                            continue;
                        }
                        else {
                            cout << "INVALID" << endl;
                        }
                    }
                    if (P == E && !giveUp) {
                        cout << "YOU WIN!!!!\n";
                    }
                } //while P!=E
                int plAg; //play again
                cout << "PLAY AGAIN?\n";
                cout << "1. YES   2.NO\n";
                cin >> plAg; //play again
                if (cin.fail() || plAg != 1)
                    playAgain = false;
            };//while play again
            break;
        } //case 1
        case 2: {
            mymaze::Level* levels = nullptr;
            int numLevels = 0;
            mazeGame.loadLevels(levels, numLevels);
            mazeGame.playLevels(levels, numLevels);
            break;
        } //case 2
        case 3: {
            while (true) {
                cout << "------------MAZE CREATION------------" << endl;
                int w = 15, h = 15;
                mymaze::Point S, E, P;
                mymaze::Point* path = nullptr;
                int pL = 0;
                bool mazeValid = false;
                bool exit = false;

                mazeGame.generateMaze(maze, w, h, S, P, E);

                while (!mazeValid && !exit) {
                    char ed;
                    bool e = true;
                    while (e == true) {
                        mazeGame.displayMaze(maze, w, h, P, S, E, path, pL);
                        cout << "Do you want to edit?";
                        cin >> ed;
                        if (ed == 'Y' || ed == 'y')
                            mazeGame.editMaze(maze, w, h);
                        else if (ed == 'N' || ed == 'n')
                            e = false;
                        else
                            cout << "Invalid Input" << endl;
                    } // while e true

                    cout << "Checking if the maze is valid..." << endl;
                    path = mazeGame.findPathBFS(maze, w, h, S, E, pL);

                    if (path != nullptr) {
                        mazeValid = true;
                        cout << "Maze is valid!" << endl;
                        delete[] path;
                        path = nullptr;
                    }
                    else {
                        cout << "Maze is invalid! No valid path exists from start to end." << endl;
                        cout << "1. Edit the maze" << endl;
                        cout << "2. Exit" << endl;
                        int choice;
                        cin >> choice;
                        if (choice == 1) {
                            mazeGame.displayMaze(maze, w, h, P, S, E, path, pL);
                            mazeGame.editMaze(maze, w, h);
                        }
                        else if (choice == 2) {
                            exit = true;
                        }
                        else {
                            cout << "Invalid input!" << endl;
                        }
                    }
                }

                if (!exit) {
                    int ans;
                    cout << "1. Play Created Game" << endl;
                    cout << "2. Exit" << endl;
                    cin >> ans;

                    if (ans == 1) {
                        mazeGame.displayMaze(maze, w, h, P, S, E, nullptr, 0);
                        bool giveUp = false;

                        while (P != E && !giveUp) {
                            int move;
                            bool validInput = false;

                            while (!validInput) {
                                cout << "1. UP" << endl;
                                cout << "2. DOWN" << endl;
                                cout << "3. LEFT" << endl;
                                cout << "4. RIGHT" << endl;
                                cout << "5. EXIT TO MAIN MENU" << endl;

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

                            mymaze::Point next = P;
                            if (move == 1) next.y -= 1;
                            else if (move == 2) next.y += 1;
                            else if (move == 3) next.x -= 1;
                            else if (move == 4) next.x += 1;
                            else if (move == 5) {
                                cout << "Returning to Main Menu...\n";
                                break;
                            }

                            if (mazeGame.isValid(next, w, h, maze)) {
                                P = next;
                                mazeGame.displayMaze(maze, w, h, P, S, E, nullptr, 0);
                            }
                            else {
                                cout << "Invalid move!" << endl;
                                cout << "Give up?: " << endl;
                                cout << "1. YES" << endl;
                                cout << "2. NO" << endl;
                                int choice;
                                cin >> choice;
                                if (choice == 1) {
                                    path = mazeGame.findPathBFS(maze, w, h, S, E, pL);
                                    mazeGame.displayMaze(maze, w, h, P, S, E, path, pL);
                                    giveUp = true;
                                    delete[] path;
                                    break;
                                }
                                else if (choice == 2) {
                                    continue;
                                }
                                else {
                                    cout << "INVALID" << endl;
                                }
                            }

                            if (P == E && !giveUp) {
                                cout << "YOU WIN!!!!\n";
                                break;
                            }
                        }

                        if (P == E) {
                            int postWinChoice;
                            cout << "1. Create a New Game" << endl;
                            cout << "2. Exit" << endl;
                            cin >> postWinChoice;
                            if (postWinChoice == 1) {
                                continue; // Restart the loop for a new game
                            }
                            else if (postWinChoice == 2) {
                                cout << "Exiting..." << endl;
                                break;
                            }
                            else {
                                cout << "Invalid input! Exiting..." << endl;
                                break;
                            }
                        }
                    }
                    else if (ans == 2) {
                        cout << "Exiting..." << endl;
                    }
                    else {
                        cout << "Invalid Input" << endl;
                    }
                }
                else {
                    break;
                }
            }

            break;
        }

        case 4: {
            cout << "GOODBYE!" << endl;
            return 0;
        }
        default:
            cout << "Invalid input!" << endl;
        } //switch
    } //while true
}
