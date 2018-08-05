    public void compress() throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(this.outputFile));
        ZipEntry zipEntry = new ZipEntry(this.inputFile.getName());
        FileInputStream fileInputStream;
        CRC32 crc = new CRC32();
        byte[] bytes = new byte[1000];
        int numBytes;
        zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
        fileInputStream = new FileInputStream(this.inputFile);
        while ((numBytes = fileInputStream.read(bytes)) > -1) {
            crc.update(bytes, 0, numBytes);
        }
        fileInputStream.close();
        zipEntry.setSize(this.inputFile.length());
        zipEntry.setTime(this.inputFile.lastModified());
        zipEntry.setCrc(crc.getValue());
        zipEntry.setComment("Product File");
        zipOutputStream.putNextEntry(zipEntry);
        fileInputStream = new FileInputStream(this.inputFile);
        while ((numBytes = fileInputStream.read(bytes)) > -1) {
            zipOutputStream.write(bytes, 0, numBytes);
        }
        fileInputStream.close();
        zipOutputStream.closeEntry();
        zipOutputStream.finish();
    }
