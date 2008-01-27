# Copyright (c) 2002-2008 Ivan Moore 
# This code is under the same license as Jester
# see http://jester.sourceforge.net

from makeWebView import *
import unittest, os, os.path, glob

testDirectory="pyTestFiles"

def giveFileSlashNLineEndingsAndOsSep(filename):
	f=open(filename,'r')
	c=f.read()
	f.close()
	f=open(filename,'wb')
	f.write(c.replace("\\",os.sep))
	f.close()
	
for filename in glob.glob(testDirectory+os.sep+"*.*"):
	giveFileSlashNLineEndingsAndOsSep(filename)
	
noChangesFileName=os.path.join(testDirectory,"noChanges.html")
noChangesFileContents=open(noChangesFileName,"rb").read()
webViewFileName=os.path.join(testDirectory,"someFile.html")
indexFileName="testIndex.html"
		
class MakeWebViewTests(unittest.TestCase):
	def makeAllChangesFile(self, changesXMLFile):
		sort = False
		ignoreZeroChangeFiles = False
		ignorePerfectScoreFiles = False
		makeWebView(os.path.join(testDirectory,changesXMLFile), indexFileName, sort, ignoreZeroChangeFiles, ignorePerfectScoreFiles, None)
		
	def checkResults(self, changesXMLFile, expectedResults):
		self.makeAllChangesFile(changesXMLFile)
		webViewFileContents=open(webViewFileName,"r").read()
		self.assertEquals(expectedResults, webViewFileContents)
		os.remove(webViewFileName)
		os.remove(indexFileName)
		
	def testNoChanges(self):
		expectedFileContents=noChangesFileContents
		self.checkResults("noChanges.xml", expectedFileContents)
		
	def testOneChange(self):
		expectedFileContents=noChangesFileContents.replace("land",'<span class="removed">land</span><span class="added">banana</span>')
		self.checkResults("oneChange.xml", expectedFileContents)
		
	def testTwoConsecutiveChanges(self):
		expectedFileContents=noChangesFileContents.replace("land",'<span class="removed">land</span><span class="added">fruit</span><span class="added">bat</span>')
		self.checkResults("twoConsecutiveChanges.xml", expectedFileContents)
		
	def testTwoChanges(self):
		expectedFileContents=noChangesFileContents.replace("land",'<span class="removed">land</span><span class="added">kitchen</span>').replace("elf",'<span class="removed">elf</span><span class="added">chef</span>')
		self.checkResults("twoChangesRelative.xml", expectedFileContents)
		
	def testTwoChangesIndexFileWhereFilesAreAbsolute(self):
		self.makeAllChangesFile("twoChangesAbsolute.xml")
		expectedResults=open(os.path.join(testDirectory,"expectedIndexAbsolute.html"), "rb").read()
		actualResults=open(indexFileName, "r").read()
		self.assertEquals(expectedResults, actualResults)
		os.remove(webViewFileName)
		os.remove(indexFileName)
		
	def testTwoChangesIndexFileWhereFilesAreRelative(self):
		self.makeAllChangesFile("twoChangesRelative.xml")
		expectedResults=open(os.path.join(testDirectory,"expectedIndexRelative.html"), "rb").read()
		actualResults=open(indexFileName, "r").read()
		self.assertEquals(expectedResults, actualResults)
		os.remove(webViewFileName)
		os.remove(indexFileName)

class WebViewOutputFolderManglingTests(unittest.TestCase):
    def testThatOutputFolderRootHasNoEffectIfNone(self):
        self.assertEquals("foo", OutputFileWriter(None).outputFileLocation("foo", False))
        self.assertEquals("foo", OutputFileWriter(None).outputFileReference("foo", False))
        self.assertEquals("foo", OutputFileWriter(None).outputFileLocation("foo", True))
        self.assertEquals("file://foo", OutputFileWriter(None).outputFileReference("foo", True))
        
    def testThatOutputFolderRootIsUsedForRelativePath(self):
        self.__assertEqualPaths("there/instead/foo/bar", OutputFileWriter("there/instead").outputFileLocation("foo/bar", False))
        self.__assertEqualPaths("there/instead/foo/bar", OutputFileWriter("there/instead").outputFileReference("foo/bar", False))
        
    def testThatOutputFolderRootIsUsedForRelativePathAndFileProtocolUsedForAbsoluteOutputFolderRoot(self):
        self.__assertEqualPaths("C:/hello/foo/bar", OutputFileWriter("C:/hello").outputFileLocation("foo/bar", False))
        self.__assertEqualPaths("file://C:/hello/foo/bar", OutputFileWriter("C:/hello").outputFileReference("foo/bar", False))

    def testThatOutputFolderRootIsNotUsedForAbsolutePath(self):
        self.__assertEqualPaths("C:/foo/bar", OutputFileWriter("there/instead").outputFileLocation("C:/foo/bar", True))
        self.__assertEqualPaths("file://C:/foo/bar", OutputFileWriter("there/instead").outputFileReference("C:/foo/bar", True))

    def __assertEqualPaths(self, path1, path2):
        self.assertEquals(path1.replace("\\","/"), path2.replace("\\","/"))
        
if __name__ == '__main__':
	unittest.main()
