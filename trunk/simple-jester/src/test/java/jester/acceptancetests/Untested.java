package jester.acceptancetests;

public class Untested {
	public int canDecrementOrWhatever(int aNum) {
		return --aNum;
	}
	public int canIncrementOrWhatever(int aNum) {
		return ++aNum;
	}
	public boolean canReturnAnyBooleanFalse() {
		return false;
	}
	public boolean canReturnAnyBooleanTrue() {
		return true;
	}
	public int canReturnAnyInt() {
		return 1;
	}
	public int canTakeAnyBranchFor(boolean condition) {
		if (condition) {
			return 1;
		} else {
			return 0;
		}
	}
	public boolean couldBeEqual() {
		return "hi" != "hello";
	}
	public boolean couldBeNotEqual() {
		String a = "hi";
		return a == a;
	}
	public int switchStatementThatWouldCauseCompilationProblem(int k) {
		switch(k){
		  case '1' :
			return 3;
		  case '2' :
			return 4;
		}
		return 1;
	}
	public void methodWithIntCommentedOut(){
		//should ignore this 1 if default comment ignoring working
	}
	int Log2(float n) { return Log(2,n); }
	
	int Log(int i,float n) { return 2; }
	
	public void dontWantWhileTrueToBeMutated(){
		while(true){//should ignore this if default ignoring working
			break;
		}
	}
}