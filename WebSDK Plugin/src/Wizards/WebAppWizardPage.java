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

package Wizards;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class WebAppWizardPage extends WizardPage implements Listener, KeyListener {

  Text projectNameText;

  Text identifierText;

  Button testButton;

  PageListener listener = null; 

  public WebAppWizardPage(String pageName, PageListener listener) {
    super(pageName);
    setTitle("Web App Project Information");
    setDescription("Please enter the necessary information for the web application.");
    this.listener = listener;    
    //listener.finishStatus(false);
  }
  
  public String getProjectName(){
    return projectNameText.getText();
  }
  
  public String getPackageName(){
    return identifierText.getText();
  }

  public void createControl(Composite parent) {
    // create the composite to hold the widgets
    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout gl = new GridLayout();
    int ncol = 2;
    gl.numColumns = ncol;
    composite.setLayout(gl);

    // create the widgets and their grid data objects
    // ask for project name.
    new Label(composite, SWT.NONE).setText("Project name*:");
    projectNameText = new Text(composite, SWT.BORDER);
    projectNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    projectNameText.addKeyListener(this);    

    // ask for Package name.
    new Label(composite, SWT.NONE).setText("Package name / Identifier:");
    identifierText = new Text(composite, SWT.BORDER);
    identifierText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    // Construct a help button for package name.
    testButton = new Button(composite, SWT.PUSH);
    testButton.setText("Package name?");
    testButton.setLayoutData(new GridData(GridData.BEGINNING));
    testButton.addListener(SWT.Selection, this);

    // set the composite as the control for this page
    setControl(composite);

  }

  @Override
  public void handleEvent(Event event) {
    if (event.widget == testButton) {
      MessageDialog
          .openInformation(
              this.getShell(),
              "Package name information",
              "The package name / identifier should be a unique string that represents your application. It is often constructed in the same way as a package name in java. Eg. com.yourcompany.yourdepartment.yourapplicationname.");
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    // TODO Auto-generated method stub
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (e.widget == projectNameText) {
      String enteredName = projectNameText.getText();
      if (enteredName.contains(" ") || enteredName.isEmpty()) {
        projectNameText.setBackground(new Color(Display.getCurrent(), 255, 0, 0));
        listener.finishStatus(false);
      } else {
        // set white background.
        projectNameText.setBackground(null);
        listener.finishStatus(true);
      }
    }
  }

}
