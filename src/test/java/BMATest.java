import com.raknoel.bma.exceptions.BinaryMatrixNoInstanceException;
import com.raknoel.bma.tools.BMA;
import generators.MatrixGenerator;
import org.junit.jupiter.api.Test;

import java.io.File;

import static generators.MatrixFileReader.readMatrixFromFile;

public class BMATest {

    public static final String testPath = "target/test-classes/";

    @Test
    void SimpleMatrixApproximation() throws BinaryMatrixNoInstanceException {
        int k = 10;
        int r = 6;

        var binaryMatrix = MatrixGenerator.generateRKMatrix(50, 10, 11, 6, 123);

        System.out.println(binaryMatrix);

        var solver = new BMA(binaryMatrix, k, r);
        var res = solver.Approximate();
        System.out.println(res);
    }

    @Test
    void KnownMatrixApproximation() throws BinaryMatrixNoInstanceException {
        int k = 2;
        int r = 2;

        var binaryMatrix = readMatrixFromFile(new File(testPath + "manualMatrixNoKernelBMA.bnm"));

        System.out.println(binaryMatrix);

        var solver = new BMA(binaryMatrix, k, r);
        var res = solver.Approximate();
        System.out.println(res);
    }
}
