package jester.tests;

public class Util {
	public static String withoutWhitespace(String string){
		return string.replaceAll(" ","").replaceAll("\t","").replaceAll("\n","").replaceAll("\r","");
	}
}
