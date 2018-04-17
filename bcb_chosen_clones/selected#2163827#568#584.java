    private byte[] thumbnail(String fileName) throws IOException {
        LOGGER.debug("Getting thumbnail");
        if (fileName == null) return null;
        File file = new File(fileName);
        if (!file.exists()) return null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        byte[] buf = new byte[4096];
        int len = -1;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        out.flush();
        out.close();
        in.close();
        return out.toByteArray();
    }
