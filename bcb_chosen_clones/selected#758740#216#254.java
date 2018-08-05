    private FileInfo inspectFile(String filePath, boolean compress) throws XPIException {
        long length = 0;
        long crc = 0;
        byte[] md5 = new byte[0];
        byte[] sha1 = new byte[0];
        File tmp = new File(baseDir, filePath);
        try {
            tmp = tmp.getCanonicalFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!tmp.exists()) throw new XPIException("  File \'" + tmp.getAbsolutePath() + "\' does not exist.", Main.ERR_FILE_NOT_FOUND);
        try {
            length = tmp.length();
            BufferedInputStream bis = null;
            try {
                bis = new BufferedInputStream(new FileInputStream(tmp));
            } catch (FileNotFoundException e1) {
            }
            digestMD5.reset();
            digestSHA.reset();
            DigestInputStream md5Stream = new DigestInputStream(bis, digestMD5);
            DigestInputStream shaStream = new DigestInputStream(md5Stream, digestSHA);
            CRC32 crc32 = new CRC32();
            byte[] data = new byte[1024 * 2];
            int byteCount;
            while ((byteCount = shaStream.read(data)) > -1) {
                crc32.update(data, 0, byteCount);
            }
            crc = crc32.getValue();
            crc32.reset();
            md5 = md5Stream.getMessageDigest().digest();
            sha1 = shaStream.getMessageDigest().digest();
            bis.close();
        } catch (IOException e1) {
            throw new XPIException("Error reading from \'" + tmp + "\'", Main.ERR_ERROR_READING_FILE);
        }
        return new FileInfo(filePath, length, crc, md5, sha1, compress);
    }
