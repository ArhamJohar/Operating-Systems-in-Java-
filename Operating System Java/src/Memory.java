
public class Memory {
	// all memory is stored in 512 frames
	public static Frame[] body = new Frame[512];
	// serves as empty bit. if occupied, will equal true
	private static boolean[] frameOccupancy = new boolean[512];

	public static int insertFrame(Frame frame) throws Exception {
		// if empty frame available
		for (int i = 0; i < frameOccupancy.length; i++)
			if (!frameOccupancy[i]) {
				// place data inside
				body[i] = frame;
				// set occupied to true
				frameOccupancy[i] = true;
				// return physical address
				return i;
			}
		// no empty frame was found. return error
		throw new Exception("Memory full.");
	}

	public static void init() {
		for (int i = 0; i < 512; i++)
			body[i] = new Frame();
		for (int i = 0; i < body.length; i++) {
			if (body[i] == null)
				body[i].init();
		}
	}
}