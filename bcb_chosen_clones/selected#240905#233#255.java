    private boolean writeAditionalEntry(ZipOutputStream zipOut, ExportFileEntry file) throws IOException {
        if (!StringUtils.hasValue(file.getHref())) {
            logger.severe("Missing href for additional export entry " + "when exporting file " + dest);
            return false;
        }
        if (!StringUtils.hasValue(file.getFilename())) {
            logger.severe("Missing filename for additional export entry " + "when exporting file " + dest);
            return false;
        }
        byte[] data;
        String uri = getAdditionalEntryUri(file);
        try {
            data = ctx.getWebServer().getRequest(uri, true);
        } catch (IOException ioe) {
            logger.severe("Encountered exception when exporting " + uri + " for export file " + dest);
            ioe.printStackTrace();
            return false;
        }
        zipOut.putNextEntry(new ZipEntry(file.getFilename()));
        zipOut.write(data);
        zipOut.closeEntry();
        return true;
    }
