package com.edu.ufcg.splab.priorj.core;

import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.eclipse.ajdt.core.AspectJPlugin;
import org.eclipse.ajdt.internal.utils.AJDTUtils;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.osgi.framework.Bundle;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.java.io.JavaIO;

/**
 * This class is the application core.
 * 
 * @author Samuel T. C. Santos
 * 
 * @version 1.0.0
 */
public class AJDTHandler  {

	private IWorkspace workspace;
	private IWorkspaceRoot root;
	/**
	 * Project Visitor
	 */
	private ProjectVisitor visitor;
	
	/**
	 * JUnit 3 Container Path
	 */
	public static Path junit3Path = new Path("org.eclipse.jdt.junit.JUNIT_CONTAINER/3");
	/**
	 * JUnit 4 Container Path
	 */
	public static Path junit4Path = new Path("org.eclipse.jdt.junit.JUNIT_CONTAINER/4");
	/**
	 * ClasspathEntry to JUnit 3 Container
	 */
	public static IClasspathEntry junit3Entry = JavaCore.newContainerEntry(junit3Path);
	/**
	 * ClasspathEntry to JUnit 4 Container
	 */
	public static IClasspathEntry  junit4Entry = JavaCore.newContainerEntry(junit4Path);
	/**
	 * Libraries to coverage and persistence.
	 */
	private static final String  COVERAGE_LIBRARY = "coverage-v1.0.6.jar";
	
	private static final String  JAVA_IO_LIBRARY = "javaio-v1.0.4.jar";
	
	private static final String  JSON_LIBRARY = "gson-2.3.1.jar";
	
	/**
	 * Our build implementation
	 */
	private PriorJBuilder builder;
	/**
	 * Aspect coverage path.
	 */
	private static final String ASPECT_FILE = "AspectCoverage.aj";
	
	/**
	 * Aspect coverage path for original project.
	 */
	private static final String ASPECT_ORIGINAL_FILE = "AspectCoverageOriginal.aj";
	
	
	
	/**
	 * Constructor default to <code>PriorJCoreHandler</code>
	 */
	public AJDTHandler()  {
		//Get the root of the workspace
		workspace = ResourcesPlugin.getWorkspace();
		root = workspace.getRoot();	
		builder = new PriorJBuilder();
	}
	
	/**
	 * Create a new Eclipse Project.
	 * 
	 * @param projectName - the eclipse project name.
	 * 
	 * @return an IProject element.
	 * 
	 * @throws CoreException  
	 */
	public IJavaProject createProject(String projectName) throws CoreException  {
		IProject project = root.getProject(projectName);
		project.create(null);
		project.open(null);
		IProjectDescription description = project.getDescription();
		description.setNatureIds( new String []  {
				JavaCore.NATURE_ID
		});
		project.setDescription(description, null);
		IJavaProject javaProject = JavaCore.create(project);
		IClasspathEntry [] buildPath = new IClasspathEntry[3];
		IFolder bin = project.getFolder("bin");
		
		IFolder folder = project.getFolder("src");
		folder.create(true, true, null);
		
		if (!bin.exists())
			bin.create(true, true, null);
		buildPath[0] = JavaCore.newSourceEntry(project.getFullPath().append("bin"));
		buildPath[1] = JavaCore.newSourceEntry(project.getFullPath().append("src"));
		buildPath[2] = JavaRuntime.getDefaultJREContainerEntry();

		
		return javaProject;
	}
	
	/**
	 * This method do a clone from existing java project.
	 *  
	 * @param originalProjectName An existing java project
	 * @param copyProjectName A name to copied project
	 * 
	 * @throws CoreException 
	 * @throws IOException 
	 */
	public IJavaProject copyProject(String originalProjectName, String copyProjectName, final boolean isOriginal) throws CoreException, IOException {
		IJavaProject copyProject = createProject(copyProjectName);
		String origin = getFullProjectPath(originalProjectName);
		String destiny = getFullProjectPath(copyProjectName);
		copy(origin,destiny);
		refresh(copyProjectName);
		IProject project = getProject(copyProjectName);
		visitor = new ProjectVisitor();
		project.accept(visitor, IResource.NONE);
		// ASPECT FILE HERE.
		copyLibrariesDependencies(copyProjectName, isOriginal);
		convertToAspectJProject(getProject(copyProjectName));
		
		addAjrtToBuildPath(copyProject);
		addCoverageLibraryToBuildPath(copyProject);
		addJavaIOLibraryToBuildPath(copyProject);
		addJsonLibraryToBuildPath(copyProject);
		
		refresh(copyProjectName);
		return copyProject;		
	}
	
	private void addJavaIOLibraryToBuildPath(IJavaProject javaProject) throws JavaModelException  {
		IClasspathEntry [] originalCP = javaProject.getRawClasspath();
		IClasspathEntry lib = JavaCore.newLibraryEntry(new Path("/"+javaProject.getElementName()+"/lib/" + JAVA_IO_LIBRARY),null, null);
		boolean found = false;
		for (int i=0; i< originalCP.length; i++) {
			if (originalCP[i].getPath().equals(lib.getPath())) {
				found = true;
				break;
			}
		}
		if (!found) {
			int originalCPLength = originalCP.length;
			IClasspathEntry [] newCP = new IClasspathEntry[originalCPLength+1];
			System.arraycopy(originalCP, 0, newCP, 0, originalCPLength);
			newCP[originalCPLength] = lib;
			javaProject.setRawClasspath(newCP, new NullProgressMonitor());	
		}
	}

	/**
	 * Copying all files and allowing overwrite.
	 * 
	 * @param origin
	 * @param destiny
	 * @throws IOException
	 */
	private void copy(String origin, String destiny) throws IOException {
		JavaIO.copyAll(new File(origin), new File(destiny),true);
	}
	
	/**
	 * Creating a default build path to AspectJ Project.
	 * 
	 * @param projectName
	 * @return
	 */
	public IClasspathEntry [] createDefaultBuildPath(String projectName)  {
		IClasspathEntry [] buildPath =  {
				JavaRuntime.getDefaultJREContainerEntry() ,
				JavaCore.newLibraryEntry(new Path("/"+projectName+"/lib/" + COVERAGE_LIBRARY),null, null),
				JavaCore.newLibraryEntry(new Path("/"+projectName+"/lib/" + JAVA_IO_LIBRARY),null, null),
				JavaCore.newLibraryEntry(new Path("/"+projectName+"/lib/" + JSON_LIBRARY),null, null),
				junit4Entry
		};
		return buildPath;
	}
	
	/**
	 * This method remove a project from workspace.
	 * 
	 * @param projectName
	 * 
	 * @throws CoreException 
	 */
	public void deleteProject(String projectName) throws CoreException {
		IProject project = getProject(projectName);
		project.delete(true, true, null);
	}
	
	/**
	 * Converts a given project to be an AspectJ Project.
	 * 
	 * @param project
	 * @throws CoreException
	 */
	public static void convertToAspectJProject(IProject project) throws CoreException {
		AJDTUtils.addAspectJNature(project, false);
	}
	
	/**
	 * attempt to update the project's build classpath with the AspectJ runtime library.
	 *   
	 * @param javaProject
	 */
	public static void addAjrtToBuildPath(IJavaProject javaProject) {
		try  {
			IClasspathEntry [] originalCP = javaProject.getRawClasspath();
			IPath ajrtPath = new Path(AspectJPlugin.ASPECTJRT_CONTAINER);
			addNewContainerClasspathEntry(javaProject, originalCP, ajrtPath);	
		}
		catch(JavaModelException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adding source folder entry to build path.
	 * 
	 * @param javaProject
	 * @param sourceFolderName
	 * @throws JavaModelException 
	 */
	public static void addSourceFolderEntryToBuildPath(IJavaProject javaProject, String sourceFolderName) throws JavaModelException {
		IClasspathEntry [] originalCP = javaProject.getRawClasspath();
		IPath sourceFolderPath = new Path(javaProject.getProject().getFullPath()+"/"+sourceFolderName);
		boolean found = false;
		for (int i=0; i< originalCP.length; i++) {
			if (originalCP[i].getPath().lastSegment().equals(sourceFolderPath.lastSegment())) {
				found = true;
				break;
			}
		}
		if (!found) {
			IClasspathEntry entry = JavaCore.newSourceEntry(sourceFolderPath);
			int originalCPLength = originalCP.length;
			IClasspathEntry [] newCP = new IClasspathEntry[originalCPLength+1];
			System.arraycopy(originalCP, 0, newCP, 0, originalCPLength);
			newCP[originalCPLength] = entry;
			javaProject.setRawClasspath(newCP, new NullProgressMonitor());
		}
	}
	
	
	/**
	 * Adding Container JUnit4 to Project.
	 *  
	 * @param javaProject
	 * @throws JavaModelException  
	 */
	public static void addJUnit4ToBuildPath(IJavaProject javaProject) throws JavaModelException  {
			IClasspathEntry [] originalCP = javaProject.getRawClasspath();
			addNewContainerClasspathEntry(javaProject, originalCP,junit4Path);
	}
	
	/**
	 * Adding a new Entry in the project indicated.
	 * 
	 * @param javaProject
	 * @param originalCP
	 * @param cpEntry
	 * @throws JavaModelException
	 */
	private static void addNewContainerClasspathEntry(IJavaProject javaProject, IClasspathEntry[] originalCP, IPath cpEntry) throws JavaModelException  {
		boolean found = false;
		for (int i=0; i< originalCP.length; i++) {
			if (originalCP[i].getPath().equals(cpEntry)) {
				found = true;
				break;
			}
		}
		if (!found) {
			IClasspathEntry entry = JavaCore.newContainerEntry(cpEntry, false);
			int originalCPLength = originalCP.length;
			IClasspathEntry [] newCP = new IClasspathEntry[originalCPLength+1];
			System.arraycopy(originalCP, 0, newCP, 0, originalCPLength);
			newCP[originalCPLength] = entry;
			javaProject.setRawClasspath(newCP, new NullProgressMonitor());
		}
	}
	
	/**
	 * Adding coverage library to project.
	 * 
	 * @param javaProject
	 * @throws JavaModelException
	 */
	public static void addCoverageLibraryToBuildPath(IJavaProject javaProject) throws JavaModelException  {
		IClasspathEntry [] originalCP = javaProject.getRawClasspath();
		IClasspathEntry lib = JavaCore.newLibraryEntry(new Path("/"+javaProject.getElementName()+"/lib/" + COVERAGE_LIBRARY),null, null);
		boolean found = false;
		for (int i = 0; i < originalCP.length; i++)  {
			if (originalCP[i].getPath().equals(lib.getPath()))  {
				found = true;
				break;
			}
		}
		if (!found) {
			int originalCPLength = originalCP.length;
			IClasspathEntry [] newCP = new IClasspathEntry[originalCPLength+1];
			System.arraycopy(originalCP, 0, newCP, 0, originalCPLength);
			newCP[originalCPLength] = lib;
			javaProject.setRawClasspath(newCP, new NullProgressMonitor());	
		}
		
	}
	
	public static void addJsonLibraryToBuildPath(IJavaProject javaProject) throws JavaModelException  {
		IClasspathEntry [] originalCP = javaProject.getRawClasspath();
		IClasspathEntry lib = JavaCore.newLibraryEntry(new Path("/"+javaProject.getElementName()+"/lib/" + JSON_LIBRARY),null, null);
		boolean found = false;
		for (int i=0; i< originalCP.length; i++) {
			if (originalCP[i].getPath().equals(lib.getPath())) {
				found = true;
				break;
			}
		}
		if (!found) {
			int originalCPLength = originalCP.length;
			IClasspathEntry [] newCP = new IClasspathEntry[originalCPLength+1];
			System.arraycopy(originalCP, 0, newCP, 0, originalCPLength);
			newCP[originalCPLength] = lib;
			javaProject.setRawClasspath(newCP, new NullProgressMonitor());	
		}
		
	}
	
	/**
	 * This method add the libraries need for the project to do coverage.
	 * 
	 * @param projectName
	 * @throws CoreException
	 */
	public void copyLibrariesDependencies(String projectName, final boolean isOriginal) throws CoreException {
		IFolder libFolder = getProject(projectName).getFolder("lib");
		IFolder reportFolder = getProject(projectName).getFolder("report");
		try {
			if (libFolder.exists()) {
				copyLibraries(libFolder);
			}else {
				libFolder.create(true, false, null);
				copyLibraries(libFolder);
			}
			if(reportFolder.exists()) {
				copyReportResourcesTo(reportFolder);
			}
			else {
				reportFolder.create(true, false, null);
				copyReportResourcesTo(reportFolder);
			}
			loacateFolderSrcToInsertAspectCode(projectName, isOriginal);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This method find a src folder and move to it the AspectJ code.
	 * 
	 * @param projectName
	 * @throws CoreException 
	 * @throws IOException 
	 */
	private void loacateFolderSrcToInsertAspectCode(String projectName, final boolean isOriginal) throws CoreException, IOException {
		IJavaProject javaProject = JavaCore.create(getProject(projectName));
		for (IClasspathEntry entry : javaProject.getRawClasspath()) {
			//only at the package from the source folder
			if (entry.getEntryKind()== IClasspathEntry.CPE_SOURCE) {
				String []  segments = entry.getPath().segments();
				String folderName="";
				for (int i=1; i<segments.length; i++) {
					folderName += segments[i];
					if (i<segments.length-1)
						folderName += JavaIO.SEPARATOR;
				}
				IFolder srcFolder = getProject(projectName).getFolder(folderName);
				if (!srcFolder.exists()) {
					if(!isOriginal) {
						srcFolder.create(true, false, null);
						copyAspectFile(srcFolder);
					} else {
						srcFolder.create(true, false, null);
						copyAspectFileOriginal(srcFolder);
					}
				}
				else {
					if(!isOriginal) {
						copyAspectFile(srcFolder);
					} else {
						copyAspectFileOriginal(srcFolder);
					}
				}
				break;
			}
			
		}
	}
	
	/**
	 * This method add the aspect file to src folder from project.
	 * 
	 * Works too!
	 * 
	 * Bundle bundle = Platform.getBundle("com.splab.priorj.plugin");
	 * URL url = bundle.getEntry("data/"+ASPECT_FILE);
	 * 
	 * Plugin install name example
	 * com.splab.priorj.plugin_1.0.0.201405280013
	 * 
	 * PLUGIN ID
	 * com.edu.ufcg.splab.priorj
	 * Development Bundle name
	 * com.splab.priorj.plugin
	 * 
	 * @param srcFolder
	 * @throws IOException
	 * @throws CoreException
	 */
	public static void copyAspectFile(IFolder srcFolder) throws IOException, CoreException {
		URL url;
		Bundle bundle = Platform.getBundle("com.splab.priorj.plugin");
		url = bundle.getEntry("data/"+ASPECT_FILE);
		IFile newFile = srcFolder.getFile(ASPECT_FILE);
		InputStream fileStream = url.openConnection().getInputStream();
		newFile.create(fileStream, false, null);
		fileStream.close();
	}
	
	/**
	 * This method add the aspect file original to src folder from project.
	 * 
	 * Works too!
	 * 
	 * Bundle bundle = Platform.getBundle("com.splab.priorj.plugin");
	 * URL url = bundle.getEntry("data/"+ASPECT_ORIGINAL_FILE);
	 * 
	 * Plugin install name example
	 * com.splab.priorj.plugin_1.0.0.201405280013
	 * 
	 * PLUGIN ID
	 * com.edu.ufcg.splab.priorj
	 * Development Bundle name
	 * com.splab.priorj.plugin
	 * 
	 * @param srcFolder
	 * @throws IOException
	 * @throws CoreException
	 */
	public static void copyAspectFileOriginal(IFolder srcFolder) throws IOException, CoreException {
		URL url;
		Bundle bundle = Platform.getBundle("com.splab.priorj.plugin");
		url = bundle.getEntry("data/"+ASPECT_ORIGINAL_FILE);
		IFile newFile = srcFolder.getFile(ASPECT_ORIGINAL_FILE);
		InputStream fileStream = url.openConnection().getInputStream();
		newFile.create(fileStream, false, null);
		fileStream.close();
	}
	
	
	/**
	 * Move the libraries from PriorJ plug-in to project.
	 * 
	 * Works too!
	 * Bundle bundle = Platform.getBundle("com.splab.priorj.plugin");
	 * url = bundle.getEntry("lib/"+library);
	 */
	private void copyLibraries(IFolder libFolder) throws IOException, CoreException {
		URL url;
		Bundle bundle = Platform.getBundle("com.splab.priorj.plugin");
		// Copy the Coverage lib from PriorJ plugin.
		url = bundle.getEntry("lib/" + COVERAGE_LIBRARY);
		IFile newLib = libFolder.getFile(COVERAGE_LIBRARY);
		InputStream fileStream = url.openConnection().getInputStream();
		newLib.create(fileStream, false, null);
		fileStream.close();
		
		// Copy the JavaIO lib from PriorJ plugin.
		url = bundle.getEntry("lib/" + JAVA_IO_LIBRARY);
		newLib = libFolder.getFile(JAVA_IO_LIBRARY);
		fileStream = url.openConnection().getInputStream();
		newLib.create(fileStream, false, null);
		fileStream.close();
		
		// Copy the ObjectDB lib from PriorJ plugin.
		url = bundle.getEntry("lib/" + JSON_LIBRARY);
		newLib = libFolder.getFile(JSON_LIBRARY);
		fileStream = url.openConnection().getInputStream();
		newLib.create(fileStream, false, null);
		fileStream.close();
	}
	/**
	 * Copying Resource to Reports.
	 * @param currentPath
	 * @throws IOException 
	 * @throws CoreException 
	 */
	public void copyReportResourcesTo(IFolder reportFolder) throws IOException, CoreException  {
		URL url;
		Bundle bundle = Platform.getBundle("com.splab.priorj.plugin");
		url = bundle.getEntry("data/report.zip");
		IFile newFile = reportFolder.getFile("report.zip");
		InputStream fileStream = url.openConnection().getInputStream();
		newFile.create(fileStream, false, null);
		fileStream.close();
		
		String location = newFile.getRawLocation().toOSString().replace("report.zip","");
		String file = newFile.getRawLocation().toOSString();
		
		unZipIt(file,location);
		JavaIO.deleteFile(file);
	}

	/**
     * Unzip it
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    private void unZipIt(String zipFileName, String outputFolder) {
		try  {
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile(zipFileName);
			// Extracts all files to the path specified
			zipFile.extractAll(outputFolder);
		} catch (ZipException e)  {
			e.printStackTrace();
		}
    }    
	
	/**
	 * This method open a java file or any other file in the code editor.
	 *   
	 * @param filePath
	 * @throws PartInitException 
	 */
	public void openFileInEditorView(String filePath) throws PartInitException {
		File fileToOpen = new File(filePath);
		if(fileToOpen.exists() && fileToOpen.isFile()) {
			IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		    IDE.openEditorOnFileStore(page, fileStore);
		}
	}
	
	
	
	private void refresh(String projectName) throws CoreException {
		IProject project = getProject(projectName);
		project.refreshLocal(IResource.DEPTH_INFINITE, null);
	}
	
	/**
	 * This method start the process with a full build.
	 * 
	 * @param project
	 * @param projectName
	 * @throws CoreException
	 */
	public void full(IProject project, String projectName) throws CoreException {
		createProject(projectName);
		builder.fullBuild(project, null);		
	}
	
	/**
	 * Get all projects in the workspace.
	 * 
	 * @return An Array of IProject 
	 */
	public IProject [] getProjects() {
		return root.getProjects();
	}
	
	/**
	 * Getting the Visitor.
	 * 
	 * @return
	 */
	public ProjectVisitor getProjectVisitor() {
		return visitor;
	}
	
	/**
	 * Get project by name.
	 * 
	 * @return the project name.
	 */
	public IProject getProject(final String projectName) {
		return root.getProject(projectName);
	}
	
	/**
	 * This method ckeck if a project exist.
	 * 
	 * @param projectName
	 * @return
	 */
	public boolean existProject(final String projectName) {
		return getProject(projectName).exists();
	}
	
	/**
	 * Getting the full path to an eclipse project.
	 * 
	 * @param projectName
	 * @return
	 */
	public String getFullProjectPath(final String projectName) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName).getLocation().toOSString();
	}
	
	/**
	 * Creating a folder into informed project.
	 * 
	 * @param projectName
	 * @param folderName
	 * @throws CoreException 
	 */
	public IFolder createFolder(final String projectName, final String folderName) throws CoreException  {
		IProject myproject = getProject(projectName);
		IFolder folder = myproject.getFolder(folderName);
		if (!folder.exists()) {
			folder.create(true, true, null);
		}
		return folder;
	}
	/**
	 * Creating a new source folder and creating a new Source Folder classpath entry.
	 * 
	 * @param projectName
	 * @param folderName
	 * @return
	 * @throws CoreException
	 */
	public IFolder createSourceFolder(final String projectName, final String folderName) throws CoreException  {
		IProject myproject = getProject(projectName);
		IFolder folder = myproject.getFolder(folderName);
		if (!folder.exists()) {
			folder.create(true, true, null);
		}
		addSourceFolderEntryToBuildPath(JavaCore.create(myproject), folderName);
		return folder;
	}
	
	
	/**
	 * Creating a package.
	 * 
	 * @param projectName
	 * @param sourceFolderName
	 * @param packageName
	 * @throws JavaModelException
	 */
	public IPackageFragment createPackage(final String projectName, final String sourceFolderName, 
			final String packageName) throws JavaModelException  {
		IProject project = getProject(projectName);
		IFolder folder = project.getFolder(sourceFolderName);
		IJavaProject javaProject = JavaCore.create(project);
		IPackageFragmentRoot srcFolder = javaProject.getPackageFragmentRoot(folder);
		IPackageFragment fragment = srcFolder.createPackageFragment(packageName, true, null);
		return fragment;
	}

	/**
	 * Creating a Compilation Unit 
	 * 
	 * @param projectName
	 * @param sourceFolderName
	 * @param packageName
	 * @param className
	 * @param sourceCode
	 * @throws JavaModelException 
	 */
	public ICompilationUnit createCompilationUnit(final String projectName, final String sourceFolderName, 
			final String packageName, final String className, final String sourceCode) throws JavaModelException  {
		IProject project = getProject(projectName);
		IFolder folder = project.getFolder(sourceFolderName);
		IJavaProject javaProject = JavaCore.create(project);
		IPackageFragmentRoot rootFolder = javaProject.getPackageFragmentRoot(folder);
		IPackageFragment pkgFolder = rootFolder.createPackageFragment(packageName, true, null);
		ICompilationUnit cu = pkgFolder.createCompilationUnit(className, sourceCode, false,null);
		return cu;
	}
		
	/**
	 * This method do a merge in two classpaths.
	 * 
	 * @param buildPathOne
	 * @param buildPathTwo
	 * @return
	 */
	public static IClasspathEntry [] mergeClasspath(IClasspathEntry[] buildPathOne,IClasspathEntry[] buildPathTwo) {
		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		doMerge(buildPathOne, entries);
		doMerge(buildPathTwo, entries);
		IClasspathEntry [] cpEntries = new IClasspathEntry[entries.size()];
		cpEntries = entries.toArray(cpEntries); 
		return cpEntries;
	}

	/**
	 * Internal classpath Merge.
	 * 
	 * @param buildPath
	 * @param entries
	 */
	private static void doMerge(IClasspathEntry[] buildPath,List<IClasspathEntry> entries)  {
		List<String> merged = new ArrayList<String>();
		for(IClasspathEntry entry : buildPath) {	
			if (entry.equals(junit3Entry) || entry.equals(junit4Entry) ) {
				if (!entries.contains(junit3Entry) && !entries.contains(junit4Entry))
					entries.add(entry);
			}
			else {
				if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY || entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					boolean found = false;
					for (IClasspathEntry cpe : entries) {
						if (cpe.getPath().lastSegment().equals(entry.getPath().lastSegment())) {
							found = true;
							break;
						}
					}
					if (!found) {
						entries.add(entry);
					}
				}
				else if (!entries.contains(entry)) {
					merged.add(entry.getPath().toOSString());
					entries.add(entry); 
				}
			}
		}
	}

	/**
	 * Saving to eclipse preferences Key and value.
	 * 
	 * @param key
	 * @param value
	 */
	public void saveInPreferences(String key, String value) {
		Preferences preferences = InstanceScope.INSTANCE.getNode("com.edu.ufcg.splab.priorj");
		Preferences sub1 = preferences.node("node1");
	    sub1.put(key, value);
        try  {
          // forces the application to save the preferences
          preferences.flush();
        } catch (BackingStoreException e2)  {
          e2.printStackTrace();
        }
	}
	/**
	 * Getting value from preferences by key.
	 * 
	 * @param key
	 * @return
	 */
	public String getFromPreferences(String key) {
		Preferences preferences = InstanceScope.INSTANCE.getNode("com.edu.ufcg.splab.priorj");
	    Preferences sub1 = preferences.node("node1");
	    return sub1.get(key, "default");
	}
	
}
