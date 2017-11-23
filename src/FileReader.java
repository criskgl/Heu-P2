import java.io.*;
import java.util.Scanner;

public class FileReader {

	public static void main(String[] args) throws IOException {
		
		String text = "";
		Scanner input = new Scanner(new File("src\\input.txt"));
		
		System.out.println();	
		
		while(input.hasNext()) {
			 String i = input.next();
	         text += i;
		}
		
		System.out.println(text);
	}
	
	public FileReader() {
		// TODO Auto-generated constructor stub
	}
}
