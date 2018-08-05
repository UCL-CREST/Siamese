    private void zipGeneratedFiles(String internalPath, File dirZip, ZipOutputStream zipOut) throws IOException {
        byte[] buf = new byte[4096];
        File[] fileArray = dirZip.listFiles();
        String fileName = "";
        if (fileArray != null) {
            for (int i = 0; i < fileArray.length; i++) {
                fileName = fileArray[i].getName();
                if (fileArray[i].isDirectory() && !fileName.startsWith(".")) {
                } else if (!fileArray[i].isDirectory() && !fileName.endsWith(".zip")) {
                    FileInputStream inFile = new FileInputStream(fileArray[i]);
                    zipOut.putNextEntry(new ZipEntry(internalPath + fileName));
                    int len;
                    while ((len = inFile.read(buf)) > 0) {
                        zipOut.write(buf, 0, len);
                    }
                    inFile.close();
                }
            }
        }
    }
