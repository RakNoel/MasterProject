package com.raknoel.bma.tools;

import com.raknoel.bma.exceptions.BinaryMatrixNoInstanceException;
import com.raknoel.bma.structures.BinaryMatrix;
import com.raknoel.bma.structures.BinarySubMatrix;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static com.raknoel.bma.tools.HammingDistance.hammingDistance;

public class BMA {
    private final int k, r;
    private final List<BinarySubMatrix> bsms;

    public BMA(BinaryMatrix bnm, int k, int r) throws BinaryMatrixNoInstanceException {
        this.k = k;
        this.r = r;
        this.bsms = new Kernel(k, r).kernelize(bnm);
    }

    public synchronized BinaryMatrix Approximate() {
        var solvers = new BMASolver[this.bsms.size()];
        var res = new ArrayList<BitSet>();
        var i = 0;
        for (var bsm : this.bsms) {
            solvers[i] = new BMASolver(bsm, k, r);
            solvers[i].run();
            res.addAll(solvers[i++].centers);
            //System.out.println(new BinaryMatrix(solvers[i++].centers, bsm.getParent().getHeight()));
        }

        return new BinaryMatrix(res, bsms.get(0).getParent().getHeight());
    }
}

class BMASolver implements Runnable {

    private final BinarySubMatrix bsm;
    private final int k;
    private final int r;
    List<BitSet> centers;

    public BMASolver(BinarySubMatrix bsm, int k, int r) {
        this.bsm = bsm;
        this.k = k;
        this.r = r;
        this.centers = new ArrayList<>();
    }

    @Override
    public void run() {
        if (bsm.isFinished()) {
            centers.add(bsm.getColumn(0));
            return;
        }

        assert (bsm.getWidth() > 0);

        var I = new ArrayList<Integer>(bsm.getWidth());
        var S = new ArrayList<BitSet>();
        for (var i = 0; i < bsm.getWidth(); i++)
            I.add(i, i);

        try {
            this.centers = findCenters(I, S, k);
        } catch (BinaryMatrixNoInstanceException e) {
            e.printStackTrace();
        }
    }

    public List<BitSet> findCenters(List<Integer> I, List<BitSet> S, int d) throws BinaryMatrixNoInstanceException {
        var PartitionGenerator = new Partition<BitSet>();

        if (!S.isEmpty() && totalHammingDist(S) <= d) return S;
        if (S.size() == this.r) {
            throw new BinaryMatrixNoInstanceException("");
        }

        for (var h = 0; h <= d; h++) {
            d = extractCoveredColumns(I, S, d, h);

            if (d >= 0 && I.size() < magicNumber(d)) {
                for (int p = 1; p < (Math.min(I.size(), r - S.size())); p++) {
                    var partitions = PartitionGenerator.PartitionFirstEmpty(p, S);
                    for (var partition : partitions) {
                        List<BitSet> optimalCenter = new ArrayList<>();
                        for (var cluster : partition) {
                            int[] position = new int[bsm.getHeight()];
                            for (int i = 0; i < position.length; i++)
                                for (BitSet col : cluster)
                                    position[i] += col.get(i) ? 1 : -1;

                            var center = new BitSet();
                            for (int i = 0; i < position.length; i++)
                                center.set(i, position[i] > 0);

                            optimalCenter.add(center);
                        }
                        var tmpS = new ArrayList<>(S);
                        tmpS.addAll(optimalCenter);
                        if (totalHammingDist(tmpS) <= d)
                            return tmpS;
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
                            return findCenters(I, tmpList, d);
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
            if (l < h - 1) {
                I.remove(i);
                a--;
                d = d - l;
            }
        }
        return d;
    }

    private int totalHammingDist(List<BitSet> S) {
        int dist = 0;
        for (var column : bsm.getColumns()) {
            int tmpDist = 0;
            for (var vector : S) tmpDist = Math.min(hammingDistance(column, vector), tmpDist);
            dist += tmpDist;
        }
        return dist;
    }
}
