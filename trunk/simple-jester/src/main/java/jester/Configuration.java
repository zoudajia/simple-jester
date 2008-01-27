package jester;

public interface Configuration {
	//e.g. ".java"
	public String sourceFileExtension();

	//e.g. "PASSED"
	public String buildPassString();

	//e.g. "jesterReport.xml"
	public String xmlReportFileName();

	public boolean shouldReportEagerly();
	public Logger getLogger();

	public boolean closeUIOnFinish();
}