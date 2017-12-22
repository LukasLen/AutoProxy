package proxy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Save extends Window{
	private static final long serialVersionUID = 1L;
	
	private static final String FILENAME = "save.dat";

	public static void write() {
		try {
			PrintWriter writer;
			writer = new PrintWriter(FILENAME, "UTF-8");
			writer.println(ssid_input);
			writer.close();
			success.setText("Saved");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void read() {
		try(BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
		    String line = br.readLine();
		    ssid_input=line;
		    input.setText(ssid_input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
