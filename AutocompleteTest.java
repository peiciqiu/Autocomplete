import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AutocompleteTest {
    private Autocomplete at;

    @Before
    public void init() {
        at = new Autocomplete();
    }

    @Test
    public void testAddWord() {
        String a = "   a  ";
        String abb = "abb";
        String abc = "abc";
        a = a.replaceAll("\\s", "");
        assertEquals(a, "a");
        at.addWord(a, 5);
        at.addWord(abb, 10);
        at.addWord(abc, 15);

        Node aCopy = at.getSubTrie("a");
        Node abCopy = at.getSubTrie("ab");
        Node abbCopy = at.getSubTrie("abb");

        //the node "a"
        for (int i = 0; i < 25; i++) {
            if (i == 0) {
                assertNotNull(at.root.getReferences()[i]);
                Node desc = at.root.getReferences()[i];
                assertEquals(desc.getTerm().getTerm(), "a");
                assertEquals(desc.getTerm().getWeight(), 5);
                assertEquals(desc.getWords(), 1);
                assertEquals(desc.getPrefixes(), 3);
            } else {
                assertNull(at.root.getReferences()[i]);
            }

        }

        //the node "ab"
        for (int i = 0; i < 25; i++) {
            if (i == 1) {
                assertNotNull(aCopy.getReferences()[i]);
                Node desc = aCopy.getReferences()[i];
                assertEquals(desc.getTerm().getTerm(), "ab");
                assertEquals(desc.getTerm().getWeight(), 0);
                assertEquals(desc.getWords(), 0);
                assertEquals(desc.getPrefixes(), 2);
            } else {
                assertNull(aCopy.getReferences()[i]);
            }
        }

        //check that the node "ab" exists
        for (int i = 0; i < 25; i++) {
            if (i == 1) {
                assertNotNull(aCopy.getReferences()[i]);
            } else {
                assertNull(aCopy.getReferences()[i]);
            }
        }

        //reference to node abb
        for (int i = 0; i < 25; i++) {
            if (i == 1) {
                assertNotNull(abCopy.getReferences()[i]);
                Node desc = abCopy.getReferences()[i];
                assertEquals(desc.getTerm().getTerm(), "abb");
                assertEquals(desc.getTerm().getTerm(), abbCopy.getTerm().getTerm());
                assertEquals(desc.getTerm().getWeight(), 10);
                assertEquals(desc.getTerm().getWeight(), abbCopy.getTerm().getWeight());
                assertEquals(desc.getWords(), 1);
                assertEquals(desc.getWords(), abbCopy.getWords());
                assertEquals(desc.getPrefixes(), 1);
                assertEquals(desc.getPrefixes(), abbCopy.getPrefixes());
            } else if (i == 2) {
                assertNotNull(abCopy.getReferences()[i]);
                Node desc = abCopy.getReferences()[i];
                assertEquals(desc.getTerm().getTerm(), "abc");
                assertEquals(desc.getTerm().getWeight(), 15);
                assertEquals(desc.getWords(), 1);
                assertEquals(desc.getPrefixes(), 1);
                assertEquals(desc.getPrefixes(), abbCopy.getPrefixes());
            } else {
                assertNull(abCopy.getReferences()[i]);
            }
        }

        //abb's reference -> all null
        for (int i = 0; i < 25; i++) {
            assertNull(abbCopy.getReferences()[i]);
        }
    }

    @Test
    public void testBuildTrie() {
        at.buildTrie("./pokemon.txt", 3);
        Node test = at.getSubTrie("alakazam");
        assertNotNull(test);
        assertEquals(test.getWords(),1);
        assertEquals(test.getTerm().getTerm(), "alakazam");
        assertEquals(test.getTerm().getWeight(), 1064080);
        assertEquals(at.root.getPrefixes(), 729);

        //something's off here
        Node test1 = at.getSubTrie("alakaz");
        assertNotNull(test1);
        assertEquals(test1.getWords(),0);
        assertEquals(test1.getTerm().getTerm(), "alakaz");
        assertEquals(at.root.getPrefixes(), 729);
    }

    @Test
    public void testGetSubTrie() {
        //build a tree manually
        at.root.getReferences()[0] = new Node("a", 0);
        at.root.getReferences()[1] = new Node("b", 0);

        Node a = at.root.getReferences()[0];
        a.setPrefixes(1);

        a.getReferences()[2] = new Node("ac", 5);
        Node ac = a.getReferences()[2];

        ac.setWords(1);
        ac.setPrefixes(1);

        //b
        Node b = at.root.getReferences()[1];
        b.getReferences()[0] = new Node("ba", 5);
        b.setPrefixes(2);

        //two legit words: ba, bag
        Node ba = b.getReferences()[0];

        ba.setWords(1);
        ba.setPrefixes(2);

        ba.getReferences()[6] = new Node("bag", 10);
        Node bag = ba.getReferences()[6];
        bag.setWords(1);
        bag.setPrefixes(1);

        Node ab = at.getSubTrie("ab");
        assertNull(ab);

        Node aCopy = at.getSubTrie("a");
        assertEquals(aCopy.getWords(), 0);
        assertEquals(aCopy.getPrefixes(), 1);
        assertNotNull(aCopy.getReferences()[2]);
        assertNull(aCopy.getReferences()[1]);

        Node acCopy = at.getSubTrie("ac");
        assertNotNull(acCopy);
        assertEquals(acCopy.getWords(), 1);
        assertEquals(acCopy.getPrefixes(), 1);

        Node adCopy = at.getSubTrie("ad");
        assertNull(adCopy);

        Node bCopy = at.getSubTrie("b");
        assertEquals(bCopy.getPrefixes(),2);

        Node baCopy = at.getSubTrie("ba");
        assertEquals(baCopy.getPrefixes(),2);
        assertEquals(baCopy.getWords(),1);

        Node bagCopy = at.getSubTrie("bag");
        assertEquals(bagCopy.getPrefixes(), 1);
        assertEquals(bagCopy.getWords(), 1);

        Node invalid = at.getSubTrie("3e");
        assertNull(invalid);

    }

    @Test
    public void testCountPrefixes() {
        at.buildTrie("./test.txt", 3);
        Node subTrie = at.getSubTrie("app");
        int num = subTrie.getPrefixes();
        assertEquals(5, num);

    }

    @Test
    public void testGetSuggestions() {
        at.buildTrie("./test.txt", 3);
        Node subTrie = at.getSubTrie("app");
        int num = subTrie.getPrefixes();
        assertEquals(5, num);

        List<ITerm> suggestions = at.getSuggestions("app");
        

        assertEquals(suggestions.size(), 5);
        assertEquals(suggestions.get(0).getTerm(), "app");
        assertEquals(suggestions.get(1).getTerm(), "append");
        assertEquals(suggestions.get(2).getTerm(), "appendix");

        List<ITerm> suggestions1 = at.getSuggestions("");
        assertEquals(suggestions1.size(), 5);
        assertEquals(suggestions1.get(0).getTerm(), "app");
        assertEquals(suggestions1.get(1).getTerm(), "append");
        assertEquals(suggestions1.get(2).getTerm(), "appendix");

    }
}