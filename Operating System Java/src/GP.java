

public class GP {
     public static short[] r = new short[16];

     public static void print() {
          String s = "GP Registers: ";
          for (int i = 0; i < r.length; i++) {
               s += r[i];
               if (i != 15)
                    s += ", ";
               else
                    s += ".";
          }
          System.out.println(s);
     }
}
