public class AVLTree {
    protected AVLNode root;

    public AVLTree(){
        this.root = null;
    }

    protected void visit(AVLNode pointer){//works
        System.out.print(pointer.key + " with balance factor of " + pointer.balanceFactor);
        System.out.println();
    }

    public AVLNode search(AVLNode pointer, int element){//works
        while(pointer != null){
            if(element == pointer.key){
                return pointer;
            }
            else if(element < pointer.key){
                pointer = pointer.left;
            }
            else if(element > pointer.key){
                pointer = pointer.right;
            }
        }
        return null;
    }

    public AVLNode searchRecursively(AVLNode pointer, int element){//works
        if(pointer == null){
            return null;
        }
        else if(element < pointer.key){
           return searchRecursively(pointer.left, element);
        }
        else if(element > pointer.key){
            return searchRecursively(pointer.right, element);
        }
        return pointer;
    }

    //inorder

    public void inorderRecursive(AVLNode pointer){//works
        if(pointer != null){
            inorderRecursive(pointer.left);
            visit(pointer);
            inorderRecursive(pointer.right);
        }
    }
/*
    public void inorderIterative(){//works
        AVLNode pointer = root;
        Stack<AVLNode> myStack = new Stack<AVLNode>();

        while(pointer != null){
            while(pointer != null){
                if(pointer.right != null){
                    myStack.push(pointer.right);
                }
                myStack.push(pointer);
                pointer = pointer.left;
            }
            pointer = myStack.pop();
            while(pointer.right == null && !myStack.isEmpty()){
                visit(pointer);
                pointer = myStack.pop();
            }
            visit(pointer);
            if(!myStack.isEmpty()){
                pointer = myStack.pop();
            }
            else{
                pointer = null;
            }
        }
    }
*/
    public void insert(int element){//insertion with a stack in order to be able to update balance factors
        AVLNode pointer = root, prev = null;

        while(pointer != null){
            prev = pointer;
            if(pointer.key < element){
                pointer = pointer.right;
            }
            else{
                pointer = pointer.left;
            }
        }

        if(root == null){
            root = new AVLNode(element);
        }
        else if(prev.key < element){
            prev.right = new AVLNode(element);
        }
        else{
            prev.left = new AVLNode(element);
        }
    }

    public AVLNode insert(int element, AVLNode pointer){
        if (pointer == null) {
            return new AVLNode(element);
        } else if (pointer.key > element) {//smaller than left side
            pointer.left = insert(element, pointer.left);
        } else if (pointer.key < element) {//greater than right side
            pointer.right = insert(element, pointer.right);
        }
        return balanceAVLTree(pointer);
    }

    //balance AVL tree
    protected AVLNode updateHeight(AVLNode pointer){

        if(pointer.left != null){
            pointer.balanceFactor -= 1;
        }

        if(pointer.right != null){
            pointer.balanceFactor += 1;
        }

        if(pointer.left == null && pointer.right == null){
            pointer.balanceFactor = 0;
        }

        if(pointer.left != null && pointer.right != null){
            if((pointer.left.balanceFactor != 0 && pointer.right.balanceFactor != 0)){
                pointer.balanceFactor = 0;
            }
            else if(pointer.left.balanceFactor == 0 && pointer.right.balanceFactor == 0){
                if((pointer.left.left == null && pointer.left.right == null) && (pointer.right.left == null && pointer.right.right == null)){
                    pointer.balanceFactor = 0;
                }
                else if((pointer.left.left != null && pointer.left.right != null) || (pointer.right.left != null && pointer.right.right != null)){
                    return pointer;
                }
            }
            else if(pointer.left.balanceFactor != 0){
                pointer.balanceFactor = -1;
            }
            else if(pointer.right.balanceFactor != 0){
                pointer.balanceFactor = 1;
            }
        }

        return pointer;
    }

    protected AVLNode rotateLeft(AVLNode parent){
        AVLNode child = parent.right;
        AVLNode newChild = child.left;

        child.left = parent;
        parent.right = newChild;
        updateHeight(parent);
        updateHeight(child);
        return child;
    }

    protected AVLNode rotateRight(AVLNode parent){
        AVLNode child = parent.left;
        AVLNode newChild = child.right;
        
        child.right = parent;
        parent.left = newChild;
        updateHeight(parent);
        updateHeight(child);
        return child;
    }

    protected AVLNode balanceAVLTree(AVLNode parent){
        parent = updateHeight(parent);
        int balance = parent.balanceFactor;
        
        if(balance < -1){
            if(parent.left.balanceFactor <= 0){
                parent = rotateRight(parent);
            }
            else{
                parent.left = rotateLeft(parent.left);
                parent = rotateRight(parent);
            }
        }
        else if(balance > 1){
            if(parent.right.balanceFactor >= 0){
                parent = rotateLeft(parent);
            }
            else {//case 4 to consider only right roation
                parent.right = rotateRight(parent.right);
                parent = rotateLeft(parent);
            }
        }

        return parent;
    }

    public int getHeight(AVLNode pointer){
        int toReturn = 0;
        if(pointer == null){
            toReturn = 0;
        }
        else{
            int left = getHeight(pointer.left);
            int right = getHeight(pointer.right);

            if(left > right){
                toReturn = left + 1;
            }
            else{
                toReturn = right + 1;
            }
        }
        return toReturn;
    }
    public static void main(String[] args) {

        AVLTree newTree = new AVLTree();

        newTree.root = newTree.insert(38, newTree.root);
        newTree.root = newTree.insert(57, newTree.root);
        newTree.root = newTree.insert(51, newTree.root);
        newTree.root = newTree.insert(23, newTree.root);
        newTree.root = newTree.insert(69, newTree.root);
        newTree.root = newTree.insert(35, newTree.root);
        newTree.root = newTree.insert(100, newTree.root);
        newTree.root = newTree.insert(63, newTree.root);
        newTree.root = newTree.insert(31, newTree.root);
        newTree.root = newTree.insert(2, newTree.root);
        newTree.inorderRecursive(newTree.root);
        System.out.println();
        System.out.println("height of tree is: " + newTree.getHeight(newTree.root));
    }
}
