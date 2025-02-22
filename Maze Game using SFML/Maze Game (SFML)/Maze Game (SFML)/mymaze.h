#pragma once
#include <iostream>
#include <unordered_map>
#include <queue>
#ifndef MYMAZE_H
#define MYMAZE_H
using namespace std;
#include <unordered_map>
#include <SFML/Graphics/RenderWindow.hpp>
#include <SFML/Graphics.hpp>
using namespace sf;

class mymaze
{
public:

    struct Point {

        int x, y;

        bool operator==(const Point& other) const {
            return x == other.x && y == other.y;
        }

        bool operator!=(const Point& other) const {
            return !(*this == other);
        }
    };

    struct PointHash {

        size_t operator()(const Point& p) const {
            return hash<int>()(p.x) ^ (hash<int>()(p.y) << 1);
        }
    };
    struct Level {

        unordered_map<Point, char, PointHash> layout;
        int width;
        int height;
    };
    const int MAX_HEIGHT = 15;
    const int MAX_WIDTH = 30;



    void loadLevels(Level*& levels, int& numLevels);

    void cleanupLevels(Level* levels, int numLevels);


    bool isValid(const Point& p, int width, int height, const unordered_map<Point, char, PointHash>& maze);
    void displayLevel(const Level& level, Point P, Point S, Point E);
    void displayMaze(const unordered_map<Point, char, PointHash>& maze, int width, int height, const Point& player, const Point& start, const Point& end, const Point* path, int pathLength);
    void  displayLevelAnswer(const Level& level, Point* path, int pathLength);

    bool isValidLevel(Point p, const Level& level);

    Point* findPathBFS(const unordered_map<Point, char, PointHash>& maze, int width, int height, const Point& start, const Point& end, int& pathLength);

    Point* findLevelPath(const Level& level, const Point& start, const Point& end, int& pathLength);

    void generateMaze(unordered_map<Point, char, PointHash>& maze, int width, int height, Point& start, Point& player, Point& end);

    void editMaze(unordered_map<Point, char, PointHash>& maze, int width, int height);

    Point findEnd(const Level& level);

    void playLevels(Level* levels, int numLevels);

    static void mainMenu();
    void displayMazeGUI(RenderWindow& window, const unordered_map<Point, char, PointHash>& maze, int width, int height, const Point& player, const Point& start, const Point& end, const Point* path, int pathLength);
    void displayLevelGUI(sf::RenderWindow& window, const Level& level, Point P, Point S, Point E, int levelNumber);
    void displayLevelAnswerGUI(sf::RenderWindow& window, const Level& level, Point* path, int pathLength);
    void playLevelsGUI(Level* levels, int numLevels, sf::RenderWindow& window);
    void editMazeGUI(sf::RenderWindow& window, unordered_map<mymaze::Point, char, mymaze::PointHash>& maze, int w, int h, mymaze::Point& S, mymaze::Point& P, mymaze::Point& E, int cellSize);
 
#endif
};
