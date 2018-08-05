    public String storeUploadedZip(byte[] zip, String name) {
        List filesToStore = new ArrayList();
        int i = 0;
        ZipInputStream zipIs = new ZipInputStream(new ByteArrayInputStream(zip));
        ZipEntry zipEntry = zipIs.getNextEntry();
        while (zipEntry != null) {
            if (zipEntry.isDirectory() == false) {
                i++;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtils.copy(zipIs, baos);
                baos.close();
            }
            zipIs.closeEntry();
            zipEntry = zipIs.getNextEntry();
        }
    }
