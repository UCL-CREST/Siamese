    private String copyAndHash(InputStream input, File into) throws IOException {
        MessageDigest digest = createMessageDigest();
        DigestInputStream dis = new DigestInputStream(input, digest);
        IOException ex;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(into);
            IOUtils.copyLarge(dis, fos);
            byte[] hash = digest.digest();
            Formatter formatter = new Formatter();
            for (byte b : hash) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        } catch (IOException e) {
            ex = e;
        } finally {
            IOUtils.closeQuietly(dis);
            IOUtils.closeQuietly(fos);
        }
        if (logger.isWarnEnabled()) logger.warn("Couldn't retrieve data from input!", ex);
        deleteTempFile(into);
        throw ex;
    }
