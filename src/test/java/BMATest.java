import com.raknoel.bma.exceptions.BinaryMatrixNoInstanceException;
import com.raknoel.bma.structures.BinaryMatrix;
import com.raknoel.bma.tools.BMA;
import generators.MatrixGenerator;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Random;

import static generators.MatrixFileReader.readMatrixFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BMATest {

    public static final String testPath = "target/test-classes/";

    @Test
    void SimpleMatrixApproximation() throws BinaryMatrixNoInstanceException {
        int k = 5;
        int r = 6;

        var binaryMatrix = MatrixGenerator.generateRKMatrix(25, 7, 5, 6, new Random().nextInt());

        System.out.println(binaryMatrix);

        var solver = new BMA(binaryMatrix, k, r);
        var res = solver.Approximate();
        System.out.printf("Best solution K: %d, R: %d %n", res.getTotalCost(), res.getWidth());
    }

    @Test
    void KnownMatrixApproximation() throws BinaryMatrixNoInstanceException {
        int k = 6;
        int r = 4;

        var binaryMatrix = readMatrixFromFile(new File(testPath + "manualMatrixNoKernelBMA.bnm"));

        System.out.println(binaryMatrix);

        var solver = new BMA(binaryMatrix, k, r);
        var res = solver.Approximate();
        System.out.printf("Best solution K: %d, R: %d %n", res.getTotalCost(), res.getWidth());
        System.out.println(new BinaryMatrix(res.getCenters(), binaryMatrix.getHeight()));

        assert binaryMatrix != null;
        assertEquals(binaryMatrix.getChild().totalHammingDist(res.getCenters()), res.getTotalCost());
        assertEquals(2, res.getWidth());
    }
}
