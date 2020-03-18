package tools;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.BiFunction;

class SegmentTree<T extends Number> {
    private Number[] tree;
    private BiFunction<Number, Number, Number> queryFunction;

    public final int SIZE;
    public final int OFFSET;

    public SegmentTree(Class<T> t, T[] leaves, T fill, BiFunction<Number, Number, Number> queryFunction) {
        int treeHeight = (int) Math.ceil(Math.log10(leaves.length + 2) / Math.log10(2.0));
        this.SIZE = (int) (Math.pow(2, treeHeight + 1) - 1);
        this.tree = (Number[]) Array.newInstance(t, this.SIZE + 1);
        Arrays.fill(this.tree, fill);
        this.OFFSET = (SIZE / 2) + 2;
        this.queryFunction = queryFunction;
        System.arraycopy(leaves, 0, tree, OFFSET, leaves.length);
        this.segment();
    }

    public Number query(int from, int to, T sumStart) throws NoSuchMethodException {
        var startLeft = OFFSET + from - 1;
        var startRight = OFFSET + to + 1;

        Number result = sumStart;

        while (getParentId(startLeft) != getParentId(startRight)) {
            boolean leftCounts = startLeft % 2 == 0;
            boolean rightCounts = startRight % 2 == 1;

            var nextLeft = getParentId(startLeft);
            var nextRight = getParentId(startRight);

            if (leftCounts) result = queryFunction.apply(result, tree[getRightChildId(nextLeft)]);
            if (rightCounts) result = queryFunction.apply(result, tree[getLeftChildId(nextRight)]);

            startLeft = nextLeft;
            startRight = nextRight;
        }

        return result;
    }

    public Number query(int from, T sumStart) throws NoSuchMethodException {
        return query(from, SIZE - OFFSET, sumStart);
    }

    public void update(int position, T value) {
        var init = position + OFFSET;
        tree[init] = value;
        while (init > 1) {
            var parent = getParentId(init);
            tree[parent] = queryFunction.apply(tree[getLeftChildId(parent)], tree[getRightChildId(parent)]);
            init = parent;
        }
    }

    public Number get(int position) {
        return this.tree[position + OFFSET];
    }


    private void segment() {
        var init = this.SIZE;
        while (init != 1) {
            var parent = getParentId(init);
            tree[parent] = queryFunction.apply(tree[getLeftChildId(parent)], tree[getRightChildId(parent)]);
            init -= 2;
        }
    }

    private int getParentId(int myId) {
        return (myId) / 2;
    }

    private int getLeftChildId(int myId) {
        return (myId * 2);
    }

    private int getRightChildId(int myId) {
        return (myId * 2) + 1;
    }


}