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

import java.io.File;
import org.apache.tools.ant.AntClassLoader;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.taskdefs.SignJar;
import org.apache.tools.ant.taskdefs.Zip;

/**
 * class for launching Ant builder and handle events from it
 */
public class AntHelper extends AbstractHelper {

  private Project p;

  public AntHelper() {
    p = new Project();
  }

  public void runAnt(File buildFile, String target, ClassLoader classLoader) {

    if(classLoader != null)
    {
      AntClassLoader antClassLoader = (AntClassLoader)classLoader;
      p.setCoreLoader(antClassLoader);
      antClassLoader.setProject(p);
      System.out.println(antClassLoader.getClasspath());
    }

    try {
      p.fireBuildStarted();
      p.init();
      ProjectHelper.configureProject(p, buildFile);
      p.executeTarget(target);
      p.fireBuildFinished(null);
    } catch (BuildException e) {
      p.fireBuildFinished(e);
    }
  }

  public void signJar(File jarFile, String keystore, String keystorePass, String alias, String keyPass, File destinationFile) {

    SignJar signJar = new SignJar();
    signJar.setProject(p);
    signJar.setJar(jarFile);
    signJar.setKeystore(keystore);
    signJar.setStorepass(keystorePass);
    signJar.setAlias(alias);
    signJar.setKeypass(keyPass);
    signJar.setSignedjar(destinationFile);
    signJar.execute();
  }

  public void addBuildListener(BuildListener listener) {
    p.addBuildListener(listener);
  }

  public void zipFile(File dir, File zipFile) {
    Zip zip = new Zip();
    zip.setProject(p);
    zip.setBasedir(dir);
    zip.setDestFile(zipFile);
    zip.execute();
  }
}
