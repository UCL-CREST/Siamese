    private boolean putFile(String filePath, boolean pathnames) throws IOException {
        File file = new File(filePath);
        FileInputStream fIn = new FileInputStream(file);
        BufferedInputStream bIn = new BufferedInputStream(fIn);
        ZipEntry fileEntry = null;
        if (pathnames) {
            int rootSeparator = filePath.indexOf(System.getProperty("file.separator"));
            fileEntry = new ZipEntry(filePath.substring(rootSeparator + 1));
        } else {
            String fileName = file.getName();
            int rootSeparator = fileName.indexOf(System.getProperty("file.separator"));
            fileEntry = new ZipEntry(fileName.substring(rootSeparator + 1));
        }
        this.zipOut.putNextEntry(fileEntry);
        int bufferSize = 1024;
        byte[] data = new byte[bufferSize];
        int byteCount;
        while ((byteCount = bIn.read(data, 0, bufferSize)) > -1) {
            this.zipOut.write(data, 0, byteCount);
        }
        this.zipOut.closeEntry();
        return true;
    }
