    public static void copyDirectory(File source, IFolder destination, IProgressMonitor monitor) {
        if (!source.isDirectory()) return;
        if (!destination.exists()) try {
            destination.create(true, true, monitor);
        } catch (CoreException e) {
            NanoVMUI.log(e);
            return;
        }
        File files[] = source.listFiles();
        for (File file : files) {
            if (file.isFile()) copyFile(file, destination, monitor); else copyDirectory(file, destination.getFolder(file.getName()), monitor);
        }
    }
