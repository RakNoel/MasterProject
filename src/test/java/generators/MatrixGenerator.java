package generators;

import com.raknoel.bma.structures.BinaryMatrix;
import com.raknoel.bma.structures.BinarySubMatrix;

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

    public static BinaryMatrix generateRandomMatrix(int width, int height, int seed) {
        BinaryMatrix bnm = new BinaryMatrix(width, height);
        Random rnd = new Random(seed);

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                bnm.setBit(i, j, rnd.nextBoolean());

        return bnm;
    }

    public static BinaryMatrix generateRKMatrix(int width, int height, int k, int r, int seed) {
        var rnd = new Random(seed);
        var centers = new ArrayList<BitSet>();

        //Produce r center
        for (int i = 0; i < r; i++){
            var tmp = new BitSet(height);
            for (int b = 0; b < height; b++)
                tmp.set(b, rnd.nextBoolean());
            centers.add(tmp);
        }

        //From centers pick width columns from centers
        var matrixLayout = new ArrayList<BitSet>();
        for (int i = 0; i < width; i++)
            matrixLayout.add(centers.get(rnd.nextInt(centers.size())));

        //Flips k bits
        for (int i = 0; i < k; i++){
            var pointer = matrixLayout.get(rnd.nextInt(matrixLayout.size()));
            pointer.flip(rnd.nextInt(height));
        }

        return new BinaryMatrix(matrixLayout, height);
    }

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
