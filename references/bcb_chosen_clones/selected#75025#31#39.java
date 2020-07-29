    protected byte[] getBytesOfFile(final File file) throws FileNotFoundException, IOException {
        byte[] bytes = new byte[8096];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        while (in.read(bytes) > 0) {
            out.write(bytes);
        }
        return out.toByteArray();
    }
