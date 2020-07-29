    private void recurseTmpDir(final String parentPath, final File parentDir, final ZipPathData zpd, final ZipOutputStream zOut) {
        String filePath = parentPath;
        if (parentDir.isDirectory()) {
            File[] subDirs = parentDir.listFiles();
            for (File subDir : subDirs) {
                recurseTmpDir(filePath + "/" + subDir.getName(), subDir, zpd, zOut);
            }
        } else {
            File file = parentDir;
            ZipEntry zipEntry = new ZipEntry(filePath);
            if (zipEntry == null) {
                zipEntry = new ZipEntry(filePath);
            }
            try {
                zOut.putNextEntry(zipEntry);
                FileInputStream fInput = new FileInputStream(file);
                byte[] buffer = new byte[10000];
                while (fInput.available() > 0) {
                    int byteRead = fInput.read(buffer);
                    zOut.write(buffer, 0, byteRead);
                }
                fInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
