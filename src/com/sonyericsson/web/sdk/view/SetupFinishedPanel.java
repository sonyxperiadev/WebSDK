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

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SetupFinishedPanel extends AbstractPanel {

  /** Creates new form SetupFinishedPanel */
  public SetupFinishedPanel() {
    initComponents();

    addComponentListener(new ComponentAdapter() {

      @Override
      public void componentShown(ComponentEvent e) {
        System.out.println("downloadPanel.componentShown");
        firePanelChanged();

      }

      @Override
      public void componentHidden(ComponentEvent e) {
      }
    });
  }

  @Override
  public boolean isBackEnabled() {
    return false;
  }

    @Override
    public boolean isNextEnabled() {
        return false;
    }
  
  

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    welcomeTopPanel = new javax.swing.JPanel();
    jLabel13 = new javax.swing.JLabel();
    jLabel14 = new javax.swing.JLabel();
    welcomeMainPanel = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTextPane1 = new javax.swing.JTextPane();

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.sonyericsson.web.sdk.controller.WebSDK.class).getContext().getResourceMap(SetupFinishedPanel.class);
    setBackground(resourceMap.getColor("Form.background")); // NOI18N
    setName("Form"); // NOI18N
    setOpaque(false);
    setLayout(new java.awt.BorderLayout());

    welcomeTopPanel.setBackground(resourceMap.getColor("welcomeTopPanel.background")); // NOI18N
    welcomeTopPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
    welcomeTopPanel.setName("welcomeTopPanel"); // NOI18N
    welcomeTopPanel.setOpaque(false);
    welcomeTopPanel.setLayout(new java.awt.BorderLayout());

    jLabel13.setFont(resourceMap.getFont("jLabel13.font")); // NOI18N
    jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
    jLabel13.setName("jLabel13"); // NOI18N
    welcomeTopPanel.add(jLabel13, java.awt.BorderLayout.LINE_START);

    jLabel14.setIcon(null);
    jLabel14.setMaximumSize(new java.awt.Dimension(50, 50));
    jLabel14.setMinimumSize(new java.awt.Dimension(50, 50));
    jLabel14.setName("jLabel14"); // NOI18N
    jLabel14.setPreferredSize(null);
    welcomeTopPanel.add(jLabel14, java.awt.BorderLayout.LINE_END);

    add(welcomeTopPanel, java.awt.BorderLayout.PAGE_START);

    welcomeMainPanel.setName("welcomeMainPanel"); // NOI18N
    welcomeMainPanel.setOpaque(false);
    welcomeMainPanel.setLayout(new java.awt.BorderLayout());

    jScrollPane1.setName("jScrollPane1"); // NOI18N
    jScrollPane1.setOpaque(false);

    jTextPane1.setName("jTextPane1"); // NOI18N
    jTextPane1.setOpaque(false);
    jScrollPane1.setViewportView(jTextPane1);

    welcomeMainPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

    add(welcomeMainPanel, java.awt.BorderLayout.CENTER);
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel14;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTextPane jTextPane1;
  private javax.swing.JPanel welcomeMainPanel;
  private javax.swing.JPanel welcomeTopPanel;
  // End of variables declaration//GEN-END:variables
}