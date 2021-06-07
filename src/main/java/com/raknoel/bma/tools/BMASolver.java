package com.raknoel.bma.tools;

import com.raknoel.bma.exceptions.BinaryMatrixNoInstanceException;
import com.raknoel.bma.structures.BMAHypothesis;
import com.raknoel.bma.structures.BinarySubMatrix;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.raknoel.bma.tools.HammingDistance.hammingDistance;

public class BMASolver implements Runnable {

    private static final AtomicInteger threadsRemaining = new AtomicInteger(0);
    private final BinarySubMatrix bsm;
    private final int k;
    private final int r;
    private final BMAHypothesis[] centers;
    private boolean noInstance;

    public BMASolver(BinarySubMatrix bsm, int k, int r) {
        BMASolver.threadsRemaining.incrementAndGet();
        this.bsm = bsm;
        this.k = k;
        this.r = r;
        this.centers = new BMAHypothesis[k + 1];
        this.noInstance = false;
    }

    public static int getRemainingThreads() {
        return threadsRemaining.getAcquire();
    }

    public BMAHypothesis[] getCenters() {
        return centers;
    }


    @Override
    public void run() {
        if (bsm.isFinished()) {
            centers[0] = new BMAHypothesis(0, bsm.getColumn(0));

            for (int i = 1; i < this.centers.length; i++)
                this.centers[i] = this.centers[0];

            BMASolver.threadsRemaining.decrementAndGet();
            return;
        }

        assert (bsm.getWidth() > 0);

        for (int i = 0, p = 0; i <= k; i++, p++) {
            try {
                var I = new ArrayList<Integer>(bsm.getWidth());
                var S = new ArrayList<BitSet>();
                for (var t = 0; t < bsm.getWidth(); t++)
                    I.add(t, t);

                this.centers[p] = findCenters(I, S, i);
            } catch (BinaryMatrixNoInstanceException e) {
                p--;
            }
        }

        if (this.centers[0] == null) this.noInstance = true;
        else for (int i = 1; i < this.centers.length; i++)
            if (this.centers[i] == null) this.centers[i] = this.centers[i - 1];

        BMASolver.threadsRemaining.decrementAndGet();
    }

    public BMAHypothesis findCenters(List<Integer> I, List<BitSet> S, int d) throws BinaryMatrixNoInstanceException {
        var PartitionGenerator = new Partition<Integer>();

        if (!S.isEmpty() && this.bsm.totalHammingDist(S) <= d) {
            return new BMAHypothesis(this.bsm.totalHammingDist(S), S);
        }
        if (S.size() == this.r) {
            throw new BinaryMatrixNoInstanceException("");
        }

        for (var h = 0; h <= d; h++) {
            d = extractCoveredColumns(I, S, d, h);

            if (I.isEmpty()) {
                throw new BinaryMatrixNoInstanceException("");
            }

            if (d >= 0 && I.size() < magicNumber(d)) {
                for (int p = 1; p < (Math.min(I.size(), r - S.size())); p++) {
                    var partitions = PartitionGenerator.PartitionFirstEmpty(p, I);
                    for (var partition : partitions) {
                        List<BitSet> optimalCenter = new ArrayList<>();
                        for (var cluster : partition) {
                            int[] position = new int[bsm.getHeight()];
                            for (int i = 0; i < position.length; i++)
                                for (Integer col : cluster)
                                    position[i] += bsm.getBit(col, i) ? 1 : -1;

                            var center = new BitSet();
                            center.or(bsm.getColumn(cluster.get(0)));
                            for (int i = 0; i < position.length; i++) {
                                center.set(bsm.getRowID(i), position[i] > 0);
                            }

                            optimalCenter.add(center);
                        }
                        var tmpS = new ArrayList<>(S);
                        tmpS.addAll(optimalCenter);
                        if (this.bsm.totalHammingDist(tmpS) <= d) {
                            return new BMAHypothesis(this.bsm.totalHammingDist(tmpS), tmpS);
                        }
                    }
                }
            }

            if (h <= d / I.size()) {
                for (var columnSelector : I) {
                    var column = bsm.getColumn(columnSelector);
                    var s = GenerateCloseNeighbours.generateCloseNeighbours(column, bsm.getHeight(), h);
                    for (var distVector : s) {
                        var tmpList = new ArrayList<>(S);
                        tmpList.add(distVector);
                        try {
                            return findCenters(new ArrayList<>(I), tmpList, d);
                        } catch (BinaryMatrixNoInstanceException e) {
                            //Ignore
                        }
                    }
                }
            }
        }
        throw new BinaryMatrixNoInstanceException("");
    }

    private double magicNumber(int d) {
        //TODO assure that w = bsm.getHeight()
        return Math.sqrt(d * r * (Math.log(bsm.getHeight()) / Math.log(r)));
    }

    private int extractCoveredColumns(List<Integer> I, List<BitSet> S, int d, int h) {
        for (var a = 0; a < I.size(); a++) {
            var i = I.get(a);
            var l = Integer.MAX_VALUE; //infinity
            for (var s : S) {
                l = Math.min(hammingDistance(s, bsm.getColumn(i)), l);
            }
            if (l < h - 1 || l == 0) {
                I.remove(i);
                a--;
                d = d - l;
            }
        }
        return d;
    }
}