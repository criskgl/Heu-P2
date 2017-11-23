import java.io.*;
import java.util.Scanner;

public class FileReader {

	public FileReader() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Scanner input = new Scanner(new File("input.txt"));
		
		System.out.println();	
		
		while(input.hasNextInt())
	}
}
