package com.github.mastersobg.treap;

import java.util.*;

public class Treap<T> implements Iterable<T> {

    private static final Random RANDOM = new Random();

    private Node root;
    private Node tNode = new Node();

    private int size = 0;

    public Treap() {
    }

    public Treap(long seed) {
        RANDOM.setSeed(seed);
    }

    public Treap(Collection<T> values) {
        for (T value : values) {
            add(value);
        }
    }

    public Treap(Collection<T> values, long seed) {
        this(values);
        RANDOM.setSeed(seed);
    }

    public void add(T value) {
        if (value == null) {
            throw new IllegalArgumentException("value is null");
        }
        Node node = new Node(value);
        ++size;
        if (root == null) {
            root = node;
        } else {
            split(root, value, tNode);
            root = merge(tNode.left, node);
            root = merge(root, tNode.right);
            tNode.left = tNode.right = null;
        }
    }

    public boolean contains(T key) {
        return contains(root, key);
    }

    public boolean remove(T key) {
        if (!contains(key)) {
            return false;
        }
        root = remove(root, key);
        --size;
        return true;
    }

    public int size() {
        return size;
    }

    public List<T> keysAsList() {
        List<T> list = new ArrayList<T>(size);
        walkTree(root, list);
        return list;
    }

    public String treeAsString() {
        StringBuilder sb = new StringBuilder();
        walkTree(root, sb);
        return sb.toString();
    }

    public T first() {
        if (size == 0) {
            throw new NoSuchElementException("tree is empty");
        }
        return first(root);
    }

    public T last() {
        if (size == 0) {
            throw new NoSuchElementException("tree is empty");
        }
        return last(root);
    }

    public T floor(T key) {
        Node node = floor(root, key);
        return node == null ? null : node.key;
    }

    public T ceiling(T key) {
        Node node = ceiling(root, key);
        return node == null ? null : node.key;
    }

    public T higher(T key) {
        Node node = higher(root, key);
        return node == null ? null : node.key;
    }

    public T lower(T key) {
        Node node = lower(root, key);
        return node == null ? null : node.key;
    }


    @Override
    public Iterator<T> iterator() {
        return new TreapIterator();
    }

    private Node lower(Node tree, T key) {
        if (tree == null) {
            return null;
        }
        int cmp = tree.getComparable().compareTo(key);
        if (cmp < 0) {
            if (tree.right != null) {
                return lower(tree.right, key);
            } else {
                return tree;
            }
        } else {
            return lower(tree.left, key);
        }
    }

    private Node higher(Node tree, T key) {
        if (tree == null) {
            return null;
        }
        int cmp = tree.getComparable().compareTo(key);
        if (cmp <= 0) {
            return higher(tree.right, key);
        } else {
            Node node = higher(tree.left, key);
            return node == null ? tree : node;
        }
    }

    private Node ceiling(Node tree, T key) {
        if (tree == null) {
            return null;
        }
        int cmp = tree.getComparable().compareTo(key);
        if (cmp == 0) {
            return tree;
        } else if (cmp < 0) {
            return ceiling(tree.right, key);
        } else {
            Node node = ceiling(tree.left, key);
            return node == null ? tree : node;
        }
    }

    private Node floor(Node tree, T key) {
        if (tree == null) {
            return null;
        }
        int cmp = tree.getComparable().compareTo(key);
        if (cmp == 0) {
            return tree;
        } else if (cmp < 0) {
            if (tree.right != null) {
                return floor(tree.right, key);
            } else {
                return tree;
            }
        } else {
            return floor(tree.left, key);
        }
    }

    private T last(Node tree) {
        if (tree.right != null) {
            return last(tree.right);
        } else {
            return tree.key;
        }
    }

    private T first(Node tree) {
        if (tree.left != null) {
            return first(tree.left);
        } else {
            return tree.key;
        }
    }

    private boolean contains(Node tree, T key) {
        if (tree == null) {
            return false;
        }
        int cmp = tree.getComparable().compareTo(key);
        if (cmp == 0) {
            return true;
        } else if (cmp < 0) {
            return contains(tree.right, key);
        } else {
            return contains(tree.left, key);
        }
    }

    private Node remove(Node tree, T key) {
        int cmp = tree.getComparable().compareTo(key);
        if (cmp == 0) {
            return merge(tree.left, tree.right);
        } else if (cmp < 0) {
            tree.right = remove(tree.right, key);
        } else {
            tree.left = remove(tree.left, key);
        }
        return tree;
    }

    private void walkTree(Node t, StringBuilder sb) {
        if (t == null) {
            return;
        }
        walkTree(t.left, sb);
        sb.append(t);
        sb.append(System.getProperty("line.separator"));
        walkTree(t.right, sb);
    }

    private void walkTree(Node t, List<T> list) {
        if (t == null) {
            return;
        }
        walkTree(t.left, list);
        list.add(t.key);
        walkTree(t.right, list);
    }

    private void split(Node root, T key, Node ret) {
        if (root == null) {
            ret.left = ret.right = null;
        } else {
            if (root.getComparable().compareTo(key) < 0) {
                split(root.right, key, ret);
                root.right = ret.left;
                ret.left = root;
            } else {
                split(root.left, key, ret);
                root.left = ret.right;
                ret.right = root;
            }
        }
    }

    private Node merge(Node t1, Node t2) {
        Node ret;
        if (t1 == null || t2 == null) {
            ret = t1 == null ? t2 : t1;
        } else {
            if (t1.priority < t2.priority) {
                t2.left = merge(t1, t2.left);
                ret = t2;
            } else {
                t1.right = merge(t1.right, t2);
                ret = t1;
            }
        }
        return ret;
    }

    private class Node {

        private int priority;
        private T key;

        private Node left, right;

        // For creating temporary node
        Node() {
        }

        Node(T value) {
            this.key = value;
            priority = RANDOM.nextInt();
        }

        public Comparable<? super T> getComparable() {
            return (Comparable<? super T>) key;
        }

        @Override
        public String toString() {
            return "[" +
                    "key = " + key +
                    " priority = " + priority +
                    " left key = " + (left == null ? "null" : left.key.toString()) +
                    " right key = " + (right == null ? "null" : right.key.toString()) +
                    "]";
        }
    }

    private class TreapIterator implements Iterator<T> {

        // The following two stacks are for modeling depth-first-search in-order iterative traverse of the tree.
        private Node []nStack;
        // sStack corresponds to the vertex's in-order traversal state.
        // false - traverse the left subtree, true - traverse the right subtree
        private boolean []sStack;
        private int ptr = 0;

        public TreapIterator() {
            nStack = new Treap.Node[size];
            sStack = new boolean[size];
            if (root != null) {
                pushStack(root, false);
            }
        }

        @Override
        public boolean hasNext() {
            return ptr > 0;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            boolean state = topStackState();
            Node node = topStackNode();
            while (!state && node.left != null) {
                popStack();
                pushStack(node, true);
                pushStack(node.left, false);

                state = topStackState();
                node = topStackNode();
            }

            popStack();

            if (node.right != null) {
                pushStack(node.right, false);
            }

            return node.key;
        }

        void pushStack(Node node, boolean state) {
            nStack[ptr] = node;
            sStack[ptr++] = state;
        }

        Node topStackNode() {
            return nStack[ptr - 1];
        }

        boolean topStackState() {
            return sStack[ptr - 1];
        }

        void popStack() {
            --ptr;
            nStack[ptr] = null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // For debug purposes only

    void checkPrioritiesState() {
        checkPrioritiesState(root, Integer.MAX_VALUE);
    }

    void checkPrioritiesState(Node root, int priority) {
        if (root == null) {
            return;
        }
        if (root.priority > priority) {
            throw new IllegalStateException("Heap nature of treap is broken");
        }
        checkPrioritiesState(root.left, root.priority);
        checkPrioritiesState(root.right, root.priority);
    }
}
