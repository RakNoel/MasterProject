package com.raknoel.bma.tools;

import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class GenerateCloseNeighbours {
    public static List<BitSet> generateCloseNeighbours(BitSet vector, int length, int distance) {
        var result = new ArrayList<BitSet>();

        //Setup numbers as bitPickers after combination
        var numbers = new ArrayList<Integer>();
        for (int i = 0; i < length; i++)
            numbers.add(i);

        Generator.combination(numbers).simple(distance).stream().forEach(bitsToFlip -> {
            var tmpVector = new BitSet(length);
            for (int i : bitsToFlip) tmpVector.set(i);
            tmpVector.xor(vector);
            result.add(tmpVector);
        });

        return result;
    }
}
