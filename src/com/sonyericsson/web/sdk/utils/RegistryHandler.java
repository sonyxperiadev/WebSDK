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

import java.util.prefs.Preferences;

/**
 * storing and accessing persistent setting using Java Preferences API
 */
public class RegistryHandler {

  private static final String INSTALL_DIRECTORY = "installdirectory";
  private static final String ANDROID_PATH = "androidpath";
  private static final String ANDROID_TARGET = "androidtarget";
  private static final String ANDROID_PROJECT_PATH = "androidstub";
  private static final String PHONEGAP_PATH = "phonegappath";
  private static final String APPLICATION_NAME = "applicationname";
  private static final String JDK_PATH = "jdkpath";
  private static final String PACKAGE_NAME = "packagename";
  private static final String ICON_PATH = "iconpath";
  private static final String OUTPUT_PATH = "outputpath";
  private static final String SOURCE_PATH = "sourcepath";
  private static final String OUTPUT = "output";
  private static final String CLOSE_WINDOW = "closewindow";
  private static final String KEYSTORE = "keystore";
  private static final String KEY_ALIAS = "keyalias";
  private static final String CONFIGURED = "configured";
  private static final String SIGNING = "signing";

  public static void setIconPath(String iconPath) {
    setRegistryValue(ICON_PATH, iconPath);
  }

  public static String getIconPath() {
    return getRegistryValue(ICON_PATH);
  }

  public static String getOutputPath() {
    return getRegistryValue(OUTPUT_PATH);
  }

  public static void setOutputPath(String path) {
    setRegistryValue(OUTPUT_PATH, path);
  }

  public static String getSourcePath(){
    return getRegistryValue(SOURCE_PATH);
  }

  public static void setSourcePath(String path){
    setRegistryValue(SOURCE_PATH, path);
  }

  public static String getInstallDir() {
    return getRegistryValue(INSTALL_DIRECTORY);
  }
  
  public static void setInstallDir(String path) {
    setRegistryValue(INSTALL_DIRECTORY, path);
  }

  public static void setAndroidSDKPath(String path) {
    setRegistryValue(ANDROID_PATH, path);
  }

  public static String getAndroidSDKPath() {
    return getRegistryValue(ANDROID_PATH);
  }

  public static void setJDKPath(String path) {
    setRegistryValue(JDK_PATH, path);
  }

  public static String getJDKPath() {
    return getRegistryValue(JDK_PATH);
  }


  public static void setAndroidTarget(int i) {
    setRegistryValue(ANDROID_TARGET, String.valueOf(i));
  }

  public static int getAndroidTarget() {
    String target = getRegistryValue(ANDROID_TARGET);
    if (target == null || target.equals("")) {
      return -1;
    }
    return Integer.parseInt(target);
  }

  public static void setPhonegapPath(String path) {
    setRegistryValue(PHONEGAP_PATH, path);
  }

  public static String getPhonegapPath() {
    return getRegistryValue(PHONEGAP_PATH);
  }

  public static void setApplicationName(String appName) {
    System.out.println("appName: " + appName);
    setRegistryValue(APPLICATION_NAME, appName);
  }

  public static void setPackageName(String packageName) {
    setRegistryValue(PACKAGE_NAME, packageName);
  }

  public static void setProjectPath(String projectPath) {
    setRegistryValue(ANDROID_PROJECT_PATH, projectPath);
  }

  public static String getProjectPath() {
    return getRegistryValue(ANDROID_PROJECT_PATH);
  }

  public static String getPackageName() {
    return getRegistryValue(PACKAGE_NAME);
  }

  public static String getApplicationName() {
    System.out.println("appName2: " + getRegistryValue(APPLICATION_NAME));
    return getRegistryValue(APPLICATION_NAME);
  }

  private static String getRegistryValue(String key) {
    Preferences systemPref = Preferences.systemRoot();
    Preferences prefs = systemPref.node("web_sdk");
    String value = prefs.get(key, "");
    return value;
  }

  private static void setRegistryValue(String key, String value) {
    Preferences systemPref = Preferences.systemRoot();
    Preferences prefs = systemPref.node("web_sdk");
    prefs.put(key, value);
  }

  public static int getOutput() {
    String registryValue = getRegistryValue(OUTPUT);
    if (registryValue == null || registryValue.equals("")) {
      return 0;
    }
    return Integer.parseInt(registryValue);
  }

  public static void setOutput(int output) {
    setRegistryValue(OUTPUT, String.valueOf(output));
  }

  public static void setCloseWindowSelected(boolean closeWindow) {
    setRegistryValue(CLOSE_WINDOW, String.valueOf(closeWindow));
  }

  public static boolean isCloseWindowSelected() {
    return Boolean.parseBoolean(getRegistryValue(CLOSE_WINDOW));
  }

  public static String getKeystoreLocation() {
    return getRegistryValue(KEYSTORE);
  }

  public static void setKeystoreLocation(String location) {
    setRegistryValue(KEYSTORE, location);
  }

  public static void setKeyAlias(String selectedKeyAlias) {
    setRegistryValue(KEY_ALIAS, selectedKeyAlias);
  }

  public static String getKeyAlias() {
    return getRegistryValue(KEY_ALIAS);
  }
  
  public static void setConfigured(boolean configured) {
    setRegistryValue(CONFIGURED, String.valueOf(configured));
  }

  public static boolean isConfigured() {
    return Boolean.parseBoolean(getRegistryValue(CONFIGURED));
  }

  public static int getSigning() {
    String registryValue = getRegistryValue(SIGNING);
    if (registryValue == null || registryValue.equals("")) {
      return 0;
    }
    return Integer.parseInt(registryValue);
  }

  public static void setSigning(int value) {
    setRegistryValue(SIGNING, String.valueOf(value));
  }
}
