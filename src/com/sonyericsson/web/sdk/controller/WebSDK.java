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

package com.sonyericsson.web.sdk.controller;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;

import com.sonyericsson.web.sdk.model.AndroidApplicationModel;
import com.sonyericsson.web.sdk.model.AndroidKeys;
import com.sonyericsson.web.sdk.model.ApplicationModel;
import com.sonyericsson.web.sdk.model.Settings;
import com.sonyericsson.web.sdk.model.SymbianApplicationModel;
import com.sonyericsson.web.sdk.model.Targets;
import com.sonyericsson.web.sdk.utils.Logger;
import com.sonyericsson.web.sdk.utils.RegistryHandler;
import com.sonyericsson.web.sdk.utils.helper.CmdHelper;
import com.sonyericsson.web.sdk.view.ProgressView;
import com.sonyericsson.web.sdk.view.SetupView;
import com.sonyericsson.web.sdk.view.ViewHelper;
import com.sonyericsson.web.sdk.view.WebSDKView;
import java.io.File;
import java.io.FilenameFilter;

/**
 * The main class of the application.
 */
public class WebSDK extends Application {

  public static String[] args;
  private WebSDKView webSDKView;
  private String selectedFolder;
  private String appName;
  private String packageName;
  private Settings settings;
  private ProgressView progressView;
  private Hashtable<String, ApplicationModel> applicationModels = new Hashtable<String, ApplicationModel>();
  private SetupView setupView;
  private final String PATH_PREFIX = "-path";
  private final String NAME_PREFIX = "-name";
  private final String ID_PREFIX = "-id";

  /**
   * At startup create and show the main frame of the application.
   */
  @Override
  protected void startup() {
    parseInparamsToSettings();
    startWebSDK();
  }

  private void initWebSDKView() {
    webSDKView = new WebSDKView(this, settings, applicationModels);
    webSDKView.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ViewHelper.putWindowInCenter(webSDKView.getFrame());
  }

  // Checks all the arguments of the inparameters and applies them as settings
  // if they exist.
  private void parseInparamsToSettings() {
    if (args.length > 0) {
      selectedFolder = getInparam(PATH_PREFIX);
      appName = getInparam(NAME_PREFIX);
      packageName = getInparam(ID_PREFIX);
      System.out.println("Selected folder : " + selectedFolder);
      System.out.println("AppName : " + appName);
      System.out.println("Package name : " + packageName);
      settings = new Settings(selectedFolder);
      if (appName.equals("") || appName == null) {
          appName = "DemoApp";
      }
      settings.setApplicationName(appName);
      if (packageName.equals("") || packageName == null) {
          packageName = "com.sonyericsson.web.demo";
      }
      settings.setPackageName(packageName);
    } else {
      selectedFolder = "";
      settings = new Settings(selectedFolder);
    }
  }

// Parses and returns the value for a specific argument.
// Arguments: PATH_PREFIX, NAME_PREFIX, ID_PREFIX
  private String getInparam(String searchForArgument) {
    String answer = "";
    for (int i = 0; i < args.length; i++) {
      if (args[i].toLowerCase().equals(searchForArgument)) {
        if ((i + 1) < args.length && !args[i + 1].startsWith("-")) {
          answer = args[i + 1];
          break;

        }
      }
    }
    return answer;
  }

  private void generateApplications(final boolean install, final boolean debug, final boolean signed) {
    progressView = new ProgressView(webSDKView.getFrame(), applicationModels.elements(), "", settings);
    new SwingWorker<Object, Object>() {

      @Override
      protected Object doInBackground() throws Exception {
        Logger.print(this, "doInBackground");
        saveSettings();
        setApplicationData();

        Enumeration<String> platforms = webSDKView.getSelectedPlatforms();
        while (platforms.hasMoreElements()) {
          String platform = platforms.nextElement();
          System.out.println("platform: " + platform);
          ApplicationModel application = applicationModels.get(platform);
          try {
            if (install) {
              application.install();
              application.finish();
            } else {
              application.generate(debug);
              if (signed) {
                application.sign();
              }

              application.finish();
            }

          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

        }
        return null;
      }
    }.execute();
    progressView.setVisible(true);
  }

  private void initApplicationModels() {
    addApplicationModel(new AndroidApplicationModel(settings));
    addApplicationModel(new SymbianApplicationModel(settings));
  }

  private void addApplicationModel(ApplicationModel applicationModel) {
    System.out.println(applicationModel);
    applicationModels.put(applicationModel.getPlatform(), applicationModel);
  }

  /**
   * A convenient static getter for the application instance.
   * @return the instance of DesktopApplication1
   */
  public static WebSDK getApplication() {
    return Application.getInstance(WebSDK.class);
  }

  /**
   * Main method launching the application.
   */
  public static void main(String[] args) {
    checkRegistrySettings();
    WebSDK.args = args;
    launch(WebSDK.class, args);
  }

  @Action
  synchronized public void installApplication() {
    Logger.print(this, "");
    saveSettings();
    generateApplications(true, false, false);
  }

  /* public void restartWebSDK(){
  try {
  String command = "javaw -jar \"" + RegistryHandler.getInstallDir() + "Resources/WebSDK.jar\" -path \""
  + settings.getSourceDir().getCanonicalPath() + "\"";
  System.out.println("Command: " + command);
  Runtime.getRuntime().exec(command);
  System.exit(0);
  } catch (IOException ex) {
  java.util.logging.Logger.getLogger(WebSDK.class.getName()).log(Level.SEVERE, null, ex);
  }
  }
   */
  @Action
  synchronized public void createApplication() {
    Logger.print(this, "");

    boolean debug = false;
    boolean signed = false;
    switch (webSDKView.getSigning()) {
      case Settings.DEBUG:
        debug = true;
        break;

      case Settings.SIGNED:
        signed = true;
        break;

    }

    generateApplications(false, debug, signed);
  }

  /**
   * Check for java compiler accessable in classpath
   */

  public boolean isToolsAvailable() {
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
    return toolsJarAvailable;
  }

  private void saveSettings() {
    String jdkPath = webSDKView.getJDKPath();
    String androidSDKPath = webSDKView.getAndroidSDKPath();
    Targets targets = webSDKView.getTargets();
    String outputPath = webSDKView.getOutputPath();
    String sourcePath = webSDKView.getSourceFolder();
    int output = webSDKView.getOutput();
    boolean closeWindow = webSDKView.isCloseWindowSelected();
    AndroidKeys androidKeys = webSDKView.getAndroidKeys();
    int signing = webSDKView.getSigning();
    if(!isToolsAvailable())
    {
        settings.setJDKPath(jdkPath);
    }
    settings.setAndroidSDKPath(androidSDKPath);
    settings.setTargets(targets);
    settings.setOutputPath(outputPath);
    settings.setSourcePath(sourcePath);
    settings.setOutput(output);
    settings.setCloseWindowSelected(closeWindow);
    settings.setAndroidKeys(androidKeys);
    settings.setSigning(signing);
  }

  private void setApplicationData() {
    appName = webSDKView.getApplicationName();
    packageName = webSDKView.getPackageName();
    String iconPath = webSDKView.getIconPath();
    String sourcePath = webSDKView.getSourceFolder();
    settings.setApplicationName(appName);
    settings.setPackageName(packageName);
    settings.setIconPath(iconPath);
    settings.setSourcePath(sourcePath);
  }

  @Override
  protected void shutdown() {
    super.shutdown();
  }

  @Action
  public void exitWebSDK() {
    saveSettings();
    setApplicationData();
    System.exit(0);
  }

  private void startWebSDK() {
    initApplicationModels();
    if (settings.isConfigured()) {
      initWebSDKView();
      show(webSDKView);
    } else {
      setupView = new SetupView(settings);
      setupView.setVisible(true);
    }
  }

  @Action
  public void finnished() {
    progressView.setVisible(false);
    progressView = null;
  }

  @Action
  public void startSimulator() {
    CmdHelper cmdHelper = new CmdHelper();
    String index_htmlPath = webSDKView.getSourceFolder() + "\\index.html";
    File simulatorInstallationPath = new File(RegistryHandler.getPhonegapPath());
    File[] phonegapSimulator = simulatorInstallationPath.listFiles(new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return (name.endsWith("Simulator") || name.endsWith("Simulator.exe"));
        }
    });
    if(phonegapSimulator.length > 0)
    {
        cmdHelper.runSimulator(phonegapSimulator[0], index_htmlPath);
    }
  }

  @Action
  public void startAndroidSDK() {
    CmdHelper cmdHelper = new CmdHelper();
    cmdHelper.runAndroidSDK();
  }

  @Action
  public void runWizard() {
      setupView = new SetupView(settings);
      setupView.setVisible(true);
      saveSettings();
      hide(webSDKView);
  }

  @Action
  public void cancelWizard() {
    if (webSDKView == null) {
      initWebSDKView();
    }
    if (!webSDKView.getFrame().isVisible()) {
      show(webSDKView);
    }
    setupView.setVisible(false);
    setupView = null;
  }

   private static void checkRegistrySettings() {
        String installDir = RegistryHandler.getInstallDir();
        String installDir2 = null;
        String jarName = "WebSDK.jar";
        String jarFolder = "Resources";
        String classPath = System.getProperty("java.class.path");
        String paths[] = classPath.split(System.getProperty("path.separator"));
        for(int n = 0; n < paths.length; n++)
        {
            if(paths[n].endsWith(jarName))
            {
                if(paths[n].indexOf(System.getProperty("file.separator")) != -1)
                {
                    String jarPath = System.getProperty("file.separator")+jarFolder+System.getProperty("file.separator")+jarName;
                    if(paths[n].lastIndexOf(jarPath) != -1)
                    {
                        System.out.println(paths[n]);
                        installDir2 = paths[n].substring(0, paths[n].lastIndexOf(jarPath)) + System.getProperty("file.separator");
                    }
                }
                else
                {
                    String userDir = System.getProperty("user.dir");
                    String resourcePath = System.getProperty("file.separator")+jarFolder;
                    if(userDir.endsWith(resourcePath))
                    installDir2 = userDir.substring(0, userDir.lastIndexOf(resourcePath)) + System.getProperty("file.separator");
                }
                break;
            }
        }
        if(installDir.equals(""))
        {
            if(installDir2 != null)
            {
                System.out.println("Install folder (detected from system properties): "+installDir2);
                RegistryHandler.setInstallDir(installDir2);
            }
            else
            {
                System.out.println("Install path could not be detected! Please, reinstall application to default location");
                System.exit(-1);
            }
        }
        else
        {
            if(installDir2 != null)
            {
                if(installDir.equals(installDir2))
                {
                    System.out.println("Install folder (taken from registry preferences): "+installDir);
                }
                else
                {
                    System.out.println("Updated install folder (detected from system properties): "+installDir2);
                    RegistryHandler.setInstallDir(installDir2);
                }
            }
            else
            {
                System.out.println("Install folder (taken from registry preferences): "+installDir);
            }
        }
    }
}
