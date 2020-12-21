import com.raknoel.bma.structures.BinaryMatrix;
import com.raknoel.bma.structures.BinarySubMatrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BinaryMatrixTest {
    private Random rnd;

    @BeforeEach
    public void beforeEach() {
        rnd = new Random();
    }

    @Test
    void NormalMatrixSetGet() {
        var bnm = new BinaryMatrix(100, 100);
        for (int i = 0; i < 1000; i++) {
            int x = rnd.nextInt(98) + 1;
            int y = rnd.nextInt(98) + 1;
            bnm.setBit(x, y, true);
            assertTrue(bnm.getBit(x, y));
        }
    }

    @Test
    void NormalSubMatrixSetGet() {
        var bnm = new BinaryMatrix(100, 100);
        var bsm = new BinarySubMatrix(bnm);
        assertEquals(bnm.getWidth(), bsm.getWidth(), "Issue during setup of matrices");

        var delCount = rnd.nextInt(10);
        for (int i = 0; i < delCount; i++)
            bsm.deleteColumn(rnd.nextInt(delCount));

        for (int i = 0; i < 1000; i++) {
            int x = rnd.nextInt(98 - delCount) + 1;
            int y = rnd.nextInt(98 - delCount) + 1;
            bnm.setBit(x, y, true);
            assertTrue(bnm.getBit(x, y));
        }

        assertEquals(bnm.getWidth() - delCount, bsm.getWidth(), "Sub matrix not of expected width");
    }

    @Test
    void TestColumnDeletion() {
        var bnm = new BinaryMatrix(100, 100);
        var bsm = new BinarySubMatrix(bnm);
        assertEquals(bnm.getWidth(), bsm.getWidth(), "Issue during setup of matrices");

        for (int i = 0; i < 1000; i++) {
            int x = rnd.nextInt(98) + 1;
            int y = rnd.nextInt(98) + 1;
            bnm.setBit(x, y, true);
            assertTrue(bnm.getBit(x, y));
        }

        var delCount = rnd.nextInt(10);
        for (int i = 0; i < delCount; i++)
            bsm.deleteColumn(i);

        assertEquals(bnm.getWidth() - delCount, bsm.getWidth(), "Sub matrix not of expected width");
        assertEquals(100, bnm.getWidth(), "com.masterproject.main.Main matrix not of correct length");

        for (var i = 0; i < bsm.getWidth(); i++) {
            var col = bsm.getColumn(i);
            var colID = bsm.getColumnID(i);
            var supposedOriginal = bnm.getColumn(colID);

            assertEquals(supposedOriginal, col, "sub matrix columnID mismatch");
        }
    }
}
