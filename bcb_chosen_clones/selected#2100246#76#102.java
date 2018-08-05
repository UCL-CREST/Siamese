    private File uploadToTmp() {
        if (fileFileName == null) {
            return null;
        }
        File tmpFile = dataDir.tmpFile(shortname, fileFileName);
        log.debug("Uploading dwc archive file for new resource " + shortname + " to " + tmpFile.getAbsolutePath());
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(file);
            output = new FileOutputStream(tmpFile);
            IOUtils.copy(input, output);
            output.flush();
            log.debug("Uploaded file " + fileFileName + " with content-type " + fileContentType);
        } catch (IOException e) {
            log.error(e);
            return null;
        } finally {
            if (output != null) {
                IOUtils.closeQuietly(output);
            }
            if (input != null) {
                IOUtils.closeQuietly(input);
            }
        }
        return tmpFile;
    }
