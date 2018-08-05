    @Override
    public byte[] read(String path) throws PersistenceException {
        path = fmtPath(path);
        try {
            S3Object fileObj = s3Service.getObject(bucketObj, path);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(fileObj.getDataInputStream(), out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new PersistenceException("fail to read s3 file - " + path, e);
        }
    }
