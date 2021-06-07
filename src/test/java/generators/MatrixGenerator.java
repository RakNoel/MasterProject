package generators;

import com.raknoel.bma.structures.BinaryMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class MatrixGenerator {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        System.out.print("Enter size of matrix: ");
        int x = scn.nextInt();
        int y = scn.nextInt();
        scn.nextLine();
        System.out.print("Enter name of new file: ");
        String name = scn.nextLine();
        System.out.printf("Generating new matrix. W:%d H:%d output:%s %n", x, y, name);

        File file = new File(name);
        try (FileOutputStream fout = new FileOutputStream(file)) {
            fout.write((x + " " + y + System.lineSeparator()).getBytes());
            Random rnd = new Random();
            for (int i = 0; i < y; i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < x; j++)
                    row.append(rnd.nextInt(2));
                row.append(System.lineSeparator());
                fout.write(row.toString().getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a complete matrix where every bit is simply randomized and is not generated with any properties.
     *
     * @param width  With of generated matrix
     * @param height Height of generated matrix
     * @param seed   Seed used in the random for generating the matrix.
     * @return Returns a new random binary matrix.
     */
    public static BinaryMatrix generateRandomMatrix(int width, int height, int seed) {
        BinaryMatrix bnm = new BinaryMatrix(width, height);
        Random rnd = new Random(seed);

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                bnm.setBit(i, j, rnd.nextBoolean());

        return bnm;
    }

    /**
     * Generates a Binary Matrix based on the given parameters. This algorithm will generate a set R with r columns
     * and pick (width - r) duplicates from R randomly. This ensures the resulting matrix M contains the set R and
     * and all elements of M exists in R. Resulting in a BinaryMatrix of r centers for k=0. The generator will then
     * flip d bits in the entire matrix randomly. If d <= k, the final matrix should have a (k=d, r) solution.
     *
     * @param width  The width of the resulting BinaryMatrix
     * @param height The height of the resulting BinaryMatrix
     * @param d      The number of bits to flip after generation (Noise)
     * @param r      The number of random centers to generate for the BinaryMatrix
     * @param seed   The seed used for the random-generator
     * @return BinaryMatrix following the promises of the method
     */
    public static BinaryMatrix generateRKMatrix(int width, int height, int d, int r, int seed) {
        if (width < r) throw new IllegalArgumentException("matrix too small to hold r columns");

        var rnd = new Random(seed);
        var centers = new ArrayList<BitSet>();

        //Produce r center
        for (int i = 0; i < r; i++) {
            var tmp = new BitSet(height);
            for (int b = 0; b < height; b++)
                tmp.set(b, rnd.nextBoolean());
            centers.add(tmp);
        }

        //Create matrix list
        var matrixLayout = new ArrayList<BitSet>();

        //From centers pick width columns from centers
        for (int i = 0; i < width - r; i++)
            matrixLayout.add((BitSet) centers.get(rnd.nextInt(centers.size())).clone());

        //Flips k bits
        for (int i = 0; i < d; i++) {
            var pointer = matrixLayout.get(rnd.nextInt(matrixLayout.size()));
            pointer.flip(rnd.nextInt(height));
        }

        //Add all centers to guarantee that M contains all R after random bit flips.
        matrixLayout.addAll(centers);

        //Randomize the list
        Collections.shuffle(matrixLayout);

        return new BinaryMatrix(matrixLayout, height);
    }

    /**
     * Generates a complete 0-binary-matrix where every bit along the diagonal is flipped to a 1.
     *
     * @param width  With of generated matrix
     * @param height Height of generated matrix
     * @return Returns a new diagonal-only binary matrix.
     */
    public static BinaryMatrix generateDiagonalMatrix(int width, int height) {
        BinaryMatrix bnm = new BinaryMatrix(width, height);
        for (int i = 0; i < width; i++) bnm.setBit(i, i, true);
        return bnm;
    }

    public static BinaryMatrix generateMatrixEqualColumns(int width, int height, int equals, int seed) {
        if (width < equals) throw new IllegalArgumentException("Equals must be smaller than or equal to width");

        Random rnd = new Random(seed);
        BinaryMatrix bnm = generateDiagonalMatrix(width, height);

        int original = rnd.nextInt(width);
        var col = bnm.getColumn(original);
        HashSet<Integer> touched = new HashSet<>();
        touched.add(original);

        for (int i = 0; i < equals; ) {
            int next = rnd.nextInt(width);
            if (!touched.contains(next)) {
                bnm.setColumn(next, col);
                touched.add(next);
                i++;
            }
        }

        return bnm;
    }

    public static BinaryMatrix generateMatrixReducibleRows(int width, int height, int reduce, int seed) {
        Random rnd = new Random(seed);
        BinaryMatrix bnm = generateDiagonalMatrix(width, height);
        HashSet<Integer> touched = new HashSet<>();
        for (int i = 0; i < reduce; ) {
            int next = rnd.nextInt(height);
            if (!touched.contains(next)) {
                boolean b = rnd.nextBoolean();
                for (int c = 0; c < width; c++) bnm.setBit(c, next, b);
                touched.add(next);
                i++;
            }
        }

        return bnm;
    }
}
