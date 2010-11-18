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
import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.TableModelEvent;

/**
 * class for storing and avalable keys for singing application
 */
public class AndroidKeys extends AbstractTableModel {

  private KeyStore kS;
  private static final String[] columnNames = {"Selected", "Alias"};
  private final Vector<String> aliases = new Vector<String>();
  private static final Boolean TRUE = new Boolean(true);
  private static final Boolean FALSE = new Boolean(false);
  private String selectedKeyAlias;
  private String keystoreLocation;
  private String keystorePassword;
  private boolean loaded;

  public AndroidKeys(String keystoreLocation) {
    this.keystoreLocation = keystoreLocation;
    init();
  }

  public void createKey(String keyAlias, String password, int validity, String name, String organisation, String organisationUnit, String location, String state, String country) {
    try {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
      keyGen.initialize(1024);
      KeyPair key = keyGen.generateKeyPair();
      PublicKey publicKey = key.getPublic();
      KeyStore.PrivateKeyEntry privateKeyEntry = new KeyStore.PrivateKeyEntry(key.getPrivate(), null);
      kS.setEntry(keyAlias, privateKeyEntry, new KeyStore.PasswordProtection(password.toCharArray()));
    } catch (KeyStoreException ex) {
      Logger.getLogger(AndroidKeys.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchAlgorithmException ex) {
      java.util.logging.Logger.getLogger(AndroidApplicationModel.class.getName()).log(Level.SEVERE, null, ex);
    }
    refresh();
    fireTableModelChanged(new TableModelEvent(this));
  }

  public String getSelectedKeyAlias() {
    System.out.println("Alias: " + selectedKeyAlias);
    return selectedKeyAlias;
  }

  public String getKeystoreLocation() {
    return keystoreLocation;
  }

  public String getKeystorePassword() {
    return keystorePassword;
  }

  public int getRowCount() {
    return aliases.size();
  }

  public int getColumnCount() {
    return columnNames.length;
  }

  public String getColumnName(int columnIndex) {
    return columnNames[columnIndex];
  }

  public Class<?> getColumnClass(int columnIndex) {
    switch (columnIndex) {
      case 0:
        return Boolean.class;
      case 1:
        return String.class;
      default:
        return null;
    }
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    if (columnIndex == 0) {
      return true;
    }
    return false;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    switch (columnIndex) {
      case 0:
        if (aliases.get(rowIndex).equalsIgnoreCase(selectedKeyAlias)) {
          return TRUE;
        }
        return FALSE;
      case 1:
        return aliases.get(rowIndex);
      default:
        return null;
    }
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    if (((Boolean)aValue).booleanValue()) {
      if (columnIndex == 0) {
        selectedKeyAlias = aliases.get(rowIndex);
      }
    }
  }

  public void reload() {
    init();
    fireTableModelChanged(new TableModelEvent(this));
  }

  private void init() {
    if (keystoreLocation == null || keystoreLocation.equals("") || keystorePassword == null) {
      return;
    }
    try {
      kS = KeyStore.getInstance("JKS");
      File keystoreFile = new File(keystoreLocation);
      if (!keystoreFile.exists()) {
        keystoreFile.createNewFile();
      }
      kS.load(new FileInputStream(keystoreFile), keystorePassword.toCharArray());
      loaded = true;
      refresh();
    } catch (Exception ex) {
      Logger.getLogger(AndroidKeys.class.getName()).log(Level.SEVERE, null, ex);
      loaded = false;
    }
  }

  private void refresh() {
    try {
      Enumeration<String> tempAliases = kS.aliases();
      while (tempAliases.hasMoreElements()) {
        String string = tempAliases.nextElement();
        aliases.add(string);
      }
    } catch (KeyStoreException ex) {
      Logger.getLogger(AndroidKeys.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  void setSelectedKeyAlias(String keyAlias) {
    selectedKeyAlias = keyAlias;
  }

  public void setKeystoreLocation(String keystoreLocation) {
    this.keystoreLocation = keystoreLocation;
    init();
    fireTableModelChanged(new TableModelEvent(this));
  }

  public void setKeystorePassword(String password) {
    keystorePassword = password;
  }

  public boolean isLoaded() {
    return loaded;
  }
}
