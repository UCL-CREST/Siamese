    private void copyDirectory(File srcDir, File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                writeFile = dstDir.mkdir();
            }
            if (writeFile) {
                String[] listDir = srcDir.list();
                for (int i = 0; i < listDir.length; i++) {
                    copyDirectory(new File(srcDir, listDir[i]), new File(dstDir, listDir[i]));
                }
            } else return;
        } else {
            copyFile(srcDir, dstDir);
        }
    }
