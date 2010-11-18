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

public class Logger {

  private final static String applicationName = "WebFeedsReader";

  private static boolean timeActivated = true;

  private static boolean activated = true;


  /**
   * Only prints the text in the debugText variable if the Debug class
   * is in the "activated" state
   *
   * @param debugText
   *          the text to be printed
   */
  public static void print(String debugText) {
    print(null, debugText);
  }

  public static void print(Object obj, Object output) {
    print(obj, String.valueOf(output));
  }

  /**
   * Only prints the text in the debugText variable if the Debug class
   * is in the "activated" state Also prints the name of the class
   * from where the command is being run
   *
   * @param obj
   *          the object initiating the print command
   * @param debugText
   *          the text to be printed
   */
  public static void print(Object obj, String debugText) {
    if (!activated) {
      return;
    }

    String className = obj != null ? obj.getClass().getName() : "";

    if (!className.equals("")) {
      className = " " + className;
    }

    String threadName = Thread.currentThread().getName();

    if (timeActivated) {
      System.out.println(System.currentTimeMillis() + " [" + applicationName + "] ("
          + Thread.activeCount() + ") (" + threadName + ")" + className + ": " + debugText);
      return;
    }

    System.out.println("[" + applicationName + "] (" + Thread.activeCount() + ") (" + threadName
        + ")" + className + ": " + debugText);
  }

  /**
   * Always prints the text in the errorText variable
   *
   * @param errorText
   *          the text to be printed
   */
  public static void printError(String errorText) {
    printError(null, errorText);
  }

  /**
   * Always prints the text in the errorText variable Also prints the
   * name of the class from where the command is being run
   *
   * @param obj
   *          the object initiating the print command
   * @param errorText
   *          the text to be printed
   */
  public static void printError(Object obj, String errorText) {
    String className = obj != null ? obj.getClass().getName() : "";
    System.out.println("(" + System.currentTimeMillis() + ") [" + applicationName + "] ("
        + Thread.activeCount() + ") (" + Thread.currentThread().getName() + ") " + className
        + ": (ERROR) " + errorText);
  }
}
