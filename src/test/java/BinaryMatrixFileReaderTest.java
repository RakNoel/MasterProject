import org.junit.jupiter.api.Test;

import java.io.File;

import static generators.MatrixFileReader.readMatrixFromFile;
import static org.junit.jupiter.api.Assertions.*;

class BinaryMatrixFileReaderTest {

    public static final String testPath = "target/test-classes/";

    @Test
    void ReadSmallFileCorrectly() {
        var bnm = readMatrixFromFile(new File(testPath + "smallMatrix.bnm"));
        assertNotNull(bnm);
        assertEquals(6, bnm.getHeight());
        assertEquals(5, bnm.getWidth());

        for (int i = 0; i < 5; i++)
            assertTrue(bnm.getBit(i, i));

        System.out.println(bnm);
    }

    @Test
    void ReadLargeFileCorrectly() {
        var bnm = readMatrixFromFile(new File(testPath + "largeMatrix.bnm"));
        assertNotNull(bnm);
    }
}
