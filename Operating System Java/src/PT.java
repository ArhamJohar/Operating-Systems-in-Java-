
import java.util.Hashtable;

public class PT {
	// first integer is logical address, second is physical address
	private Hashtable<Integer, Integer> pageTable = new Hashtable<Integer, Integer>();
	public int activePage = 0;

	public void assign(int pageNum, Byte[] frameData) throws Exception {
		int frameIndex = Memory.insertFrame(new Frame(frameData));
		if (frameIndex != -1)
			// we have page no, frame no
			// System.out.println("Success: " + pageNum + ":" + frameIndex);
			pageTable.put(pageNum, frameIndex);
	}

	public int frameIndex(int pno) {
		return pageTable.get(pno);
	}

	public void print() {
		System.out.println();
		for (int i = 0; i < pageTable.size(); i++)
			System.out.println("page " + i + ": frame " + pageTable.get(i));
	}

	public int size() {
		return pageTable.size();
	}
}
