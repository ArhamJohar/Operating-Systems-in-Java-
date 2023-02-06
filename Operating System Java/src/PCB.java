
import java.util.ArrayList;

public class PCB {
	public int activePage = 0;
	public int id;
	public int priority;
	public String name;
	public ArrayList<Byte> dataSegment = new ArrayList<Byte>(); // datasegment
	public ArrayList<Byte> codeSegment = new ArrayList<Byte>(); // codesegment
	public PT cpt;
	public PT dpt;
	public short[] gpr = new short[16];
	public short[] spr = new short[16];
	public float wTime;
	public float eTime;
	public int[] flag = new int[16];

	public boolean isTerminated() {
		return false;
	}

	PCB(int id, int priority, String name, int[] flag, ArrayList<Byte> dataSegment, ArrayList<Byte> codeSegment, PT cpt,
			PT dpt,
			float wTime,
			float eTime) {
		this.id = id;
		this.priority = priority;
		this.name = name;
		this.dataSegment = dataSegment;
		this.codeSegment = codeSegment;
		this.flag = flag;
		this.cpt = cpt;
		this.dpt = dpt;
		this.wTime = wTime;
		this.eTime = eTime;
		this.spr = SP.r.clone();
		this.gpr = GP.r.clone();
	}

}
