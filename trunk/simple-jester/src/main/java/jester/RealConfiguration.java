package jester;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Properties;

public class RealConfiguration implements Configuration {
	private Logger myLogger = new RealLogger();
	private Properties myProperties;

	public RealConfiguration(String configFileName, PrintStream errorStream) throws IOException {
		myProperties = new Properties();
		if (configFileName != null) {
			InputStream configPropertyFile = new FileInputStream(configFileName);
			myProperties.load(configPropertyFile);
		} else {
			errorStream.println("Warning - no config specified so using default configuration values.");
		}
	}

	public RealConfiguration(String configFileName) throws IOException {
		this(configFileName, System.err);
	}

	private boolean isTrue(String value) {
		return value.toLowerCase(Locale.ENGLISH).equals("true");
	}

	private boolean isTrueProperty(String propertyName, boolean defaultValue) {
		String propertyValue = myProperties.getProperty(propertyName);
		if (propertyValue == null) {
			return defaultValue;
		}
		return isTrue(propertyValue);
	}

	public boolean shouldReportEagerly() {
		return isTrueProperty("shouldReportEagerly", false);
	}

	public String sourceFileExtension() {
		return stringProperty("sourceFileExtension", ".java");
	}

	private String stringProperty(String propertyName, String defaultValue) {
		String result = myProperties.getProperty(propertyName);
		if (result == null) {
			return defaultValue;
		}
		return result;
	}

	public String buildPassString() {
		return stringProperty("buildPassString", "BUILD SUCCESSFUL");
	}

	public Logger getLogger() {
		return myLogger;
	}

	public String xmlReportFileName() {
		return stringProperty("xmlReportFileName", "jesterReport.xml");
	}

	public boolean closeUIOnFinish() {
		return isTrueProperty("closeUIOnFinish", true);
	}
}