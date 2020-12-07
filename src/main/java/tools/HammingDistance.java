package tools;

import java.util.BitSet;

public class HammingDistance {
    public static int hammingDistance(BitSet a, BitSet b) {
        BitSet r = new BitSet();
        r.or(a);
        r.xor(b);
        return r.cardinality();
    }
}
