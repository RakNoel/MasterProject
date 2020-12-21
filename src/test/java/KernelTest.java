import com.raknoel.bma.exceptions.BinaryMatrixNoInstanceException;
import com.raknoel.bma.extra.BinaryMatrixFactory;
import com.raknoel.bma.extra.MatrixGenerator;
import com.raknoel.bma.structures.BinaryMatrix;
import com.raknoel.bma.structures.BinarySubMatrix;
import com.raknoel.bma.tools.Kernel;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KernelTest {

    public static final int SEED = 123;
    public static final String testPath = "target/test-classes/";

    @Test
    void ColumnReduceTest() {
        BinaryMatrix hasEquals = MatrixGenerator.generateMatrixEqualColumns(10, 10, 5, SEED);
        BinaryMatrix hasNoEquals = MatrixGenerator.generateDiagonalMatrix(1000, 1000);

        Kernel kernel = new Kernel(2, 2);

        System.out.println(hasEquals);

        kernel.columnReduce(new BinarySubMatrix(hasEquals));
        kernel.columnReduce(new BinarySubMatrix(hasNoEquals));

        System.out.println(hasEquals);

        assertEquals(hasNoEquals.getWidth(), hasNoEquals.getWidth());
        assertEquals(hasEquals.getWidth(), hasEquals.getWidth() - 1);
    }

    @Test
    void RowReduceTest() {
        BinaryMatrix cantReduce = MatrixGenerator.generateDiagonalMatrix(1000, 1000);
        BinaryMatrix canReduce = MatrixGenerator.generateMatrixReducibleRows(10, 10, 5, SEED);

        Kernel kernel = new Kernel(3, 2);

        System.out.println(canReduce);

        kernel.rowReduce(new BinarySubMatrix(cantReduce));
        kernel.rowReduce(new BinarySubMatrix(canReduce));

        System.out.println(canReduce);

        assertEquals(cantReduce.getHeight(), cantReduce.getHeight());
        assertEquals(canReduce.getHeight() - 5, canReduce.getHeight());
    }

    @Test
    void TestKernelOnKnownMatrix() throws BinaryMatrixNoInstanceException {
        BinaryMatrix bnm = BinaryMatrixFactory
                .buildBinaryMatrixFactory()
                .readFromFile(new File(testPath + "3partitionsMatrix.bnm"));

        System.out.println(bnm);

        var res = new Kernel(1, 3).kernelize(bnm);

        for (var a : res) System.out.println(a);

        assertEquals(3, res.size());
    }
}
