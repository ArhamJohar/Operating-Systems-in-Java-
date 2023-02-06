
import java.util.ArrayList;

public class Page{
	ArrayList<Byte[]> pages = new ArrayList<Byte[]>();

	public void assign() {	
		Byte[] page = new Byte[128];
		pages.add(page);
	}

}
