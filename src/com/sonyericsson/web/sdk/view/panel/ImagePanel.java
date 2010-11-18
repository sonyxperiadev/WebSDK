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

package com.sonyericsson.web.sdk.view.panel;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

public class ImagePanel extends JPanel implements ImageObserver {

  private Image image;
  private boolean resizable = true;

  public ImagePanel() {
    // TODO Auto-generated constructor stub
  }

  public ImagePanel(Image image, boolean resizable) {
    this.image = image;
    this.resizable = resizable;
  }

  public void setImage(Image image) {
    this.image = image;
  }

  public void setResizable(boolean resizable) {
    this.resizable = resizable;
  }

  @Override
  public void paintComponent(Graphics g) {
    System.out.println("paintComponent");
    if (resizable) {
      g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    } else {
      g.drawImage(image, 0, 0, this);
    }
  }

  @Override
  public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
    if ((infoflags & ALLBITS) == ALLBITS) {
      repaint();
      return false;
    }
    return true;
  }
}
