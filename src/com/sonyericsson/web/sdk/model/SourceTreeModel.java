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

import java.io.File;
import java.io.Serializable;

import javax.swing.tree.TreePath;

public class SourceTreeModel extends AbstractTreeModel implements Serializable {

  String root;

  public SourceTreeModel(String startPath) {
    root = startPath;
  }

  public Object getRoot() {
    System.out.println("getRoot");

    System.out.println("return: " + root);
    return new File(root);
  }

  public Object getChild(Object parent, int index) {
    System.out.println("getChild; " + parent);
    File directory = (File) parent;
    String[] children = directory.list();
    System.out.println("return: " + children[index]);
    return new File(directory, children[index]);
  }

  public int getChildCount(Object parent) {
    System.out.println("getChildCount" + parent);
    File fileSysEntity = (File) parent;
    if (fileSysEntity.isDirectory()) {
      String[] children = fileSysEntity.list();

      System.out.println("return: " + children.length);
      return children.length;
    } else {

      System.out.println("return: " + 0);
      return 0;
    }
  }

  public boolean isLeaf(Object node) {
    System.out.println("isLeaf: " + node);

    System.out.println("return: " + ((File) node).isFile());
    return ((File) node).isFile();
  }

  public void valueForPathChanged(TreePath path, Object newValue) {
    System.out.println("valueForPathChanged: " + path);
  }

  public int getIndexOfChild(Object parent, Object child) {
    System.out.println("getIndexOfChild: " + child);
    File directory = (File) parent;
    File fileSysEntity = (File) child;
    String[] children = directory.list();
    int result = -1;

    for (int i = 0; i < children.length; ++i) {
      if (fileSysEntity.getName().equals(children[i])) {
        result = i;
        break;
      }
    }

    return result;
  }
}


