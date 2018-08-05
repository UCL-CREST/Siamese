    public boolean storeDataForTransactionIdentifier(String transactionIdentifier, InputStream inputStream, int inputLength) {
        try {
            if (true) {
                System.out.println("This does not work because Java 1.5 does not support appending to zip files");
                return false;
            }
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(this.zipFilePath, true));
            ZipEntry entry = new ZipEntry(ArchiveFileSupport.fileNameForTransactionIdentifier(transactionIdentifier));
            zipOutputStream.putNextEntry(entry);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, length);
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
