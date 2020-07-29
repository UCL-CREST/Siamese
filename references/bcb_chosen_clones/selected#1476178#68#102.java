    @Override
    public File fetchHSMFile(String fsID, String filePath) throws HSMException {
        log.debug("fetchHSMFile called with fsID=" + fsID + ", filePath=" + filePath);
        if (absIncomingDir.mkdirs()) {
            log.info("M-WRITE " + absIncomingDir);
        }
        File tarFile;
        try {
            tarFile = File.createTempFile("hsm_", ".tar", absIncomingDir);
        } catch (IOException x) {
            throw new HSMException("Failed to create temp file in " + absIncomingDir, x);
        }
        log.info("Fetching " + filePath + " from cloud storage");
        FileOutputStream fos = null;
        try {
            if (s3 == null) createClient();
            S3Object object = s3.getObject(new GetObjectRequest(bucketName, filePath));
            fos = new FileOutputStream(tarFile);
            IOUtils.copy(object.getObjectContent(), fos);
        } catch (AmazonClientException ace) {
            s3 = null;
            throw new HSMException("Could not list objects for: " + filePath, ace);
        } catch (Exception x) {
            throw new HSMException("Failed to retrieve " + filePath, x);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error("Couldn't close output stream for: " + tarFile);
                }
            }
        }
        return tarFile;
    }
