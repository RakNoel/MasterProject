package com.raknoel.bma.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Partition<T> extends ArrayList<List<T>> {
    public static void main(String[] args) {
        var p = new Partition<String>();
        var t = new ArrayList<String>();

        t.add("A");
        t.add("B");
        t.add("C");
        t.add("D");

        var r = p.PartitionFirstEmpty(3, t);

        for (var pp : r) {
            System.out.print('{');
            for (var sub : pp) {
                System.out.print(Arrays.toString(sub.toArray()));
            }
            System.out.println('}');
        }
    }

    public List<Partition<T>> PartitionFirstEmpty(int k, List<T> A) {
        var cpA = new ArrayList<T>();
        cpA.addAll(A);

        var res = Partition(k, A);
        var tmp = Partition(k - 1, cpA);
        for (var p : tmp) {
            var x = new ArrayList<T>();
            p.add(x);
        }

        res.addAll(tmp);
        return res;
    }

    public List<Partition<T>> Partition(int k, List<T> A) {
        if (k == 1) {
            var res = new ArrayList<Partition<T>>();
            var tmp = new Partition<T>();
            tmp.add(A);
            res.add(tmp);
            return res;
        }
        if (k == A.size()) {
            var res = new ArrayList<Partition<T>>();
            var tmp = new Partition<T>();
            for (var a : A) {
                var sub = new ArrayList<T>();
                sub.add(a);
                tmp.add(sub);
            }
            res.add(tmp);
            return res;
        }

        var B = new ArrayList<Partition<T>>();
        var a1 = A.remove(0);
        var AA = Partition(k, A);

        for (var p : AA) {
            for (int i = 0; i < p.size(); i++) {
                var pp = new Partition<T>();
                pp.addAll(p);
                var x = pp.remove(i);
                var y = new ArrayList<>(x);
                y.add(a1);
                pp.add(y);

                B.add(pp);
            }
        }

        var AAA = Partition(k - 1, A);

        for (var p : AAA) {
            var tmp = new ArrayList<T>();
            tmp.add(a1);
            p.add(tmp);
            B.add(p);
        }

        return B;
    }
}
