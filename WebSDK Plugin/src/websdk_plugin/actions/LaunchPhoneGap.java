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

package websdk_plugin.actions;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import websdk_plugin.PhoneGapActivator;
import Utilities.CmdHelper;
import Utilities.RegistryHandler;

public class LaunchPhoneGap implements IObjectActionDelegate {

  private ISelection selection;

  /**
	 * 
	 */
  public LaunchPhoneGap() {
    // TODO Auto-generated constructor stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse
   * .jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
   */
  @Override
  public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction
   * )
   */
  @Override
  public void run(IAction action) {
    // TODO Auto-generated method stub
    String dir = "";
    CmdHelper cmdHelper = new CmdHelper();
    try {

      String project = selection.toString();

      // The string is always [P/name].
      // So name is from index 3 to (length - 1).
      String name = project.substring(3, project.length() - 1);
      
      dir = ResourcesPlugin.getWorkspace().getRoot().getProject(name).getLocation().toString();
//      MessageDialog.openInformation(PhoneGapActivator.getDefault().getWorkbench()
//          .getActiveWorkbenchWindow().getShell(), "title", "Dir: " + dir);
    } catch (Throwable t) {
      System.out.println("Something went wrong");
      t.printStackTrace();
    }
    String SimulatorInstallationPath = RegistryHandler.getInstallDir() + "PhoneGap Simulator";
    String cmdargs[];
    String index_htmlPath = dir + "/index.html";
    cmdHelper.runSimulator(SimulatorInstallationPath, index_htmlPath);   
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface
   * .action.IAction, org.eclipse.jface.viewers.ISelection)
   */
  @Override
  public void selectionChanged(IAction action, ISelection selection) {
    // TODO Auto-generated method stub
    this.selection = selection;
  }

}
