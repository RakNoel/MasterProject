import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tools.BinaryMatrixFactory;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BinaryMatrixFactoryTest {

    public static BinaryMatrixFactory factory;
    public static final String testPath = "target/test-classes/";

    @BeforeAll
    static void beforeAll(){
        factory = BinaryMatrixFactory.BuildBinaryMatrixFactory();
    }

    @Test
    public void ReadSmallFileCorrectly() throws FileNotFoundException {
        var bnm = factory.readFromFile(new File(testPath + "smallMatrix.bnm"));
        assertEquals(6, bnm.getHeight());
        assertEquals(5, bnm.getWidth());

        for (int i = 0; i < 5; i++)
            assertTrue(bnm.getBit(i, i));

        System.out.println(bnm);
    }

    @Test
    public void ReadLargeFileCorrectly() throws FileNotFoundException {
        var bnm = factory.readFromFile(new File(testPath + "largeMatrix.bnm"));
        System.out.println("Finished");
    }
}
