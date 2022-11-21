public class BTree {
    protected BTreeNode root;

    public BTree(int key){
        root = new BTreeNode(key);
    }

    public BTreeNode BtreeSearch(int key){
        return BtreeSearch(key, this.root);
    }

    protected BTreeNode BtreeSearch(int key, BTreeNode node){
        if(node != null){
            int i = 1;
            for(; i <= node.keyTally && node.keys[i - 1] < key; ++i);
            if(i > node.keyTally || node.keys[i - 1] > key){
                return BtreeSearch(key, node.references[i - 1]);
            }
            else{ return node;}
        }
        else{ return null;}
    }

    public BTreeNode findNode(int key){
        return findNode(key, this.root);
    }

    protected BTreeNode findNode(int key, BTreeNode node){
        if(node != null){
            for(int i = 0; i < node.globalArraySize; ++i){
                if(i == node.keyTally){
                    //go to last reference because value is bigger than
                    //largest value in node keys array
                    if(node.references[i] != null){
                        return findNode(key, node.references[i]);
                    }
                    else return node;
                }
                else{
                    if(key < node.keys[i]){
                        if(node.references[i] != null){
                            return findNode(key, node.references[i]);
                        }
                        else return node;
                    }
                }
            }
        }
        return null;
    }

    protected BTreeNode findParent(BTreeNode node, BTreeNode parent, int key){
        if(node != null){
            for(int i = 0; i < node.globalArraySize; ++i){
                if(i == node.keyTally){
                    //go to last reference because value is bigger than
                    //largest value in node keys array
                    if(node.references[i] != null){
                        return findParent(node.references[i], node, key);
                    }
                    else return parent;
                }
                else{
                    if(key < node.keys[i]){
                        if(node.references[i] != null){
                            return findParent(node.references[i], node, key);
                        }
                        else return parent;
                    }
                    else if(key == node.keys[0])return parent;
                }
            }
        }
        return null;
    }

    protected int findIndex(BTreeNode parent, BTreeNode child){
        int toReturn = 0;
        for(int i = 0; i < parent.globalArraySize; ++i){
            if(parent.references[i] == child){
                toReturn = i;
            }
        }
        return toReturn;
    }

    protected int findposToInsert(int array[], int key, int keyTally){
        int toReturn = 0;

        for(int i = 0; i < keyTally; ++i){
            if(key < array[i]){
                toReturn = i;
                break;
            }
            else if(i == keyTally - 1){
                toReturn = i + 1;
                break;
            }
        }
        return toReturn;
    }

    protected int[] insertIntoArray(int globalArraySize, int array[], int key, int pos, int keyTally){
        int toReturn[] = new int[globalArraySize - 1];

        for(int i = 0; i < globalArraySize; ++i){
            if(i < pos){
                toReturn[i] = array[i];
            }
            else if(i == pos){
                toReturn[i] = key;
            }
            else if(!(i > keyTally)){
                toReturn[i] = array[i - 1];
            }
        }
        return toReturn;
    }
    
    protected BTreeNode takeLowerArray(BTreeNode node, int array[], int middleElement){
        BTreeNode newNode = node;
        
        int newArray[] = new int[newNode.globalArraySize - 1];
        BTreeNode references[] = new BTreeNode[newNode.globalArraySize];
        newNode.keyTally = 0;

        for(int i = 0; i < newNode.globalArraySize - 1; ++i){
            if(array[i] < middleElement){
                newArray[i] = array[i];
                ++newNode.keyTally;
            }
        }

        for(int i = 0; i < newNode.globalArraySize; ++i){
            if(newNode.keys[i] < middleElement){
                references[i] = newNode.references[i];
            }
            else if(newNode.keys[i] == middleElement){
                references[i] = newNode.references[i];
                break;
            }
        }
        newNode.keys = newArray;
        newNode.references = references;

        return newNode;
    }

    protected BTreeNode takeUpperArray(BTreeNode node, int array[], int middleElement){
        int newArray[] = new int[node.globalArraySize - 1];
        BTreeNode references[] = new BTreeNode[node.globalArraySize];
        int j = 0;
        node.keyTally = 0;

        for(int i = 0; i < node.globalArraySize; ++i){
            if(array[i] > middleElement){
                newArray[j] = array[i];
                ++node.keyTally;
                ++j;
            }
        }
        j = 0;

        for(int i = 0; i < node.globalArraySize - 1; ++i){
            if(node.keys[i] > middleElement){
                references[j] = node.references[i + 1];
                ++j;
            }
            else if(node.keys[i] == middleElement){
                references[j] = node.references[i + 1];
                ++j;
            }
        }
        node.keys = newArray;
        node.references = references;
        return node;
    }

    protected BTreeNode alignReferences(BTreeNode node, BTreeNode parentNode, int numOfNonNull){
        for(int i = 0; i < parentNode.keyTally; ++i){
            if((i == parentNode.keyTally - 1) && (parentNode.keys[i] == node.keys[0])){
                //System.out.println("in last modi");
                for(int j = 0; j < numOfNonNull; ++j){
                    parentNode.references[i + j] = node.references[j];
                }
                break;
            }
            else if((parentNode.keys[i] == node.keys[0]) && (i < parentNode.keyTally - 1)){
                //System.out.println("in first modi");
                int counter = 0;
                for(int j = numOfNonNull; j < parentNode.globalArraySize; ++j){
                    if(counter < parentNode.keyTally - 1){
                        parentNode.references[j] = parentNode.references[j - 1];
                        ++counter;
                    }
                    else if(counter >= parentNode.keyTally){
                        break;
                    }
                }
                for(int j = 0; j < numOfNonNull; ++j){
                    parentNode.references[j] = node.references[j];
                }
                break;
            }
        }
        return parentNode;
    }

    protected BTreeNode concatenate(BTreeNode parent, BTreeNode node){
        int numOfNonNull = 0;

        for(int i = 0; i < node.globalArraySize; ++i){
            if(node.references[i] != null){
                ++numOfNonNull;
            }
        }

        int pos = findposToInsert(parent.keys, node.keys[0], parent.keyTally);
        for(int i = 0; i < parent.globalArraySize; ++i){
            if(parent.references[i] == node){
                parent.keys = insertIntoArray(parent.globalArraySize, parent.keys, node.keys[0], pos, parent.keyTally);
                ++parent.keyTally;
                parent = alignReferences(node, parent, numOfNonNull);
                break;
            }
        }

        return parent;
    }
    
    protected int[] removeAndModify(int array[], int keyToRemove, int size){
        int toReturn[] = new int[size - 1];
        boolean smallerThan = true;

        for(int i = 0; i < size - 1; ++i){
            if(array[i] < keyToRemove && smallerThan == true){
                toReturn[i] = array[i];
            }
            else if((array[i] >= keyToRemove) && !(i + 1 >= size - 1)){
                toReturn[i] = array[i + 1];
                smallerThan = false;
            }
        }
        return toReturn;
    }
    
    protected BTreeNode splitParent(BTreeNode parent, BTreeNode node){
        int pos = findposToInsert(parent.keys, node.keys[0], parent.keyTally);
        for(int i = 0; i < parent.globalArraySize; ++i){
            if(parent.references[i] == node){
                int array[] = insertIntoArray(parent.globalArraySize + 1, parent.keys, node.keys[0], pos, parent.keyTally);
                
                BTreeNode copyObject1 = new BTreeNode(parent);
                BTreeNode copyObject2 = new BTreeNode(parent);
                BTreeNode node1 = takeLowerArray(copyObject1, array, array[2]);
                BTreeNode node2 = takeUpperArray(copyObject2, array, array[2]);
                int key = array[2];
                
                for(int j = 0; j < parent.globalArraySize; ++j){
                    if(node1.references[j] != null && node1.references[j] == node){
                        node1.keys = removeAndModify(node1.keys, node.keys[0], node1.globalArraySize);
                        --node1.keyTally;
                        node1 = concatenate(node1, node);
                        break;
                    }
                    if(node2.references[j] != null && node2.references[j] == node){
                        node2.keys = removeAndModify(node2.keys, node.keys[0], node2.globalArraySize);
                        --node2.keyTally;
                        node2 = concatenate(node2, node);
                        break;
                    }
                }

                parent = new BTreeNode(key);
                parent.references[0] = node1;
                parent.references[1] = node2;
                break;
            }
        }

        return parent;
    }

    protected BTreeNode mergeArrays(BTreeNode parent, int indexOfParentKey, BTreeNode sibling, BTreeNode node){
        int newLength = 1 + sibling.keyTally + node.keyTally;
        int array[] = new int[newLength];
        int k = 0, j = 0, m = 0;
        for(int i = 0; i < newLength; ++i){
            if(k < 1){
                array[i] = parent.keys[indexOfParentKey];
                ++k;
            }
            else if(j < sibling.keyTally){
                array[i] = sibling.keys[j];
                ++j;
            }
            else if(m < node.keyTally){
                array[i] = node.keys[m];
                ++m;
            }
        }

        BTreeNode copyObject1 = null;
        if(parent == this.root){
            copyObject1 = new BTreeNode(parent);
        }
        else{
            copyObject1 = sibling;
        }
        
        array = bubbleSort(array);
        copyObject1.keys = array;
        copyObject1.keyTally = newLength;
        return copyObject1;
    }

    protected BTreeNode[] reAlignReferences(BTreeNode[] referencesArray, int size){
        boolean realign = false;
        
        for(int i = 0; i < size; ++i){
            if(referencesArray[i] == null && !(i + 1 >= size)){
                referencesArray[i] = referencesArray[i + 1];
                realign = true;
            }
            else if(realign == true && !(i + 1 >= size)){
                referencesArray[i] = referencesArray[i + 1];
            } 
        }

        return referencesArray;
    }

    protected BTreeNode mergeRootChildren(BTreeNode node){
        BTreeNode node1 = node.references[0];
        BTreeNode node2 = node.references[1];

        for(int i = 0; i < node.globalArraySize; ++i){
            if(node1.references[i] != null){
                node.references[i] = node1.references[i];
            }
        }

        int j = 0;
        for(int i = node1.keyTally + 1; i < node.globalArraySize; ++i){
            if(node2.references[j] != null){
                //System.out.println("linking");
                node.references[i] = node2.references[j];
                ++j;
            }
        }

        //make different function for this
        node = mergeArrays(node, 0, node1, node2);

        return node;
    }

    protected int[] bubbleSort(int[] arr) {  
        int n = arr.length;  
        int temp = 0;  
         for(int i=0; i < n; i++){  
            for(int j=1; j < (n-i); j++){  
                if(arr[j-1] > arr[j]){  
                    //swap elements  
                    temp = arr[j-1];  
                    arr[j-1] = arr[j];  
                    arr[j] = temp;  
                }  
            }
        } 
        return arr;
    }  

    public void BtreeInsert(int key){
        //use search algo to find a spot in leaf nodes then return node
        BTreeNode node = findNode(key);
        BTreeNode parentNode = findParent(this.root, this.root, node.keys[0]);
        int indexOfChild = findIndex(parentNode, node);
        
        int pos = findposToInsert(node.keys, key, node.keyTally);
        if(node.keyTally != node.globalArraySize - 1){
            //find position to place key in array
            node.keys = insertIntoArray(node.globalArraySize, node.keys, key, pos, node.keyTally);
            ++node.keyTally;
            return;
        }
        else{
            //split into two nodes
            int array[] = insertIntoArray(node.globalArraySize + 1, node.keys, key, pos, node.keyTally);
            BTreeNode copyObject1 = new BTreeNode(node);
            BTreeNode copyObject2 = new BTreeNode(node);
            BTreeNode node1 = this.takeLowerArray(copyObject1, array, array[2]);
            BTreeNode node2 = this.takeUpperArray(copyObject2, array, array[2]);//key tallys are initialised in this function
            key = array[2];

            if(node == this.root){
                this.root = new BTreeNode(key);
                this.root.references[0] = node1;
                this.root.references[1] = node2;
            }
            else{
                //leave empty for now
                //node = its parent and then proceed to process nodes parent
                //node = parentNode;
                node = new BTreeNode(key);
                node.references[0] = node1;
                node.references[1] = node2;
                parentNode.references[indexOfChild] = node;
                if(parentNode.keyTally == parentNode.globalArraySize - 1){
                    //then parent is full and split
                    //System.out.println("working");
                    parentNode = splitParent(parentNode, node);
                    //displayTree(parentNode);
                    this.root = parentNode;//not a final solution
                    //may cause problems if parent was not the root
                    //displayTree(parentNode);
                }
                else{
                    parentNode = concatenate(parentNode, node);
                }
            }
            return;
        }
    }

    public void BtreeDelete(int key){
        //find node
        BTreeNode node = this.BtreeSearch(key);
        BTreeNode parentNode = findParent(this.root, this.root, node.keys[0]);
        if(node != null){
            if(node.references[0] != null){//if node is not a leaf
                //make into leaf
                int indexOfKey = 0;
                for(int i = 0; i < node.globalArraySize - 1; ++i){
                    if(node.keys[i] == key){
                        indexOfKey = i;
                    }
                }
                BTreeNode child = node.references[indexOfKey];
                node.keys[indexOfKey] = child.keys[child.keyTally - 1];
                child.keys = removeAndModify(child.keys, child.keys[child.keyTally - 1], child.globalArraySize);
                --child.keyTally;
            }
            else{
                //delete k from node
                node.keys = this.removeAndModify(node.keys, key, node.globalArraySize);
                --node.keyTally;
            }
            BTreeNode siblingNode = null;
            int indexOfParentKey = 0;
            for(int i = 0; i < parentNode.globalArraySize; ++i){
                if(parentNode.references[i] != null && parentNode.references[i] == node){
                    if(!(i - 1 < 0) && (parentNode.references[i - 1].keyTally > ((node.globalArraySize - 1)/2))){
                        siblingNode = parentNode.references[i - 1];
                        indexOfParentKey = i - 1;
                        break;
                    }
                    else if(!(i + 1 >= parentNode.globalArraySize) && (parentNode.references[i + 1].keyTally > ((node.globalArraySize - 1)/2))){
                        siblingNode = parentNode.references[i + 1];
                        indexOfParentKey = i;
                        break;
                    }
                }
            }

            while(true){
                if(!(node.keyTally < ((node.globalArraySize - 1)/2))){
                        //does not underflow
                        return;
                    }
                else if(siblingNode != null){
                    //there is a sbiling with enough keys
                    //redistribute keys between node and its sibling

                    if(parentNode.keys[indexOfParentKey] > siblingNode.keys[siblingNode.keyTally - 1]){
                        int temp = parentNode.keys[indexOfParentKey];
                        parentNode.keys[indexOfParentKey] = siblingNode.keys[siblingNode.keyTally - 1];
                        siblingNode.keys[siblingNode.keyTally - 1] = 0;
                        --siblingNode.keyTally;
                        
                        int pos = findposToInsert(node.keys, temp, node.keyTally);
                        node.keys = insertIntoArray(node.globalArraySize, node.keys, temp, pos, node.keyTally);
                        ++node.keyTally;
                    }
                    else if(parentNode.keys[indexOfParentKey] < siblingNode.keys[0]){
                        int temp = parentNode.keys[indexOfParentKey];
                        parentNode.keys[indexOfParentKey] = siblingNode.keys[0];
                        siblingNode.keys = removeAndModify(siblingNode.keys, siblingNode.keys[0], siblingNode.globalArraySize);
                        --siblingNode.keyTally;

                        int pos = findposToInsert(node.keys, temp, node.keyTally);
                        node.keys = insertIntoArray(node.globalArraySize, node.keys, temp, pos, node.keyTally);
                        ++node.keyTally;
                    }
                    return;
                }
                else if(parentNode == this.root){
                    if(parentNode.keyTally == 1){
                        //merge node, its sibling and the parent to make a new root
                        this.root = mergeRootChildren(this.root);//continue with testing here
                    }
                    else{
                        //merge node and its sibling
                        //yeah rn idk and me tired
                        for(int i = 0; i < parentNode.globalArraySize; ++i){
                            if(parentNode.references[i] != null && parentNode.references[i] == node){
                                if(!(i + 1 >= parentNode.globalArraySize)){
                                    siblingNode = parentNode.references[i + 1];
                                    indexOfParentKey = i;
                                    break;
                                }
                                else if((i + 1 >= parentNode.globalArraySize)){
                                    siblingNode = parentNode.references[i - 1];
                                    indexOfParentKey = i - 1;
                                    break;
                                }
                            }
                        }
    
                        //merge the three arrays and replace in either node
                        //or sibling node and change tallys
                       //delink reference to either node or sibling node via parent
                        //reduce parent tally by one and remove prev parent key
                        if(parentNode.references[indexOfParentKey] == node){
                            node = mergeArrays(parentNode, indexOfParentKey, siblingNode, node);
                            parentNode.keys = removeAndModify(parentNode.keys, parentNode.keys[indexOfParentKey], parentNode.globalArraySize);
                            --parentNode.keyTally;
                            parentNode.references[indexOfParentKey] = node;
                        }
                        else if(parentNode.references[indexOfParentKey + 1] == node){
                            siblingNode = mergeArrays(parentNode, indexOfParentKey, siblingNode, node);
                            parentNode.keys = removeAndModify(parentNode.keys, parentNode.keys[indexOfParentKey + 1], parentNode.globalArraySize);
                            --parentNode.keyTally;
                            parentNode.references[indexOfParentKey + 1] = node;
                        }
                        parentNode.references[indexOfParentKey + 1] = null;
                        parentNode.references = reAlignReferences(parentNode.references, parentNode.globalArraySize);
    
                        this.root = parentNode;
                    }
                    return;
                }
                else{
                    //merge node and its sibling
                    //node = its parent
                    for(int i = 0; i < parentNode.globalArraySize; ++i){
                        if(parentNode.references[i] != null && parentNode.references[i] == node){
                            if(!(i + 1 >= parentNode.globalArraySize)){
                                siblingNode = parentNode.references[i + 1];
                                indexOfParentKey = i;
                                break;
                            }
                            else if((i + 1 >= parentNode.globalArraySize)){
                                siblingNode = parentNode.references[i - 1];
                                indexOfParentKey = i - 1;
                                break;
                            }
                        }
                    }

                    //merge the three arrays and replace in either node
                    //or sibling node and change tallys
                   //delink reference to either node or sibling node via parent
                    //reduce parent tally by one and remove prev parent key
                    if(parentNode.references[indexOfParentKey] == node){
                        node = mergeArrays(parentNode, indexOfParentKey, siblingNode, node);
                        parentNode.keys = removeAndModify(parentNode.keys, parentNode.keys[indexOfParentKey], parentNode.globalArraySize);
                        --parentNode.keyTally;
                        parentNode.references[indexOfParentKey] = node;
                    }
                    else if(parentNode.references[indexOfParentKey + 1] == node){
                        siblingNode = mergeArrays(parentNode, indexOfParentKey, siblingNode, node);
                        parentNode.keys = removeAndModify(parentNode.keys, parentNode.keys[indexOfParentKey + 1], parentNode.globalArraySize);
                        --parentNode.keyTally;
                        parentNode.references[indexOfParentKey + 1] = node;
                    }
                    parentNode.references[indexOfParentKey + 1] = null;
                    parentNode.references = reAlignReferences(parentNode.references, parentNode.globalArraySize);

                    node = parentNode;
                    parentNode = findParent(this.root, this.root, node.keys[0]);
                    siblingNode = null;
                }
            }
        }
    }

    public void displayTree(BTreeNode pointer){
        if(pointer != null){
            System.out.print("array values: ");
            for(int i = 0; i < pointer.globalArraySize - 1; ++i){
                System.out.print(pointer.keys[i] + " ");
            }
            System.out.print("key tally: " + pointer.keyTally + " is leaf? " + pointer.leaf);
            System.out.println();
            System.out.println();
            for(int i = 0; i < pointer.globalArraySize; ++i){
                displayTree(pointer.references[i]);
            }
        }
    }

    public static void main(String[] args) {
        BTree btree = new BTree(8);

        btree.BtreeInsert(14);
        btree.BtreeInsert(2);
        btree.BtreeInsert(15);
        btree.BtreeInsert(3);
        btree.BtreeInsert(1);
        btree.BtreeInsert(16);
        btree.BtreeInsert(6);
        btree.BtreeInsert(5);
        btree.BtreeInsert(27);
        btree.BtreeInsert(37);
        btree.BtreeInsert(18);
        btree.BtreeInsert(25);
        btree.BtreeInsert(7);
        btree.BtreeInsert(13);
        btree.BtreeInsert(20);
        btree.BtreeInsert(22);
        btree.BtreeInsert(23);
        btree.BtreeInsert(24);

        btree.BtreeDelete(6);
        btree.BtreeDelete(7);
        btree.BtreeDelete(8);
        btree.BtreeDelete(16);
        btree.displayTree(btree.root);
    }
}
