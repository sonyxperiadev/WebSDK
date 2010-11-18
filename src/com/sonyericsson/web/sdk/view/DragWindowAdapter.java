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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;

public class DragWindowAdapter extends MouseAdapter
        implements MouseMotionListener {

  private JFrame frame;
  private int mousePrevX, mousePrevY;
  private int frameX, frameY;

  public DragWindowAdapter(JFrame frame) {
    this.frame = frame;
  }

  public void mousePressed(MouseEvent e) {
    super.mousePressed(e);
    System.out.println("mousePressed");
    mousePrevX = e.getX();
    mousePrevY = e.getY();
    frameX = 0;
    frameY = 0;
  }

  public void mouseDragged(MouseEvent e) {
    System.out.println("mouseDragged");
    int X = e.getX();
    int Y = e.getY();
    int MsgX = frame.getX();
    int MsgY = frame.getY();
    int moveX = X - mousePrevX; // Negative if move left
    int moveY = Y - mousePrevY; // Negative if move down
    if (moveX == 0 && moveY == 0) {
      return;
    }
    mousePrevX = X - moveX;
    mousePrevY = Y - moveY;
    if (frameX == MsgX && frameY == MsgY) {
      frameX = 0;
      frameY = 0;
      return;
    }
    int newFrameX = MsgX + moveX;
    int newFrameY = MsgY + moveY;
    frameX = newFrameX;
    frameY = newFrameY;
    frame.setLocation(newFrameX, newFrameY);
  }

  public void mouseMoved(MouseEvent e) {
    System.out.println("mouseMoved");
  }
}
