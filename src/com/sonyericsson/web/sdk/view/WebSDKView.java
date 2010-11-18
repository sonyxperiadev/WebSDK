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

import com.sonyericsson.web.sdk.model.AndroidKeys;
import com.sonyericsson.web.sdk.model.Targets;
import com.sonyericsson.web.sdk.view.panel.SourceTreePanel;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.TransferHandler;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.FrameView;

import com.sonyericsson.web.sdk.model.ApplicationModel;
import com.sonyericsson.web.sdk.model.Settings;
import com.sonyericsson.web.sdk.resources.Config;
import com.sonyericsson.web.sdk.resources.Resources;
import com.sun.awt.AWTUtilities;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.HashMap;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * The application's main frame.
 */
public class WebSDKView extends FrameView implements PanelListener, ActionListener {

  private AndroidPanel androidPanel;
  private boolean nameOk;
  private Settings settings;
  private final Hashtable<String, ApplicationModel> applicationModels;
  private File iconPath;
  private String defaultIconPath;
  private boolean installEnabled;
  private boolean createEnabled;
  private String sourcePath;
  private boolean validSourceFile;
  private boolean packageOk;
  private boolean projectOk;
  private SourceTreePanel sourceTreePanel;
  private String outputPath;
  private int output;
  static HashMap<String, Sample> samples = new HashMap<String, Sample>();
  /*
   * Hide RSS Reader
  private String rssURL;
   */

  public WebSDKView(Application app, Settings settings, Hashtable<String, ApplicationModel> applicationModels) {
    super(app);
    this.settings = settings;
    this.applicationModels = applicationModels;
    String samplesDirectory;
    if(System.getProperty("os.name").equals("Mac OS X"))
	{
       samplesDirectory = settings.getInstallDirectory()+"../../../../Samples";
	}
	else
	{
	   samplesDirectory = settings.getInstallDirectory()+"/Samples";
	}

    try {
      Sample newSample = new Sample("Mavericks", "com.sonyericsson.demo.mavericks", new File(samplesDirectory + "/Mavericks").getCanonicalPath());
      samples.put(newSample.name, newSample);
      newSample = new Sample("Phonegap Demo", "com.phonegap.demo", new File(samplesDirectory + "/Phonegap Demo").getCanonicalPath());
      samples.put(newSample.name, newSample);
      newSample = new Sample("Pigeon Twitter", "com.sonyericsson.demo.pigeon", new File(samplesDirectory + "/Pigeon Twitter").getCanonicalPath());
      samples.put(newSample.name, newSample);
    } catch (IOException ex) {
      Logger.getLogger(WebSDKView.class.getName()).log(Level.SEVERE, null, ex);
    }

    outputPath = settings.getOutputPath();
    sourcePath = settings.getSourcePath();

    defaultIconPath = settings.getDefaultIconPath();
    iconPath = new File(defaultIconPath);

    getFrame().setIconImage(Resources.getLogo());
    getFrame().setTitle(Config.NAME);
    getFrame().setUndecorated(true);
    getFrame().setSize(1232, 799);

    initComponents();

    for (Sample sample : samples.values()) {
      final JButton button = new JButton();
      button.setText(sample.name);
      button.setName(sample.name);
      button.addActionListener(this);
      button.setContentAreaFilled(false);
      button.setFocusPainted(false);
      button.setFont(new Font("Tahoma", Font.PLAIN, 12));
      button.addMouseListener(new MouseAdapter() {

                @Override
        public void mouseEntered(MouseEvent e) {
          button.setForeground(new Color(0, 168, 181));
        }

                @Override
        public void mouseExited(MouseEvent e) {
          button.setForeground(null);
        }
      });
      sampleApplicationsPanel.add(button);
    }

    androidPanel = new AndroidPanel(getFrame(), settings, this);
    rightCardPanel.add(androidPanel, "Android");

    DragWindowAdapter dwa = new DragWindowAdapter(getFrame());
    topFramePanel.addMouseListener(dwa);
    topFramePanel.addMouseMotionListener(dwa);
    ResizeWindowAdapter rsa = new ResizeWindowAdapter(getFrame());
    resizeLabel.addMouseListener(rsa);
    resizeLabel.addMouseMotionListener(rsa);

    try {
      AWTUtilities.setWindowOpaque(getFrame(), false);
    } catch (Throwable t) {
    }
    topFramePanel.setImage(Resources.getTopFrame());
    leftFramePanel.setImage(Resources.getLeftFrame());
    rightFramePanel.setImage(Resources.getRightFrame());
    bottomFramePanel.setImage(Resources.getBottomFrame());

    output = settings.getOutput();

    switch (output) {
      case Settings.CUSTOM_FOLDER:
        selectedRadioButton.setSelected(true);
        break;
      case Settings.CURRENT_FOLDER:
      default:
        currentRadioButton.setSelected(true);
        break;
    }
    outputTextField.setEnabled(selectedRadioButton.isSelected());
    outputBrowseButton.setEnabled(selectedRadioButton.isSelected());

    getFrame().setTransferHandler(new SourceTransferHandler());

    TransferHandler parentHandler = sourceTextField.getTransferHandler();

    sourceTextField.setTransferHandler(new SourceTransferHandler(parentHandler));
    String appName = settings.getApplicationName();
    if (appName.equals("") || appName == null) {
        appName = "DemoApp";
        settings.setApplicationName(appName);
    }
    appNameField.setText(appName);
    String packageName = settings.getPackageName();
    if (packageName.equals("") || packageName == null) {
        packageName = "com.sonyericsson.web.demo";
        settings.setPackageName(packageName);
    }
    packageField.setText(packageName);
    outputTextField.setText(outputPath);

    try {
      setIcon(iconPath);
    } catch (MalformedURLException ex) {
      Logger.getLogger(WebSDKView.class.getName()).log(Level.SEVERE, null, ex);
    }

    setSourcePath(new File(settings.getSourceFolder()));
    sourceTextField.setText(sourcePath);
    updateButtons();

    androidButton.setIcon(getAndroidIcon());
    androidButton.setSelectedIcon(getAndroidSelectedIcon());
    androidButton.setRolloverIcon(getAndroidHoverIcon());
    initTree();

    /*
     * Hide RSS Reader
     startRSSReader();
     */

    getFrame().pack();
  }

  private void initTree() {
    sourceTreePanel = new SourceTreePanel(sourcePath);
    sourceTreePanel.setOpaque(false);
    sourceTreePanel.setBorder(null);
    folderPanel.add(sourceTreePanel, BorderLayout.CENTER);
  }

  @Action
  public void defaultIcon() {
    try {
      iconPath = new File(defaultIconPath);
      setIcon(iconPath);
    } catch (MalformedURLException ex) {
      Logger.getLogger(WebSDKView.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void checkName() {
    String appName = appNameField.getText();
    if (appName == null || appName.equals("")) {
      nameOk = false;
      chooseNameLabel.setForeground(Color.red);
    } else {
      nameOk = true;
      chooseNameLabel.setForeground(null);
    }
  }

  private void checkPackage() {
    String packageName = packageField.getText();
    if (packageName == null || packageName.contains(" ") || packageName.equals("")) {
      packageOk = false;
      packageLabel.setForeground(Color.red);
    } else {
      packageOk = true;
      packageLabel.setForeground(null);
    }
  }

  private Icon getAndroidIcon() {

    if (androidPanel.isCreateable() && androidPanel.isInstallable()) {
      return Resources.getAndroidIcon();
    }
    return Resources.getAndroidWarningIcon();
  }

  private Icon getAndroidHoverIcon() {

    if (androidPanel.isCreateable() && androidPanel.isInstallable()) {
      return Resources.getAndroidHoverIcon();
    }
    return Resources.getAndroidWarningHoverIcon();
  }

  private Icon getAndroidSelectedIcon() {

    if (androidPanel.isCreateable() && androidPanel.isInstallable()) {
      return Resources.getAndroidSelectedIcon();
    }
    return Resources.getAndroidWarningSelectedIcon();
  }

  /**
   * This method is called from within the constructor to initialize
   * the form. WARNING: Do NOT modify this code. The content of this
   * method is always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed"
  // <editor-fold defaultstate="collapsed"
  // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainFrame = new javax.swing.JFrame();
        backgroundPanel = new javax.swing.JPanel();
        mainFramePanel = new javax.swing.JPanel();
        splitPane = new javax.swing.JSplitPane();
        leftPanel = new javax.swing.JPanel();
        leftCardPanel = new javax.swing.JPanel();
        mainPanel = new com.sonyericsson.web.sdk.view.panel.RoundedRectanglePanel();
        valuePanel = new javax.swing.JPanel();
        applicationPanel = new javax.swing.JPanel();
        namePanel = new javax.swing.JPanel();
        chooseNameLabel = new javax.swing.JLabel();
        appNameField = new javax.swing.JTextField();
        packagePanel = new javax.swing.JPanel();
        packageField = new javax.swing.JTextField();
        packageLabel = new javax.swing.JLabel();
        iconPanel = new javax.swing.JPanel();
        chooseIconLabel = new javax.swing.JLabel();
        iconImagePanel = new com.sonyericsson.web.sdk.view.panel.ImagePanel();
        iconButtonPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        browseButton = new javax.swing.JButton();
        lowerPanel = new javax.swing.JPanel();
        platformPanel = new javax.swing.JPanel();
        platformLabel = new javax.swing.JLabel();
        androidCheckBox = new javax.swing.JCheckBox();
        symbianCheckBox = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        sampleApplicationsPanel = new com.sonyericsson.web.sdk.view.panel.RoundedRectanglePanel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        rightPanel = new javax.swing.JPanel();
        rightCardPanel = new com.sonyericsson.web.sdk.view.panel.RoundedRectanglePanel();
        folderPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        outputPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        outputLabel = new javax.swing.JLabel();
        currentRadioButton = new javax.swing.JRadioButton();
        selectedRadioButton = new javax.swing.JRadioButton();
        outputTextField = new javax.swing.JTextField();
        outputBrowseButton = new javax.swing.JButton();
        sourcePanel = new javax.swing.JPanel();
        sourceTextField = new javax.swing.JTextField();
        sourceLabel = new javax.swing.JLabel();
        sourceBrowseButton = new javax.swing.JButton();
        topFramePanel = new com.sonyericsson.web.sdk.view.panel.ImagePanel();
        jLabel1 = new javax.swing.JLabel();
        windowControlPanel = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        leftFramePanel = new com.sonyericsson.web.sdk.view.panel.ImagePanel();
        jPanel2 = new javax.swing.JPanel();
        wizardButton = new javax.swing.JButton();
        sampleAppsToggleButton = new javax.swing.JToggleButton();
        bottomFramePanel = new com.sonyericsson.web.sdk.view.panel.ImagePanel();
        buttonPanel = new javax.swing.JPanel();
        rightButtonPanel = new javax.swing.JPanel();
        previewButton = new javax.swing.JButton();
        createButton = new javax.swing.JButton();
        installButton = new javax.swing.JButton();
        exitlButton = new javax.swing.JButton();
        rssLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        resizeLabel = new javax.swing.JLabel();
        rightFramePanel = new com.sonyericsson.web.sdk.view.panel.ImagePanel();
        jPanel1 = new javax.swing.JPanel();
        folderButton = new javax.swing.JRadioButton();
        androidButton = new javax.swing.JRadioButton();
        optionsButtonGroup = new javax.swing.ButtonGroup();
        outputButtonGroup = new javax.swing.ButtonGroup();

        mainFrame.setMinimumSize(new java.awt.Dimension(600, 400));
        mainFrame.setName("mainFrame"); // NOI18N

        backgroundPanel.setName("backgroundPanel"); // NOI18N
        backgroundPanel.setOpaque(false);
        backgroundPanel.setLayout(new java.awt.BorderLayout());

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(WebSDKView.class);
        mainFramePanel.setBackground(resourceMap.getColor("mainFramePanel.background")); // NOI18N
        mainFramePanel.setName("mainFramePanel"); // NOI18N
        mainFramePanel.setLayout(new java.awt.CardLayout());

        splitPane.setBackground(resourceMap.getColor("splitPane.background")); // NOI18N
        splitPane.setBorder(null);
        splitPane.setDividerLocation(450);
        splitPane.setDividerSize(2);
        splitPane.setForeground(resourceMap.getColor("splitPane.foreground")); // NOI18N
        splitPane.setName("splitPane"); // NOI18N
        splitPane.setOpaque(false);

        leftPanel.setName("leftPanel"); // NOI18N
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new java.awt.BorderLayout());

        leftCardPanel.setName("leftCardPanel"); // NOI18N
        leftCardPanel.setOpaque(false);
        leftCardPanel.setLayout(new java.awt.CardLayout());

        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setMinimumSize(new java.awt.Dimension(350, 350));
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(450, 450));
        mainPanel.setLayout(new java.awt.BorderLayout());

        valuePanel.setName("valuePanel"); // NOI18N
        valuePanel.setOpaque(false);
        valuePanel.setLayout(new java.awt.BorderLayout());

        applicationPanel.setName("applicationPanel"); // NOI18N
        applicationPanel.setOpaque(false);
        applicationPanel.setLayout(new javax.swing.BoxLayout(applicationPanel, javax.swing.BoxLayout.Y_AXIS));

        namePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        namePanel.setName("namePanel"); // NOI18N
        namePanel.setOpaque(false);
        namePanel.setLayout(new java.awt.BorderLayout(10, 0));

        chooseNameLabel.setFont(resourceMap.getFont("sourceLabel.font")); // NOI18N
        chooseNameLabel.setForeground(resourceMap.getColor("platformLabel.foreground")); // NOI18N
        chooseNameLabel.setText(resourceMap.getString("chooseNameLabel.text")); // NOI18N
        chooseNameLabel.setToolTipText(resourceMap.getString("chooseNameLabel.toolTipText")); // NOI18N
        chooseNameLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chooseNameLabel.setName("chooseNameLabel"); // NOI18N
        namePanel.add(chooseNameLabel, java.awt.BorderLayout.PAGE_START);

        appNameField.setFont(resourceMap.getFont("sourceTextField.font")); // NOI18N
        appNameField.setText(resourceMap.getString("appNameField.text")); // NOI18N
        appNameField.setToolTipText(resourceMap.getString("appNameField.toolTipText")); // NOI18N
        appNameField.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        appNameField.setDragEnabled(true);
        appNameField.setMargin(new java.awt.Insets(0, 0, 0, 0));
        appNameField.setName("appNameField"); // NOI18N
        appNameField.setPreferredSize(new java.awt.Dimension(30, 30));
        appNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                appNameFieldKeyReleased(evt);
            }
        });
        namePanel.add(appNameField, java.awt.BorderLayout.CENTER);

        applicationPanel.add(namePanel);

        packagePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        packagePanel.setName("packagePanel"); // NOI18N
        packagePanel.setOpaque(false);
        packagePanel.setLayout(new java.awt.BorderLayout(10, 0));

        packageField.setFont(resourceMap.getFont("sourceTextField.font")); // NOI18N
        packageField.setText(resourceMap.getString("packageField.text")); // NOI18N
        packageField.setToolTipText(resourceMap.getString("packageField.toolTipText")); // NOI18N
        packageField.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        packageField.setDragEnabled(true);
        packageField.setMargin(new java.awt.Insets(0, 0, 0, 0));
        packageField.setName("packageField"); // NOI18N
        packageField.setPreferredSize(new java.awt.Dimension(30, 30));
        packageField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                packageFieldKeyReleased(evt);
            }
        });
        packagePanel.add(packageField, java.awt.BorderLayout.CENTER);

        packageLabel.setFont(resourceMap.getFont("sourceLabel.font")); // NOI18N
        packageLabel.setForeground(resourceMap.getColor("platformLabel.foreground")); // NOI18N
        packageLabel.setLabelFor(packageField);
        packageLabel.setText(resourceMap.getString("packageLabel.text")); // NOI18N
        packageLabel.setToolTipText(resourceMap.getString("packageLabel.toolTipText")); // NOI18N
        packageLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        packageLabel.setName("packageLabel"); // NOI18N
        packagePanel.add(packageLabel, java.awt.BorderLayout.PAGE_START);

        applicationPanel.add(packagePanel);

        valuePanel.add(applicationPanel, java.awt.BorderLayout.PAGE_START);

        iconPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 10, 5));
        iconPanel.setName("iconPanel"); // NOI18N
        iconPanel.setOpaque(false);
        iconPanel.setLayout(new java.awt.BorderLayout());

        chooseIconLabel.setFont(resourceMap.getFont("sourceLabel.font")); // NOI18N
        chooseIconLabel.setForeground(resourceMap.getColor("platformLabel.foreground")); // NOI18N
        chooseIconLabel.setText(resourceMap.getString("chooseIconLabel.text")); // NOI18N
        chooseIconLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chooseIconLabel.setName("chooseIconLabel"); // NOI18N
        iconPanel.add(chooseIconLabel, java.awt.BorderLayout.PAGE_START);

        iconImagePanel.setToolTipText(resourceMap.getString("iconImagePanel.toolTipText")); // NOI18N
        iconImagePanel.setName("iconImagePanel"); // NOI18N
        iconImagePanel.setPreferredSize(new java.awt.Dimension(237, 291));

        javax.swing.GroupLayout iconImagePanelLayout = new javax.swing.GroupLayout(iconImagePanel);
        iconImagePanel.setLayout(iconImagePanelLayout);
        iconImagePanelLayout.setHorizontalGroup(
            iconImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 185, Short.MAX_VALUE)
        );
        iconImagePanelLayout.setVerticalGroup(
            iconImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 194, Short.MAX_VALUE)
        );

        iconPanel.add(iconImagePanel, java.awt.BorderLayout.CENTER);

        iconButtonPanel.setName("iconButtonPanel"); // NOI18N
        iconButtonPanel.setOpaque(false);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(WebSDKView.class, this);
        jButton1.setAction(actionMap.get("defaultIcon")); // NOI18N
        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setToolTipText(resourceMap.getString("jButton1.toolTipText")); // NOI18N
        jButton1.setBorder(null);
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setPressedIcon(resourceMap.getIcon("jButton1.pressedIcon")); // NOI18N
        jButton1.setRolloverIcon(resourceMap.getIcon("jButton1.rolloverIcon")); // NOI18N
        iconButtonPanel.add(jButton1);
        jButton1.getAccessibleContext().setAccessibleName(resourceMap.getString("jButton1.AccessibleContext.accessibleName")); // NOI18N

        browseButton.setAction(actionMap.get("browseIcon")); // NOI18N
        browseButton.setIcon(resourceMap.getIcon("browseButton.icon")); // NOI18N
        browseButton.setText(resourceMap.getString("browseButton.text")); // NOI18N
        browseButton.setToolTipText(resourceMap.getString("browseButton.toolTipText")); // NOI18N
        browseButton.setBorder(null);
        browseButton.setBorderPainted(false);
        browseButton.setContentAreaFilled(false);
        browseButton.setFocusPainted(false);
        browseButton.setName("browseButton"); // NOI18N
        browseButton.setPressedIcon(resourceMap.getIcon("browseButton.pressedIcon")); // NOI18N
        browseButton.setRolloverIcon(resourceMap.getIcon("browseButton.rolloverIcon")); // NOI18N
        iconButtonPanel.add(browseButton);

        iconPanel.add(iconButtonPanel, java.awt.BorderLayout.LINE_END);

        valuePanel.add(iconPanel, java.awt.BorderLayout.CENTER);

        lowerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        lowerPanel.setName("lowerPanel"); // NOI18N
        lowerPanel.setOpaque(false);
        lowerPanel.setPreferredSize(new java.awt.Dimension(134, 134));
        lowerPanel.setLayout(new java.awt.BorderLayout());

        platformPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        platformPanel.setName("platformPanel"); // NOI18N
        platformPanel.setOpaque(false);
        platformPanel.setPreferredSize(new java.awt.Dimension(140, 140));
        platformPanel.setLayout(new javax.swing.BoxLayout(platformPanel, javax.swing.BoxLayout.PAGE_AXIS));

        platformLabel.setFont(resourceMap.getFont("sourceLabel.font")); // NOI18N
        platformLabel.setForeground(resourceMap.getColor("platformLabel.foreground")); // NOI18N
        platformLabel.setText(resourceMap.getString("platformLabel.text")); // NOI18N
        platformLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        platformLabel.setName("platformLabel"); // NOI18N
        platformPanel.add(platformLabel);

        androidCheckBox.setFont(resourceMap.getFont("symbianCheckBox.font")); // NOI18N
        androidCheckBox.setForeground(resourceMap.getColor("platformLabel.foreground")); // NOI18N
        androidCheckBox.setText(resourceMap.getString("androidCheckBox.text")); // NOI18N
        androidCheckBox.setToolTipText(resourceMap.getString("androidCheckBox.toolTipText")); // NOI18N
        androidCheckBox.setFocusPainted(false);
        androidCheckBox.setIcon(resourceMap.getIcon("androidCheckBox.icon")); // NOI18N
        androidCheckBox.setName("androidCheckBox"); // NOI18N
        androidCheckBox.setOpaque(false);
        androidCheckBox.setSelectedIcon(resourceMap.getIcon("androidCheckBox.selectedIcon")); // NOI18N
        androidCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                androidCheckBoxStateChanged(evt);
            }
        });
        platformPanel.add(androidCheckBox);

        symbianCheckBox.setFont(resourceMap.getFont("symbianCheckBox.font")); // NOI18N
        symbianCheckBox.setForeground(resourceMap.getColor("platformLabel.foreground")); // NOI18N
        symbianCheckBox.setText(resourceMap.getString("symbianCheckBox.text")); // NOI18N
        symbianCheckBox.setToolTipText(resourceMap.getString("symbianCheckBox.toolTipText")); // NOI18N
        symbianCheckBox.setFocusPainted(false);
        symbianCheckBox.setIcon(resourceMap.getIcon("androidCheckBox.icon")); // NOI18N
        symbianCheckBox.setName("symbianCheckBox"); // NOI18N
        symbianCheckBox.setOpaque(false);
        symbianCheckBox.setSelectedIcon(resourceMap.getIcon("symbianCheckBox.selectedIcon")); // NOI18N
        symbianCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                symbianCheckBoxStateChanged(evt);
            }
        });
        platformPanel.add(symbianCheckBox);

        lowerPanel.add(platformPanel, java.awt.BorderLayout.CENTER);

        jSeparator2.setName("jSeparator2"); // NOI18N
        lowerPanel.add(jSeparator2, java.awt.BorderLayout.PAGE_START);

        valuePanel.add(lowerPanel, java.awt.BorderLayout.PAGE_END);

        mainPanel.add(valuePanel, java.awt.BorderLayout.CENTER);

        leftCardPanel.add(mainPanel, "Main");

        sampleApplicationsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sampleApplicationsPanel.setName("sampleApplicationsPanel"); // NOI18N
        sampleApplicationsPanel.setLayout(new javax.swing.BoxLayout(sampleApplicationsPanel, javax.swing.BoxLayout.PAGE_AXIS));

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jLabel2.setName("jLabel2"); // NOI18N
        sampleApplicationsPanel.add(jLabel2);

        jSeparator3.setMaximumSize(new java.awt.Dimension(32767, 10));
        jSeparator3.setName("jSeparator3"); // NOI18N
        sampleApplicationsPanel.add(jSeparator3);

        leftCardPanel.add(sampleApplicationsPanel, "SampleApplications");

        leftPanel.add(leftCardPanel, java.awt.BorderLayout.CENTER);

        splitPane.setLeftComponent(leftPanel);

        rightPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rightPanel.setName("rightPanel"); // NOI18N
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new java.awt.BorderLayout());

        rightCardPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightCardPanel.setName("rightCardPanel"); // NOI18N
        rightCardPanel.setLayout(new java.awt.CardLayout());

        folderPanel.setName("folderPanel"); // NOI18N
        folderPanel.setOpaque(false);
        folderPanel.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel3.setToolTipText(resourceMap.getString("jPanel3.toolTipText")); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.BorderLayout());

        jSeparator1.setName("jSeparator1"); // NOI18N
        jPanel3.add(jSeparator1, java.awt.BorderLayout.PAGE_START);

        outputPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        outputPanel.setToolTipText(resourceMap.getString("outputPanel.toolTipText")); // NOI18N
        outputPanel.setAlignmentX(0.37988827F);
        outputPanel.setName("outputPanel"); // NOI18N
        outputPanel.setOpaque(false);
        outputPanel.setLayout(new java.awt.BorderLayout());

        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setOpaque(false);
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        outputLabel.setFont(resourceMap.getFont("sourceLabel.font")); // NOI18N
        outputLabel.setForeground(resourceMap.getColor("platformLabel.foreground")); // NOI18N
        outputLabel.setText(resourceMap.getString("outputLabel.text")); // NOI18N
        outputLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        outputLabel.setName("outputLabel"); // NOI18N
        jPanel4.add(outputLabel);

        outputButtonGroup.add(currentRadioButton);
        currentRadioButton.setFont(resourceMap.getFont("currentRadioButton.font")); // NOI18N
        currentRadioButton.setText(resourceMap.getString("currentRadioButton.text")); // NOI18N
        currentRadioButton.setToolTipText(resourceMap.getString("currentRadioButton.toolTipText")); // NOI18N
        currentRadioButton.setFocusPainted(false);
        currentRadioButton.setIcon(resourceMap.getIcon("currentRadioButton.icon")); // NOI18N
        currentRadioButton.setName("currentRadioButton"); // NOI18N
        currentRadioButton.setOpaque(false);
        currentRadioButton.setSelectedIcon(resourceMap.getIcon("currentRadioButton.selectedIcon")); // NOI18N
        jPanel4.add(currentRadioButton);

        outputButtonGroup.add(selectedRadioButton);
        selectedRadioButton.setFont(resourceMap.getFont("currentRadioButton.font")); // NOI18N
        selectedRadioButton.setText(resourceMap.getString("selectedRadioButton.text")); // NOI18N
        selectedRadioButton.setToolTipText(resourceMap.getString("selectedRadioButton.toolTipText")); // NOI18N
        selectedRadioButton.setFocusPainted(false);
        selectedRadioButton.setIcon(resourceMap.getIcon("currentRadioButton.icon")); // NOI18N
        selectedRadioButton.setName("selectedRadioButton"); // NOI18N
        selectedRadioButton.setOpaque(false);
        selectedRadioButton.setSelectedIcon(resourceMap.getIcon("currentRadioButton.selectedIcon")); // NOI18N
        selectedRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                selectedRadioButtonItemStateChanged(evt);
            }
        });
        jPanel4.add(selectedRadioButton);

        outputPanel.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        outputTextField.setFont(resourceMap.getFont("sourceTextField.font")); // NOI18N
        outputTextField.setToolTipText(resourceMap.getString("outputTextField.toolTipText")); // NOI18N
        outputTextField.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        outputTextField.setMargin(new java.awt.Insets(0, 0, 0, 0));
        outputTextField.setName("outputTextField"); // NOI18N
        outputTextField.setPreferredSize(new java.awt.Dimension(30, 30));
        outputTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                outputTextFieldKeyReleased(evt);
            }
        });
        outputPanel.add(outputTextField, java.awt.BorderLayout.CENTER);

        outputBrowseButton.setAction(actionMap.get("browseOutputPath")); // NOI18N
        outputBrowseButton.setIcon(resourceMap.getIcon("sourceBrowseButton.icon")); // NOI18N
        outputBrowseButton.setText(resourceMap.getString("outputBrowseButton.text")); // NOI18N
        outputBrowseButton.setToolTipText(resourceMap.getString("outputBrowseButton.toolTipText")); // NOI18N
        outputBrowseButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        outputBrowseButton.setBorderPainted(false);
        outputBrowseButton.setContentAreaFilled(false);
        outputBrowseButton.setDisabledIcon(resourceMap.getIcon("sourceBrowseButton.disabledIcon")); // NOI18N
        outputBrowseButton.setDisabledSelectedIcon(resourceMap.getIcon("sourceBrowseButton.disabledSelectedIcon")); // NOI18N
        outputBrowseButton.setFocusPainted(false);
        outputBrowseButton.setName("outputBrowseButton"); // NOI18N
        outputBrowseButton.setPressedIcon(resourceMap.getIcon("outputBrowseButton.pressedIcon")); // NOI18N
        outputBrowseButton.setRolloverIcon(resourceMap.getIcon("sourceBrowseButton.rolloverIcon")); // NOI18N
        outputPanel.add(outputBrowseButton, java.awt.BorderLayout.LINE_END);

        jPanel3.add(outputPanel, java.awt.BorderLayout.PAGE_END);

        folderPanel.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        sourcePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        sourcePanel.setToolTipText(resourceMap.getString("sourcePanel.toolTipText")); // NOI18N
        sourcePanel.setName("sourcePanel"); // NOI18N
        sourcePanel.setOpaque(false);
        sourcePanel.setLayout(new java.awt.BorderLayout());

        sourceTextField.setFont(resourceMap.getFont("sourceTextField.font")); // NOI18N
        sourceTextField.setToolTipText(resourceMap.getString("sourceTextField.toolTipText")); // NOI18N
        sourceTextField.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        sourceTextField.setDragEnabled(true);
        sourceTextField.setMargin(new java.awt.Insets(0, 0, 0, 0));
        sourceTextField.setName("sourceTextField"); // NOI18N
        sourceTextField.setPreferredSize(new java.awt.Dimension(30, 30));
        sourceTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sourceTextFieldKeyReleased(evt);
            }
        });
        sourcePanel.add(sourceTextField, java.awt.BorderLayout.CENTER);

        sourceLabel.setFont(resourceMap.getFont("sourceLabel.font")); // NOI18N
        sourceLabel.setForeground(resourceMap.getColor("platformLabel.foreground")); // NOI18N
        sourceLabel.setText(resourceMap.getString("sourceLabel.text")); // NOI18N
        sourceLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        sourceLabel.setName("sourceLabel"); // NOI18N
        sourcePanel.add(sourceLabel, java.awt.BorderLayout.PAGE_START);

        sourceBrowseButton.setAction(actionMap.get("browseSourceFolder")); // NOI18N
        sourceBrowseButton.setIcon(resourceMap.getIcon("sourceBrowseButton.icon")); // NOI18N
        sourceBrowseButton.setText(resourceMap.getString("sourceBrowseButton.text")); // NOI18N
        sourceBrowseButton.setToolTipText(resourceMap.getString("sourceBrowseButton.toolTipText")); // NOI18N
        sourceBrowseButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        sourceBrowseButton.setBorderPainted(false);
        sourceBrowseButton.setContentAreaFilled(false);
        sourceBrowseButton.setDisabledIcon(resourceMap.getIcon("sourceBrowseButton.disabledIcon")); // NOI18N
        sourceBrowseButton.setDisabledSelectedIcon(resourceMap.getIcon("sourceBrowseButton.disabledSelectedIcon")); // NOI18N
        sourceBrowseButton.setFocusPainted(false);
        sourceBrowseButton.setName("sourceBrowseButton"); // NOI18N
        sourceBrowseButton.setPressedIcon(resourceMap.getIcon("sourceBrowseButton.pressedIcon")); // NOI18N
        sourceBrowseButton.setRolloverIcon(resourceMap.getIcon("sourceBrowseButton.rolloverIcon")); // NOI18N
        sourcePanel.add(sourceBrowseButton, java.awt.BorderLayout.LINE_END);

        folderPanel.add(sourcePanel, java.awt.BorderLayout.PAGE_START);

        rightCardPanel.add(folderPanel, "Folder");

        rightPanel.add(rightCardPanel, java.awt.BorderLayout.CENTER);

        splitPane.setRightComponent(rightPanel);

        mainFramePanel.add(splitPane, "Main");

        backgroundPanel.add(mainFramePanel, java.awt.BorderLayout.CENTER);

        topFramePanel.setName("topFramePanel"); // NOI18N
        topFramePanel.setPreferredSize(new java.awt.Dimension(46, 46));
        topFramePanel.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setForeground(resourceMap.getColor("platformLabel.foreground")); // NOI18N
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        jLabel1.setIconTextGap(10);
        jLabel1.setName("jLabel1"); // NOI18N
        topFramePanel.add(jLabel1, java.awt.BorderLayout.CENTER);

        windowControlPanel.setName("windowControlPanel"); // NOI18N
        windowControlPanel.setOpaque(false);

        jButton4.setAction(actionMap.get("minimize")); // NOI18N
        jButton4.setIcon(resourceMap.getIcon("jButton4.icon")); // NOI18N
        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setFocusPainted(false);
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setPressedIcon(resourceMap.getIcon("jButton4.pressedIcon")); // NOI18N
        jButton4.setRolloverIcon(resourceMap.getIcon("jButton4.rolloverIcon")); // NOI18N
        windowControlPanel.add(jButton4);

        jButton3.setAction(actionMap.get("maximize")); // NOI18N
        jButton3.setIcon(resourceMap.getIcon("jButton3.icon")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setFocusPainted(false);
        jButton3.setName("jButton3"); // NOI18N
        jButton3.setPressedIcon(resourceMap.getIcon("jButton3.pressedIcon")); // NOI18N
        jButton3.setRolloverIcon(resourceMap.getIcon("jButton3.rolloverIcon")); // NOI18N
        windowControlPanel.add(jButton3);

        jButton2.setAction(actionMap.get("exitWebSDK")); // NOI18N
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

        topFramePanel.add(windowControlPanel, java.awt.BorderLayout.LINE_END);

        backgroundPanel.add(topFramePanel, java.awt.BorderLayout.PAGE_START);

        leftFramePanel.setName("leftFramePanel"); // NOI18N
        leftFramePanel.setPreferredSize(new java.awt.Dimension(103, 103));

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.PAGE_AXIS));

        wizardButton.setAction(actionMap.get("runWizard")); // NOI18N
        wizardButton.setIcon(resourceMap.getIcon("wizardButton.icon")); // NOI18N
        wizardButton.setText(resourceMap.getString("wizardButton.text")); // NOI18N
        wizardButton.setBorder(null);
        wizardButton.setBorderPainted(false);
        wizardButton.setContentAreaFilled(false);
        wizardButton.setFocusPainted(false);
        wizardButton.setName("wizardButton"); // NOI18N
        wizardButton.setPressedIcon(resourceMap.getIcon("wizardButton.pressedIcon")); // NOI18N
        wizardButton.setRolloverIcon(resourceMap.getIcon("wizardButton.rolloverIcon")); // NOI18N
        jPanel2.add(wizardButton);

        sampleAppsToggleButton.setAction(actionMap.get("showSampleApplications")); // NOI18N
        sampleAppsToggleButton.setIcon(resourceMap.getIcon("sampleAppsToggleButton.icon")); // NOI18N
        sampleAppsToggleButton.setText(resourceMap.getString("sampleAppsToggleButton.text")); // NOI18N
        sampleAppsToggleButton.setBorder(null);
        sampleAppsToggleButton.setBorderPainted(false);
        sampleAppsToggleButton.setContentAreaFilled(false);
        sampleAppsToggleButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        sampleAppsToggleButton.setFocusPainted(false);
        sampleAppsToggleButton.setName("sampleAppsToggleButton"); // NOI18N
        sampleAppsToggleButton.setRolloverIcon(resourceMap.getIcon("sampleAppsToggleButton.rolloverIcon")); // NOI18N
        sampleAppsToggleButton.setRolloverSelectedIcon(resourceMap.getIcon("sampleAppsToggleButton.rolloverSelectedIcon")); // NOI18N
        sampleAppsToggleButton.setSelectedIcon(resourceMap.getIcon("sampleAppsToggleButton.selectedIcon")); // NOI18N
        jPanel2.add(sampleAppsToggleButton);

        leftFramePanel.add(jPanel2);

        backgroundPanel.add(leftFramePanel, java.awt.BorderLayout.LINE_START);

        bottomFramePanel.setMinimumSize(new java.awt.Dimension(68, 68));
        bottomFramePanel.setName("bottomFramePanel"); // NOI18N
        bottomFramePanel.setOpaque(false);
        bottomFramePanel.setPreferredSize(new java.awt.Dimension(69, 69));
        bottomFramePanel.setLayout(new java.awt.BorderLayout());

        buttonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0));
        buttonPanel.setName("buttonPanel"); // NOI18N
        buttonPanel.setOpaque(false);
        buttonPanel.setRequestFocusEnabled(false);
        buttonPanel.setLayout(new java.awt.BorderLayout());

        rightButtonPanel.setName("rightButtonPanel"); // NOI18N
        rightButtonPanel.setOpaque(false);
        rightButtonPanel.setLayout(new javax.swing.BoxLayout(rightButtonPanel, javax.swing.BoxLayout.LINE_AXIS));

        previewButton.setAction(actionMap.get("startSimulator")); // NOI18N
        previewButton.setIcon(resourceMap.getIcon("previewButton.icon")); // NOI18N
        previewButton.setText(resourceMap.getString("previewButton.text")); // NOI18N
        previewButton.setToolTipText(resourceMap.getString("previewButton.toolTipText")); // NOI18N
        previewButton.setBorder(null);
        previewButton.setContentAreaFilled(false);
        previewButton.setFocusPainted(false);
        previewButton.setName("previewButton"); // NOI18N
        previewButton.setPressedIcon(resourceMap.getIcon("previewButton.pressedIcon")); // NOI18N
        previewButton.setRolloverIcon(resourceMap.getIcon("previewButton.rolloverIcon")); // NOI18N
        rightButtonPanel.add(previewButton);

        createButton.setAction(actionMap.get("createApplication")); // NOI18N
        createButton.setIcon(resourceMap.getIcon("createButton.icon")); // NOI18N
        createButton.setText(resourceMap.getString("createButton.text")); // NOI18N
        createButton.setToolTipText(resourceMap.getString("createButton.toolTipText")); // NOI18N
        createButton.setBorder(null);
        createButton.setContentAreaFilled(false);
        createButton.setFocusPainted(false);
        createButton.setName("createButton"); // NOI18N
        createButton.setRolloverIcon(resourceMap.getIcon("createButton.rolloverIcon")); // NOI18N
        createButton.setRolloverSelectedIcon(resourceMap.getIcon("createButton.rolloverSelectedIcon")); // NOI18N
        rightButtonPanel.add(createButton);

        installButton.setAction(actionMap.get("installApplication")); // NOI18N
        installButton.setForeground(resourceMap.getColor("installButton.foreground")); // NOI18N
        installButton.setIcon(resourceMap.getIcon("installButton.icon")); // NOI18N
        installButton.setText(resourceMap.getString("installButton.text")); // NOI18N
        installButton.setToolTipText(resourceMap.getString("installButton.toolTipText")); // NOI18N
        installButton.setBorder(null);
        installButton.setBorderPainted(false);
        installButton.setContentAreaFilled(false);
        installButton.setFocusPainted(false);
        installButton.setName("installButton"); // NOI18N
        installButton.setRolloverIcon(resourceMap.getIcon("installButton.rolloverIcon")); // NOI18N
        rightButtonPanel.add(installButton);

        buttonPanel.add(rightButtonPanel, java.awt.BorderLayout.LINE_END);

        exitlButton.setAction(actionMap.get("exitWebSDK")); // NOI18N
        exitlButton.setIcon(resourceMap.getIcon("exitlButton.icon")); // NOI18N
        exitlButton.setText(resourceMap.getString("exitlButton.text")); // NOI18N
        exitlButton.setBorder(null);
        exitlButton.setBorderPainted(false);
        exitlButton.setContentAreaFilled(false);
        exitlButton.setFocusPainted(false);
        exitlButton.setName("exitlButton"); // NOI18N
        exitlButton.setPressedIcon(resourceMap.getIcon("exitlButton.pressedIcon")); // NOI18N
        exitlButton.setRolloverIcon(resourceMap.getIcon("exitlButton.rolloverIcon")); // NOI18N
        buttonPanel.add(exitlButton, java.awt.BorderLayout.LINE_START);

        rssLabel.setFont(resourceMap.getFont("rssLabel.font")); // NOI18N
        rssLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rssLabel.setText(resourceMap.getString("rssLabel.text")); // NOI18N
        rssLabel.setName("rssLabel"); // NOI18N
        rssLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rssLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                rssLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rssLabelMouseExited(evt);
            }
        });
        buttonPanel.add(rssLabel, java.awt.BorderLayout.PAGE_END);

        bottomFramePanel.add(buttonPanel, java.awt.BorderLayout.CENTER);

        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setOpaque(false);
        jPanel5.setLayout(new java.awt.BorderLayout());

        resizeLabel.setIcon(resourceMap.getIcon("resizeLabel.icon")); // NOI18N
        resizeLabel.setText(resourceMap.getString("resizeLabel.text")); // NOI18N
        resizeLabel.setName("resizeLabel"); // NOI18N
        resizeLabel.setPreferredSize(new java.awt.Dimension(30, 30));
        jPanel5.add(resizeLabel, java.awt.BorderLayout.PAGE_END);

        bottomFramePanel.add(jPanel5, java.awt.BorderLayout.LINE_END);

        backgroundPanel.add(bottomFramePanel, java.awt.BorderLayout.PAGE_END);

        rightFramePanel.setName("rightFramePanel"); // NOI18N
        rightFramePanel.setPreferredSize(new java.awt.Dimension(104, 104));

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.PAGE_AXIS));

        folderButton.setAction(actionMap.get("showFolder")); // NOI18N
        optionsButtonGroup.add(folderButton);
        folderButton.setSelected(true);
        folderButton.setText(resourceMap.getString("folderButton.text")); // NOI18N
        folderButton.setToolTipText(resourceMap.getString("folderButton.toolTipText")); // NOI18N
        folderButton.setBorder(null);
        folderButton.setContentAreaFilled(false);
        folderButton.setFocusPainted(false);
        folderButton.setIcon(resourceMap.getIcon("folderButton.icon")); // NOI18N
        folderButton.setName("folderButton"); // NOI18N
        folderButton.setRolloverIcon(resourceMap.getIcon("folderButton.rolloverIcon")); // NOI18N
        folderButton.setSelectedIcon(resourceMap.getIcon("folderButton.selectedIcon")); // NOI18N
        jPanel1.add(folderButton);

        androidButton.setAction(actionMap.get("showAndroid")); // NOI18N
        optionsButtonGroup.add(androidButton);
        androidButton.setText(resourceMap.getString("androidButton.text")); // NOI18N
        androidButton.setToolTipText(resourceMap.getString("androidButton.toolTipText")); // NOI18N
        androidButton.setBorder(null);
        androidButton.setContentAreaFilled(false);
        androidButton.setFocusPainted(false);
        androidButton.setIcon(resourceMap.getIcon("androidButton.icon")); // NOI18N
        androidButton.setName("androidButton"); // NOI18N
        androidButton.setPressedIcon(resourceMap.getIcon("androidButton.pressedIcon")); // NOI18N
        androidButton.setRolloverIcon(resourceMap.getIcon("androidButton.rolloverIcon")); // NOI18N
        jPanel1.add(androidButton);

        rightFramePanel.add(jPanel1);

        backgroundPanel.add(rightFramePanel, java.awt.BorderLayout.LINE_END);

        mainFrame.getContentPane().add(backgroundPanel, java.awt.BorderLayout.PAGE_START);

        setComponent(backgroundPanel);
    }// </editor-fold>//GEN-END:initComponents

  private void androidCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_androidCheckBoxStateChanged
    updateButtons();
  }//GEN-LAST:event_androidCheckBoxStateChanged

  private void symbianCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_symbianCheckBoxStateChanged
    updateButtons();
  }//GEN-LAST:event_symbianCheckBoxStateChanged

  private void appNameFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_appNameFieldKeyReleased
    updateButtons();
  }//GEN-LAST:event_appNameFieldKeyReleased

  private void packageFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_packageFieldKeyReleased
    updateButtons();
  }//GEN-LAST:event_packageFieldKeyReleased

  private void sourceTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sourceTextFieldKeyReleased
    final File file = new File(sourceTextField.getText());
    setSourcePath(file);
    if (file.exists()) {
      sourceTreePanel.setPath(file.getPath());
    }
    updateButtons();
}//GEN-LAST:event_sourceTextFieldKeyReleased

  private void selectedRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_selectedRadioButtonItemStateChanged
    outputTextField.setEnabled(selectedRadioButton.isSelected());
    outputBrowseButton.setEnabled(selectedRadioButton.isSelected());
    updateButtons();
}//GEN-LAST:event_selectedRadioButtonItemStateChanged

  private void outputTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_outputTextFieldKeyReleased

    outputPath = outputTextField.getText();
    updateButtons();
}//GEN-LAST:event_outputTextFieldKeyReleased

  private void rssLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rssLabelMouseClicked
    /*
     * Hide RSS Reader
     if(rssURL != null)
    {
        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
          try {
            System.out.println("Open external browser: " + rssURL);
            desktop.browse(new URI(rssURL));
          } catch (IOException ex) {
            Logger.getLogger(SetupView.class.getName()).log(Level.SEVERE, null, ex);
          } catch (URISyntaxException ex) {
            Logger.getLogger(SetupView.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
    }
     */
  }//GEN-LAST:event_rssLabelMouseClicked

  private void rssLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rssLabelMouseEntered
    // Hide RSS Reader
    //rssLabel.setForeground(new Color(0, 168, 181));
  }//GEN-LAST:event_rssLabelMouseEntered

  private void rssLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rssLabelMouseExited
    // Hide RSS Reader
    //rssLabel.setForeground(null);
  }//GEN-LAST:event_rssLabelMouseExited

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton androidButton;
    private javax.swing.JCheckBox androidCheckBox;
    private javax.swing.JTextField appNameField;
    private javax.swing.JPanel applicationPanel;
    private javax.swing.JPanel backgroundPanel;
    private com.sonyericsson.web.sdk.view.panel.ImagePanel bottomFramePanel;
    private javax.swing.JButton browseButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel chooseIconLabel;
    private javax.swing.JLabel chooseNameLabel;
    private javax.swing.JButton createButton;
    private javax.swing.JRadioButton currentRadioButton;
    private javax.swing.JButton exitlButton;
    private javax.swing.JRadioButton folderButton;
    private javax.swing.JPanel folderPanel;
    private javax.swing.JPanel iconButtonPanel;
    private com.sonyericsson.web.sdk.view.panel.ImagePanel iconImagePanel;
    private javax.swing.JPanel iconPanel;
    private javax.swing.JButton installButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPanel leftCardPanel;
    private com.sonyericsson.web.sdk.view.panel.ImagePanel leftFramePanel;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel lowerPanel;
    private javax.swing.JFrame mainFrame;
    private javax.swing.JPanel mainFramePanel;
    private com.sonyericsson.web.sdk.view.panel.RoundedRectanglePanel mainPanel;
    private javax.swing.JPanel namePanel;
    private javax.swing.ButtonGroup optionsButtonGroup;
    private javax.swing.JButton outputBrowseButton;
    private javax.swing.ButtonGroup outputButtonGroup;
    private javax.swing.JLabel outputLabel;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JTextField outputTextField;
    private javax.swing.JTextField packageField;
    private javax.swing.JLabel packageLabel;
    private javax.swing.JPanel packagePanel;
    private javax.swing.JLabel platformLabel;
    private javax.swing.JPanel platformPanel;
    private javax.swing.JButton previewButton;
    private javax.swing.JLabel resizeLabel;
    private javax.swing.JPanel rightButtonPanel;
    private com.sonyericsson.web.sdk.view.panel.RoundedRectanglePanel rightCardPanel;
    private com.sonyericsson.web.sdk.view.panel.ImagePanel rightFramePanel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JLabel rssLabel;
    private com.sonyericsson.web.sdk.view.panel.RoundedRectanglePanel sampleApplicationsPanel;
    private javax.swing.JToggleButton sampleAppsToggleButton;
    private javax.swing.JRadioButton selectedRadioButton;
    private javax.swing.JButton sourceBrowseButton;
    private javax.swing.JLabel sourceLabel;
    private javax.swing.JPanel sourcePanel;
    private javax.swing.JTextField sourceTextField;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JCheckBox symbianCheckBox;
    private com.sonyericsson.web.sdk.view.panel.ImagePanel topFramePanel;
    private javax.swing.JPanel valuePanel;
    private javax.swing.JPanel windowControlPanel;
    private javax.swing.JButton wizardButton;
    // End of variables declaration//GEN-END:variables

  public String getApplicationName() {
    return appNameField.getText();
  }

  public String getPackageName() {
    return packageField.getText();
  }

  public Enumeration<String> getSelectedPlatforms() {
    Vector<String> platforms = new Vector<String>();
    if (androidCheckBox.isSelected()) {
      System.out.println("add: " + androidCheckBox.getText());
      platforms.add("Android");
    }
    if (symbianCheckBox.isSelected()) {
      System.out.println("add: " + symbianCheckBox.getText());
      platforms.add("Symbian");
    }
    return platforms.elements();
  }

  private void checkPlatforms() {
    if (!androidCheckBox.isSelected() && !symbianCheckBox.isSelected()) {
      createEnabled = false;
      installEnabled = false;
    }
    if (androidCheckBox.isSelected()) {
      createEnabled = androidPanel.isCreateable();
      installEnabled = androidPanel.isInstallable();
    }
    if (symbianCheckBox.isSelected()) {
      installEnabled = false;
    }
  }

  public void refresh() {
    updateButtons();
  }

  private void updateButtons() {
    installEnabled = true;
    createEnabled = true;
    checkName();
    checkPackage();
    checkPlatforms();
    checkOutput();
    previewButton.setEnabled(validSourceFile);
    installButton.setEnabled(nameOk && packageOk && installEnabled && validSourceFile);
    createButton.setEnabled(nameOk && packageOk && createEnabled && validSourceFile);
  }

  @Action
  public void browseIcon() {
    JFileChooser chooser = new JFileChooser();
    int returnVal = chooser.showOpenDialog(getFrame());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      try {
        iconPath = chooser.getSelectedFile();
        setIcon(iconPath);
      } catch (MalformedURLException ex) {
        Logger.getLogger(WebSDKView.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public String getIconPath() {
    return iconPath.getPath();
  }

  private void setIcon(File iconPath) throws MalformedURLException {
    if (iconPath == null || !iconPath.exists()) {
      iconPath = new File(defaultIconPath);

      if (!iconPath.exists()) {
        return;
      }
    }
    Toolkit kit = Toolkit.getDefaultToolkit();
    Image icon = kit.createImage(iconPath.toURI().toURL());
    iconImagePanel.setImage(icon);
    iconImagePanel.setResizable(false);
    iconImagePanel.repaint();
  }

  private void checkOutput() {
    boolean outputIsValid = true;
    if (selectedRadioButton.isSelected()) {
      if (outputTextField.getText().length() == 0) {

        outputIsValid = false;
      } else {
        File file = new File(outputPath);
        if (!file.isDirectory()) {
          outputIsValid = false;
        }
      }
    }
    if (outputIsValid) {
      outputLabel.setForeground(null);
    } else {
      outputLabel.setForeground(Color.red);
    }
  }

  @Action
  public void browseSourceFolder() {
    JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
    if (sourcePath.equals("")) {
      chooser.setCurrentDirectory(new java.io.File("c:\\"));
    } else {
      chooser.setCurrentDirectory(new java.io.File(sourcePath));
    }
    int returnVal = chooser.showOpenDialog(getFrame());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File selectedFolder = chooser.getSelectedFile();
      setSourcePath(selectedFolder);
      sourceTextField.setText(selectedFolder.getPath());
      sourceTreePanel.setPath(selectedFolder.getPath());
      updateButtons();
    }
  }

  @Action
  public void browseOutputPath() {
    JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
    if (outputPath.equals("")) {
      chooser.setCurrentDirectory(new java.io.File("c:\\"));
    } else {
      chooser.setCurrentDirectory(new java.io.File(outputPath));
    }
    int returnVal = chooser.showOpenDialog(getFrame());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File selectedFolder = chooser.getSelectedFile();
      outputTextField.setText(selectedFolder.getPath());
      outputPath = selectedFolder.getPath();
    }
    updateButtons();
  }

  public String getSourceFolder() {
    return sourcePath;
  }

  public String getJDKPath() {
    return androidPanel.getJDKPath();
  }

  public String getAndroidSDKPath() {
    return androidPanel.getAndroidSDKPath();
  }

  public Targets getTargets() {
    return androidPanel.getTargets();
  }

  public AndroidKeys getAndroidKeys() {
    return androidPanel.getAndroidKeys();
  }

  public int getSigning() {
    return androidPanel.getSigning();
  }

  public String getOutputPath() {
    return outputPath;
  }

  public int getOutput() {
    if (currentRadioButton.isSelected()) {
      return Settings.CURRENT_FOLDER;
    } else {
      return Settings.CUSTOM_FOLDER;
    }
  }

  public boolean isCloseWindowSelected() {
    return false;
  }

  public void changed(Component component) {
    updateButtons();
    if (component instanceof AndroidPanel) {
      androidButton.setIcon(getAndroidIcon());
      androidButton.setSelectedIcon(getAndroidSelectedIcon());
      androidButton.setRolloverIcon(getAndroidHoverIcon());
    }
  }

  private void updateCurrentRadioButtonPath() {
    if (validSourceFile) {
      currentRadioButton.setText("Current Folder (" + new File(sourcePath).getParent() + ")");
    } else {
      currentRadioButton.setText("Current Folder ()");
    }
  }

  private void clearCurrentRadioButtonPath() {
    currentRadioButton.setText("Current Folder ()");
  }

  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (source instanceof JButton) {
      JButton button = (JButton) source;
      Sample sample = samples.get(button.getName());
      appNameField.setText(sample.name);
      packageField.setText(sample.identifier);
      setSourcePath(new File(sample.source));
      sourceTreePanel.setPath(sample.source);
      sourceTextField.setText(sample.source);
      try {
        selectedRadioButton.setSelected(true);
        String selectedFolder = new JFileChooser().getFileSystemView().getDefaultDirectory().getCanonicalPath();
        outputTextField.setText(selectedFolder);
        outputPath = selectedFolder;
      } catch (IOException ex) {
        Logger.getLogger(WebSDKView.class.getName()).log(Level.SEVERE, null, ex);
      }
      sampleAppsToggleButton.setSelected(false);
      updateButtons();
      showSampleApplications();
    }
  }

  /*
   * Hide RSS Reader
  private void startRSSReader() {

      rssLabel.setText("Sony Ericsson Developer World News");

    try
    {
        URL feedURL = new URL("http://developer.sonyericsson.com/wportal/1.716426?localLinksEnabled=false&cc=gb&lc=en");
        XmlReader feedReader = new XmlReader(feedURL);
        final SyndFeed newsFeed = new SyndFeedInput().build(feedReader);

        if(newsFeed != null)
        {
            new Thread()
            {
                @Override
                public void run()
                {
                    List newsList = newsFeed.getEntries();
                    if(!newsList.isEmpty())
                    {
                        Iterator newsIterator = newsList.iterator();
                        while(true)
                        {
                            if(!newsIterator.hasNext())
                            {
                                newsIterator = newsList.iterator();
                            }
                            SyndEntry newsEntry = (SyndEntry)newsIterator.next();
                            //rssLabel.setText("All Correct! " + System.currentTimeMillis());
                            //rssLabel.setToolTipText("All Correct!" + System.currentTimeMillis());
                            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
                            String newsLine = dateFormat.format(newsEntry.getPublishedDate())
                                    + " " + newsEntry.getTitle();
                            rssLabel.setText(newsLine);
                            rssLabel.setToolTipText(newsEntry.getDescription().getValue());
                            rssURL = newsEntry.getLink();
                            try
                            {
                                Thread.sleep(5000);
                            }
                            catch(InterruptedException e) {}
                        }
                    }
                }
            }.start();
        }
    }
    catch(MalformedURLException e)
    {
        System.out.println("MalformedURLException: "+e.getMessage());
    }
    catch(IOException e)
    {
        System.out.println("IOException: "+e.getMessage());
    }
    catch(IllegalArgumentException e)
    {
        System.out.println("IllegalArgumentException: "+e.getMessage());
    }
    catch(FeedException e)
    {
        System.out.println("FeedException: "+e.getMessage());
    }
  }
  */

  private class SourceTransferHandler extends TransferHandler {

    private TransferHandler parentHandler;

    public SourceTransferHandler() {
    }

    public SourceTransferHandler(TransferHandler parentHandler) {
      this.parentHandler = parentHandler;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
      if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
        if (parentHandler != null) {
          return parentHandler.canImport(support);
        } else {
          return false;
        }
      }

      return true;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
      if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
        if (parentHandler != null) {
          return parentHandler.importData(support);
        } else {
          return false;
        }
      }

      Transferable t = support.getTransferable();

      try {
        java.util.List<File> fileList = (java.util.List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

        if (fileList.size() != 1) {
          return false;
        }
        File file = fileList.get(0);

        setSourcePath(file);
        sourceTextField.setText(file.getPath());
        sourceTreePanel.setPath(file.getPath());

        updateButtons();

      } catch (UnsupportedFlavorException e) {
        return false;
      } catch (IOException e) {
        return false;
      }
      return true;
    }
  };

  private void setSourcePath(File file) {
    File folder;

    if (file.isDirectory()) {
      folder = file;
    } else if (file.exists()) {
      folder = file.getParentFile();
    } else {
      folder = file;
    }
    sourcePath = folder.getPath();
    String[] list = folder.list();
    validSourceFile = false;
    if (list != null) {
      for (int i = 0; i < list.length; i++) {
        String string = list[i];
        if (string.equalsIgnoreCase("index.html")) {
          validSourceFile = true;
          break;
        }
      }
    }
    if (validSourceFile) {
      sourceLabel.setForeground(null);
    } else {
      sourceLabel.setForeground(Color.red);
      clearCurrentRadioButtonPath();
    }
    updateCurrentRadioButtonPath();
  }

  @Action
  public void showAndroid() {
    CardLayout cardLayout = (CardLayout) rightCardPanel.getLayout();
    cardLayout.show(rightCardPanel, "Android");
  }

  @Action
  public void showFolder() {
    CardLayout cardLayout = (CardLayout) rightCardPanel.getLayout();
    cardLayout.show(rightCardPanel, "Folder");
  }

  @Action
  public void maximize() {
    int extendedState = getFrame().getExtendedState();
    if ((extendedState & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
      getFrame().setExtendedState(JFrame.NORMAL);
    } else {
      getFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
  }

  @Action
  public void minimize() {
    int extendedState = getFrame().getExtendedState();
    getFrame().setExtendedState(extendedState | JFrame.ICONIFIED);
  }

  @Action
  public void showSampleApplications() {
    if (sampleAppsToggleButton.isSelected()) {
      CardLayout cardLayout = (CardLayout) leftCardPanel.getLayout();
      cardLayout.show(leftCardPanel, "SampleApplications");
    } else {
      CardLayout cardLayout = (CardLayout) leftCardPanel.getLayout();
      cardLayout.show(leftCardPanel, "Main");
    }
  }

  private class Sample {

    String name;
    String identifier;
    String source;

    public Sample(String name, String identifier, String source) {
      this.name = name;
      this.identifier = identifier;
      this.source = source;
    }
  }
}
