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

import java.io.File;

import javax.swing.JFileChooser;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class SecondPage extends WizardPage implements Listener {

  Combo targetCombo;

  Text identifierText;

  Button browseButton;

  Composite composite;

  public SecondPage(String pageName) {
    super(pageName);
    setTitle("Step 2 of Web App Project Information.");
    setDescription("Please enter the Android specific information for the web application.");
  }

  public void createControl(Composite parent) {
    // create the composite to hold the widgets
    composite = new Composite(parent, SWT.NONE);
    GridLayout gl = new GridLayout();
    int ncol = 3;
    gl.numColumns = ncol;
    composite.setLayout(gl);

    // create the widgets and their grid data objects
    // ask for project name.
    new Label(composite, SWT.NONE).setText("Choose Android target:");
    Combo targetCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
    targetCombo.add("1.1");
    targetCombo.add("1.5");
    targetCombo.add("1.6");
    targetCombo.add("2.0");
    new Label(composite, SWT.NONE).setText("");

    // ask for Package name.
    new Label(composite, SWT.NONE).setText("Icon image:");
    Text identifierText = new Text(composite, SWT.BORDER);
    identifierText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    browseButton = new Button(composite, SWT.PUSH);
    browseButton.setText("Browse");
    browseButton.addListener(SWT.Selection, this);

    // new Label(composite,
    // SWT.NONE).setText("(UPackage name / Identifier, should follow the java package structure in order to ensure uniqueness)");
    // Combo travelDate = new Combo(composite, SWT.BORDER |
    // SWT.READ_ONLY);
    // GridData gd = new GridData();
    // gd.horizontalAlignment = GridData.BEGINNING;
    // gd.widthHint = 25;
    // travelDate.setLayoutData(gd);

    // Combo travelMonth = new Combo(composite, SWT.BORDER |
    // SWT.READ_ONLY);
    // travelMonth.setLayoutData(new
    // GridData(GridData.FILL_HORIZONTAL));
    //
    // Combo travelYear = new Combo(composite, SWT.BORDER |
    // SWT.READ_ONLY);
    // travelYear.setLayoutData(new
    // GridData(GridData.FILL_HORIZONTAL));

    // Similar widgets are constructed for date of return ...
    // createLine(composite, ncol);
    // Departure
    // new Label(composite, SWT.NONE).setText("From:");
    // Text projectNameText = new Text(composite, SWT.BORDER);
    // gd = new GridData(GridData.FILL_HORIZONTAL);
    // gd.horizontalSpan = ncol - 1;
    // fromText.setLayoutData(gd);

    // Similar for Destination ...
    // createLine(composite, ncol);

    // Travel by plane
    // Button planeButton = new Button(composite, SWT.RADIO);
    // planeButton.setText("Take a plane");
    // gd = new GridData(GridData.FILL_HORIZONTAL);
    // gd.horizontalSpan = ncol;
    // planeButton.setLayoutData(gd);
    // planeButton.setSelection(true);

    // set the composite as the control for this page
    setControl(composite);

  }

  @Override
  public void handleEvent(Event event) {
    if (event.widget == browseButton) {
      JFileChooser chooser = new JFileChooser();
      chooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);      
      int returnVal = 0; //= chooser.showOpenDialog();
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File selectedFolder = chooser.getSelectedFile();
        identifierText.setText(selectedFolder.getPath());        
      } 
    }
  }

}
