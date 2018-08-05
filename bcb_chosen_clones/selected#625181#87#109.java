    public static void doZip(final String basePath, final String directory, final ZipOutputStream zipOutputStream) throws IOException {
        log.debug("~doZip(..) : About to zip contents of directory [" + directory + "]");
        final File zipDirectory = new File(directory);
        final String[] zipDirectoryList = zipDirectory.list();
        final byte[] readBuffer = new byte[4096];
        int bytesIn = 0;
        for (int dirIdx = 0; dirIdx < zipDirectoryList.length; dirIdx++) {
            final File file = new File(zipDirectory, zipDirectoryList[dirIdx]);
            if (file.isDirectory()) {
                final String filePath = file.getPath();
                doZip(basePath, filePath, zipOutputStream);
                continue;
            }
            final FileInputStream fileInputStream = new FileInputStream(file);
            final String zipFilePath = file.getCanonicalPath().substring(basePath.length() + 1);
            final ZipEntry zipEntry = new ZipEntry(zipFilePath);
            zipOutputStream.putNextEntry(zipEntry);
            while ((bytesIn = fileInputStream.read(readBuffer)) != -1) {
                zipOutputStream.write(readBuffer, 0, bytesIn);
            }
            fileInputStream.close();
        }
    }
