import extra.MatrixGenerator;
import org.junit.jupiter.api.Test;
import tools.BinaryMatrix;
import tools.Kernel;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KernelTest {

    public static final int SEED = 123;

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
}
