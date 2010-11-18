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

package com.sonyericsson.web.sdk.model;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;

// This class takes care of the event listener lists required by TreeModel.
// It also adds "fire" methods that call the methods in TreeModelListener.
// Look in TreeModelSupport for all of the pertinent code.
public abstract class AbstractTreeModel implements TreeModel {

   private Vector<TreeModelListener> vector = new Vector<TreeModelListener>();

   public void addTreeModelListener( TreeModelListener listener ) {
      if ( listener != null && !vector.contains( listener ) ) {
         vector.addElement( listener );
      }
   }

   public void removeTreeModelListener( TreeModelListener listener ) {
      if ( listener != null ) {
         vector.removeElement( listener );
      }
   }

   public void fireTreeNodesChanged( TreeModelEvent e ) {
      Enumeration<TreeModelListener> listeners = vector.elements();
      while ( listeners.hasMoreElements() ) {
         TreeModelListener listener = listeners.nextElement();
         listener.treeNodesChanged( e );
      }
   }

   public void fireTreeNodesInserted( TreeModelEvent e ) {
      Enumeration<TreeModelListener> listeners = vector.elements();
      while ( listeners.hasMoreElements() ) {
         TreeModelListener listener = listeners.nextElement();
         listener.treeNodesInserted( e );
      }
   }

   public void fireTreeNodesRemoved( TreeModelEvent e ) {
      Enumeration<TreeModelListener> listeners = vector.elements();
      while ( listeners.hasMoreElements() ) {
         TreeModelListener listener = listeners.nextElement();
         listener.treeNodesRemoved( e );
      }
   }

   public void fireTreeStructureChanged( TreeModelEvent e ) {
      Enumeration<TreeModelListener> listeners = vector.elements();
      while ( listeners.hasMoreElements() ) {
         TreeModelListener listener = listeners.nextElement();
         listener.treeStructureChanged( e );
      }
   }
}
