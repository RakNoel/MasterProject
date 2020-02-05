//
// Created by RakNoel on 02/02/2020.
//

/**
 * Quick notes:
 * - Define binary matrix A of columns a0..an-1 then d_h = cardinality(a1 ^ a2)
 * - boost::dynamic_bitset
 */

#include <iostream>
#include <vector>
#include <string>

#include "BinaryFunctions.cpp"

class BinaryMatrix {
private:
    int width{}, height{};
    std::vector<bool> binArray{};

    int convert1dto2d(int x, int y) {
        return (y * width) + x;
    }

public:
    BinaryMatrix(int width, int height) {
        this->width = width;
        this->height = height;
        int tmp = width * height;
        this->binArray = std::vector<bool>(tmp);
    }

    int getWidth() {
        return this->width;
    }

    int getHeight() {
        return this->height;
    }

    std::vector<bool> getRow(int y) {
        std::vector<bool> tmpRow;
        tmpRow.reserve(this->width);
        for (int i = 0; i < this->width; i++)
            tmpRow.push_back(this->binArray[convert1dto2d(i, y)]);
        return tmpRow;
    }

    std::vector<bool> getColumn(int x) {
        std::vector<bool> tmpCol;
        tmpCol.reserve(this->height);
        for (int i = 0; i < this->height; i++)
            tmpCol.push_back(this->binArray[convert1dto2d(x, i)]);
        return tmpCol;
    }

    void set(int x, int y, bool b) {
        this->binArray[convert1dto2d(x, y)] = b;
    }

    bool get(int x, int y) {
        return this->binArray[convert1dto2d(x, y)];
    }

    std::string to_string() {
        std::string str;
        for (int y = this->height - 1; y >= 0; y--) {
            for (int x = 0; x < this->height; x++)
                str += std::to_string(this->get(x, y)) + "  ";
            str += "\n";
        }
        return str;
    }
};

int main() {
    auto *testMatrix = new BinaryMatrix(5, 5);

    testMatrix->set(0, 0, true);
    testMatrix->set(1, 1, true);
    testMatrix->set(2, 2, true);
    testMatrix->set(2, 0, true);
    testMatrix->set(4, 4, true);

    std::cout << testMatrix->to_string() << std::endl;

    auto dh1 = HammingDistance(testMatrix->getColumn(0), testMatrix->getColumn(2)); //should be 1
    auto dh2 = HammingDistance(testMatrix->getColumn(0), testMatrix->getColumn(1)); //should be 2

    std::cout << dh1 << "  " << dh2 << std::endl;
}