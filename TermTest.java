import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TermTest {

    @Test
    public void testCompareTo() {
        Term term1 = new Term("ab",15);
        Term term2 = new Term("cd",10);
        int result = term1.compareTo(term2);
        assertTrue(result < 0);
    }

    @Test
    public void testByPrefixOrder() {
        List<Term> l = new ArrayList<Term>();
        l.add(new Term("applez", 5));
        l.add(new Term("applea", 50));
        l.add(new Term("bee", 10));
        l.add(new Term("applee", 15));

        l.sort(ITerm.byPrefixOrder(2));

        assertEquals(l.get(0).getTerm(), "applez");
        assertEquals(l.get(1).getTerm(), "applea");
        assertEquals(l.get(2).getTerm(), "applee");
        assertEquals(l.get(3).getTerm(), "bee");
    }

    @Test
    public void testByReverseWeightOrder() {
        List<Term> l = new ArrayList<Term>();
        l.add(new Term("applez", 5));
        l.add(new Term("applea", 50));
        l.add(new Term("bee", 10));
        l.add(new Term("applee", 15));

        l.sort(ITerm.byReverseWeightOrder());
        assertEquals(l.get(0).getTerm(), "applea");
        assertEquals(l.get(1).getTerm(), "applee");
        assertEquals(l.get(2).getTerm(), "bee");
        assertEquals(l.get(3).getTerm(), "applez");

    }

    @Test
    public void testEquals() {
        Term term1 = new Term("apple", 5);
        Term term2 = new Term("apple", 10);
        assertEquals(term1, term2);
    }



}