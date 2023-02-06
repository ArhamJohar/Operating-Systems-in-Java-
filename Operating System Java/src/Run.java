
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Run {
	static File folder = new File("D:\\OS_Phase2\\Dump");

	public static void main(String[] args) {

		// read processes off files
		String folderPath = "D:\\OS\\demofiles\\";
		String[] fileNames = { "p5" };
		Process[] processes = new Process[fileNames.length];
		Memory.init();
		IS.init();

		// initialise queues
		for (int i = 0; i < fileNames.length; i++) {
			// create Process objects
			try {
				// initialize process
				processes[i] = new Process(folderPath + fileNames[i]);
				// load process into memory
				processes[i].loadIntoMemory();
				System.out.println(processes[i].getId());
				// enqueue processes into scheduler
				Scheduler.insert(processes[i].pcb());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Scheduler.setFirst();
		// begin cycle that ends at empty running queue
		do {
			Fec.decodeExecute();
			Scheduler.iterate();
		} while (Scheduler.runningQ != null);
	}

	public static void writeFile(PCB pcb) {
		try {
			if (!folder.exists())
				folder.mkdirs();
			RandomAccessFile raf = new RandomAccessFile(folder + "\\" + pcb.name + ".txt", "rw");

			// write name
			raf.writeBytes("File Name: " + pcb.name + "\r\n");
			// write id
			raf.writeBytes("ID: " + pcb.id + "\r\n");
			// write priority
			raf.writeBytes("Priority: " + pcb.priority + "\r\n");

			// write code segment
			raf.writeBytes("Code Segment:");
			for (int i = 0; i < pcb.codeSegment.size(); i++)
				raf.writeBytes(pcb.codeSegment.get(i).toString() + ", ");
			// write data segment
			raf.writeBytes("\r\nData Segment:");
			for (int i = 0; i < pcb.dataSegment.size(); i++)
				raf.writeBytes(pcb.dataSegment.get(i).toString() + ", ");

			// write code page table
			raf.writeBytes("\r\n\r\nCode Page Table" + "\r\n");
			for (int i = 0; i < pcb.cpt.size(); i++)
				raf.writeBytes("page " + i + ": frame " + pcb.cpt.frameIndex(i) + "\r\n");
			// write data page table
			raf.writeBytes("\r\nData Page Table" + "\r\n");
			for (int i = 0; i < pcb.dpt.size(); i++)
				raf.writeBytes("page " + i + ": frame " + pcb.dpt.frameIndex(i) + "\r\n");

			// write special purpose registers
			raf.writeBytes("\r\nSPR: " + "\r\n");
			for (int i = 0; i < SP.r.length; i++)
				raf.writeBytes(SP.r[i] + ", ");
			// write general purpose registers
			raf.writeBytes("\r\nGPR: " + "\r\n");
			for (int i = 0; i < GP.r.length; i++)
				raf.writeBytes(GP.r[i] + ", ");
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void printProc(PCB pcb) {
		// write name
		System.out.println("File Name: " + pcb.name + "\r\n");
		// write id
		System.out.println("ID: " + pcb.id + "\r\n");
		// write priority
		System.out.println("Priority: " + pcb.priority + "\r\n");

		// write code segment
		System.out.println("Code Segment:");
		for (int i = 0; i < pcb.codeSegment.size(); i++)
			System.out.print(pcb.codeSegment.get(i).toString() + ", ");
		// write data segment
		System.out.println("\r\nData Segment:");
		for (int i = 0; i < pcb.dataSegment.size(); i++)
			System.out.print(pcb.dataSegment.get(i).toString() + ", ");

		// write code page table
		System.out.println("\r\n\r\nCode Page Table" + "\r\n");
		for (int i = 0; i < pcb.cpt.size(); i++)
			System.out.print("page " + i + ": frame " + pcb.cpt.frameIndex(i) + "\r\n");
		// write data page table
		System.out.println("\r\nData Page Table" + "\r\n");
		for (int i = 0; i < pcb.dpt.size(); i++)
			System.out.print("page " + i + ": frame " + pcb.dpt.frameIndex(i) + "\r\n");

		// write special purpose registers
		System.out.println("\r\nSPR: " + "\r\n");
		for (int i = 0; i < SP.r.length; i++)
			System.out.print(SP.r[i] + ", ");
		// write general purpose registers
		System.out.println("\r\nGPR: " + "\r\n");
		for (int i = 0; i < GP.r.length; i++)
			System.out.print(GP.r[i] + ", ");
	}
}