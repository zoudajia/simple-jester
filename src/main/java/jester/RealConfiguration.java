package jester;

import java.io.*;
import java.util.Locale;
import java.util.Properties;

public class RealConfiguration implements Configuration {
	public static final String DEFAULT_CONFIGURATION_FILENAME = "jester.cfg";

	private Logger myLogger = new RealLogger();
	private Properties myProperties;

	public RealConfiguration(String configFileName, PrintStream errorStream) throws IOException {

		myProperties = new Properties();
		// FIXME Let's not load this from the class loader
		// or at least not only from the Class loader
		InputStream configPropertyFile = ClassLoader.getSystemResourceAsStream(configFileName);
		if (configPropertyFile == null) {
			configPropertyFile = getClass().getResourceAsStream(configFileName);
		}
		if (configPropertyFile != null) {
			myProperties.load(configPropertyFile);
		} else {
			errorStream.println("Warning - could not find " + DEFAULT_CONFIGURATION_FILENAME + " so using default configuration values.");
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