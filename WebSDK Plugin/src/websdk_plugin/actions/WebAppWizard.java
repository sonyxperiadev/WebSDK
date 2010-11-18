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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import Utilities.FileHandler;
import Utilities.RegistryHandler;
import Wizards.PageListener;
import Wizards.SecondPage;
import Wizards.WebAppWizardPage;

public class WebAppWizard extends Wizard implements INewWizard, PageListener {

  WebAppWizardPage page1;

  SecondPage page2;

  boolean canFinish;

  IWorkbench workbench;

  IStructuredSelection selection;

  public void init(IWorkbench workbench, IStructuredSelection selection) {
    this.workbench = workbench;
    this.selection = selection;
  }

  @Override
  public void addPages() {
    page1 = new WebAppWizardPage("Test", this);
    addPage(page1);
    // page2 = new SecondPage("test2");
    // addPage(page2);
  }

  @Override
  public boolean performFinish() {
    boolean finish = false;
    String projectName = page1.getProjectName();
    String packageName = page1.getPackageName();
    System.out.println(projectName);
    System.out.println(packageName);
    IProgressMonitor monitor = new NullProgressMonitor();
    IWorkspace workspace = ResourcesPlugin.getWorkspace();    
    IWorkspaceRoot workspaceRoot = workspace.getRoot();
    IProject project = workspaceRoot.getProject(projectName);
    if (project.exists()) {
      MessageDialog.openError(
          getShell(),
          "Projectname already exists.",
          "The chosen projectname already exists. Please choose another name.");
    } else {
      try {
        project.create(monitor);
        project.open(monitor);
        IProjectDescription desc = project.getDescription();        
        desc.setNatureIds(new String[] { PhoneGapNature.NATURE_ID });
        project.setDescription(desc, monitor);
      } catch (CoreException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      File file = new File(RegistryHandler.getInstallDir()+"/pluginFileTemplate");
      copyDefaultFolderToProject(file, projectName);
      try {
        project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
      } catch (CoreException e) {
        e.printStackTrace();
      }
      finish = true;
    }
    return finish;
  }

  private void copyDefaultFolderToProject(File defaultFolder, String projectName) {
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IWorkspaceRoot workspaceRoot = workspace.getRoot();
    IPath location = workspaceRoot.getLocation();
    File workspaceFolder = location.toFile();
    File projectDir = new File(workspaceFolder, projectName);
    if (projectDir.exists() && defaultFolder.exists()) {
      FileHandler fileHandler = new FileHandler();
      try {
        fileHandler.copyFolderContent(defaultFolder, projectDir);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public boolean canFinish() {
    return canFinish;
  }

  @Override
  public void finishStatus(boolean canFinish) {
    this.canFinish = canFinish;
    this.getContainer().updateButtons();
  }

}
