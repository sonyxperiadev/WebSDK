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

package com.sonyericsson.web.sdk.resources;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * class for accessing WebSDK Packager resources: icons, background, buttons
 */
public class Resources {

  private static Image logo;
  private static Image defaultIcon;
  private static Image background;
  private static Image bottomFrame;
  private static Image topFrame;
  private static Image leftFrame;
  private static Image rightFrame;
  private static Image resizeImage;
  private static Icon androidIcon;
  private static Icon androidWarningIcon;
  private static ImageIcon androidHoverIcon;
  private static ImageIcon androidWarningHoverIcon;
  private static ImageIcon androidSelectedIcon;
  private static ImageIcon androidWarningSelectedIcon;

  public static Image getLogo() {

    if (logo == null) {
      logo = loadImage("com/sonyericsson/web/sdk/view/resources/se_logo.png");
    }
    
    return logo;
  }

  public static Image getBackground() {

    if (background == null) {
      background = loadImage("com/sonyericsson/web/sdk/view/resources/MainFrame.png");
    }

    return background;

  }

  public static Image getTopFrame() {

    if (topFrame == null) {
      topFrame = loadImage("com/sonyericsson/web/sdk/view/resources/TopFrame.png");
    }

    return topFrame;

  }
  public static Image getLeftFrame() {

    if (leftFrame == null) {
      leftFrame = loadImage("com/sonyericsson/web/sdk/view/resources/LeftFrame.png");
    }

    return leftFrame;

  }
  public static Image getRightFrame() {

    if (rightFrame == null) {
      rightFrame = loadImage("com/sonyericsson/web/sdk/view/resources/RightFrame.png");
    }

    return rightFrame;

  }
  public static Image getBottomFrame() {

    if (bottomFrame == null) {
      bottomFrame = loadImage("com/sonyericsson/web/sdk/view/resources/BottomFrame.png");
    }

    return bottomFrame;

  }

  public static Image getDefaultIcon() {

    if (defaultIcon == null) {
      defaultIcon = loadImage("com/sonyericsson/web/sdk/resources/logo2.jpg");
    }

    return defaultIcon;
  }

  public static Image getResizeImage() {

    if (resizeImage == null) {
      resizeImage = loadImage("com/sonyericsson/web/sdk/resources/Resize.png");
    }

    return resizeImage;
  }

  private static Image loadImage(String resource) {
    URL url = ClassLoader.getSystemResource(resource);
      Toolkit kit = Toolkit.getDefaultToolkit();
      return kit.createImage(url);
  }

  public static Icon getAndroidIcon() {

    if (androidIcon == null) {
      androidIcon = new ImageIcon(loadImage("com/sonyericsson/web/sdk/view/resources/Button_Android.png"));
    }

    return androidIcon;
  }


  public static Icon getAndroidWarningIcon() {

    if (androidWarningIcon == null) {
      androidWarningIcon = new ImageIcon(loadImage("com/sonyericsson/web/sdk/view/resources/Button_Android_warning.png"));
    }

    return androidWarningIcon;
  }


  public static Icon getAndroidHoverIcon() {

    if (androidHoverIcon == null) {
      androidHoverIcon = new ImageIcon(loadImage("com/sonyericsson/web/sdk/view/resources/Button_Android_hover.png"));
    }

    return androidHoverIcon;
  }


  public static Icon getAndroidWarningHoverIcon() {

    if (androidWarningHoverIcon == null) {
      androidWarningHoverIcon = new ImageIcon(loadImage("com/sonyericsson/web/sdk/view/resources/Button_Android_warning_hover.png"));
    }

    return androidWarningHoverIcon;
  }


  public static Icon getAndroidSelectedIcon() {

    if (androidSelectedIcon == null) {
      androidSelectedIcon = new ImageIcon(loadImage("com/sonyericsson/web/sdk/view/resources/Button_Android_selected.png"));
    }

    return androidSelectedIcon;
  }


  public static Icon getAndroidWarningSelectedIcon() {

    if (androidWarningSelectedIcon == null) {
      androidWarningSelectedIcon = new ImageIcon(loadImage("com/sonyericsson/web/sdk/view/resources/Button_Android_warning_selected.png"));
    }

    return androidWarningSelectedIcon;
  }
}
