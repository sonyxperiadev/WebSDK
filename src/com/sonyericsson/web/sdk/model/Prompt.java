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

public class Prompt {

  private String value;
  private String title;
  private String information;
  private boolean finished;

  public String getInformation() {
    return information;
  }

  public void setInformation(String information) {
    this.information = information;
  }
  
  private Object lock = new Object();

  public void setValue(String value) {
    if (!finished) {
      this.value = value;
    }
  }

  public String getValue() {
    return value;
  }

  public void setTitle(String title) {
    if (!finished) {
      this.title = title;
    }
  }

  public String getTitle() {
    return title;
  }

  public void finished() {
    System.out.println("finished");
    synchronized (lock) {
      if (!finished) {
        finished = true;
        lock.notifyAll();
      }
    }
  }

  public void waitForAnswer() {
    System.out.println("waitForAnswer");

    synchronized (lock) {
      if (!finished) {
        try {
          lock.wait();
        } catch (InterruptedException e) {
        }
      }
    }
  }
}
