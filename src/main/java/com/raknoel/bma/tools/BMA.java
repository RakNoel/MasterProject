package com.raknoel.bma.tools;

import com.raknoel.bma.exceptions.BinaryMatrixNoInstanceException;
import com.raknoel.bma.structures.BMAHypothesis;
import com.raknoel.bma.structures.BinaryMatrix;
import com.raknoel.bma.structures.BinarySubMatrix;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BMA {
    private final int k, r;
    private final List<BinarySubMatrix> bsms;

    public BMA(BinaryMatrix bnm, int k, int r) throws BinaryMatrixNoInstanceException {
        this.k = k;
        this.r = r;
        this.bsms = new Kernel(k, r).kernelize(bnm);
    }

    public synchronized BMAHypothesis Approximate() {
        var solvers = new BMASolver[this.bsms.size()];

        var pointer = 0;
        for (var bsm : this.bsms) {
            solvers[pointer] = new BMASolver(bsm, k, r - this.bsms.size() + 1);
            solvers[pointer++].run();
        }

        var solution = new int[k + 1];
        var bags = new ArrayList[k + 1];
        for (int i = 0; i <= k; i++) {
            solution[i] = solvers[0].getCenters()[i].getWidth();
            bags[i] = new ArrayList<BitSet>();
            bags[i].addAll(solvers[0].getCenters()[i].getCenters());
        }

        var res = findBestSolutions(solvers, solution, bags);

        var bestK = 0;
        var bestR = res[bestK];
        for (int i = 0; i < res.length; i++) {
            if (bestR > res[i]) {
                bestK = i;
                bestR = res[bestK];
            }
        }

        return new BMAHypothesis(bestK, bags[bestK]);
    }

    private int[] findBestSolutions(BMASolver[] solvers, int[] solution, ArrayList[] bags) {
        if (solution == null) throw new IllegalArgumentException("Solution array may not be null");

        for (int s = 1; s < solvers.length; s++) {
            var working = solvers[s].getCenters();

            var newSolution = new int[k + 1];
            for (int i = 0; i <= k; i++) {
                int dec = i;
                int inc = 0;
                int best = solution[dec] + working[inc].getWidth();

                var p = inc;
                while (dec > 0) {
                    var contender = solution[--dec] + working[++inc].getWidth();
                    if (best > contender) {
                        best = contender;
                        p = inc;
                    }
                }

                newSolution[i] = best;
                bags[i].addAll(working[p].getCenters());
            }
            solution = newSolution;
        }
        return solution;
    }
}
