
import java.util.ArrayList;
import java.util.Comparator;

public class Scheduler {
    public static boolean processTerminated = false;
    public static PCB runningQ;
    /** Priority Scheduled Queue */
    private static ArrayList<PCB> q1 = new ArrayList<PCB>();
    /** Round-Robin Scheduled Queue */
    private static ArrayList<PCB> q2 = new ArrayList<PCB>();
    private static final int quantum = 8;
    private static int sliceCounter = -1;

    /**
     * New PCB insertion into scheduler.
     * 
     * @param pcb
     */
    public static void insert(PCB pcb) {
        // if priority 1
        if (pcb.priority > -1 && pcb.priority < 16) {
            q1.add(pcb);
            q1.sort(Comparator.comparingInt(a -> a.priority));
        }
        // if priority 2
        else if (pcb.priority > 15 && pcb.priority < 32)
            q2.add(pcb);
    }

    public static void setFirst() {
        // if q1 populated, set q1
        if (!q1.isEmpty()) {
            runningQ = q1.get(0);
            restoreState();
            q1.remove(0);
        }
        // else if q2 populated, set q2, reset counter
        else if (!q2.isEmpty()) {
            runningQ = q2.get(0);
            sliceCounter = 0;
            restoreState();
            q2.remove(0);
        }
        // else do nothing
    }

    /**
     * Checks queues, changes running process if needed,
     * returns running PCB, whether new or not
     * 
     * @return
     */
    public static void iterate() {
        // if runningQ terminated
        if (processTerminated) {
            processTerminated = false;
            // --if q1 populated, set runningQ to q1
            if (!q1.isEmpty()) {
                runningQ = q1.get(0);
                restoreState();
                q1.remove(0);
            }
            // --else if q2 populated, set runningQ to q2
            else if (!q2.isEmpty()) {
                runningQ = q2.get(0);
                restoreState();
                sliceCounter = 0;
                q2.remove(0);
            }
            // --else set runningQ to null
            else
                runningQ = null;
        }
        // else if runningQ remaining
        else {
            // --if q1 do nothing
            if (runningQ.priority < 16 && runningQ.priority > -1) {
            }
            // --else if q2
            else if (!q2.isEmpty()) {
                // ----if q1 populated, nullify quantum, replace
                if (!q1.isEmpty()) {
                    updateState();
                    insert(runningQ);
                    sliceCounter = -1;
                    runningQ = q1.get(0);
                    restoreState();
                    q1.remove(0);
                }
                // ----else if quantum remaining, increment counter
                else if (sliceCounter < (quantum / 2)) {
                    sliceCounter++;
                }
                // ----else if quantum expired
                else {
                    // ------if q2 empty, reset counter
                    if (q2.isEmpty()) {
                        sliceCounter = 0;
                    }
                    // ------else reset counter, replace
                    else {
                        sliceCounter = 0;
                        updateState();
                        insert(runningQ);
                        runningQ = q2.get(0);
                        restoreState();
                        q2.remove(0);
                    }
                }
            }
        }
    }

    private static void restoreState() {
        System.out.println(Scheduler.runningQ.name + "\n");
        GP.r = runningQ.gpr;
        SP.r = runningQ.spr;
        IS.flag = runningQ.flag;
    }

    private static void updateState() {
        runningQ.gpr = GP.r;
        runningQ.spr = SP.r;
        runningQ.flag = IS.flag;
    }
}
