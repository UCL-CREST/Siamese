    private void copyDirectory(File tempDir, File workDir) {
        if (tempDir.isDirectory()) {
            for (File subFile : tempDir.listFiles()) {
                if (subFile.isDirectory()) {
                    File workFile = new File(workDir, File.separator + subFile.getName());
                    copyDirectory(subFile, workFile);
                } else if (subFile.isFile()) {
                    String tempName = subFile.getName();
                    String workName = tempName.substring(0, tempName.lastIndexOf("."));
                    File workFile = new File(workDir, File.separator + workName);
                    copyFile(tempName, subFile, workFile);
                }
            }
        } else if (tempDir.isFile()) {
            String tempName = tempDir.getName();
            String workName = tempName.substring(0, tempName.lastIndexOf("."));
            File workFile = new File(workDir, File.separator + workName);
            copyFile(tempName, tempDir, workFile);
        }
    }
