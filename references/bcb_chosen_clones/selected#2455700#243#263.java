    public static void copyFilesFromDirectory(File fromDirectory, File toDirectory, FileFilter filter) throws IllegalArgumentException, IOException {
        if ((fromDirectory != null) && (toDirectory != null)) {
            if (fromDirectory.isDirectory()) {
                File[] someSourceFiles = fromDirectory.listFiles(filter);
                if ((someSourceFiles != null) && (someSourceFiles.length > 0)) {
                    if (!toDirectory.exists()) {
                        toDirectory.mkdir();
                    }
                    for (int i = 0; i < someSourceFiles.length; i++) {
                        File aFile = someSourceFiles[i];
                        if (aFile.isFile()) {
                            File aToFile = new File(toDirectory, aFile.getName());
                            copyFile(aFile, aToFile);
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("'" + fromDirectory + "' is not a directory");
            }
        }
    }
