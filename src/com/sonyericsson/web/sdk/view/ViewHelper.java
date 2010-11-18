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

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class ViewHelper {

    public ViewHelper() {
    }

    public static void putWindowInCenter(JFrame window) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        window.pack();
        int leftCoordinate = (screenSize.width / 2) - (window.getWidth() / 2);
        int topCoordinate = (screenSize.height / 2) - (window.getHeight() / 2);
        window.setLocation(leftCoordinate, topCoordinate);
    }
    
}
