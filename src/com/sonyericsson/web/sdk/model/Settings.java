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
import java.util.Enumeration;
import java.util.Vector;

import com.sonyericsson.web.sdk.utils.RegistryHandler;

/**
 * class for storing and accessing WebSDK Packager configuration settings and current status
 */
public class Settings {

  public static final int CURRENT_FOLDER = 0;
  public static final int CUSTOM_FOLDER = 1;
  public static final int DEBUG = 0;
  public static final int UNSIGNED = 1;
  public static final int SIGNED = 2;
  private String installDirectory;
  private String sourceFolder;

  public Settings(String sourceFolder) {
      this.sourceFolder = sourceFolder;
    this.installDirectory = RegistryHandler.getInstallDir();
  }

  public String getSourceFolder() {
    if (sourceFolder == null || sourceFolder.equals("")){
      return getSourcePath();
    }
    return sourceFolder;
  }

  public String getInstallDirectory() {
    return installDirectory;
  }

  public String getDefaultIconPath() {
    return installDirectory + "/Resources/se.png";
  }

  public Enumeration<Framework> getFrameworks() {
    Vector<Framework> frameworks = new Vector<Framework>();

    return frameworks.elements();
  }

  public void addFramework(Framework framework) {
  }

  public String getAndroidToolPath() {
    return RegistryHandler.getAndroidSDKPath() + "\\tools";
  }

  public String getAndroidSDKPath() {
    return RegistryHandler.getAndroidSDKPath();
  }

  public void setAndroidSDKPath(String androidSDKPath) {
    RegistryHandler.setAndroidSDKPath(androidSDKPath);
  }

  public String getJDKPath() {
    return RegistryHandler.getJDKPath();
  }

  public void setJDKPath(String jdkPath) {
    RegistryHandler.setJDKPath(jdkPath);
  }

  public void setProjectPath(String projectPath) {
    RegistryHandler.setProjectPath(projectPath);
  }

  public String getProjectPath() {
    return RegistryHandler.getProjectPath();
  }

  public String getPhonegapPath() {
    return RegistryHandler.getPhonegapPath();
  }

  public void setPhonegapPath(String phonegapPath) {
    RegistryHandler.setPhonegapPath(phonegapPath);
  }

  public Targets getTargets() {
    int selectedTarget = RegistryHandler.getAndroidTarget();
    Targets targets = new Targets(selectedTarget);        
    return targets;
  }

  public void setTargets(Targets targets) {
    for (int i = 0; i < targets.getColumnCount(); i++) {
      if (targets.getValueAt(i, 0) != null && ((Boolean) targets.getValueAt(i, 0)).booleanValue()) {
        RegistryHandler.setAndroidTarget(i);
      }
    }
  }

  public String getOutputPath() {
    return RegistryHandler.getOutputPath();
  }

  public void setOutputPath(String path) {
    RegistryHandler.setOutputPath(path);
  }

  public String getSourcePath(){
    return RegistryHandler.getSourcePath();
  }

  public void setSourcePath(String path){
    RegistryHandler.setSourcePath(path);
  }

  public int getOutput() {
    return RegistryHandler.getOutput();
  }

  public void setOutput(int output) {
    RegistryHandler.setOutput(output);
  }

  public File getDestinationDir() {
    switch (getOutput()) {
      case CURRENT_FOLDER:
        return new File(getSourceFolder()).getParentFile();
      case CUSTOM_FOLDER:
        return new File(getOutputPath());
    }
    return null;
  }

  public File getSourceDir() {
    return new File(getSourceFolder());
  }

  public void setApplicationName(String appName) {
    RegistryHandler.setApplicationName(appName);
  }

  public void setPackageName(String packageName) {
    RegistryHandler.setPackageName(packageName);
  }

  public void setIconPath(String iconPath) {
    RegistryHandler.setIconPath(iconPath);
  }

  public String getApplicationName() {
    return RegistryHandler.getApplicationName();
  }

  public String getPackageName() {
    return RegistryHandler.getPackageName();
  }

  public void setCloseWindowSelected(boolean closeWindow) {
    RegistryHandler.setCloseWindowSelected(closeWindow);
  }

  public boolean isCloseWindowSelected() {
    return RegistryHandler.isCloseWindowSelected();
  }

  public AndroidKeys getAndroidKeys() {
    AndroidKeys androidKeys = new AndroidKeys(RegistryHandler.getKeystoreLocation());
    androidKeys.setSelectedKeyAlias(RegistryHandler.getKeyAlias());
    return androidKeys;
  }

  public void setAndroidKeys(AndroidKeys androidKeys) {
    RegistryHandler.setKeystoreLocation(androidKeys.getKeystoreLocation());
    RegistryHandler.setKeyAlias(androidKeys.getSelectedKeyAlias());
  }

  public boolean isConfigured() {
    return RegistryHandler.isConfigured();
  }

  public void setConfigured(boolean configured) {
    RegistryHandler.setConfigured(configured);
  }

  public int getSigning() {
    return RegistryHandler.getSigning();
  }

  public void setSigning(int signing) {
    RegistryHandler.setSigning(signing);
  }
}
