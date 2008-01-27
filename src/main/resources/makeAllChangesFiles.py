# Copyright (c) 2002 Ivan Moore 
# This code is under the same license as Jester
# see http://jester.sourceforge.net

from xml.parsers import expat
import sys
from os.path import splitext

class ChangesParser:
	def __init__(self):
		self.parser = expat.ParserCreate()
		self.parser.StartElementHandler = self.startElement
		self.parser.EndElementHandler = self.endElement
	def feed(self, data):
		self.parser.Parse(data,0)
	def close(self):
		self.parser.Parse("",1)
		del self.parser
	def endElement(self, tag):
		if tag=="JestedFile":
			self.allChangesFile.write(self.originalContents[self.indexInOriginalOfLastChange:])
			self.allChangesFile.close()
	def startElement(self, tag, attrs):
		if tag=="JestedFile":
			self.filename = str(attrs["fileName"])
			self.originalContents = open(self.filename,"rb").read()
			newFilename = splitext(self.filename)[0]+".jester"
			self.allChangesFile = open(newFilename,"wb")
			self.indexInOriginalOfLastChange = 0
		elif tag=="ChangeThatDidNotCauseTestsToFail":
			indexOfChange = int(attrs["index"])
			if indexOfChange>=self.indexInOriginalOfLastChange:
				originalUptoChange = self.originalContents[self.indexInOriginalOfLastChange:indexOfChange]
				self.allChangesFile.write(originalUptoChange)
			self.allChangesFile.write(attrs["to"])
			self.indexInOriginalOfLastChange = indexOfChange + len(attrs["from"])

def makeAllChangesFiles(fileName):
	file = open(fileName)
	contents = file.read()
	file.close()

	p = ChangesParser()
	p.feed(contents)
	p.close()
	
if __name__ == '__main__':		
	if len(sys.argv)<2:
		print "useage: python makeAllChangesFiles.py jesterReport.xml"
		sys.exit(1)

	fileName = sys.argv[1]	
	makeAllChangesFiles(fileName)