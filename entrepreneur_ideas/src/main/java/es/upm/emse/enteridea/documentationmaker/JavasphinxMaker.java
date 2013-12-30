package es.upm.emse.enteridea.documentationmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JavasphinxMaker {

	public JavasphinxMaker() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Runtime rt = Runtime.getRuntime();

		try {

			Process proc = rt.exec(args[0]);
			System.out.println("Processing files:");
			showOutput(proc);
			
			if (args.length > 1) {
				proc = rt.exec(args[1]);
				showOutput(proc);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void showOutput(Process proc) throws IOException {
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(
				proc.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(
				proc.getErrorStream()));
		String s = null;
		while ((s = stdInput.readLine()) != null) {
			System.out.println(s);
		}

		// read any errors from the attempted command
		System.out.println("Errors:");
		while ((s = stdError.readLine()) != null) {
			System.out.println(s);
		}
	}

}
