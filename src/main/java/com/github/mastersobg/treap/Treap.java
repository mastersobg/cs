package com.github.mastersobg.treap;

import java.util.*;

public class Treap<T> {

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
