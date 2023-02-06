
public class Frame {
	public Byte[] body = new Byte[128];

	Frame() {
		body = new Byte[128];
		for (int i = 0; i < 128; i++)
			body[i] = -1;
	}

	Frame(Byte[] data) {
		body = data;
	}

	public boolean isFree() {
		boolean isFree = true;
		for (Byte b : body)
			if (b != -1) {
				isFree = false;
				break;
			}
		return isFree;
	}
	public void init() {
		for (int i = 0; i < 128; i++)
			body[i] = -1;
	}
}
