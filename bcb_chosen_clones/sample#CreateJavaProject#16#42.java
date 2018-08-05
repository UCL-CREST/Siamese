	public static IProject CreateJavaProject(String name, IPath classpath) throws CoreException {
		// Create and Open New Project in Workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject(name);
		project.create(null);
		project.open(null);
		
		// Add Java Nature to new Project
		IProjectDescription desc = project.getDescription();
		desc.setNatureIds(new String[] { JavaCore.NATURE_ID});
		project.setDescription(desc, null);
		
		// Get Java Project Object
		IJavaProject javaProj = JavaCore.create(project);
		
		// Set Output Folder
		IFolder binDir = project.getFolder("bin");
		IPath binPath = binDir.getFullPath();
		javaProj.setOutputLocation(binPath, null);
		
		// Set Project's Classpath
		IClasspathEntry cpe = JavaCore.newLibraryEntry(classpath, null, null);
		javaProj.setRawClasspath(new IClasspathEntry[] {cpe}, null);
		
		return project;
	}
