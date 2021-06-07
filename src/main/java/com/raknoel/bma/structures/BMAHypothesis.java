package com.raknoel.bma.structures;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BMAHypothesis {
    private final int d;
    private final List<BitSet> centers;

    public BMAHypothesis(int d, List<BitSet> centers) {
        this.d = d;
        this.centers = centers;
        if (this.centers == null) {
            throw new IllegalArgumentException("centers is null");
        }
    }

    public BMAHypothesis(int d, BitSet center) {
        this.d = d;
        this.centers = new ArrayList<>();
        this.centers.add(center);
    }

    public BMAHypothesis(int d) {
        this.d = d;
        this.centers = new ArrayList<>();
    }

    public int getTotalCost() {
        return d;
    }

    public List<BitSet> getCenters() {
        return centers;
    }

    public int getWidth() {
        return centers.size();
    }

    @Override
    public String toString() {
        var bld = new StringBuilder();
        for (int i = 0; i < centers.get(0).length(); i++) {
            for (int j = 0; j < centers.size(); j++) {
                bld.append(centers.get(j).get(i) ? '1' : '0').append(" ");
            }
            bld.append(System.lineSeparator());
        }
        return bld.toString();
    }
}