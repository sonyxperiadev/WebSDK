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

package com.sonyericsson.web.sdk.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SymbianApplicationModelTest {

    public SymbianApplicationModelTest() {
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
   * Test of generate method, of class SymbianApplicationModel.
   */
  @Test
  public void testGenerate() {
    System.out.println("generate");
    boolean debug = true;
    SymbianApplicationModel instance = new SymbianApplicationModel(null);
    instance.generate(debug);
    // TODO review the generated test code and remove the default call to fail.
   // fail("The test case is a prototype.");
  }

  /**
   * Test of sign method, of class SymbianApplicationModel.
   */
  @Test
  public void testSign() {
    // Not implemented
    
//    System.out.println("sign");
//    SymbianApplicationModel instance = null;
//    instance.sign();
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of install method, of class SymbianApplicationModel.
   */
  @Test
  public void testInstall() {
    // Not implemented
    
//    System.out.println("install");
//    SymbianApplicationModel instance = null;
//    instance.install();
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of finish method, of class SymbianApplicationModel.
   */
  @Test
  public void testFinish() {
    //Not implemented

//    System.out.println("finish");
//    SymbianApplicationModel instance = null;
//    instance.finish();
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of isCreatable method, of class SymbianApplicationModel.
   */
  @Test
  public void testIsCreatable() {
    System.out.println("isCreatable");
    SymbianApplicationModel instance = new SymbianApplicationModel(null);
    boolean expResult = true;
    boolean result = instance.isCreatable();
   assertEquals("testIsCreatable allways true", expResult, result);
  }

  /**
   * Test of isInstallable method, of class SymbianApplicationModel.
   */
  @Test
  public void testIsInstallable() {
    System.out.println("isInstallable");
    SymbianApplicationModel instance = new SymbianApplicationModel(null);
    boolean expResult = false;
    boolean result = instance.isInstallable();
    assertEquals("testIsInstallable allways false", expResult, result);
  }

}