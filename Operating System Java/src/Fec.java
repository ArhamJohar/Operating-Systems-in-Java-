public class Fec {

	public static void decodeExecute() {
		// take frame from pcb, the page which we were at
		Frame frame = Memory.body[Scheduler.runningQ.cpt.frameIndex(Scheduler.runningQ.activePage)];
		// if program counter exceeds code counter, move to next page
		if (SP.r[3] == 0) {
			IS.init();
			int j = -1;
			for (int n = 0; n < 128; n++)
				if (frame.body[n] != null)
					if ((short) frame.body[n] != -1)
						j++;
			SP.r[3] = (short) j;
		}
		if (SP.r[9] >= SP.r[3] && Scheduler.runningQ.cpt.size() > (Scheduler.runningQ.activePage + 1)) {
			if (Scheduler.runningQ.activePage < Scheduler.runningQ.cpt.size()) {
				Scheduler.runningQ.activePage = Scheduler.runningQ.activePage + 1;
				frame = Memory.body[Scheduler.runningQ.cpt.frameIndex(Scheduler.runningQ.activePage)];
				SP.r[9] = 0;
				IS.init();
				int j = -1;
				for (int n = 0; n < 128; n++)
					if (frame.body[n] != null)
						if ((short) frame.body[n] != -1)
							j++;
				SP.r[3] = (short) j;
			}
		}
		// storing the value of our instruction to be executed
		SP.r[10] = frame.body[SP.r[9]];
		int z = SP.r[10];
		// converting our instruction to hexstring
		String opcode = Integer.toHexString(z);

		// here we're checking the first character of our hexstring
		// opcode to check the kind of instruction it is
		if (opcode.charAt(0) == '1') {
			SP.r[9] = (short) (SP.r[9] + 1);
			int r1 = frame.body[SP.r[9]];
			SP.r[9] = (short) (SP.r[9] + 1);
			int r2 = frame.body[SP.r[9]];
			// calling our instruction from instruction set class
			IS.executeRR(opcode, r1, r2);
		} else if (opcode.charAt(0) == '3') {
			SP.r[9] = (short) (SP.r[9] + 1);
			// storing index of register where result is to be stored
			short r1 = frame.body[SP.r[9]];

			SP.r[9] = (short) (SP.r[9] + 1);
			// value of the number that is to be used as our immediate value
			int num1 = frame.body[SP.r[9]];
			// we've converted this number to binary string
			String n1 = Integer.toBinaryString(num1);
			int n1length = n1.length();
			if (n1length < 8) {
				int nbits = 8 - n1length;
				for (int i = 0; i < nbits; i++) {
					// we're adding extra 0s to make it 8 bits
					n1 = "0" + n1;
				}
				nbits = 0;
			}
			// if length is 32 that means it was a negative number and the BinaryString
			// method has
			// returned to us a 32 bits 2s complement binary number
			if (n1length == 32) {
				// to get rid of extra bits, we're extracting a substring to get 8 bits
				n1 = n1.substring(23, 31);
			}

			SP.r[9] = (short) (SP.r[9] + 1);
			int num2 = frame.body[SP.r[9]];
			String n2 = Integer.toBinaryString(num2);
			int n2length = n2.length();
			if (n2length < 8) {
				int nbits = 8 - n2length;
				for (int i = 0; i < nbits; i++)
					n2 = "0" + n2;
				nbits = 0;
			}
			if (n2length == 32)
				n2 = n2.substring(23, 31);

			String n3 = n1 + n2;
			int result = Integer.parseInt(n3, 2);
			short x = SP.r[9];
			IS.executeRI(opcode, r1, result);
			SP.r[9] = x;
			n3 = null;
		} else if (opcode.charAt(0) == '5') {

			SP.r[9] = (short) (SP.r[9] + 1);
			// storing index of register where result is to be stored
			short r1 = frame.body[SP.r[9]];

			SP.r[9] = (short) (SP.r[9] + 1);
			// value of the number that is to be used as our immediate value
			int basereg = frame.body[SP.r[9]];

			SP.r[9] = (short) (SP.r[9] + 1);
			int offsetindex2 = frame.body[SP.r[9]];

			IS.executeMI(opcode, r1, basereg, offsetindex2, Scheduler.runningQ);

		} else if (opcode.charAt(0) == '7') {
			// PC++;
			SP.r[9] = (short) (SP.r[9] + 1);
			short r1 = frame.body[SP.r[9]];
			IS.executeSO(opcode, r1);
		} else if (opcode.charAt(0) == 'f') {
			if (opcode.length() == 8)
				opcode = opcode.substring(6, 8);
			IS.executeNO(opcode);
		} else
			System.out.println(opcode + ": Invalid opcode.");

		if (SP.r[9] < 127 && SP.r[10] != -13) {
			SP.r[9] = (short) (SP.r[9] + 1);
			SP.r[10] = frame.body[SP.r[9]];
		}
		if (SP.r[9] >= SP.r[3] && Scheduler.runningQ.cpt.size() == (Scheduler.runningQ.activePage + 1)) {
			IS.executeNO("f3");
		}
	}
}
