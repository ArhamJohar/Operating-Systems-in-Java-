import java.util.Stack;


public class IS {
	// String opcode;
	private static Stack<Short> stack = new Stack<Short>();
	static int[] flag = new int[16];

	public static void init() {
		for (int i = 0; i < flag.length; i++)
			flag[i] = 0;
	}

	public static void executeRR(String op, int i, int j) {
		switch (op) {

			case "16":
				GP.r[i] = GP.r[j];
				break;

			case "17":
				GP.r[i] = (short) (GP.r[i] + GP.r[j]);

				if (GP.r[i] < Math.pow(2, -15) || GP.r[i] > Math.pow(2, 15))
					flag[3] = 1; // overflow

				if (GP.r[i] == 0)
					flag[1] = 1; // zero bit of flag

				if (GP.r[i] < 0)
					flag[2] = 1; // sign bit of flag

				break;

			case "18":
				GP.r[i] = (short) (GP.r[i] - GP.r[j]);

				if (GP.r[i] < Math.pow(2, -15) || GP.r[i] > Math.pow(2, 15))
					flag[3] = 1;

				if (GP.r[i] == 0)
					flag[1] = 1;

				if (GP.r[i] < 0)
					flag[2] = 1;

				break;

			case "19":

				try {
					GP.r[i] = (short) (GP.r[i] * GP.r[j]);

					if (GP.r[i] < Math.pow(2, -15) || GP.r[i] > Math.pow(2, 15))
						flag[3] = 1;

					if (GP.r[i] == 0)
						flag[1] = 1;

					if (GP.r[i] < 0)
						flag[2] = 1;

				} catch (Exception ex) {
					if (GP.r[i] == 0) {
						GP.r[i] = 0;
						flag[1] = 1;
					}
					if (GP.r[j] == 0) {
						GP.r[j] = 0;
					}

				}
				break;

			case "1A":
				GP.r[i] = (short) (GP.r[i] / GP.r[j]);

				if (GP.r[i] < Math.pow(2, -15) || GP.r[i] > Math.pow(2, 15)) {
					flag[3] = 1;
				}
				if (GP.r[i] == 0)
					flag[1] = 1;

				if (GP.r[i] < 0)
					flag[2] = 1;

				break;

			case "1B":
				GP.r[i] = (short) (GP.r[i] & GP.r[j]);

				if (GP.r[i] < Math.pow(2, -15) || GP.r[i] > Math.pow(2, 15))
					flag[3] = 1;

				if (GP.r[i] == 0)
					flag[1] = 1;

				if (GP.r[i] < 0)
					flag[2] = 1;

				break;

			case "1C":
				GP.r[i] = (short) (GP.r[i] | GP.r[j]);

				if (GP.r[i] < Math.pow(2, -15) || GP.r[i] > Math.pow(2, 15))
					flag[3] = 1;

				if (GP.r[i] == 0)
					flag[1] = 1;

				if (GP.r[i] < 0)
					flag[2] = 1;
				break;

		}

	}

	public static void executeRI(String op, int i, int num) {
		switch (op) {
			case "30":
				GP.r[i] = (short) num;
				break;
			case "31":
				GP.r[i] = (short) (GP.r[i] + num);
				if (GP.r[i] < Math.pow(2, -15) || GP.r[i] > Math.pow(2, 15))
					flag[3] = 1;

				if (GP.r[i] == 0)
					flag[1] = 1;

				if (GP.r[i] < 0)
					flag[2] = 1;
				break;
			case "32":
				GP.r[i] = (short) (GP.r[i] - num);
				if (GP.r[i] < Math.pow(2, -15) || GP.r[i] > Math.pow(2, 15))
					flag[3] = 1;

				if (GP.r[i] == 0)
					flag[1] = 1;

				if (GP.r[i] < 0)
					flag[2] = 1;
				break;
			case "33":
				try {
					GP.r[i] = (short) (GP.r[i] * num);
					if (GP.r[i] < Math.pow(2, -15) || GP.r[i] > Math.pow(2, 15))
						flag[3] = 1;

					if (GP.r[i] == 0)
						flag[1] = 1;

					if (GP.r[i] < 0)
						flag[2] = 1;

				} catch (Exception ex) {
					if (GP.r[i] == 0) {
						GP.r[i] = 0;
					}
					if (num == 0) {

					}
					if (GP.r[i] < Math.pow(2, -15) || GP.r[i] > Math.pow(2, 15))
						flag[3] = 1;

					if (GP.r[i] == 0)
						flag[1] = 1;

					if (GP.r[i] < 0)
						flag[2] = 1;
				}
				break;

			case "34":
				try {
					GP.r[i] = (short) (GP.r[i] / num);
					if (num == 0) {
						throw new ArithmeticException();
					} else if (GP.r[i] < Math.pow(2, -15) || GP.r[i] > Math.pow(2, 15))
						flag[3] = 1;

					if (GP.r[i] == 0)
						flag[1] = 1;

					if (GP.r[i] < 0)
						flag[2] = 1;

				}

				catch (ArithmeticException ex) {
					System.out.println("Error: Can not be divided by zero");
				}
				break;

			case "35":
				GP.r[i] = (short) (GP.r[i] & num);
				if (GP.r[i] < Math.pow(2, -15) || GP.r[i] > Math.pow(2, 15))
					flag[3] = 1;

				if (GP.r[i] == 0)
					flag[1] = 1;

				if (GP.r[i] < 0)
					flag[2] = 1;
				break;

			case "36":
				GP.r[i] = (short) (GP.r[i] | num);
				if (GP.r[i] < Math.pow(2, -15) || GP.r[i] > Math.pow(2, 15))
					flag[3] = 1;

				if (GP.r[i] == 0)
					flag[1] = 1;

				if (GP.r[i] < 0)
					flag[2] = 1;
				break;

			case "37":
				if (flag[1] == 1)
					SP.r[9] = (short) (SP.r[1] + num);
				break;
			case "38":
				if (flag[1] == 1)
					SP.r[9] = (short) (SP.r[1] + num);
				break;
			case "39":
				if (flag[0] == 0)
					SP.r[9] = (short) (SP.r[1] + num);
				break;
			case "3A":
				if (flag[2] == 1)
					SP.r[9] = (short) (SP.r[1] + num);
				break;
			case "3B":
				SP.r[9] = (short) (SP.r[1] + num);
				break;
			case "3C":
				stack.push(SP.r[9]);
				SP.r[9] = (short) (SP.r[1] + num);
				break;
			//
			// case "3D": reg1 = (short)(reg1/reg2);
			// break;
		}
	}

	public static void executeNO(String op) {
		switch (op) {
			case "f1":
				stack.pop();
				SP.r[9] = (short) (SP.r[1] + 1);
				break;
			case "f2":
				break;
			case "f3":
				System.out.println("Terminated");
				Scheduler.processTerminated = true;
				Run.writeFile(Scheduler.runningQ);
				Run.printProc(Scheduler.runningQ);
				break;
		}
	}

	public static void executeMI(String op, int i, int basereg, int offsetindex2, PCB pcb) {
		switch (op) {
			case "51":
				GP.r[i] = pcb.dataSegment.get(basereg + offsetindex2);
				break;
			case "52":
				pcb.dataSegment.add(basereg + offsetindex2, (byte) GP.r[i]);
				break;
		}
	}

	public static void executeSO(String op, int i) {
		switch (op) {
			case "71": {
				int x = GP.r[i];
				String bin = Integer.toBinaryString(x);
				char temp = bin.charAt(0);

				if (temp == 1) {
					flag[0] = 1; // carry bit of flag
				}

				GP.r[i] = (short) (GP.r[i] << 1);

				if (GP.r[i] == 0)
					flag[1] = 1; // zero bit of flag

				if (GP.r[i] < 0) {
					flag[2] = 1; // sign bit of flag
				}

				break;
			}

			case "72":

				int x = GP.r[i];
				String bin = Integer.toBinaryString(x);
				char temp = bin.charAt(bin.length() - 1);
				if (temp == 1) {
					flag[0] = 1;
				} // carry bit of flag

				GP.r[i] = (short) (GP.r[i] >> 1);

				if (GP.r[i] == 0)
					flag[1] = 1; // zero bit of flag

				if (GP.r[i] < 0) {
					flag[2] = 1; // sign bit of flag
				}
				break;

			case "73":
				GP.r[i] = (short) (Integer.rotateLeft(GP.r[i], 1));
				break;

			case "74":
				GP.r[i] = (short) (Integer.rotateRight(GP.r[i], 1));
				break;

			case "75":
				GP.r[i] = (short) (GP.r[i] + 1);
				break;

			case "76":
				GP.r[i] = (short) (GP.r[i] - 1);
				break;

			case "77":
				stack.push(GP.r[i]);
				stack.peek();
				break;

			case "78":
				if (!stack.isEmpty()) {
					GP.r[i] = stack.pop();
				}
				break;

		}
	}

}
