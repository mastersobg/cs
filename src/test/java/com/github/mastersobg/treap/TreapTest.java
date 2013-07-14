package com.github.mastersobg.treap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class TreapTest {

    protected static final long RND_SEED = 123456789123l;

    private Treap<Integer> tree;
    private List<Integer> list;
    private Treap<Integer> emptyTree;

    /* Supported operations:
    1. insert  +
    2. remove  +
    3. contains +
    5. print     +
    8. size  +
    9. build  +
    14. first() +
    15. last() +
    16. floor
     ceil
      higher
       lower
       iterating
     */

    protected boolean checkIncreasing(List<Integer> list) {
        Integer prev = Integer.MIN_VALUE;
        for (Integer o : list) {
            if (prev.compareTo(o) > 0) {
                return false;
            }
            prev = o;
        }
        return true;
    }

    protected List<Integer> getRandomValuesList(int size) {
        List<Integer> list = new ArrayList<Integer>(size);
        Random rnd = new Random(RND_SEED);
        for (int i = 0; i < size; ++i) {
            list.add(rnd.nextInt());
        }
        return list;
    }

    @Before
    public void setup() {
        list = getRandomValuesList(100);
        tree = new Treap<Integer>(list, RND_SEED);
        emptyTree = new Treap<Integer>();
    }

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
    public void testContainsInEmpty() {
        Assert.assertFalse(emptyTree.contains(100));
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

    @Test
    public void testRemoveDuplicated() {
        for (int i = 0; i < 10; ++i) {
            emptyTree.add(100);
        }
        for (int i = 0; i < 10; ++i) {
            Assert.assertTrue(emptyTree.contains(100));
            Assert.assertTrue(emptyTree.remove(100));
            tree.checkPrioritiesState();
            Assert.assertTrue(checkIncreasing(tree.keysAsList()));
        }
        Assert.assertFalse(emptyTree.contains(100));
        Assert.assertFalse(emptyTree.remove(100));
    }

    @Test
    public void testRemoveFromEmpty() {
        Assert.assertFalse(emptyTree.remove(100));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testAddNull() {
        emptyTree.add(null);
    }

    @Test
    public void testContainsRemoveNull() {
        Assert.assertFalse(emptyTree.contains(null));
        Assert.assertFalse(emptyTree.remove(null));
    }

    @Test(expected = NoSuchElementException.class)
    public void testFirstNull() {
        emptyTree.first();
    }

    @Test
    public void testFirst() {
        Collections.sort(list);
        for (Integer o : list) {
            Assert.assertEquals(o, tree.first());
            tree.remove(o);
        }
    }


    @Test(expected = NoSuchElementException.class)
    public void testLastNull() {
        emptyTree.first();
    }

    @Test
    public void testLast() {
        Collections.sort(list);
        for (int i = list.size() - 1; i >= 0; --i) {
            Assert.assertEquals(list.get(i), tree.last());
            tree.remove(list.get(i));
        }
    }

}
