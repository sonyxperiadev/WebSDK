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

/*
 * SymbianAplicationModel uses in order to create a PhoneGap application for Symbian from a 
 * pre-selected folder.
 * 
 * The selected application folder will be copied in to a temporary folder
 * 1)A info.plist (Symbian specific) file will be added in to the selected application folder.
 * 2)The phonegap.js will be added
 * 3)The selected application icon(will be shown on the phone desktop) will be added 
 * 4)The application folder will be zipped outside of the source folder as a .wgz application file
 * 5)The temporary folder is removed... DONE! we have a symbian installation file
 *
 */

package com.sonyericsson.web.sdk.model;

import java.io.File;
import java.io.IOException;
import com.sonyericsson.web.sdk.utils.FileHandler;
import com.sonyericsson.web.sdk.utils.RegistryHandler;
import com.sonyericsson.web.sdk.utils.helper.AntHelper;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for generate, install and sign Symbian applications
 */

public class SymbianApplicationModel extends AbstractApplicationModel {

  private String installDirectory; // installation for Web SDK "C:\Program Files\Sony Ericsson\Web SDK\"
  private String phoneGapJsFile;
  private String folderToBeZip;   // get the folder for the PhoneGap implementation
  private InputStream infoFileToAdd;      // Address to the "Info.plist" template.
  private String applicationName;  // The name of the appication that will be shown on the desktop.
  private String identifier;      // Identifier a.e com.scroll.basic.widget //"<key>DisplayName</key>" + "\r\n" + "<string>";
  private String defaultIconPath;
  private File destinationDir;
  /** Symbian info file to change 
   *
   *   see Info.plist file on com.sonyericsson.web.sdk.resources 
   *  <key>DisplayName</key>
   *  <string>My Application</string>
   */
  private static final String ATTRIBUTE_PREFIX_APP_NAME = "<key>DisplayName</key>";
  private static final String ATTRIBUTE_SUFFIX_APP_NAME = "</string>";
  private static final String ATTRIBUTE_PREFIX_IDENTIFIER = "<key>Identifier</key>";
  private static final String ATTRIBUTE_SUFFIX_IDENTIFIER = "</string>";
  private static final String PLATFORM = "Symbian";

  public SymbianApplicationModel(Settings settings) {
    super(PLATFORM, settings);

    this.installDirectory = RegistryHandler.getInstallDir();
    this.phoneGapJsFile = installDirectory + "/PhonegapSDK/Default/Symbian/lib/phonegap.js";
    //this.infoFileToAdd = this.getClass().getResourceAsStream("/com/sonyericsson/web/sdk/resources/Info.plist");

  }

  /**
   * generate Symbian application
   * @param debug flag for using debug keys for signing
   */

  public void generate(boolean debug) {
    fireModelChanged(new Event(null, 10, 0, "Started", PLATFORM)); // start the progress dialog 10%
    createWGZFile();
    fireModelChanged(new Event(null, 100, 0, "Created", PLATFORM)); // update progress to 100%
  }

  /**
   * sing application using configured keystore and key password
   * not implemented for Symbian
   */
  public void sign() {
    // throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * install application to device or emulator
   * not implemented for Symbian
   */
public void install() {
    // throw new UnsupportedOperationException("Not supported yet.");
  }

  public void finish() {
    fireModelChanged(new Event(null, 100, 0, "Finished", PLATFORM)); // update progress to 100%
  }

  /**
   * createWGZFile
   * 
   * Create an installation file from the source folder containing the PhoneGap application
   * The file will be placed outside of the source folder. 
   * Two files will be added in to the        
   * 
   */
  private void createWGZFile() {
      this.infoFileToAdd = this.getClass().getResourceAsStream("/com/sonyericsson/web/sdk/resources/Info.plist");
        try {
            File stringsXmlFile;
            File symbianApplicationFile;
            File sourceDir;
            File phoneGap_jsFile;
            File tempDir;
            File tempDirToZip;
            this.folderToBeZip = getSelectedFolder();
            this.applicationName = getName();
            this.identifier = getPackageName();
            this.defaultIconPath = getIconpath();
            FileHandler instance = new FileHandler();
            
            
            
            System.out.println(" folder to be zipped " + folderToBeZip);
            sourceDir = new File(folderToBeZip);
            destinationDir = getDestinationDir();            
            tempDir = new File(System.getProperty("java.io.tmpdir") + "/temp_wgz_" + applicationName, applicationName);
            tempDir.mkdirs();
            File newFile = new File (tempDir, "Info.plist");
            tempDirToZip = new File(tempDir.getParent());
            AntHelper antHelper = new AntHelper();
            File icon = new File(defaultIconPath);
            File icon_png = new File(tempDir, "Icon.png");
            String iconName = icon.getName();
            File iconCopied = new File(tempDir + "/" + iconName);
            symbianApplicationFile = new File(destinationDir, applicationName + ".wgz");
            phoneGap_jsFile = new File(phoneGapJsFile);
            instance.deleteFolder(symbianApplicationFile);
            
            // copy stream Info.plist to a  file
                           
            OutputStream out = new FileOutputStream(newFile);
            byte buf[] = new byte [1024];
            int len;
            while ((len= this.infoFileToAdd.read(buf)) >0 )
                out.write(buf, 0, len);
            out.close();
            this.infoFileToAdd.close();
            
           
            // modify info file
            instance.insertStringInToAFile(ATTRIBUTE_PREFIX_APP_NAME, ATTRIBUTE_SUFFIX_APP_NAME, "\r\n" + "\t" + "<string>" + applicationName, newFile);
            instance.insertStringInToAFile(ATTRIBUTE_PREFIX_IDENTIFIER, ATTRIBUTE_SUFFIX_IDENTIFIER, "\r\n" + "\t" + "<string>" + identifier, newFile);
            instance.copyFileToDir(phoneGap_jsFile, tempDir);
            instance.copyFileToDir(icon, tempDir);
            iconCopied.renameTo(icon_png);

            // copy source dir to the temp dir
            instance.copyDirectory(sourceDir, tempDir);
            antHelper.zipFile(tempDirToZip, symbianApplicationFile);
            instance.deleteFolder(tempDirToZip);

        } catch (IOException ex) {
            Logger.getLogger(SymbianApplicationModel.class.getName()).log(Level.SEVERE, null, ex);
        }
      



  }

  @Override
  public boolean isCreatable() {
    return true;
  }

  @Override
  public boolean isInstallable() {
    return false;
  }
}
