import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testEquals() {
        Node node1 = new Node("apple", 5);
        Node node2 = new Node("apple", 10);
        assertEquals(node1, node2);
    }
}