    @Override
    public void listener(UploadEvent event) throws Exception {
        this.log.debug("listener");
        UploadItem item = event.getUploadItem();
        this.log.debug("filename: #0", item.getFileName());
        this.filename = item.getFileName();
        this.log.debug("content type: #0", item.getContentType());
        String extension = FilenameUtils.getExtension(this.filename).toLowerCase();
        this.contentType = supportedFileExtensions.get(extension);
        if (null == this.contentType) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            ZipEntry zipEntry = new ZipEntry(this.filename);
            zipOutputStream.putNextEntry(zipEntry);
            IOUtils.write(item.getData(), zipOutputStream);
            zipOutputStream.close();
            this.filename = FilenameUtils.getBaseName(this.filename) + ".zip";
            this.document = outputStream.toByteArray();
            this.contentType = "application/zip";
            return;
        }
        this.log.debug("file size: #0", item.getFileSize());
        this.log.debug("data bytes available: #0", (null != item.getData()));
        if (null != item.getData()) {
            this.document = item.getData();
            return;
        }
        File file = item.getFile();
        if (null != file) {
            this.log.debug("tmp file: #0", file.getAbsolutePath());
            this.document = FileUtils.readFileToByteArray(file);
        }
    }
