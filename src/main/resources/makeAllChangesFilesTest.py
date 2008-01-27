# Copyright (c) 2002, 2004 Ivan Moore 
# This code is under the same license as Jester
# see http://jester.sourceforge.net

import makeAllChangesFiles
import unittest, os, os.path

testDirectory="pyTestFiles"
testFile="someFile"
testFileExtension=".txt"
originalFileName=os.path.join(testDirectory,testFile+testFileExtension)
originalFileContents=open(originalFileName,"rb").read()
allChangesFileName=os.path.join(testDirectory,testFile+".jester")
		
class MakeAllChangesFilesTests(unittest.TestCase):		
	def checkResults(self, changesXMLFile, expectedResults):
		makeAllChangesFiles.makeAllChangesFiles(os.path.join(testDirectory,changesXMLFile))
		allChangesFileContents=open(allChangesFileName,"rb").read()
		self.assertEquals(expectedResults, allChangesFileContents)
		os.remove(allChangesFileName)
		
	def testNoChanges(self):
		expectedFileContents=originalFileContents
		self.checkResults("noChanges.xml", expectedFileContents)
		
	def testOneChange(self):
		expectedFileContents=originalFileContents.replace("land","banana")
		self.checkResults("oneChange.xml", expectedFileContents)
		
	def testTwoConsecutiveChanges(self):
		expectedFileContents=originalFileContents.replace("land","fruitbat")
		self.checkResults("twoConsecutiveChanges.xml", expectedFileContents)
		
	def testTwoChanges(self):
		expectedFileContents=originalFileContents.replace("land","kitchen").replace("elf","chef")
		self.checkResults("twoChangesRelative.xml", expectedFileContents)

if __name__ == '__main__':
	unittest.main()
