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
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Level;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.AntClassLoader;

import com.sonyericsson.web.sdk.utils.FileHandler;
import com.sonyericsson.web.sdk.utils.Logger;
import com.sonyericsson.web.sdk.utils.RegistryHandler;
import com.sonyericsson.web.sdk.utils.helper.AntHelper;
import com.sonyericsson.web.sdk.utils.helper.CmdHelper;
import com.sonyericsson.web.sdk.utils.helper.HelperListener;
import org.apache.tools.ant.types.Path;

/**
 * Class for generate, install and sign Android applications
 */

public class AndroidApplicationModel extends AbstractApplicationModel implements HelperListener {

  private String installDirectory;
  private String defaultAndroidProjectPath;
  private CmdHelper cmdHelper;
  private String phonegapPackage = "com.phonegap";
  private File tempBuildFile;
  private int targetId;
  private AntClassLoader current;
  private AntHelper antHelper;
  private AntListener antListener;
  private String action;
  private FileHandler fileHandler;
  private String tempPath;
  private String tempAndroidProjectPath;
  private String tempWwwPath;
  private String tempManifestPath;
  private String tempSrcPath;
  private String tempStringsPath;
  private String tempDrawableFolder;
  private int progress;
  private String password;
  private final AndroidKeys androidKeys;
  private String buildProperties;
  private Settings settings;
  private static final String PLATFORM = "Android";

  public AndroidApplicationModel(Settings settings) {
    super(PLATFORM, settings);

    this.settings = settings;
    this.installDirectory = RegistryHandler.getInstallDir();
    this.tempPath = System.getProperty("java.io.tmpdir");
    //this.defaultAndroidProjectPath = installDirectory + "/PhonegapSDK/Default/android";
    this.defaultAndroidProjectPath = settings.getProjectPath();
    this.tempAndroidProjectPath = tempPath + "/android";
    this.tempBuildFile = new File(tempPath + "/android/" + "build.xml");
    this.tempWwwPath = tempAndroidProjectPath + "/assets/www";
    this.tempManifestPath = tempAndroidProjectPath + "/AndroidManifest.xml";
    this.tempSrcPath = tempAndroidProjectPath + "/src/";
    this.tempStringsPath = tempAndroidProjectPath + "/res/values/strings.xml";
    this.tempDrawableFolder = tempAndroidProjectPath + "/res/drawable";
    this.buildProperties = tempAndroidProjectPath + "/build.properties";

    this.antListener = new AntListener();
    this.fileHandler = new FileHandler();
    this.cmdHelper = new CmdHelper();
    cmdHelper.addListener(this);
    antHelper = new AntHelper();
    antHelper.addBuildListener(antListener);
    current = new AntClassLoader(null, Path.systemClasspath);
    androidKeys = settings.getAndroidKeys();
  }

  /**
   * generate Android application
   * @param debug flag for using debug keys for signing
   */
  public void generate(boolean debug) {
    action = "Generating";
    progress = 0;

    prepare();

    String target;
    if (debug) {
      target = "debug";
    } else {
      target = "release";
    }

    antHelper.runAnt(tempBuildFile, target, current);
    try {
      fileHandler.copyDirectory(new File(tempAndroidProjectPath + "/bin"), getDestinationDir(), new FileFilter() {

        public boolean accept(File pathname) {
          final String fileName = pathname.getName().toLowerCase();

          return fileName.endsWith(".apk") || fileName.equalsIgnoreCase("bin");
        }
      });
    } catch (IOException ex) {
      java.util.logging.Logger.getLogger(AndroidApplicationModel.class.getName()).log(Level.SEVERE, null, ex);
    }
    fireModelChanged(new Event(null, 100, 0, "Created", PLATFORM));
  }

  /**
   * sing application using configured keystore and key password
   */
  public void sign() {
    System.out.println("sign");
    if (androidKeys == null || androidKeys.getKeystoreLocation() == null || androidKeys.getKeystoreLocation().equals("")) {
      return;
    }
    System.out.println("sign2");
    String keystorePassword = androidKeys.getKeystorePassword();

    if (keystorePassword == null || keystorePassword.equals("")) {
      keystorePassword = prompt("Keystore password", "Please enter the password for your keystore");
    }
    if (keystorePassword == null || keystorePassword.equals("")) {
      return;
    }
    password = prompt("Key password", "Please enter the password for your key");
    if (password == null || password.equals("")) {
      return;
    }
    action = "Signing";
    progress = 0;
    fireModelChanged(new Event(null, 10, 0, action, PLATFORM));
    File apkFile = new File(getDestinationDir().getPath() + System.getProperty("file.separator") + getNameForFile() + "-unsigned.apk");
    File signedApkFile = new File(getDestinationDir().getPath() + System.getProperty("file.separator") + getNameForFile() + ".apk");

    antHelper.signJar(apkFile, androidKeys.getKeystoreLocation(), keystorePassword, androidKeys.getSelectedKeyAlias(), password, signedApkFile);
    fireModelChanged(new Event(null, 100, 0, "Signed", PLATFORM));
  }

  /**
   * install application to device or emulator
   */
  public void install() {
    action = "Installing";
    progress = 0;
    prepare();
    antHelper.runAnt(tempBuildFile, "install", current);
    fireModelChanged(new Event(null, 100, 0, "Installed", PLATFORM));
  }

  /**
   * clean up temporary folder
   */
  public void finish() {
    fileHandler.deleteFolder(new File(tempAndroidProjectPath));
    fireModelChanged(new Event(null, 100, 0, "Finished", PLATFORM));
  }

  /**
   * checking for conditions to create application
   * @return true if all correct
   */
  public boolean isCreatable() {
    return isValid();
  }

  /**
   * checking for conditions to install application
   * @return true if all correct
   */
  public boolean isInstallable() {
    return isValid();
  }

  /**
   * checking for Android SDK and java compiler configured and accessable
   * @return true if all correct
   */
  private boolean isValid() {
    File sdkToolsFolder = new File(RegistryHandler.getAndroidSDKPath() + "\\tools");
    return sdkToolsFolder.exists() && isToolsJarInWebSDK();
  }

  /**
   * Prepare java compiler before starting Android tools
   * @param jdkPath path to JDK
   */

  public void copyJdkTools(String jdkPath) {
    File targetDir = new File(settings.getInstallDirectory() + "\\Resources\\lib");
    File tools_jar = new File(jdkPath + "\\lib\\tools.jar");
    if (tools_jar.exists() && targetDir.exists()) {
      try {
        new FileHandler().copyFileToDir(tools_jar, targetDir);
      } catch (IOException ex) {
        java.util.logging.Logger.getLogger(AndroidApplicationModel.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  /**
   * Check for java compiler accessable in classpath or from external jar-file
   */

  private boolean isToolsJarInWebSDK() {
        boolean toolsJarAvailable = false;
        try {
            // just check whether this throws an exception
            Class.forName("com.sun.tools.javac.Main");
            toolsJarAvailable = true;
        } catch (Exception e) {
            try {
                Class.forName("sun.tools.javac.Main");
                toolsJarAvailable = true;
            } catch (Exception e2) {
                // ignore
            }
        }
    if(toolsJarAvailable)
    {
        return true;
    }
    else
    {
        File toolsJar = new File(settings.getInstallDirectory() + "\\Resources\\lib\\tools.jar");
        return toolsJar.exists();
    }
  }

  private void prepare() {
    String jdkPath = RegistryHandler.getJDKPath();
    if (!isToolsJarInWebSDK()) {
      copyJdkTools(jdkPath);
    }
    File tools_jar = new File(jdkPath + "\\lib\\tools.jar");
    current.addPathComponent(tools_jar);

    try {
      fileHandler.deleteFolder(new File(tempAndroidProjectPath));

      copyAssets();

      copyProject();

      changeIcon();

      changePackage();

      changeName();

      createBuildProperties();

      targetId = RegistryHandler.getAndroidTarget() + 1;
      // Update R.java and some properties.
      cmdHelper.updateAndroidProject(targetId, tempAndroidProjectPath, getNameForFile());
    } catch (IOException ex) {
      java.util.logging.Logger.getLogger(AndroidApplicationModel.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void copyAssets() throws IOException {
    fireModelChanged(new Event(null, (progress++) % 100, 0, action, PLATFORM));
    File targetAssetsFolder = new File(tempWwwPath);
    // Delete assets/www folder and recreate it.
    // Copy assets to assets/www
    fileHandler.copyDirectory(new File(getSelectedFolder()), targetAssetsFolder, new FileFilter() {

      public boolean accept(File pathname) {

        fireModelChanged(new Event(null, (progress++) % 100, 0, action, PLATFORM));
        return !pathname.getPath().toLowerCase().contains("phonegap.js");
      }
    });
  }

  private void copyProject() {
    Logger.print(this, "copyProject");
    try {
      defaultAndroidProjectPath = settings.getProjectPath();
      fileHandler.copyDirectory(new File(defaultAndroidProjectPath), new File(tempAndroidProjectPath), new FileFilter() {

        public boolean accept(File pathname) {

          fireModelChanged(new Event(null, (progress++) % 100, 0, action, PLATFORM));
          Logger.print(this, pathname.getPath());
          if (pathname.getPath().contains("src")) {
            return false;
          }
          if (pathname.getPath().contains("assets")) {
            return false;
          }
          return true;
        }
      });
    } catch (IOException ex) {
      java.util.logging.Logger.getLogger(AndroidApplicationModel.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void changePackage() {
    defaultAndroidProjectPath = settings.getProjectPath();
    //File androidPhonegapFolder = new File(installDirectory + "/PhonegapSDK/Default/android/src/com/phonegap");
    File androidPhonegapFolder = new File(defaultAndroidProjectPath + "/src/com/phonegap");
    System.out.println("Folder: " + androidPhonegapFolder.getPath());
    File[] files = androidPhonegapFolder.listFiles(new FileFilter() {

      public boolean accept(File pathname) {
        fireModelChanged(new Event(null, (progress++) % 100, 0, action, PLATFORM));
        if (pathname.getPath().endsWith("java")) {
          return true;
        }
        return false;
      }
    });
    System.out.println("Package: " + getPackageName());
    for (int i = 0; files != null && i < files.length; i++) {
      File file = files[i];
      fileHandler.changePackage(phonegapPackage, getPackageName(), new File(tempSrcPath), file);
    }
    File manifestXml = new File(tempManifestPath);
    fileHandler.changeStringInFile(phonegapPackage, getPackageName(), manifestXml);
  }

  private void changeName() {
    // The file in which the AppName is stored.
    File stringsXml = new File(tempStringsPath);
    fireModelChanged(new Event(null, (progress++) % 100, 0, action, PLATFORM));

    String stringToInstert = "\"" + getName() + "\"";
    String firstString = "<string name=\"app_name\">";
    String endString = "</string>";
    fileHandler.insertStringInToAFile(firstString, endString, stringToInstert, stringsXml);

  }

  private void createBuildProperties() {
    StringBuffer buffer = new StringBuffer();
    String string = buffer.toString();
    //fileHandler.createFile(buildProperties, string.getBytes());
  }

  private void changeIcon() {
    try {
      File icon = new File(getIconpath());
      fileHandler.copyFileToDir(icon, new File(tempDrawableFolder));
      System.out.println("icon name: " + icon.getPath());
      fileHandler.changeStringInFile("@drawable/icon", "@drawable/" + removePrefix(icon.getName()), new File(tempManifestPath));
    } catch (IOException ex) {
      java.util.logging.Logger.getLogger(AndroidApplicationModel.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  public void newMessage(String message) {
    fireModelChanged(new Event(message, (progress++) % 100, 0, action, PLATFORM));
  }

  private String removePrefix(String name) {
    int endIndex = name.indexOf(".");
    return name.substring(0, endIndex);
  }

  private String getNameForFile() {
    return getName().replace(' ', '_');
  }

  private class AntListener implements BuildListener {

    public void buildStarted(BuildEvent arg0) {
      Logger.print(this, "buildStarted");
    }

    public void buildFinished(BuildEvent arg0) {
      Logger.print(this, "buildFinished");
    }

    public void targetStarted(BuildEvent arg0) {
      Logger.print(this, "targetStarted");
    }

    public void targetFinished(BuildEvent arg0) {
      Logger.print(this, "targetFinished");
    }

    public void taskStarted(BuildEvent arg0) {
      Logger.print(this, "taskStarted");
    }

    public void taskFinished(BuildEvent arg0) {
      fireModelChanged(new Event(null, progress++, 0, action, PLATFORM));
    }

    public void messageLogged(BuildEvent arg0) {
      fireModelChanged(new Event(arg0.getMessage(), 0, 0, action, PLATFORM));
    }
  }
}
