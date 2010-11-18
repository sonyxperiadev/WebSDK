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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class test the basic functionality of the RegistryHandler.
 * It writes and reads to the registry and checks that the two are consistent.
 */
public class RegistryHandlerTest {

    public RegistryHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        RegistryHandler.setAndroidSDKPath("");
        RegistryHandler.setAndroidTarget(1000);
        RegistryHandler.setApplicationName("");
        RegistryHandler.setCloseWindowSelected(false);
        RegistryHandler.setConfigured(false);
        RegistryHandler.setIconPath("");
        RegistryHandler.setInstallDir("");
        RegistryHandler.setJDKPath("");
        RegistryHandler.setKeyAlias("");
        RegistryHandler.setKeystoreLocation("");
        RegistryHandler.setOutput(1000);
        RegistryHandler.setOutputPath("");
        RegistryHandler.setPackageName("");
        RegistryHandler.setSigning(1000);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setIconPath and getIconPath method, of class RegistryHandler.
     */
    @Test
    public void setAndGetIconPath() {
        System.out.println("setAndGetIconPath");
        String iconPath = "testPath";
        RegistryHandler.setIconPath(iconPath);        
        assertEquals(RegistryHandler.getIconPath(), iconPath);
    }        

    /**
     * Test of setOutputPath and getOutputPath method, of class RegistryHandler.
     */
    @Test
    public void setAndGetOutputPath() {
        System.out.println("setAndGetOutputPath");
        String path = "c:\test";
        RegistryHandler.setOutputPath(path);
        assertEquals(RegistryHandler.getOutputPath(), path);
    }

    /**
     * Test of getInstallDir and setInstallDir method, of class RegistryHandler.
     */
    @Test
    public void setAndGetInstallDir() {
        System.out.println("setAndGetInstallDir");
        String path = "c:\test";
        RegistryHandler.setInstallDir(path);
        assertEquals(RegistryHandler.getInstallDir(), path);       
    }

    /**
     * Test of setAndroidSDKPath and getAndroidSDKPath method, of class RegistryHandler.
     */
    @Test
    public void setAndGetAndroidSDKPath() {
        System.out.println("setAndGetAndroidSDKPath");
        String path = "c:\test";
        RegistryHandler.setAndroidSDKPath(path);
        assertEquals(RegistryHandler.getAndroidSDKPath(), path);
    }    

    /**
     * Test of setJDKPath  and getJDKPath method, of class RegistryHandler.
     */
    @Test
    public void setAndGetJDKPath() {
        System.out.println("setAndGetJDKPath");
        String path = "c:\test";
        RegistryHandler.setJDKPath(path);
        assertEquals(RegistryHandler.getJDKPath(), path);
    }   

    /**
     * Test of setAndroidTarget and getAndroidTarget method, of class RegistryHandler.
     */
    @Test
    public void setAndGetAndroidTarget() {
        System.out.println("setAndroidTarget");
        int i = 0;
        RegistryHandler.setAndroidTarget(i);
        assertEquals(RegistryHandler.getAndroidTarget(), i);
    }    

    /**
     * Test of setApplicationName and getApplicationName method, of class RegistryHandler.
     */
    @Test
    public void setAndGetApplicationName() {
        System.out.println("setAndGetApplicationName");
        String appName = "Test";
        RegistryHandler.setApplicationName(appName);
        assertEquals(RegistryHandler.getApplicationName(), appName);
    }

    /**
     * Test of setPackageName and getPackageName method, of class RegistryHandler.
     */
    @Test
    public void setAndGetPackageName() {
        System.out.println("setAndGetPackageName");
        String packageName = "packageName";
        RegistryHandler.setPackageName(packageName);
        assertEquals(RegistryHandler.getPackageName(), packageName);
    }          

    /**
     * Test of setOutput and getOutput method, of class RegistryHandler.
     */
    @Test
    public void setAndGetOutput() {
        System.out.println("setAndGetOutput");
        int output = 0;
        RegistryHandler.setOutput(output);
        assertEquals(RegistryHandler.getOutput(), output);
    }

    /**
     * Test of setCloseWindowSelected and isCloseWindowSelected method, of class RegistryHandler.
     */
    @Test
    public void setAndIsCloseWindowSelected() {
        System.out.println("setAndIsCloseWindowSelected");
        boolean closeWindow = true;
        RegistryHandler.setCloseWindowSelected(closeWindow);
        assertEquals(RegistryHandler.isCloseWindowSelected(), closeWindow);
    }        

    /**
     * Test of setKeystoreLocation and getKeyStoreLocation method, of class RegistryHandler.
     */
    @Test
    public void setAndGetKeystoreLocation() {
        System.out.println("setAndGetKeystoreLocation");
        String location = "c:\test";
        RegistryHandler.setKeystoreLocation(location);
        assertEquals( RegistryHandler.getKeystoreLocation(), location);
    }

    /**
     * Test of setKeyAlias and getKeyAlias method, of class RegistryHandler.
     */
    @Test
    public void setAndGetKeyAlias() {
        System.out.println("setAndGetKeyAlias");
        String selectedKeyAlias = "testAlias";
        RegistryHandler.setKeyAlias(selectedKeyAlias);
        assertEquals(RegistryHandler.getKeyAlias(), selectedKeyAlias);
    }   

    /**
     * Test of setConfigured and isConfigured method, of class RegistryHandler.
     */
    @Test
    public void setAndIsConfigured() {
        System.out.println("setAndIsConfigured");
        boolean configured = true;
        RegistryHandler.setConfigured(configured);
        assertEquals(RegistryHandler.isConfigured(), configured);
    }      

    /**
     * Test of setSigning and getSigning method, of class RegistryHandler.
     */
    @Test
    public void setAndGetSigning() {
        System.out.println("setAndGetSigning");
        int value = 1;
        RegistryHandler.setSigning(value);
        assertEquals(RegistryHandler.getSigning(), value);
    }

}