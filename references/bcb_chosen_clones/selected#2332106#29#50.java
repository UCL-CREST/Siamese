    @Override
    public void close() throws IOException {
        super.close();
        byte[] signatureData = toByteArray();
        ZipOutputStream zipOutputStream = new ZipOutputStream(this.targetOutputStream);
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(this.originalZipFile));
        ZipEntry zipEntry;
        while (null != (zipEntry = zipInputStream.getNextEntry())) {
            if (!zipEntry.getName().equals(ODFUtil.SIGNATURE_FILE)) {
                ZipEntry newZipEntry = new ZipEntry(zipEntry.getName());
                zipOutputStream.putNextEntry(newZipEntry);
                LOG.debug("copying " + zipEntry.getName());
                IOUtils.copy(zipInputStream, zipOutputStream);
            }
        }
        zipInputStream.close();
        zipEntry = new ZipEntry(ODFUtil.SIGNATURE_FILE);
        LOG.debug("writing " + zipEntry.getName());
        zipOutputStream.putNextEntry(zipEntry);
        IOUtils.write(signatureData, zipOutputStream);
        zipOutputStream.close();
    }
