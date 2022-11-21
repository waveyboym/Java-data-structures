public class BTreeNode {
    public int globalArraySize = 5;
    public boolean leaf = true;
    public int keyTally = 1;
    public int keys[] = new int[globalArraySize - 1];
    public BTreeNode references[] = new BTreeNode[globalArraySize];

    public BTreeNode(int key){
        keys[0] = key;
        for(int i = 0; i < globalArraySize; ++i){
            references[i] = null;
        }
    }

    public BTreeNode(BTreeNode data){
        this.globalArraySize = data.globalArraySize;
        this.keyTally = data.keyTally;
        for(int i = 0; i < data.globalArraySize - 1; ++i){
            this.keys[i] = data.keys[i];
        }
        for(int i = 0; i < data.globalArraySize; ++i){
            this.references[i] = data.references[i];
        }
    }
}
