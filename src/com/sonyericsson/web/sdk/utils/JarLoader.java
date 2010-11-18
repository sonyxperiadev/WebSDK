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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarLoader extends URLClassLoader {

  public JarLoader(URL[] urls) {
    super(urls);
  }


  public JarLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
  }


  public void addFile(String path) throws MalformedURLException {
    addFile(new File(path));
  }

  public void addFile(File file) throws MalformedURLException {
    addURL(file.toURI().toURL());
  }

  public void addFolder(String path) throws MalformedURLException {
    File folder = new File(path);
    File[] files = folder.listFiles();
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      //System.out.println("File: " + file.getPath());
      addFile(file);
    }
  }
  
  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    System.out.println("findClass: " + name);
    return super.findClass(name);
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    System.out.println("loadClass: " + name);
    return super.loadClass(name);
  }

  @Override
  protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    System.out.println("loadClass2: " + name);
    return super.loadClass(name, resolve);
  }
}
