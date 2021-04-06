import com.raknoel.bma.exceptions.BinaryMatrixNoInstanceException;
import com.raknoel.bma.tools.BMA;
import generators.MatrixGenerator;
import org.junit.jupiter.api.Test;

public class BMATest {
    @Test
    void SimpleMatrixApproximation() throws BinaryMatrixNoInstanceException {
        int k = 3;
        int r = 6;

        var binaryMatrix = MatrixGenerator.generateRKMatrix(50, 10, k, r, 123);

        System.out.println(binaryMatrix);

        var solver = new BMA(binaryMatrix, k, r);
        var res = solver.Approximate();
        System.out.println(res);
    }
}
