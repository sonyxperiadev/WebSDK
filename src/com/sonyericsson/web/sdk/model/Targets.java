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

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.event.TableModelEvent;

import com.sonyericsson.web.sdk.utils.helper.CmdHelper;
import com.sonyericsson.web.sdk.utils.helper.HelperListener;
import javax.swing.SwingWorker;

/**
 * storing and accessing available Android targets 
 */
public class Targets extends AbstractTableModel implements HelperListener {

  private String[] columNames = {"selected", "id", "name", "type", "api level"};
  Object[] currentTarget;
  Vector<Object[]> rows = new Vector<Object[]>();

  public Targets(final int selectedTarget) {    
    final TableModelEvent event = new TableModelEvent(this);
    new SwingWorker<Object, Object>() {

      @Override
      protected Object doInBackground() throws Exception {          
        parseTargets();                
        if (selectedTarget != -1) {
          setSelectedIndex(selectedTarget);
        }
        fireTableModelChanged(event);
        return null;
      }
    }.execute();
        
  }    

  public int getRowCount() {
    return rows.size();
  }

  public int getColumnCount() {
    return columNames.length;
  }

  public String getColumnName(int columnIndex) {
    return columNames[columnIndex];
  }

  public Class<?> getColumnClass(int columnIndex) {
    return rows.elementAt(0)[columnIndex].getClass();
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    if (columnIndex == 0) {
      return true;
    }
    return false;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    if (rowIndex >= rows.size()) {
      return null;
    }
    return rows.elementAt(rowIndex)[columnIndex];
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    boolean selected = ((Boolean)aValue).booleanValue();
    //Logger.print(this, selected);
    if (selected) {
      Enumeration<Object[]> en = rows.elements();
      while (en.hasMoreElements()) {
        Object[] nextElement = en.nextElement();
        nextElement[columnIndex] = new Boolean(false);
      }
    }
    rows.elementAt(rowIndex)[columnIndex] = aValue;
      TableModelEvent tableModelEvent = new TableModelEvent(this);
    fireTableModelChanged(tableModelEvent);
  }

  private void parseTargets() {
    CmdHelper cmdHelper = new CmdHelper();
    cmdHelper.addListener(this);
    cmdHelper.listTargets();
  }

  public void newMessage(String message) {
    parseMessage(message);
  }

  private void parseMessage(String message) {
    String value = null;
    if (message.toLowerCase().contains("id:")) {
      currentTarget = new Object[5];
      rows.add(currentTarget);
      value = parseValue(message);
      currentTarget[1] = value;
      currentTarget[0] = new Boolean(false);
    } else if (message.toLowerCase().contains("name:")) {
      value = parseValue(message);
      currentTarget[2] = value;
    } else if (message.toLowerCase().contains("type:")) {
      value = parseValue(message);
      if(value.toLowerCase().contains("add-on")){
          rows.remove(currentTarget);
      }      
      currentTarget[3] = value;
    } else if (message.toLowerCase().contains("level:")) {
      value = parseValue(message);
      currentTarget[4] = value;
    }    
  }

  public void setSelectedIndex(int i) {
    if (i >= rows.size()) {
      return;
    }
    rows.elementAt(i)[0] = new Boolean(true);
  }

  public int getSelectedIndex() {
    for (int i = 0; i < getColumnCount(); i++) {
      if (getValueAt(i, 0) != null && ((Boolean) getValueAt(i, 0)).booleanValue()) {
        return i;
      }
    }
    return -1;
  }

  private String parseValue(String message) {
    int beginIndex = message.indexOf(":") + 1;
    String value = message.substring(beginIndex);
    return value;
  }

  public void refresh() {
    rows.removeAllElements();
    parseTargets();
    TableModelEvent tableModelEvent = new TableModelEvent(this);
    fireTableModelChanged(tableModelEvent);
  }
}
