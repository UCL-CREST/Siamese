    protected void doDownload(S3Bucket bucket, S3Object s3object) throws Exception {
        String key = s3object.getKey();
        key = trimPrefix(key);
        String[] path = key.split("/");
        String fileName = path[path.length - 1];
        String dirPath = "";
        for (int i = 0; i < path.length - 1; i++) {
            dirPath += path[i] + "/";
        }
        File outputDir = new File(downloadFileOutputDir + "/" + dirPath);
        if (outputDir.exists() == false) {
            outputDir.mkdirs();
        }
        File outputFile = new File(outputDir, fileName);
        long size = s3object.getContentLength();
        if (outputFile.exists() && outputFile.length() == size) {
            return;
        }
        long startTime = System.currentTimeMillis();
        log.info("Download start.S3 file=" + s3object.getKey() + " local file=" + outputFile.getAbsolutePath());
        FileOutputStream fout = null;
        S3Object dataObject = null;
        try {
            fout = new FileOutputStream(outputFile);
            dataObject = s3.getObject(bucket, s3object.getKey());
            InputStream is = dataObject.getDataInputStream();
            IOUtils.copyStream(is, fout);
            downloadedFileList.add(key);
            long downloadTime = System.currentTimeMillis() - startTime;
            log.info("Download complete.Estimete time=" + downloadTime + "ms " + IOUtils.toBPSText(downloadTime, size));
        } catch (Exception e) {
            log.error("Download fail. s3 file=" + key, e);
            outputFile.delete();
            throw e;
        } finally {
            IOUtils.closeNoException(fout);
            if (dataObject != null) {
                dataObject.closeDataInputStream();
            }
        }
    }
