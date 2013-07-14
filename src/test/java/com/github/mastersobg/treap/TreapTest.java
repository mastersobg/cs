package com.github.mastersobg.treap;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class TreapTest {

    private static final long RND_SEED = 123456789123l;

    /* Supported operations:
    1. insert
    2. remove
    3. find
    4. reverse
    5. print
    6. segment operations
    7. count
    8. size
    9. build
    10. unite
    11. intersect
    12. explicit/implicit trees
    13. getKth
     */

    @Test
    public void simpleTest() {
        Treap<Integer> tree1 = new Treap<Integer>(RND_SEED);
        List<Integer> list = getRandomValuesList(100);
        for (Integer i : list) {
            tree1.add(i);
        }

        Treap<Integer> tree2 = new Treap<Integer>(list, RND_SEED);

        Collections.sort(list);

        tree1.checkPrioritiesState();
        Assert.assertEquals(list, tree1.keysAsList());
        Assert.assertEquals(100, tree1.size());

        tree2.checkPrioritiesState();
        Assert.assertEquals(list, tree2.keysAsList());
        Assert.assertEquals(100, tree2.size());
    }

    @Test
    public void testContains() {
        List<Integer> list = getRandomValuesList(100);
        Treap<Integer> tree = new Treap<Integer>(list, RND_SEED);
        for (Integer i : list) {
            Assert.assertTrue(tree.contains(i));
        }
        for (int i = 0; i < 100; ++i) {
            if (!list.contains(i)) {
                Assert.assertFalse(tree.contains(i));
            }
        }
    }

    @Test
    public void testAllEqual() {
        Treap<Integer> tree = new Treap<Integer>(RND_SEED);
        for (int i = 0; i < 100; ++i) {
            tree.add(100);
        }
        tree.checkPrioritiesState();
        Assert.assertTrue(checkIncreasing(tree.keysAsList()));
        Assert.assertEquals(100, tree.size());
    }

    @Test
    public void testRemove() {
        List<Integer> list = getRandomValuesList(100);
        HashSet<Integer> set = new HashSet<Integer>();
        for (Integer o : list) {
            if (set.contains(o)) {
                Assert.assertTrue(false);
            }
            set.add(o);
        }
        Treap<Integer> tree = new Treap<Integer>(list, RND_SEED);
        int size = 100;
        for (Integer o : list) {
            Assert.assertTrue(tree.remove(o));
            // Of course the treap can contain dublicate values but for this test they are not
            // so I can check this way that the value was actually removed
            Assert.assertFalse(tree.contains(o));
            --size;
            tree.checkPrioritiesState();
            Assert.assertTrue(checkIncreasing(tree.keysAsList()));
            Assert.assertEquals(size, tree.size());
        }
        Assert.assertEquals(0, tree.keysAsList().size());

    }

    private boolean checkIncreasing(List<Integer> list) {
        Integer prev = Integer.MIN_VALUE;
        for (Integer o : list) {
            if (prev.compareTo(o) > 0) {
                return false;
            }
            prev = o;
        }
        return true;
    }

    private List<Integer> getRandomValuesList(int size) {
        List<Integer> list = new ArrayList<Integer>(size);
        Random rnd = new Random(RND_SEED);
        for (int i = 0; i < size; ++i) {
            list.add(rnd.nextInt());
        }
        return list;
    }

}
