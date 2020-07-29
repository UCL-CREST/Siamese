    public static void copy(File src, File dest) throws IOException {
        if (!src.exists()) {
            throw new IOException(StaticUtils.format(OStrings.getString("LFC_ERROR_FILE_DOESNT_EXIST"), new Object[] { src.getAbsolutePath() }));
        }
        FileInputStream fis = new FileInputStream(src);
        dest.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(dest);
        byte[] b = new byte[BUFSIZE];
        int readBytes;
        while ((readBytes = fis.read(b)) > 0) fos.write(b, 0, readBytes);
        fis.close();
        fos.close();
    }
