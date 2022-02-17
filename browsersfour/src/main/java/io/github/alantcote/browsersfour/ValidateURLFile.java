/**
 * 
 */
package io.github.alantcote.browsersfour;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Program to validate the URLs found in a file that lists them one to a line.
 */
public class ValidateURLFile {

	/**
	 * 
	 */
	public ValidateURLFile() {
	}

	/**
	 * @param args command line arguments.
	 */
	public static void main(String[] args) {
		ValidateURLFile program = new ValidateURLFile();

		program.validate(args[0]);
	}
	
	public void validate(String filename) {
		System.out.println("Validating " + filename + " ...");
		
		File file = new File(filename);
		
		try {
			FileInputStream fis = new FileInputStream(file);
			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(fis));
			String line;
			
			while ((line = lnr.readLine()) != null) {
				validateURL(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void validateURL(String urlString) {
		try {
			URL url = new URL(urlString);
			
			URLConnection conn = url.openConnection();
			
			if (conn instanceof HttpURLConnection) {
				HttpURLConnection httpConn = (HttpURLConnection) conn;
				int resCode = httpConn.getResponseCode();
				
				System.out.println("" + resCode + " " + urlString);
			} else {
				System.out.println("??? " + urlString);
			}
		} catch (MalformedURLException e) {
			System.out.println("MUE " + urlString);
//			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOE " + urlString);
//			e.printStackTrace();
		}
	}

}
