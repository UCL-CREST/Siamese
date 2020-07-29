    private void addToZipFile(ZipOutputStream zipOut, File file) throws IOException {
        byte[] data = new byte[BUFFER_SIZE];
        FileInputStream fileStream = new FileInputStream(file);
        BufferedInputStream bufferedFileStream = new BufferedInputStream(fileStream, BUFFER_SIZE);
        ZipEntry entry = new ZipEntry(file.getName());
        zipOut.putNextEntry(entry);
        int count = 0;
        while ((count = bufferedFileStream.read(data, 0, BUFFER_SIZE)) != -1) {
            zipOut.write(data, 0, count);
        }
        zipOut.flush();
        bufferedFileStream.close();
    }
