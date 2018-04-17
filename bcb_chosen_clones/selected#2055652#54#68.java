    private static void addFileToJar(File file, ZipOutputStream targetStream, String string) throws IOException {
        String dataFileName = string;
        InputStream is = new FileInputStream(file);
        BufferedInputStream sourceStream = new BufferedInputStream(is);
        ZipEntry theEntry = new ZipEntry(dataFileName);
        targetStream.putNextEntry(theEntry);
        byte[] data = new byte[1024];
        int bCnt;
        while ((bCnt = sourceStream.read(data, 0, 1024)) != -1) {
            targetStream.write(data, 0, bCnt);
        }
        targetStream.flush();
        targetStream.closeEntry();
        sourceStream.close();
    }
