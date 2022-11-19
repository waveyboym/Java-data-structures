import java.io.IOException;

public class Main {

	private static void ascendingOrder(BPlusTree tree) {
		if (tree != null) {
			System.out.println("Printing in ascending order using leaf nodes: ");
			BPlusTreeNode leaf = tree.getFirstLeaf(tree.get_ROOT());
			while (leaf != null) {
				System.out.print("[");
				for(int i = 0; i < leaf.keys.length; ++i){
					if(i < leaf.keys.length - 1)System.out.print(leaf.keys[i] + "|");
					else System.out.print(leaf.keys[i]);
				}
				System.out.println("]");
				leaf = leaf.nextLeaf;
			}
		}
		System.out.println();
	}

	public static void main(String args[]) throws IOException{

		System.out.println("BPLUS TREE");
		System.out.println();
		try{
			BPlusTree tree = new BPlusTree();
			/*int array[] = {1, 2, 4, 3, 7, 10, 5, 6, 50, 60, 12, 15, 22, 23, 17, 88, 56, 73, 25, 99, 105,24, 14, 106, 200, 300,
				373,377,385,391,449,451,461,465,470,512,561,699,762,798,865,866,870,934,963,980};
				
				,8079,69354,57477,70850,21281,71341,44702,18271,62102,
				7565,64809,55313,31180,57183,36371,32098,7781,24114,53701,86444,67478,92374,93112,10431,8973,20308,63341,23092,
				79420,33577,93051,67692,37126,20547,23180,94923,95636,71236,36731,62460,45720
*/
			
			int array[] = {1532,1646,62994,88689,6268,31724,98135,35598,41136,23586,8500,6525,52433,83904,59450,57472,36153,19940
				,81120,72589,14165,10532,86322,91851,33259,38560,75648,23944,90986,90537,21643,50086,22272,53679,55671,84920,5721
				,53370,17445,1772,35538,69526,98178,7182,91321,5594,16641,42382,91872,42197,95053,14849,58451,55125,1800
				,7642,2085,12561,65211,86135,99824};

			for(int i = 0; i < array.length; ++i)tree.insert(array[i], null);
			//tree.delete(88);
			tree.displayEntireTree(tree.get_ROOT());
			
			ascendingOrder(tree);
		}
		catch(IOException e){ System.out.println(e.getMessage());}
	}
}