/* License (MIT)
 * Copyright 2009 Sony Ericsson Mobile Communications AB
 * website: http://developer.sonyericsson.com/
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * Software), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.sonyericsson.web.sdk.utils;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileHandlerTest {

    public FileHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    
    /**
     * Test of changeAndroidAppName method, of class FileHandler.
     */
    @Test
    public void testInserStringToFile() throws URISyntaxException {
        System.out.println("testAndroidAppNameChange");
        double number = Math.random();
        String name = "" + number;
        URL fileUrl = this.getClass().getResource("/com/sonyericsson/web/sdk/test/resources/strings.xml");
        File stringsXmlFile = new File(fileUrl.toURI());
        FileHandler instance = new FileHandler();
        String firstString = "<string name=\"app_name\">";
        String endString = "</string>";
        instance.insertStringInToAFile(firstString, endString, name, stringsXmlFile);
        String resultName = instance.getAndroidAppNameFromXMLFile(stringsXmlFile);
        assertEquals("The name in the strings.xml should now be " + name, name, resultName);
    }

    /**
     * Test of copyFileToDir method, of class FileHandler.
     */
    @Test
    public void testCopyFileToDir() throws Exception {
        System.out.println("testCopyFileToDir");
        boolean fileCreated = false;
        URL resultUrl = this.getClass().getResource("/com/sonyericsson/web/sdk/test/junk/strings.xml");

        // Remove any previous version of the resultFile.
        if (resultUrl != null) {
            System.out.println("The resultFile already exist. Deleting it and continues with the test.");
            File deleteFile = new File(resultUrl.toURI());
            deleteFile.delete();
        }
        URL fileUrl = this.getClass().getResource("/com/sonyericsson/web/sdk/test/resources/strings.xml");
        URL targetUrl = this.getClass().getResource("/com/sonyericsson/web/sdk/test/junk");
        File stringsXmlFile = new File(fileUrl.toURI());
        File targetDir = new File(targetUrl.toURI());

        FileHandler instance = new FileHandler();
        instance.copyFileToDir(stringsXmlFile, targetDir);
        resultUrl = this.getClass().getResource("/com/sonyericsson/web/sdk/test/junk/strings.xml");
        System.out.println("resultUrl.toURI(): " + resultUrl.toURI());
        File resultFile = new File(resultUrl.toURI());
        if (resultFile.exists()) {
            fileCreated = true;
        }
        assertEquals("Couldn't find String.xml in the target folder.", true, fileCreated);
    }

    
    /**
     * Test of copyDirectory null parameters method, of class FileHandler.
     */
    @Test
    public void testCopyDirectory() throws Exception {
        System.out.println("testCopyDirectory");
        URL sourceUrl = this.getClass().getResource("/com/sonyericsson/web/sdk/test/junk");
        File sourceLocation = new File(sourceUrl.toURI());
        File targetLocation = new File("C:/temp/TargetLocation/");
        FileHandler instance = new FileHandler();
        instance.copyDirectory(sourceLocation, targetLocation);
        if (sourceLocation.getTotalSpace() != targetLocation.getTotalSpace())
        {
          System.out.println("testCopyDirectory faild");
        }
        //assertFalse("testCopyDirectory: No NullPointerException when sourceLocation or targetLocation is null",
        //    exceptionCaught);
        
        instance.deleteFolder(targetLocation);
        
    }
    
    /**
     * Test of copyDirectory null parameters method, of class FileHandler.
     */
    @Test
    public void testCopyDirectoryForNullString() throws Exception {
        System.out.println("testCopyDirectory null test");
        File sourceLocation = null;
        File targetLocation = null;
        boolean exceptionCaught = false;
        FileHandler instance = new FileHandler();
        try {
        instance.copyDirectory(sourceLocation, targetLocation);
        } catch (NullPointerException exception) {
          exceptionCaught = true;
        }
        assertTrue("testCopyDirectory: No NullPointerException when sourceLocation or targetLocation is null",
            exceptionCaught);
    }


    /**
     * Test of deleteFolder method, of class FileHandler.
     * @throws IOException 
     */
    @Test
    public void deleteFolderOrFile() throws IOException {
        System.out.println("deleteFolder");
        File file = new File("C:/temp/temp.txt");
        FileHandler instance = new FileHandler();
        file.createNewFile();
        instance.deleteFolder(file);
        boolean fileExixt = file.exists();
        if (fileExixt){
          fail("deleteFolderOrFile Fail sThe removed file or folder exixt!");
        }
        // TODO review the generated test code and remove the default call to fail.        
    }

    /**
     * Test of addFilePathsToList method, of class FileHandler.
     */
    @Test
    public void addFilePathsToList() {
        System.out.println("addFilePathsToList");
        File folder = null;
        ArrayList<File> files = null;
        FileHandler instance = new FileHandler();
        instance.addFilePathsToList(folder, files);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
