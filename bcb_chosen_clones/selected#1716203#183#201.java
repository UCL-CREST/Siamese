    public void copyDirectory(File inputDir, String outputDir, boolean recursive) throws IOException {
        if (inputDir.exists()) {
            if (inputDir.isDirectory()) {
                File[] files = listAllFiles(inputDir, false);
                File dir = new File(outputDir);
                if (!dir.exists()) dir.mkdir();
                outputDir += System.getProperty("file.separator") + inputDir.getName();
                dir = new File(outputDir);
                if (!dir.exists()) dir.mkdir();
                for (int i = 0; i < files.length; i++) copyFile(files[i], new File(dir, files[i].getName()));
                if (recursive) {
                    File[] dirs = listDirectories(inputDir, false);
                    for (int i = 0; i < dirs.length; i++) {
                        copyDirectory(dirs[i], outputDir, recursive);
                    }
                }
            } else throw new IOException(inputDir + " is not a directory.");
        } else throw new IOException(inputDir + " does not exist.");
    }
