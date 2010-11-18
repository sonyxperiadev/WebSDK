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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileHandler {

  private byte[] readFileToBuffer(File file) {
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    long fileSize = file.length();
    System.out.println("fileSize = " + fileSize);
    byte[] buf = new byte[(int)fileSize];
    try {
      fis = new FileInputStream(file);

      // Here BufferedInputStream is added for fast reading.
      bis = new BufferedInputStream(fis);

      int readBytes = bis.read(buf);
      int slask = bis.read(buf);
      //System.out.println("readBytes = " + readBytes);
      //System.out.println("slask = " + slask); // This should be -1

      // dispose all the resources after using them.
      fis.close();
      bis.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Return the byte array.
    return buf;
  }

  public void createFile(String pathName, byte[] buffer) {
    try {
      StringBuffer strBuffer = new StringBuffer(pathName);
      int index = strBuffer.lastIndexOf("/");
      if (index == -1) {
        index = strBuffer.lastIndexOf("\\");
      }
      strBuffer.delete(index, strBuffer.length());
      File newFile = new File(strBuffer.toString());
      if (!newFile.exists()) {
        newFile.mkdirs();
      }
      System.out.println(strBuffer.toString());
      newFile = new File(pathName);
      FileOutputStream fos = null;
      BufferedOutputStream bos = null;

      try {
        fos = new FileOutputStream(newFile);
        bos = new BufferedOutputStream(fos);
        bos.write(buffer);
        bos.flush();
      } catch (IOException ex) {
        Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
      }

      fos.close();
      bos.close();
    } catch (IOException ex) {
      Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   *  This function change a parameter in a xml file  
   * @param preFix
   * @param Suffix
   * @param name
   * @param stringsXml
   */
  
  public void insertStringInToAFile(String preFix, String suffix, String name, File stringsXml) {
    byte[] byteBuffer = readFileToBuffer(stringsXml);
    int startIndex = 0;
    int stopIndex = 0;
    String convertedBuf = new String(byteBuffer);
    StringBuffer stringBuffer = new StringBuffer(convertedBuf);
    startIndex = stringBuffer.indexOf(preFix);
    startIndex = startIndex + preFix.length();
    stopIndex = stringBuffer.indexOf(suffix, startIndex);
    stringBuffer.delete(startIndex, stopIndex);
    stringBuffer.insert(startIndex, name);
    convertedBuf = stringBuffer.toString();
    System.out.println(convertedBuf);
    byteBuffer = convertedBuf.getBytes();
    String filePath = stringsXml.getAbsolutePath();
    stringsXml.delete();
    createFile(filePath, byteBuffer);
  }

  public void changeStringInFile(String oldString, String newString, File file) {
    byte[] byteBuffer = readFileToBuffer(file);
    String string = new String(byteBuffer);

    // Compile regular expression
    Pattern pattern = Pattern.compile(oldString);

    // Replace all occurrences of pattern in input
    Matcher matcher = pattern.matcher(string);
    String output = matcher.replaceAll(newString);
    file.delete();
    createFile(file.getPath(), output.getBytes());
  }

  public void changePackage(String oldPackageName, String newPackageName, File newSrcFolder, File file) {
    System.out.println("p: "+ newPackageName);
    byte[] byteBuffer = readFileToBuffer(file);
    String string = new String(byteBuffer);

    // Compile regular expression
    Pattern pattern = Pattern.compile(oldPackageName);

    // Replace all occurrences of pattern in input
    Matcher matcher = pattern.matcher(string);
    String output = matcher.replaceAll(newPackageName);
    System.out.println(newPackageName);
    pattern = Pattern.compile("\\.");
    matcher = pattern.matcher(newPackageName);
    String packagePath = matcher.replaceAll("/");
    String filepath = newSrcFolder.getPath() + "/" +  packagePath + "/" + file.getName();

    System.out.println("Filepath: " + filepath);
    createFile(filepath, output.getBytes());
  }

  public String getAndroidAppNameFromXMLFile(File stringsXml) {
    byte[] byteBuffer = readFileToBuffer(stringsXml);
    int startIndex = 0;
    int stopIndex = 0;
    String firstString = "<string name=\"app_name\">";
    String endString = "</string>";
    String convertedBuf = new String(byteBuffer);
    StringBuffer stringBuffer = new StringBuffer(convertedBuf);
    startIndex = stringBuffer.indexOf(firstString);
    startIndex = startIndex + firstString.length();
    stopIndex = stringBuffer.indexOf(endString, startIndex);
    return stringBuffer.substring(startIndex, stopIndex);
  }

  // Copies a file to a directory that must exist in advance.
  public void copyFileToDir(File file, File targetDir) throws IOException {
    System.out.println("targetDir: " + targetDir.getPath());
    System.out.println("File: " + file.getPath());
    if (file.exists() && targetDir.exists()) {
      File newFile = new File(targetDir, file.getName());
      if (!newFile.exists()) {
        newFile.createNewFile();
      }
      InputStream in = new FileInputStream(file);
      OutputStream out = new FileOutputStream(newFile);
      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
      in.close();
      out.close();
    }
  }


  public void copyFolderContent(File sourceLocation, File targetLocation) throws IOException {
    copyFolderContent(sourceLocation, targetLocation, null);
  }

  public void copyFolderContent(File sourceLocation, File targetLocation, FileFilter filter) throws IOException {
    boolean isDirectory;
    
    if (sourceLocation == null || targetLocation == null) {
      NullPointerException exception = new NullPointerException();
      throw exception;
    }
        
    isDirectory = sourceLocation.isDirectory();
    if (filter != null && !filter.accept(sourceLocation)) {
      return;
    }
    if (isDirectory) {
      if (!targetLocation.exists()) {
        targetLocation.mkdirs();
      }
      String[] children = sourceLocation.list();
      for (int i = 0; i < children.length; i++) {
        copyFolderContent(new File(sourceLocation, children[i]), new File(targetLocation, children[i]), filter);
      }
    } else {

      // InputStream in = new FileInputStream(sourceLocation);
      OutputStream out = new FileOutputStream(targetLocation);

      // Copy the bits from instream to outstream
      byte[] buf = readFileToBuffer(sourceLocation); // new
      // byte[1024];      

      out.write(buf, 0, buf.length);

      // in.close();
      out.close();
    }
  }
 

  
  /*
   * deleteFolder(File folder) - This method deletes a complete folder
   * or a file. All sub-folders and sub-files will be deleted
   * automatically. Files and folders that are write-protected, will
   * not be deleted.
   */
  public void deleteFolder(File folder) {
    if (folder.exists()) {
      if (folder.isDirectory()) {
        String[] children = folder.list();
        for (int i = 0; i < children.length; i++) {
          deleteFolder(new File(folder, children[i]));
        }
        if (folder.canWrite()) {
          folder.delete();
        }
      } else {
        if (folder.canWrite()) {
          folder.delete();
          System.out.println(folder.getPath() + " deleted");
        }
      }
    }
  }

  public void addFilePathsToList(File folder, ArrayList<File> files) {
    File file;
    if (folder.isDirectory()) {
      files.add(folder);
      String[] children = folder.list();
      for (int i = 0; i < children.length; i++) {
        file = new File(folder, children[i]);
        addFilePathsToList(file, files);
      }
    } else {
      files.add(folder);
    }
  }

  public void addSrcToBuildProperties(File buildProperies, String string) {
    createFile(buildProperies.getPath(), string.getBytes());
  }
  
  
  public void zipPrinter(File file) {

    try {

      ZipInputStream zin = new ZipInputStream(new FileInputStream(file));

      ZipEntry entry = zin.getNextEntry();

 

      while (entry != null) {

        String name = entry.getName();

        System.out.println("entry name = " + name);

        System.out.println("entry size = " + entry.getCompressedSize());

        System.out.println("entry compression method = " + entry.getMethod());

        System.out.println("entry CRC checksum = " + entry.getCrc());

        System.out.println("--------------");

        entry = zin.getNextEntry();

      }

    } catch (FileNotFoundException e) {

      // TODO Auto-generated catch block

      e.printStackTrace();

    } catch (IOException e) {

      // TODO Auto-generated catch block

      e.printStackTrace();

    }

  }

  

  
}
