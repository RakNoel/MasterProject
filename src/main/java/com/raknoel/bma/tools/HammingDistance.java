package com.raknoel.bma.tools;

import java.util.BitSet;

public class HammingDistance {

    private HammingDistance() {
    }

    public static int hammingDistance(BitSet a, BitSet b) {
        BitSet r = new BitSet();
        r.or(a);
        r.xor(b);
        return r.cardinality();
    }
}
