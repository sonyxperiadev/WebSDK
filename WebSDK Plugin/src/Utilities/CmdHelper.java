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

package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import Utilities.RegistryHandler;

public class CmdHelper extends AbstractHelper {

  private String installDir;
  private String androidToolsPath;
  private String androidPhonegapDefaultPath;
  private String cmdPrefix;

  public CmdHelper() {
    installDir = RegistryHandler.getInstallDir();
    androidPhonegapDefaultPath = System.getProperty("java.io.tmpdir") + "/android";
    androidToolsPath = RegistryHandler.getAndroidSDKPath() + "\\tools";
    cmdPrefix = "cmd /c";
  }

  public void updateAndroidProject(int target, String projectPath, String name) {
    String updateAndroidAppCommand = cmdPrefix + " " + androidToolsPath + "\\android update project -t " + target + " -p " + " \"" + androidPhonegapDefaultPath + "\" -n " + name;
    System.out.println("updateAndroidAppCommand: " + updateAndroidAppCommand);
    runCmd(updateAndroidAppCommand, installDir);
  }

  public void buildAndroidPhonegapDebug(String workingDir) {
    String cmd = cmdPrefix + " ant debug";
    runCmd(cmd, workingDir);
  }

  public void buildAndroidPhonegapRelease(String workingDir) {
    String cmd = cmdPrefix + " ant release";
    runCmd(cmd, workingDir);
  }

  public void installAndroidPhonegapOnEmulator(String workingDir) {
    String cmd = cmdPrefix + " ant install";
    runCmd(cmd, workingDir);
  }

  public void reInstallAndroidPhonegapOnEmulator(String workingDir) {
    String cmd = cmdPrefix + " ant reinstall";
    runCmd(cmd, workingDir);
  }

  private void runCmd(String cmd, String workingDir) {
    String[] env = new String[1];// makePATHVariable(androidToolsPath);
    env[0] = "path=" + androidToolsPath;

    String s = null;

    try {
      Process p = null;
      if (workingDir == null) {
        p = Runtime.getRuntime().exec(cmd);
      } else {
        File workingDirectory = new File(workingDir);
        p = Runtime.getRuntime().exec(cmd, null, workingDirectory);
      }
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

      BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

      // read the output from the command
      while ((s = stdInput.readLine()) != null) {
        fireNewMessage(s);
      }

      // read any errors from the attempted command
      while ((s = stdError.readLine()) != null) {
        fireNewMessage(s);
      }

    } catch (IOException e) {
      fireNewMessage(e.getStackTrace().toString());
      e.printStackTrace();
    }
  }

  public boolean signAndroid(String name, String outputFolder) {
    boolean success = false;
    String signAndroidAppCommand = cmdPrefix + " " + androidToolsPath + "\\apkBuilder.bat \"" + outputFolder + "\\" + name + ".apk\" -rj \"" + androidPhonegapDefaultPath + "\\" + "\\bin\\" + name + "-unsigned.apk\"";
    runCmd(signAndroidAppCommand, null);
    return success;
  }

  public void listTargets() {
    String cmd = cmdPrefix + " " + androidToolsPath + "\\android list targets";
    System.out.println(cmd);
    runCmd(cmd, null);
  }

  public void runSimulator(String simulatorPath, String applicationIndexFilePath)
  {
      //String cmd = cmdPrefix + " \""  + simulatorPath + "/PhoneGap Simulator.exe\"" + " " + applicationIndexFilePath;      

     applicationIndexFilePath =  applicationIndexFilePath.replace('\\', '/');

      String cmd = cmdPrefix + " simulator.exe \"" + applicationIndexFilePath + "\"";
      System.out.println(cmd);
      runCmd(cmd, simulatorPath);
  }

}
