package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BinaryMatrixFactory {
    private static BinaryMatrixFactory self;

    public static BinaryMatrixFactory BuildBinaryMatrixFactory() {
        if (self == null) self = new BinaryMatrixFactory();
        return self;
    }

    public BinaryMatrix readFromFile(File file) throws FileNotFoundException {
        Scanner scn = new Scanner(file);
        var fLine = scn.nextLine().split(" ");
        int x = Integer.parseInt(fLine[0]);
        int y = Integer.parseInt(fLine[1]);

        var bnm = new BinaryMatrix(x, y);
        char[][] tmp = new char[y][x];

        for (int i = 0; i < y; i++)
            tmp[i] = scn.nextLine().toCharArray();

        for (int i = 0; i < x; i++)
            for (int j = 0; j < y; j++)
                bnm.setBit(i, j, tmp[j][i] == '1');

        return bnm;
    }
}
