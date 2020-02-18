import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.BinaryMatrix;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BinaryMatrixTest {
    private BinaryMatrix bnm;
    private Random rnd;

    @BeforeEach
    public void beforeEach(){
        bnm = new BinaryMatrix(100, 100);
        rnd = new Random();
    }

    @Test
    public void NormalSetGet() {
        for (int i = 0; i < 1000; i++) {
            int x = rnd.nextInt(98) + 1;
            int y = rnd.nextInt(98) + 1;
            bnm.setBit(x, y, true);
            assertTrue(bnm.getBit(x, y));
        }
    }
}
