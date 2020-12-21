package com.raknoel.bma;

import com.raknoel.bma.exceptions.BinaryMatrixNoInstanceException;
import com.raknoel.bma.extra.BinaryMatrixFactory;
import com.raknoel.bma.tools.Kernel;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        var factory = new BinaryMatrixFactory();
        var binaryMatrix = factory.generateRandom(new Random(), 100, 100, 7, 8);

        Kernel a = new Kernel(7, 8);
        try {
            var res = a.kernelize(binaryMatrix);

            System.out.printf("Solution found of %d sub matrices %n", res.size());

            for (var sub : res)
                System.out.println(sub);
        } catch (BinaryMatrixNoInstanceException noInstance) {
            noInstance.printStackTrace();
        }
    }
}
