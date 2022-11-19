public class AVLNode {
    public int balanceFactor;
    public int key;
    public AVLNode right;
    public AVLNode left;

    public AVLNode(){
        this.balanceFactor = 0;
        this.key = 0;
        this.right = null;
        this.left = null;
    }

    public AVLNode(int element){
        this.balanceFactor = 0;
        this.right = null;
        this.left = null;
        this.key = element;
    }
}
