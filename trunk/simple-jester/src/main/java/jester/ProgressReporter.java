package jester;

import java.awt.Color;

public interface ProgressReporter {
    
	void setMaximum(int numberOfFilesThatWillBeTested);
	void progress();
    // XXX This shouldn't be a color. This should be pass or fail
	void setColor(Color aColor);
	void setText(String text);
    
}
