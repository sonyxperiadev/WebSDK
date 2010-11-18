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

package com.sonyericsson.web.sdk.view;

import com.sonyericsson.web.sdk.model.AndroidApplicationModel;
import java.awt.CardLayout;
import java.awt.Component;

import java.util.logging.Logger;
import org.jdesktop.application.Action;

import com.sonyericsson.web.sdk.model.Settings;
import com.sonyericsson.web.sdk.resources.Config;
import com.sonyericsson.web.sdk.resources.Resources;
import com.sonyericsson.web.sdk.utils.FileHandler;
import com.sonyericsson.web.sdk.utils.RegistryHandler;
import com.sun.awt.AWTUtilities;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.JFrame;

/**
 * setup wizard for configuring paths to extenal tools 
 */

public class SetupView extends JFrame implements PanelListener {

  public static final String DOWNLOAD_AND_INSTALL_TO = "Download and install to: ";
  public static final String LOCATED_IN = "Located in: ";
  public static final String DONT_INSTALL = "Don\'t install";
  private Component currentCard;
  private final SetupController setupController;
  private final Settings settings;
  private SetupJDKPanel jdkPanel;
  private SetupAndroidPanel androidPanel;
  private SetupPluginPanel pluginPanel;
  private SetupPhonegapPanel phonegapPanel;
  private SetupPleaseWaitPanel pleaseWaitPanel;
  private SetupResultPanel resultPanel;
  private SetupFinishedPanel finishedPanel;

  /** Creates new form SetupView */
  public SetupView(Settings settings) {

    setUndecorated(true);
    try {
      AWTUtilities.setWindowOpaque(this, false);
    } catch (Throwable t) {
    }
    setIconImage(Resources.getLogo());
    setTitle(Config.NAME);
    this.settings = settings;
    initComponents();
    nextButton.setVisible(false);
    backButton.setVisible(false);
    welcomePanel.setPanelListener(this);
    cardPanel.add(welcomePanel, "Welcome");

    jdkPanel = new SetupJDKPanel(settings);
    jdkPanel.setPanelListener(this);
    if(!isToolsAvailable())
    {
        cardPanel.add(jdkPanel, "JDK");
    }

    androidPanel = new SetupAndroidPanel(settings);
    androidPanel.setPanelListener(this);
    cardPanel.add(androidPanel, "Android");
    pluginPanel = new SetupPluginPanel();
    pluginPanel.setPanelListener(this);
    if(!System.getProperty("os.name").equals("Mac OS X"))
    {
      cardPanel.add(pluginPanel, "Plugin");
    }
    phonegapPanel = new SetupPhonegapPanel(settings);
    phonegapPanel.setPanelListener(this);
    cardPanel.add(phonegapPanel, "PhoneGap");
    resultPanel = new SetupResultPanel(this);
    resultPanel.setPanelListener(this);
    cardPanel.add(resultPanel, "Result");
    pleaseWaitPanel = new SetupPleaseWaitPanel();
    pleaseWaitPanel.setPanelListener(this);
    cardPanel.add(pleaseWaitPanel, "PleaseWait");
    finishedPanel = new SetupFinishedPanel();
    finishedPanel.setPanelListener(this);
    cardPanel.add(finishedPanel, "Finished");
    //setSize(800, 600);
    ViewHelper.putWindowInCenter(this);


    setupController = new SetupController();

    DragWindowAdapter dwa = new DragWindowAdapter(this);
    topPanel.addMouseListener(dwa);
    topPanel.addMouseMotionListener(dwa);
  }

  public String getJDKPanelPath() {
    return jdkPanel.getPath();
  }

  public String getAndroidPanelPath() {
    return androidPanel.getPath();
  }

  public boolean shouldPluginBeInstalled() {
    return pluginPanel.shouldPluginBeInstalled();
  }

  public String getPhonegapPanelPath() {
    return phonegapPanel.getPath();
  }

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

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        installPanel = new com.sonyericsson.web.sdk.view.panel.RoundedRectanglePanel();
        topPanel = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        windowControlPanel = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cardPanel = new javax.swing.JPanel();
        welcomePanel = new com.sonyericsson.web.sdk.view.SetupWelcomePanel();
        buttonPanel = new javax.swing.JPanel();
        cancelButton1 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        rightButtonPanel = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(600, 400));
        setName("Form"); // NOI18N
        setResizable(false);

        installPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        installPanel.setMaximumSize(new java.awt.Dimension(600, 400));
        installPanel.setMinimumSize(new java.awt.Dimension(600, 400));
        installPanel.setName("installPanel"); // NOI18N
        installPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        installPanel.setLayout(new java.awt.BorderLayout());

        topPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 5, 5));
        topPanel.setName("topPanel"); // NOI18N
        topPanel.setOpaque(false);
        topPanel.setPreferredSize(new java.awt.Dimension(40, 40));
        topPanel.setLayout(new java.awt.BorderLayout());

        jSeparator1.setName("jSeparator1"); // NOI18N
        topPanel.add(jSeparator1, java.awt.BorderLayout.PAGE_END);

        windowControlPanel.setName("windowControlPanel"); // NOI18N
        windowControlPanel.setOpaque(false);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(SetupView.class, this);
        jButton4.setAction(actionMap.get("minimize")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(SetupView.class);
        jButton4.setIcon(resourceMap.getIcon("jButton4.icon")); // NOI18N
        jButton4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setFocusPainted(false);
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setPressedIcon(resourceMap.getIcon("jButton4.pressedIcon")); // NOI18N
        jButton4.setRolloverIcon(resourceMap.getIcon("jButton4.rolloverIcon")); // NOI18N
        windowControlPanel.add(jButton4);

        jButton2.setAction(actionMap.get("quit")); // NOI18N
        jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setBorder(null);
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setFocusPainted(false);
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setPressedIcon(resourceMap.getIcon("jButton2.pressedIcon")); // NOI18N
        jButton2.setRolloverIcon(resourceMap.getIcon("jButton2.rolloverIcon")); // NOI18N
        windowControlPanel.add(jButton2);

        topPanel.add(windowControlPanel, java.awt.BorderLayout.LINE_END);

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setForeground(resourceMap.getColor("jLabel1.foreground")); // NOI18N
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setIconTextGap(10);
        jLabel1.setName("jLabel1"); // NOI18N
        topPanel.add(jLabel1, java.awt.BorderLayout.CENTER);

        installPanel.add(topPanel, java.awt.BorderLayout.PAGE_START);

        cardPanel.setName("cardPanel"); // NOI18N
        cardPanel.setOpaque(false);
        cardPanel.setLayout(new java.awt.CardLayout());

        welcomePanel.setBackground(resourceMap.getColor("welcomePanel.background")); // NOI18N
        welcomePanel.setName("welcomePanel"); // NOI18N
        cardPanel.add(welcomePanel, "card2");

        installPanel.add(cardPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel.setName("buttonPanel"); // NOI18N
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new java.awt.BorderLayout());

        cancelButton1.setAction(actionMap.get("cancelWizard")); // NOI18N
        cancelButton1.setIcon(resourceMap.getIcon("cancelButton1.icon")); // NOI18N
        cancelButton1.setText(resourceMap.getString("cancelButton1.text")); // NOI18N
        cancelButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 5));
        cancelButton1.setBorderPainted(false);
        cancelButton1.setContentAreaFilled(false);
        cancelButton1.setFocusPainted(false);
        cancelButton1.setName("cancelButton1"); // NOI18N
        cancelButton1.setPressedIcon(resourceMap.getIcon("cancelButton1.pressedIcon")); // NOI18N
        cancelButton1.setRolloverIcon(resourceMap.getIcon("cancelButton1.rolloverIcon")); // NOI18N
        buttonPanel.add(cancelButton1, java.awt.BorderLayout.LINE_START);

        jSeparator2.setName("jSeparator2"); // NOI18N
        buttonPanel.add(jSeparator2, java.awt.BorderLayout.PAGE_START);

        rightButtonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 5));
        rightButtonPanel.setName("rightButtonPanel"); // NOI18N
        rightButtonPanel.setOpaque(false);
        rightButtonPanel.setLayout(new java.awt.BorderLayout());

        backButton.setAction(actionMap.get("previousCard")); // NOI18N
        backButton.setIcon(resourceMap.getIcon("backButton.icon")); // NOI18N
        backButton.setText(resourceMap.getString("backButton.text")); // NOI18N
        backButton.setBorder(null);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setName("backButton"); // NOI18N
        backButton.setPressedIcon(resourceMap.getIcon("backButton.pressedIcon")); // NOI18N
        backButton.setRolloverIcon(resourceMap.getIcon("backButton.rolloverIcon")); // NOI18N
        rightButtonPanel.add(backButton, java.awt.BorderLayout.LINE_START);

        nextButton.setAction(actionMap.get("nextCard")); // NOI18N
        nextButton.setIcon(resourceMap.getIcon("nextButton.icon")); // NOI18N
        nextButton.setText(resourceMap.getString("nextButton.text")); // NOI18N
        nextButton.setBorder(null);
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setFocusPainted(false);
        nextButton.setName("nextButton"); // NOI18N
        nextButton.setPressedIcon(resourceMap.getIcon("nextButton.pressedIcon")); // NOI18N
        nextButton.setRolloverIcon(resourceMap.getIcon("nextButton.rolloverIcon")); // NOI18N
        rightButtonPanel.add(nextButton, java.awt.BorderLayout.LINE_END);

        buttonPanel.add(rightButtonPanel, java.awt.BorderLayout.LINE_END);

        installPanel.add(buttonPanel, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(installPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

  @Action
  public void nextCard() {
    System.out.println("nextCard");
    if (currentCard != null && currentCard.equals(resultPanel)) {
      CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
      cardLayout.next(cardPanel);
      nextButton.setVisible(false);
      backButton.setVisible(false);
      cancelButton1.setVisible(false);
      Thread newThread = new Thread() {

        @Override
        public void run() {
          setupController.setup();
          setupController.restart();
        }
      };
      newThread.start();
    } else if (currentCard != null && currentCard.equals(finishedPanel)) {
      //setupController.restart();
    } else {
      resultPanel.updateInfo();
      CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
      cardLayout.next(cardPanel);
    }
  }

  @Action
  public void previousCard() {
    CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
    cardLayout.previous(cardPanel);
  }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton1;
    private javax.swing.JPanel cardPanel;
    private com.sonyericsson.web.sdk.view.panel.RoundedRectanglePanel installPanel;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton nextButton;
    private javax.swing.JPanel rightButtonPanel;
    private javax.swing.JPanel topPanel;
    private com.sonyericsson.web.sdk.view.SetupWelcomePanel welcomePanel;
    private javax.swing.JPanel windowControlPanel;
    // End of variables declaration//GEN-END:variables

  public void changed(Component component) {
    if (component instanceof AbstractPanel) {
      currentCard = component;
      nextButton.setVisible(((AbstractPanel) component).isNextVisible());
      backButton.setVisible(((AbstractPanel) component).isBackVisible());
      nextButton.setEnabled(((AbstractPanel) component).isNextEnabled());
      backButton.setEnabled(((AbstractPanel) component).isBackEnabled());
    }
  }
  // End of variables declaration

  private class SetupController {

    private void setup() {


    if (!isToolsAvailable()) {
        copyJdkTools(jdkPanel.getPath());
    }

      if (pluginPanel.shouldPluginBeInstalled()) {
        installPlugin();
      }
      AddSetupPathsToRegistry();
      settings.setConfigured(true);

      CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
      cardLayout.show(cardPanel, "Finished");
    }

    public void installPlugin() {
      File plugin = new File(RegistryHandler.getInstallDir() + "eclipse plugin/WebSDK_Plugin_1.0.0.jar");
      File targetDir = new File(pluginPanel.getEclipsePath() + "/plugins/");
      if (targetDir.exists() && targetDir.isDirectory()) {
        try {
          FileHandler fh = new FileHandler();
          fh.copyFileToDir(plugin, targetDir);
        } catch (IOException ex) {
          Logger.getLogger(SetupView.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

    public void copyJdkTools(String jdkPath) {
      File targetDir = new File(settings.getInstallDirectory() + "Resources\\lib");
      File tools_jar = new File(jdkPath + "\\lib\\tools.jar");
      
      if (tools_jar.exists() && targetDir.exists()) {
        try {
          new FileHandler().copyFileToDir(tools_jar, targetDir);
        } catch (IOException ex) {
          java.util.logging.Logger.getLogger(AndroidApplicationModel.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

    public void AddSetupPathsToRegistry() {
      String jdkPath = "";
      if(!isToolsAvailable())
      {
          jdkPath = jdkPanel.getPath();
      }
      String androidPath = androidPanel.getPath();
      String phonegapPath = phonegapPanel.getPath();
      RegistryHandler.setAndroidSDKPath(androidPath);
      RegistryHandler.setJDKPath(jdkPath);
      RegistryHandler.setPhonegapPath(phonegapPath);
    }

    public void restart() {
      try {
      if(System.getProperty("os.name").equals("Mac OS X"))
	  {
        String command = new File(RegistryHandler.getInstallDir() + "../../MacOS/JavaApplicationStub").getCanonicalPath();
        System.out.println("Command: "+command);
        Runtime.getRuntime().exec(command);
        System.exit(0);
	  }
	  else
	  {
        String path = settings.getSourceDir().toString();
        String commands[] = {
            "java", "-jar",
            RegistryHandler.getInstallDir() + "Resources/WebSDK.jar",
            "-path",
            path
        };
        System.out.println("Command: java -jar " + RegistryHandler.getInstallDir() + "Resources/WebSDK.jar -path " + path);
        Runtime.getRuntime().exec(commands);
        System.exit(0);
	  }
      } catch (IOException ex) {
        //java.util.logging.Logger.getLogger(WebSDK.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  @Action
  public void minimize() {
    int extendedState = getExtendedState();
    setExtendedState(extendedState | JFrame.ICONIFIED);
  }
}
