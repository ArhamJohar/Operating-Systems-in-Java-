
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Process {
	private int id; // process ID
	private int priority; // process priority
	private int dataSize; // data segment size
	private int codeSize; // code segment size
	private String name; // process file name
	private ArrayList<Byte> dataSegment = new ArrayList<Byte>(); // datasegment
	private ArrayList<Byte> codeSegment = new ArrayList<Byte>(); // codesegment

	private Byte[][] dataPages = new Byte[0][128];
	private Byte[][] codePages = new Byte[0][128];

	private PT dpt = new PT(); // data page table
	private PT cpt = new PT(); // code page table

	private int[] flag = new int[16];

	private float wTime; // process waiting time
	private float eTime; // process execution time

	public PCB pcb() {
		return new PCB(id, priority, name, flag, dataSegment, codeSegment, cpt, dpt, wTime, eTime);
	}

	// loads into memory and populates code and data page tables
	public void loadIntoMemory() throws Exception {
		for (int i = 0; i < dataPages.length; i++)
			dpt.assign(i, dataPages[i]);
		for (int i = 0; i < codePages.length; i++)
			cpt.assign(i, codePages[i]);
	}

	private void assignId(int num1, int num2) {
		// we've converted this number to binary string
		String n1 = Integer.toBinaryString(num1);
		int n1length = n1.length();

		if (n1length < 8) {
			int nbits = 8 - n1length;
			// we're adding extra 0s to make it 8 bits
			for (int j = 0; j < nbits; j++)
				n1 = "0" + n1;
		}
		// if length is 32 that means it was a negative number and the BinaryString
		// method has
		// returned to us a 32 bits 2s complement binary number
		// to get rid of extra bits, we're extracting a substring to get 8 bits
		if (n1length == 32)
			n1 = n1.substring(23, 31);

		String n2 = Integer.toBinaryString(num2);
		int n2length = n2.length();

		if (n2length < 8) {
			int nbits = 8 - n2length;
			for (int j = 0; j < nbits; j++)
				n2 = "0" + n2;
		}

		if (n2length == 32)
			n2 = n2.substring(23, 31);

		String n3 = n1 + n2;

		this.id = Integer.parseInt(n3, 2);
	}

	private int assignData(ArrayList<Byte> storing) {
		int num4 = storing.get(3);
		int num5 = storing.get(4);
		// we've converted this number to binary string
		String n4 = Integer.toBinaryString(num4);
		int n4length = n4.length();

		if (n4length < 8) {
			int nbits = 8 - n4length;
			// we're adding extra 0s to make it 8 bits
			for (int j = 0; j < nbits; j++)
				n4 = "0" + n4;
		}
		// if length is 32 that means it was a negative number and the BinaryString
		// method has
		// returned to us a 32 bits 2s complement binary number
		if (n4length == 32)
			// to get rid of extra bits, we're extracting a substring to get 8 bits
			n4 = n4.substring(23, 31);

		String n5 = Integer.toBinaryString(num5);
		int n5length = n5.length();

		if (n5length < 8) {
			int nbits = 8 - n5length;
			for (int j = 0; j < nbits; j++)
				n5 = "0" + n5;
		}

		if (n5length == 32)
			n5 = n5.substring(23, 31);

		String n6 = n4 + n5;
		dataSize = Integer.parseInt(n6, 2);

		// ----------- STORE DATA SEGMENT
		int cursor;
		for (cursor = 8; cursor < this.dataSize + 8; cursor++)
			dataSegment.add(storing.get(cursor));

		float totalpages = dataSegment.size() / 128;

		if (totalpages <= 1) {
			dataPages = new Byte[1][128];
			// System.out.print("Data segment page " + 0 + ": ");
			// loop is to copy content of data segment to our page
			for (int j = 0; j < dataSegment.size(); j++) {
				dataPages[0][j] = dataSegment.get(j);
				// System.out.print(dataPages[0][j] + ",");
			}
			// System.out.println();
		}

		if (totalpages > 1) {
			dataPages = new Byte[(int) Math.ceil(totalpages)][128];
			// for loop to add these many number of pages to our arraylist

			int l = 0;
			int u = 128;

			// running for all the pages that were created
			for (int i = 0; i < Math.ceil(totalpages); i++) {
				if (u <= dataSegment.size()) {
					int k = 0;
					// System.out.print("Data segment page " + i + ": ");
					// storing contents from data segment from index 'l' to index 'u' into
					// our page 'i'
					for (int j = l; j < u; j++) {
						dataPages[i][k] = dataSegment.get(j);
						// System.out.print(dataPages[i][k] + ",");
						k++;
					}
					// System.out.println();
				}
				l = u; // 128
				u = u + 128; // 128+128 = 256
				if (u > dataSegment.size())
					u = dataSegment.size();
			}
		}
		return cursor + 1;
	}

	private void assignCode(int cursor, ArrayList<Byte> storing) {

		// ----------- STORING CODE IN CODE SEGMENT
		for (; cursor < storing.size(); cursor++)
			codeSegment.add(storing.get(cursor));

		// ----------- ASSIGN CODE PAGES
		float totalpages = codeSegment.size() / 128;

		if (totalpages <= 1) {
			codePages = new Byte[1][128];

			// System.out.print("Code segment page " + 0 + ": ");
			for (int j = 0; j < codeSegment.size(); j++) // loop is to copy content of data segment to our page
			{
				codePages[0][j] = codeSegment.get(j);
				// System.out.print(codePages[0][j] + ",");
			}
		}

		if (totalpages > 1) {
			codePages = new Byte[(int) Math.ceil(totalpages)][128];
			// for loop to add these many number of pages to our arraylist

			int l = 0;
			int u = 128;

			// running for all the pages that were created
			for (int i = 0; i < Math.ceil(totalpages); i++) {
				if (u <= codeSegment.size()) {
					int k = 0;
					// System.out.print("Code segment page " + i + ": ");
					// storing contents from data segment from index 'l' to index 'u' into
					// our page 'i'
					for (int j = l; j < u; j++) {
						codePages[i][k] = codeSegment.get(j);

						k++;
					}
					// System.out.println();
				}
				l = u; // 128
				u = u + 128; // 128+128 = 256
				if (u > codeSegment.size())
					u = codeSegment.size();
			}
		}
	}

	Process(String filepath) {
		for (int i = 0; i < flag.length; i++)
			flag[i] = 0;
		// the file reads bytes into this array
		ArrayList<Byte> storing = new ArrayList<Byte>();
		try (InputStream fileInputStream = new FileInputStream(filepath);) {
			// ----------- ASSIGN NAME
			String[] pathArray = filepath.split("\\\\");
			this.name = pathArray[pathArray.length - 1];
			Integer byteRead;
			while ((byteRead = fileInputStream.read()) != -1) {
				storing.add(byteRead.byteValue());
			}
			// ----------- ASSIGN PRIORITY
			this.priority = storing.get(0).intValue();
			System.out.println("-------------------------" + priority);
			if (priority < 0 || priority > 31) {
				System.out.println("Invalid priority " + priority + ". Priority set to lowest.");
				priority = 31;
			}
			// ----------- ASSIGN ID
			assignId(storing.get(1), storing.get(2));
			// ----------- ASSIGN DATA SIZE, SEGMENT, PAGES
			int cursor = assignData(storing);
			// ----------- ASSIGN CODE SIZE, SEGMENT, PAGES
			assignCode(cursor, storing);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}
}