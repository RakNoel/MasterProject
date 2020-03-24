import extra.MatrixGenerator;
import org.junit.jupiter.api.Test;
import tools.BinaryMatrix;
import tools.BinaryMatrixFactory;
import tools.BinaryMatrixNoInstanceException;
import tools.Kernel;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KernelTest {

    public static final int SEED = 123;
    public static final String testPath = "target/test-classes/";

    @Test
    public void ColumnReduceTest() {
        BinaryMatrix hasEquals = MatrixGenerator.generateMatrixEqualColumns(10, 10, 5, SEED);
        BinaryMatrix hasNoEquals = MatrixGenerator.generateDiagonalMatrix(1000, 1000);

        Kernel kernel = new Kernel(2, 2);

        System.out.println(hasEquals);

        kernel.ColumnReduce(hasEquals);
        kernel.ColumnReduce(hasNoEquals);

        System.out.println(hasEquals);

        assertEquals(hasNoEquals.getWidth(), hasNoEquals.getOriginalWidth());
        assertEquals(hasEquals.getWidth(), hasEquals.getOriginalWidth() - 1);
    }

    @Test
    public void RowReduceTest() {
        BinaryMatrix cantReduce = MatrixGenerator.generateDiagonalMatrix(1000, 1000);
        BinaryMatrix canReduce = MatrixGenerator.generateMatrixReducibleRows(10, 10, 5, SEED);

        Kernel kernel = new Kernel(3, 2);

        System.out.println(canReduce);

        kernel.RowReduce(cantReduce);
        kernel.RowReduce(canReduce);

        System.out.println(canReduce);

        assertEquals(cantReduce.getOriginalHeight(), cantReduce.getHeight());
        assertEquals(canReduce.getOriginalHeight() - 5, canReduce.getHeight());
    }

    @Test
    public void TestKernelOnKnownMatrix() throws FileNotFoundException, BinaryMatrixNoInstanceException {
        BinaryMatrix bnm = BinaryMatrixFactory
                .BuildBinaryMatrixFactory()
                .readFromFile(new File(testPath + "3partitionsMatrix.bnm"));

        System.out.println(bnm);

        var res = new Kernel(1, 3).Kernelize(bnm);

        for (var a : res) System.out.println(a);

        assertEquals(3, res.size());
    }
}
