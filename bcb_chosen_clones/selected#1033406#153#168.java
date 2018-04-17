    public void deployDir(File srcDir, String destDir) {
        File[] dirFiles = srcDir.listFiles();
        for (int k = 0; dirFiles != null && k < dirFiles.length; k++) {
            if (!dirFiles[k].getName().startsWith(".")) {
                if (dirFiles[k].isFile()) {
                    File deployFile = new File(destDir + File.separator + dirFiles[k].getName());
                    if (dirFiles[k].lastModified() != deployFile.lastModified() || dirFiles[k].length() != deployFile.length()) {
                        IOUtils.copy(dirFiles[k], deployFile);
                    }
                } else if (dirFiles[k].isDirectory()) {
                    String newDestDir = destDir + File.separator + dirFiles[k].getName();
                    deployDir(dirFiles[k], newDestDir);
                }
            }
        }
    }
