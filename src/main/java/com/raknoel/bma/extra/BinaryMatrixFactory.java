package com.raknoel.bma.extra;

import com.raknoel.bma.structures.BinaryMatrix;
import com.raknoel.bma.tools.HammingDistance;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

public class BinaryMatrixFactory {

    private BinaryMatrixFactory() {
    }

    public static BinaryMatrix generateRandom(Random random, int width, int height, int k, int r) {
        var resulting = getVectorsFromCenters(random, width, height, k, generateCenters(random, height, k, r));
        return new BinaryMatrix(resulting, height);
    }

    private static List<BitSet> getVectorsFromCenters(Random random, int width, int height, int k, List<BitSet> distancedCenters) {
        var resulting = new ArrayList<BitSet>(width);
        resulting.addAll(distancedCenters);

        while (resulting.size() < width) {
            var newVector = new BitSet();
            newVector.or(distancedCenters.get(random.nextInt(distancedCenters.size())));
            for (int i = random.nextInt(k); i > 0; i--) newVector.flip(random.nextInt(height));
            resulting.add(newVector);
        }
        return resulting;
    }

    private static List<BitSet> generateCenters(Random random, int height, int k, int r) {
        ArrayList<BitSet> centers;
        do {
            centers = new ArrayList<>(r);
            for (int i = 0; i < r; i++) {
                var center = new BitSet(height);
                for (int j = 0; j < height; j++)
                    center.set(j, random.nextBoolean());
                centers.add(center);
            }
        } while (validCenters(centers, k));

        return centers;
    }

    private static boolean validCenters(List<BitSet> centers, int k) {
        for (int i = 0; i < centers.size() - 1; i++) {
            for (int j = i; j < centers.size(); j++) {
                if (HammingDistance.hammingDistance(centers.get(i), centers.get(j)) <= k) return false;
            }
        }
        return true;
    }
}
