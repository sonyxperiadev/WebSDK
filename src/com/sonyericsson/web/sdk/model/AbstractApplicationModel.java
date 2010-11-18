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

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import com.sonyericsson.web.sdk.utils.RegistryHandler;

public abstract class AbstractApplicationModel implements ApplicationModel {

  private Vector<ModelListener> listeners;
  private String platform;
  
  private final Settings settings;
  private PromptListener promptListener;

  public AbstractApplicationModel(String platform, Settings settings) {
    this.platform = platform;
    listeners = new Vector<ModelListener>();
    this.settings = settings;
  }

  public String getIconpath() {
    return RegistryHandler.getIconPath();
  }

  public String getSelectedFolder() {
    return settings.getSourceFolder();
  }

  public String getName() {
    return settings.getApplicationName();
  }

  public String getPackageName() {
    return settings.getPackageName();
  }

  public File getDestinationDir() {
    return settings.getDestinationDir();
  }

  @Override
  public String getPlatform() {
    return platform;
  }

  @Override
  public void addListener(ModelListener listener) {
    listeners.add(listener);
  }

  @Override
  public void removeListener(ModelListener listener) {
    listeners.remove(listener);
  }

  @Override
  public void setPromptListener(PromptListener listener) {
    promptListener = listener;
  }
  protected void fireModelChanged(final Event event) {
    Enumeration<ModelListener> elements = listeners.elements();
    while (elements.hasMoreElements()) {
      ModelListener listener = elements.nextElement();
      listener.changed(event);
    }
  }

  protected String prompt(String title, String information) {
    if (promptListener == null) {
      return null;
    }

    Prompt prompt = new Prompt();
    prompt.setTitle(title);
    prompt.setInformation(information);

    promptListener.prompt(prompt);

    prompt.waitForAnswer();

    return prompt.getValue();
  }
}
