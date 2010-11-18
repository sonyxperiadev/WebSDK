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

package com.sonyericsson.web.sdk.utils.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sonyericsson.web.sdk.utils.RegistryHandler;

/** 
 * class for launching external comamnd line tools
 */
public class CmdHelper extends AbstractHelper {

  private String installDir;
  private File androidToolsPath;
  private String androidPhonegapDefaultPath;
  private String cmdPrefix;

  public CmdHelper() {
    installDir = RegistryHandler.getInstallDir();
    androidPhonegapDefaultPath = System.getProperty("java.io.tmpdir") + "/android";
    androidToolsPath = new File(RegistryHandler.getAndroidSDKPath() + "/tools");
    if(System.getProperty("os.name").equals("Mac OS X"))
    {
        cmdPrefix = "";
    }
    else
    {
        cmdPrefix = "cmd /c";
    }
  }

  public void updateAndroidProject(int target, String projectPath, String name) {
    if(name.indexOf(" ") != -1)
    {
      name = "\""+name+"\"";
    }
    String updateAndroidAppCommand = cmdPrefix + " " + RegistryHandler.getAndroidSDKPath() + "/tools/android update project -t " + target + " -p . -n " + name;
    System.out.println("updateAndroidAppCommand: " + updateAndroidAppCommand);
    runCmd(updateAndroidAppCommand, new File(androidPhonegapDefaultPath));
  }

  public void listTargets() {
    String cmd = cmdPrefix + " " + RegistryHandler.getAndroidSDKPath() +"/tools/android list targets";
    System.out.println(cmd);
    runCmd(cmd, androidToolsPath);
  }

  public void runSimulator(File simulatorPath, String applicationIndexFilePath) {

    applicationIndexFilePath = applicationIndexFilePath.replace('\\', '/');

    //String cmd = "cmd /c start \"\" \""+simulatorPath.getAbsolutePath()+"\" \"" + applicationIndexFilePath + "\"";
    String cmds[] = {simulatorPath.getAbsolutePath(), applicationIndexFilePath};
    System.out.println(cmds[0] + " " + cmds[1]);
    //runCmd(cmd, simulatorPath.getParentFile(), true);
    runCmd(cmds, null, true);
  }

  public void runAndroidSDK() {
    String cmd = cmdPrefix + " " + RegistryHandler.getAndroidSDKPath() +"/tools/android";
    System.out.println(cmd);
    runCmd(cmd, androidToolsPath, true);
  }
  private void runCmd(final String cmds[], final File workingDir, boolean async) {
    if (async) {
      new Thread(new Runnable() {
        public void run() {
          runCmd(cmds, workingDir);
        }
      }).start();
    } else {
      runCmd(cmds, workingDir);
    }
  }

  private void runCmd(final String cmd, final File workingDir, boolean async) {
    if (async) {
      new Thread(new Runnable() {

        public void run() {
          runCmd(cmd, workingDir);
        }
      }).start();

    } else {
      runCmd(cmd, workingDir);
    }

  }

  private void runCmd(String cmd, File workingDir) {

    String s = null;
    try {
      Process p = null;
      if (workingDir == null) {
        p = Runtime.getRuntime().exec(cmd);
      } else {
        p = Runtime.getRuntime().exec(cmd, null, workingDir);
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
  private void runCmd(String[] cmds, File workingDir) {
    String s = null;
    try {
      Process p = null;
      if (workingDir == null) {
        p = Runtime.getRuntime().exec(cmds);
      } else {
        p = Runtime.getRuntime().exec(cmds, null, workingDir);
      }
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
      while ((s = stdInput.readLine()) != null) {
        fireNewMessage(s);
      }
      while ((s = stdError.readLine()) != null) {
        fireNewMessage(s);
      }
    } catch (IOException e) {
      fireNewMessage(e.getStackTrace().toString());
      e.printStackTrace();
    }
    }
}
