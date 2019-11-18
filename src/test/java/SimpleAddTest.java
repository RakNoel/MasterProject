import org.junit.jupiter.api.Test;
import tools.simpleAdd;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleAddTest {

    @Test
    void SimpleAdd_Correct() {
        var p = simpleAdd.myAdd(2, 2);
        assertEquals(4, p);
    }
}
