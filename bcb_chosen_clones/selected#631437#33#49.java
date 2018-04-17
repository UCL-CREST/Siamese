    public void createZipFile(final String inputFileName, final String zipFileName) {
        try {
            FileInputStream inStream = new FileInputStream(inputFileName);
            ZipOutputStream outStream = new ZipOutputStream(new FileOutputStream(zipFileName));
            outStream.putNextEntry(new ZipEntry(inputFileName));
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, bytesRead);
            }
            outStream.closeEntry();
            outStream.close();
            inStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
