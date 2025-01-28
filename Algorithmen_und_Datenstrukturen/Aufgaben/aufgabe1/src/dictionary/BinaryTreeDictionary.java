// O. Bittel
// 22.09.2022
package dictionary;

import java.util.Iterator;

/**
 * Implementation of the Dictionary interface as AVL tree.
 * <p>
 * The entries are ordered using their natural ordering on the keys,
 * or by a Comparator provided at set creation time, depending on which constructor is used.
 * <p>
 * An iterator for this dictionary is implemented by using the parent node reference.
 *
 * @param <K> Key.
 * @param <V> Value.
 */
public class BinaryTreeDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {
    private V oldValue;

    static private class Node<K, V> {
        K key;
        V value;
        int height;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        Node(K k, V v) {
            key = k;
            value = v;
            height = 0;
            left = null;
            right = null;
            parent = null;
        }
    }


    private Node<K, V> root = null;
    private int size = 1;

    // ...
    private Node<K,V> rotateRight(Node<K,V> p) {
        assert p.left != null;
        Node<K, V> q = p.left;
        p.left = q.right;
        if (p.left != null)
            p.left.parent = p;
        q.right = p;
        q.right.parent = q;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        q.height = Math.max(getHeight(q.left), getHeight(q.right)) + 1;
        return q;
    }
    private Node<K,V> rotateLeft(Node<K,V> p) {
        assert p.right != null;
        Node<K,V> q = p.right;
        p.right = q.left;
        if (p.right != null)
            p.right.parent = p;
        q.left = p;
        q.left.parent = q;
        p.height = Math.max(getHeight(p.right), getHeight(p.left)) +1;
        q.height = Math.max(getHeight(q.right), getHeight(q.left)) +1;
        return q;    }

    private Node<K,V> rotateLeftRight(Node<K,V> p) {
        assert p.left != null;
        p.left = rotateLeft(p.left);
        return rotateRight(p);
    }
    private Node<K,V> rotateRightLeft(Node<K,V> p) {
        assert p.right != null;
        p.right = rotateRight(p.right);
        return rotateLeft(p);
    }
    //Alda 4-19
    private Node<K,V> balance(Node<K,V> p) {
        if (p == null)
            return null;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        if (getBalance(p) == -2) {
            if (getBalance(p.left) <= 0)
                p = rotateRight(p);
            else
                p = rotateLeftRight(p);
        }
        else if (getBalance(p) == +2) {
            if (getBalance(p.right) >= 0)
                p = rotateLeft(p);
            else
                p = rotateRightLeft(p);
        }
        return p;
    }

    private int getHeight(Node<K,V> p) {
        if (p == null) {
            return -1;
        } else {
            return p.height;
        }
    }

    private int getBalance(Node<K,V> p) {
        if (p == null) {
            return 0;
        } else {
            return getHeight(p.right) - getHeight(p.left);
        }
    }


    /*
    @Override
    public V insert(K key, V value) {
        if(root == null){
            root = new Node<>(key, value);
            size++;
            return null;
        } else if ( key == null || value == null){
            System.out.println("Key or Value is null");
            throw new NullPointerException();
        } else {
            Node<K,V> p = root;
            while (true){
                int cmp = key.compareTo(p.key);
                if(cmp < 0){
                    if(p.left == null){
                        p.left = new Node<>(key, value);
                        p.left.parent = p;
                        size++;
                        return null;
                    } else {
                        p = p.left;
                    }
                } else if (cmp > 0){
                    if(p.right == null){
                        p.right = new Node<>(key, value);
                        p.right.parent = p;
                        size++;
                        return null;
                    } else {
                        p = p.right;
                    }
                } else {
                    V oldValue = p.value;
                    p.value = value;
                    balance(p);
                    return oldValue;
                }
            }
        }
    }

     */

    @Override
    public V insert(K key, V value) {
        root = insertR(key, value, root);
        if (root != null) {
            root.parent = null;
        }
        return oldValue;
    }

    private Node<K,V> insertR(K key, V value, Node<K,V> p) {
        if (p == null) {
            p = new Node(key, value);
            oldValue = null;
            size++;
        }
        else if (key.compareTo(p.key) < 0){
            p.left = insertR(key, value, p.left);
            if (p.left != null) {
                p.left.parent = p;
            }

        } else if (key.compareTo(p.key) > 0){
            p.right = insertR(key, value, p.right);
            if (p.right != null) {
                p.right.parent = p;
            }

        } else {
            oldValue = p.value;
            p.value = value;
        }
        p = balance(p);
        return p;
    }
    @Override
    public V search(K key) {
        //case leere liste
        if (size == 0){
            return null;
        //falsche übergabeparameter
        } else if (key == null){
            throw new NullPointerException();
        } else {
            Node<K,V> p = root;
            while (true){
                int cmp = key.compareTo(p.key);
                //wenn key kleiner als p.key dann gehe nach links
                if(cmp < 0){
                    if(p.left == null){
                        return null;
                    } else {
                        p = p.left;
                    }
                }
                //wenn key größer als p.key dann gehe nach rechts
                else if (cmp > 0){
                    if(p.right == null){
                        return null;
                    } else {
                        p = p.right;
                    }
                } else {
                    return p.value;
                }
            }
        }
    }

    @Override
    public V remove(K key) {
        root = removeR(key, root);
        if (root != null) {
            root.parent = null;
        }
        if (oldValue != null) {
            size--;
        }
        return oldValue;
    }

    private static class MinEntry<K, V> {
        private K key;
        private V value;
    }

    private Node<K, V> removeR(K key, Node<K, V> p) {

        if (p == null) {
            oldValue = null;
        } else if (key.compareTo(p.key) < 0) {
            p.left = removeR(key, p.left);
        } else if (key.compareTo(p.key) > 0) {
            p.right = removeR(key, p.right);
        } else if (p.left == null || p.right == null) {
            oldValue = p.value;
            p = (p.left != null) ? p.left : p.right;
            size--;
        } else {
            MinEntry<K, V> min = new MinEntry<>();
            p.right = getRemMinR(p.right, min);
            oldValue = p.value;
            p.key = min.key;
            p.value = min.value;
            size--;
        }
        p = balance(p);
        return p;
    }

    private Node<K, V> getRemMinR(Node<K, V> p, MinEntry<K, V> min) {
        assert p != null;
        if (p.left == null) {
            min.key = p.key;
            min.value = p.value;
            p = p.right;
        } else {
            p.left = getRemMinR(p.left, min);
        }
        p = balance(p);
        return p;
    }
/*
    @Override
    public V remove(K key) {
        if(root == null){
            return null;
        } else if (key == null){
            throw new NullPointerException();
        } else {
            Node<K,V> p = root;
            while (true){
                int cmp = key.toString().compareTo(p.key.toString());
                if(cmp < 0){
                    if(p.left == null){
                        return null;
                    } else {
                        p = p.left;
                    }
                } else if (cmp > 0){
                    if(p.right == null){
                        return null;
                    } else {
                        p = p.right;
                    }
                } else {
                    if(p.left == null && p.right == null){
                        V oldValue = p.value;
                        p.value = null;
                        p.key = null;
                        size--;
                        return oldValue;
                    } else if (p.left != null && p.right == null){
                        Node<K,V> q = p.left;
                        V oldValue = p.value;
                        p.value = q.value;
                        p.key = q.key;
                        size--;
                        return oldValue;
                    } else if (p.right != null && p.left == null){
                        Node<K,V> q = p.right;
                        V oldValue = p.value;
                        p.value = q.value;
                        p.key = q.key;
                        size--;
                        return oldValue;
                    }

                }
            }
        }
    }

 */
    @Override
    public int size() {
        //prettyPrint();
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new BinTreeIterator();
    }

    private class BinTreeIterator implements Iterator<Entry<K, V>> {
        private Node<K, V> cursor;

        public BinTreeIterator() {
            this.cursor = null;
        }

        public boolean hasNext() {
            //Baum hat keine Elemente
            if (root == null) {
                return false;
            //suche den ersten Knoten
            } else if (cursor == null){
                cursor = leftMostChild(root);
                return true;
            }
            //wenn der aktuelle einen rechten nachvolger hat suche das linkenste Kind
            if (cursor.right != null) {
                cursor = leftMostChild(cursor.right);
            }
            //wenn der knoten keinen rechten nachvolger hat, cursor auf den nächst höheren linken knoten setzen
            else {
                cursor = parentOfLeftMostAncestor(cursor);
                return cursor != null;
            }
            return true;
        }

        public Entry<K, V> next() {
            return new Entry<K, V>(cursor.key, cursor.value);
        }
    }

    private Node<K,V> leftMostChild(Node<K,V> p) {
        assert p != null;
        while (p.left != null)
            p = p.left;
        return p;
    }

    private Node<K,V> parentOfLeftMostAncestor(Node<K,V> p) {
        assert p != null;
        // wenn p.parrent nicht root ist und p nicht der rechte knoten seines vaters ist
        while (p.parent != null && p.parent.right == p)
            p = p.parent;
        return p.parent; // kann auch null sein
    }



    /**
     * Pretty prints the tree
     */
    public void prettyPrint() {
        printR(0, root);
    }

    private void printR(int level, Node<K, V> p) {
        printLevel(level);
        if (p == null) {
            System.out.println("#");
        } else {
            System.out.println(p.key + " " + p.value + "^" + ((p.parent == null) ? "null" : p.parent.key));
            if (p.left != null || p.right != null) {
                printR(level + 1, p.left);
                printR(level + 1, p.right);
            }
        }
    }

    private static void printLevel(int level) {
        if (level == 0) {
            return;
        }
        for (int i = 0; i < level - 1; i++) {
            System.out.print("   ");
        }
        System.out.print("|__");
    }
}
