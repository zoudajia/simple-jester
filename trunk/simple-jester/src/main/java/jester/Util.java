package jester;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Vector;

public class Util {
	public static Vector runCommand(String commandLine, Logger aLogger) throws IOException {
		aLogger.log("Trying to run command \"" + commandLine + "\"");
		Process proc = Runtime.getRuntime().exec(commandLine);

		BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		BufferedReader err = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

		Vector output = new Vector();
		String str;
		while ((str = br.readLine()) != null) {
			output.addElement(str);
		}
		boolean firstLine = true;
		while ((str = err.readLine()) != null) {
			if (firstLine) {
				System.out.println("ERR> NOTE - jester has tried to 'exec' \"" + commandLine + "\"");
			}
			// XXX see bug 1346212
			firstLine = false;
			System.out.println("ERR> " + str);
		}
		try {
			proc.waitFor();
		} catch (InterruptedException e) {
			aLogger.log("process was interrupted for " + commandLine);
		}
		br.close();
		err.close();

		aLogger.log("running command \"" + commandLine + "\" resulted in \"" + output + "\"");

		return output;
	}

	public static String readFile(String fileName) throws IOException {
		Reader reader = new FileReader(fileName);
		try {
			return readContents(reader);
		} finally {
			reader.close();
		}
	}

	private static String readContents(Reader reader) throws IOException {
		StringBuffer buff = new StringBuffer();

		int currentCharacter;
		while (true) {
			currentCharacter = reader.read();
			if (currentCharacter == -1) {
				break;
			}
			buff.append((char) currentCharacter);
		}

		return buff.toString();
	}

	// TODO: how are you supposed to do this?
	private static String readContentsIgnoringCarrageReturns(Reader reader) throws IOException {
		StringBuffer buff = new StringBuffer();
		BufferedReader lineReader = new BufferedReader(reader);

		String line = lineReader.readLine();
		while (line != null) {
			buff.append(line + "\n");
			line = lineReader.readLine();
		}

		return buff.toString();
	}

	public static String readFileOnClassPath(String fileName) throws IOException {
		InputStream fileInputStream = ClassLoader.getSystemResourceAsStream(fileName);
		if (fileInputStream == null) {
			throw new FileNotFoundException("Could not find " + fileName + " on the classpath");
		}
		return readContentsIgnoringCarrageReturns(new InputStreamReader(fileInputStream));
	}
}