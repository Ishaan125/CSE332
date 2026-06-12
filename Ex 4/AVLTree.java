public class AVLTree<K extends Comparable<K>, V> extends BinarySearchTree<K, V> {

    private V oldValue;

    public AVLTree() {
        super();
    }

    // Inserts the key-value pair and maintains the AVL balance property. 
    // Returns the previous value associated with the key if it exists, otherwise returns null.
    public V insert(K key, V value) {
        oldValue = null;
        root = helpInsert(key, value, root);
        return oldValue;
    }

    // Recursive helper for insert. Inserts the key-value pair and maintains the AVL balance property.
    private TreeNode<K, V> helpInsert(K key, V value, TreeNode<K, V> curr) {
        if (curr == null) {
            size++;
            return new TreeNode<>(key, value);
        }

        int cmp = curr.key.compareTo(key);
        // If the key already exists, update the value and return without modifying the structure of the tree.
        if (cmp == 0) {
            oldValue = curr.value;
            curr.value = value;
            return curr;
        }
        // Otherwise, insert the new key-value pair and then check if we need to perform any rotations to maintain the AVL balance property. 
        if (cmp < 0) {
            curr.right = helpInsert(key, value, curr.right);
        } else {
            curr.left = helpInsert(key, value, curr.left);
        }

        // After inserting the new key-value pair, update the height of the current node and check if it is balanced. If it is not balanced, do the appropriate rotations to restore balance.
        curr.updateHeight();
        int balance = getBalance(curr);

        // Left Left Case
        if (balance > 1 && curr.left != null && key.compareTo(curr.left.key) < 0) {
            return rotateRight(curr);
        }
        // Right Right Case
        if (balance < -1 && curr.right != null && key.compareTo(curr.right.key) > 0) {
            return rotateLeft(curr);
        }
        // Left Right Case
        if (balance > 1 && curr.left != null && key.compareTo(curr.left.key) > 0) {
            curr.left = rotateLeft(curr.left);
            return rotateRight(curr);
        }
        // Right Left Case
        if (balance < -1 && curr.right != null && key.compareTo(curr.right.key) < 0) {
            curr.right = rotateRight(curr.right);
            return rotateLeft(curr);
        }
        return curr;
    }

    // Helper method to return the height difference between the left and right nodes
    private int getBalance(TreeNode<K, V> node) {
        int leftHeight = node.left == null ? -1 : node.left.height;
        int rightHeight = node.right == null ? -1 : node.right.height;
        return leftHeight - rightHeight;
    }

    // Left rotation
    private TreeNode<K, V> rotateLeft(TreeNode<K, V> node) {
        TreeNode<K, V> newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        node.updateHeight();
        newRoot.updateHeight();
        return newRoot;
    }

    // Right rotation
    private TreeNode<K, V> rotateRight(TreeNode<K, V> node) {
        TreeNode<K, V> newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        node.updateHeight();
        newRoot.updateHeight();
        return newRoot;
    }
}
