    private static void writeDataFile(ZipOutputStream myZip, String inputFile) throws Exception {
        final int BUFFER = 4096;
        int count;
        byte data[] = new byte[BUFFER];
        ByteArrayOutputStream myBuffer = new ByteArrayOutputStream();
        ZipOutputStream myDataZip = new ZipOutputStream(myBuffer);
        myDataZip.putNextEntry(new ZipEntry(inputFile));
        FileInputStream myDataFile = new FileInputStream(inputFile);
        while ((count = myDataFile.read(data, 0, BUFFER)) != -1) {
            myDataZip.write(data, 0, count);
        }
        myDataZip.closeEntry();
        myDataZip.close();
        myZip.putNextEntry(new ZipEntry("data"));
        ByteArrayInputStream myBufferReader = new ByteArrayInputStream(myBuffer.toByteArray());
        int j = 0;
        while ((j = myBufferReader.read()) != -1) {
            myZip.write(j);
        }
        myZip.closeEntry();
    }
