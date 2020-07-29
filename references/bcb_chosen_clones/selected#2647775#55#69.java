    public static String computeMD5(InputStream input) {
        InputStream digestStream = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            digestStream = new DigestInputStream(input, md5);
            IOUtils.copy(digestStream, new NullOutputStream());
            return new String(Base64.encodeBase64(md5.digest()), "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(digestStream);
        }
    }
