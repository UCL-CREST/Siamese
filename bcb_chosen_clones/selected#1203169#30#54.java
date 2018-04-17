    private void zipFolder(File aDataFolder, ZipOutputStream aZipOutputStream, String aBasePath) {
        File[] lFiles = aDataFolder.listFiles();
        int lFilesCount = lFiles.length;
        for (int i = 0; i < lFilesCount; i++) {
            File lFile = lFiles[i];
            if (lFile.isDirectory()) {
                zipFolder(lFile, aZipOutputStream, aBasePath + "/" + lFile.getName());
            } else {
                try {
                    aZipOutputStream.putNextEntry(new ZipEntry(aBasePath + "/" + lFile.getName()));
                    byte[] lData = new byte[1024];
                    FileInputStream lFileInputStream = new FileInputStream(lFile);
                    int lReadLength = lFileInputStream.read(lData);
                    while (lReadLength > 0) {
                        aZipOutputStream.write(lData, 0, lReadLength);
                        lReadLength = lFileInputStream.read(lData);
                    }
                    lFileInputStream.close();
                    aZipOutputStream.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
