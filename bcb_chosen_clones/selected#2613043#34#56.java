    public Page(String id, byte[] data) {
        this.id = id;
        try {
            ZipOutputStream oZipOutputStream;
            ByteArrayOutputStream oByteArrayOutputStream;
            oByteArrayOutputStream = new ByteArrayOutputStream();
            oZipOutputStream = new ZipOutputStream(oByteArrayOutputStream);
            oZipOutputStream.putNextEntry(new ZipEntry(Page.class.getName()));
            oZipOutputStream.write(data);
            oZipOutputStream.finish();
            oZipOutputStream.flush();
            oZipOutputStream.closeEntry();
            oZipOutputStream.close();
            oByteArrayOutputStream.close();
            this.data = oByteArrayOutputStream.toByteArray();
            this.zipped = true;
        } catch (IOException e) {
            logger.error("Error while zipping the page", e);
            this.data = data;
            this.zipped = false;
        }
        this.properties = new HashMap();
    }
