    private void downloadFile(File target, String s3key) throws IOException, S3ServiceException {
        InputStream in = downloadData(s3key);
        if (in == null) {
            throw new IOException("No data found");
        }
        in = new InflaterInputStream(new CryptInputStream(in, cipher, getDataEncryptionKey()));
        File temp = File.createTempFile("dirsync", null);
        FileOutputStream fout = new FileOutputStream(temp);
        try {
            IOUtils.copy(in, fout);
            if (target.exists()) {
                target.delete();
            }
            IOUtils.closeQuietly(fout);
            IOUtils.closeQuietly(in);
            FileUtils.moveFile(temp, target);
        } catch (IOException e) {
            fetchStream(in);
            throw e;
        } finally {
            IOUtils.closeQuietly(fout);
            IOUtils.closeQuietly(in);
        }
    }
