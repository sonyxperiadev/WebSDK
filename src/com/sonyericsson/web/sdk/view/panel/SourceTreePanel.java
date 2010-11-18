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

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import java.net.URL;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.sonyericsson.web.sdk.model.SourceTreeModel;
import java.awt.Color;
import java.awt.Font;
import java.net.URLClassLoader;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;

/**
 * tree-like browser for source code
 */
public class SourceTreePanel extends JPanel {

  private JTree tree;
  private final JScrollPane scrollPane;

  public SourceTreePanel(String startPath) {
    this(new SourceTreeModel(startPath));
  }

  public SourceTreePanel(SourceTreeModel model) {
    //setOpaque(false);
    UIManager.put("Tree.rowHeight", new Integer(20));
    tree = new JTree(model) {

      public String convertValueToText(Object value, boolean selected,
              boolean expanded, boolean leaf, int row,
              boolean hasFocus) {
        return ((File) value).getName();
      }
    };
    JPopupMenu popupMenu = new JPopupMenu();
    popupMenu.add(new JMenuItem("Open"));
    tree.setFont(new Font("Tahoma", Font.PLAIN, 12));
    URL leafUrl = URLClassLoader.getSystemResource("com/sonyericsson/web/sdk/view/resources/IconFile.png");
    URL folderUrl = URLClassLoader.getSystemResource("com/sonyericsson/web/sdk/view/resources/IconFolder.png");
    ImageIcon leafIcon = new ImageIcon(leafUrl);
    ImageIcon folderIcon = new ImageIcon(folderUrl);
    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
    renderer.setLeafIcon(leafIcon);
    renderer.setOpenIcon(folderIcon);
    renderer.setClosedIcon(folderIcon);
    renderer.setBackgroundSelectionColor(new Color(0, 168, 181));
    //renderer.setComponentPopupMenu(popupMenu);
    tree.setCellRenderer(renderer);
    setLayout(new BorderLayout());

    scrollPane = new JScrollPane();
    scrollPane.setViewportView(tree);
    this.add(scrollPane);
    scrollPane.setOpaque(false);
    tree.setBorder(null);
    //tree.setOpaque(false);

    tree.addTreeSelectionListener(new TreeSelectionListener() {

      public void valueChanged(TreeSelectionEvent e) {
        File node = (File) e.getPath().getLastPathComponent();
        System.out.println("You selected " + node);
      }
    });

    tree.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
          TreePath tp = tree.getClosestPathForLocation(evt.getX(), evt.getY());
          File file = (File) tp.getLastPathComponent();
          // open the file here
          if (file.isFile() && file.exists()) {
            System.out.format("You double clicked %s", file.toString());
            try {
              Desktop.getDesktop().open(file);
            } catch (IOException ex) {
              System.out.println("File Error: Unable to open");
            }
          }
        }
      }
    });

    //tree.setLargeModel( true );
    tree.setRootVisible(false);
    tree.setShowsRootHandles(true);
    tree.putClientProperty("JTree.lineStyle", "Angled");
  }

  public JTree getTree() {
    return tree;
  }

  public void setTreeModel(TreeModel treeModel) {
    tree.setModel(treeModel);
  }

  public void setPath(String path) {
    setTreeModel(new SourceTreeModel(path));
  }
}


