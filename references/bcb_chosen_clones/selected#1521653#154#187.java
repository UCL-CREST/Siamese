    private boolean copyFiles(File sourceDir, File destinationDir) {
        boolean result = false;
        try {
            if (sourceDir != null && destinationDir != null && sourceDir.exists() && destinationDir.exists() && sourceDir.isDirectory() && destinationDir.isDirectory()) {
                File sourceFiles[] = sourceDir.listFiles();
                if (sourceFiles != null && sourceFiles.length > 0) {
                    File destFiles[] = destinationDir.listFiles();
                    if (destFiles != null && destFiles.length > 0) {
                        for (int i = 0; i < destFiles.length; i++) {
                            if (destFiles[i] != null) {
                                destFiles[i].delete();
                            }
                        }
                    }
                    for (int i = 0; i < sourceFiles.length; i++) {
                        if (sourceFiles[i] != null && sourceFiles[i].exists() && sourceFiles[i].isFile()) {
                            String fileName = destFiles[i].getName();
                            File destFile = new File(destinationDir.getAbsolutePath() + "/" + fileName);
                            if (!destFile.exists()) destFile.createNewFile();
                            FileInputStream in = new FileInputStream(sourceFiles[i]);
                            FileOutputStream out = new FileOutputStream(destFile);
                            FileChannel fcIn = in.getChannel();
                            FileChannel fcOut = out.getChannel();
                            fcIn.transferTo(0, fcIn.size(), fcOut);
                        }
                    }
                }
            }
            result = true;
        } catch (Exception e) {
            System.out.println("Exception in copyFiles Method : " + e);
        }
        return result;
    }
