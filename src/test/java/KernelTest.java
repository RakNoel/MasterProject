import com.raknoel.bma.exceptions.BinaryMatrixNoInstanceException;
import com.raknoel.bma.structures.BinarySubMatrix;
import com.raknoel.bma.tools.Kernel;
import generators.MatrixGenerator;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

import static generators.MatrixFileReader.readMatrixFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KernelTest {

    public static final int SEED = 123;
    public static final String testPath = "target/test-classes/";

    @Test
    void ColumnReduceTest() {
        BinarySubMatrix hasEquals = MatrixGenerator.generateMatrixEqualColumns(10, 10, 5, SEED).getChild();
        BinarySubMatrix hasNoEquals = MatrixGenerator.generateDiagonalMatrix(1000, 1000).getChild();

        Kernel kernel = new Kernel(2, 2);

        kernel.columnReduce(hasEquals);
        kernel.columnReduce(hasNoEquals);

        assertEquals(hasNoEquals.getWidth(), hasNoEquals.getParent().getWidth());
        assertEquals(hasEquals.getWidth(), hasEquals.getParent().getWidth() - 1);
    }

    @Test
    void RowReduceTest() {
        var cantReduce = MatrixGenerator.generateDiagonalMatrix(1000, 1000).getChild();
        var canReduce = MatrixGenerator.generateMatrixReducibleRows(10, 10, 5, SEED).getChild();

        Kernel kernel = new Kernel(3, 2);

        kernel.rowReduce(cantReduce);
        kernel.rowReduce(canReduce);

        assertEquals(cantReduce.getHeight(), cantReduce.getParent().getHeight());
        assertEquals(canReduce.getParent().getHeight() - 5, canReduce.getHeight());
    }

    @Test
    void TestPartitionOnKnownMatrix() throws BinaryMatrixNoInstanceException {
        var bnm = Objects.requireNonNull(
                readMatrixFromFile(
                        new File(testPath + "4partitionsMatrix.bnm")
                )
        ).getChild();

        var kernel = new Kernel(1, 4);
        kernel.columnReduce(bnm);
        var partitions = kernel.partition(bnm);

        for (var a : partitions)
            System.out.println(a);

        assertEquals(4, partitions.size());
    }
}
