package az.edu.aztu.ticketdesk.structures;

import java.util.Objects;

/** Ordinary, deliberately unbalanced BST. Iterative operations also handle a chain-shaped tree. */
public final class BinarySearchTree<V> {
    private static final class Node<V> {
        int key; V value; Node<V> left,right;
        Node(int key,V value) { this.key=key; this.value=value; }
    }
    private record Level<V>(Node<V> node,int depth) { }
    private Node<V> root;
    private int size;
    public int size() { return size; }
    public V get(int key) {
        Node<V> n=root;
        while(n!=null) { if(key==n.key) return n.value; n=key<n.key?n.left:n.right; }
        return null;
    }
    public V put(int key,V value) {
        Objects.requireNonNull(value);
        if(root==null) { root=new Node<>(key,value); size++; return null; }
        Node<V> n=root;
        while(true) {
            if(key==n.key) { V old=n.value; n.value=value; return old; }
            if(key<n.key) { if(n.left==null) { n.left=new Node<>(key,value); break; } n=n.left; }
            else { if(n.right==null) { n.right=new Node<>(key,value); break; } n=n.right; }
        }
        size++; return null;
    }
    public V remove(int key) {
        Node<V> parent=null,n=root;
        while(n!=null && n.key!=key) { parent=n; n=key<n.key?n.left:n.right; }
        if(n==null) return null;
        V old=n.value;
        if(n.left!=null && n.right!=null) {
            Node<V> successorParent=n,successor=n.right;
            while(successor.left!=null) { successorParent=successor; successor=successor.left; }
            n.key=successor.key; n.value=successor.value;
            parent=successorParent; n=successor;
        }
        Node<V> child=n.left!=null?n.left:n.right;
        if(parent==null) root=child; else if(parent.left==n) parent.left=child; else parent.right=child;
        size--; return old;
    }
    public int[] keysInOrder() {
        int[] keys=new int[size]; int i=0;
        var stack=new SimpleStack<Node<V>>(); Node<V> n=root;
        while(n!=null || !stack.isEmpty()) {
            while(n!=null) { stack.push(n); n=n.left; }
            n=stack.pop(); keys[i++]=n.key; n=n.right;
        }
        return keys;
    }
    /** Height counts nodes: empty=0, one node=1. */
    public int height() {
        if(root==null) return 0;
        int max=0; var queue=new SimpleQueue<Level<V>>(); queue.offer(new Level<>(root,1));
        while(!queue.isEmpty()) {
            Level<V> level=queue.poll(); Node<V> n=level.node(); max=Math.max(max,level.depth());
            if(n.left!=null) queue.offer(new Level<>(n.left,level.depth()+1));
            if(n.right!=null) queue.offer(new Level<>(n.right,level.depth()+1));
        }
        return max;
    }
    public int searchComparisons(int key) {
        int count=0; Node<V> n=root;
        while(n!=null) { count++; if(n.key==key) break; n=key<n.key?n.left:n.right; }
        return count;
    }
}
