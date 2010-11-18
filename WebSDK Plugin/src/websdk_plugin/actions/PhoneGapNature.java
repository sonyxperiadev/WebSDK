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

package websdk_plugin.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class PhoneGapNature implements IProjectNature {

	public static final String NATURE_ID = "com.example.phonegap.PhoneGapNature";
	private IProject project;

	@Override
	public void configure() throws CoreException {
		// TODO Auto-generated method stub
		//--Path path;
		//path = new Path("/opt/PhoneGap Simulator/share/www/");
		//--path = new Path("C:\\Program Files\\Sony Ericsson PhoneGap Simulator\\www");
		//--project.getFolder("www").createLink(path, 0, null);
		/*
		project.getFolder("www").create(true, true, null);
		Bundle bundle = PhoneGapActivator.getDefault().getBundle();
		Enumeration template_files = bundle.getEntryPaths("www");
		while(template_files.hasMoreElements())
		{
			String file_path = (String)template_files.nextElement();
			URL file_url = bundle.getEntry(file_path);
			IFile file = project.getFile(file_url.getFile());
			try
			{
				file.create(file_url.openStream(), true, null);
			}
			catch(IOException e)
			{
				System.out.println("IOException: "+e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		*/
	}

	@Override
	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IProject getProject() {
		// TODO Auto-generated method stub
		return project;
	}

	@Override
	public void setProject(IProject project) {
		// TODO Auto-generated method stub
		this.project = project;
	}

}
