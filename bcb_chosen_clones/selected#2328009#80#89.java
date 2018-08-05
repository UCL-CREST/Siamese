    protected File compress(File orig, IWrapCompression wrapper) throws IOException {
        File compressed = File.createTempFile("test.", ".gz");
        FileOutputStream fos = new FileOutputStream(compressed);
        OutputStream wos = wrapper.wrap(fos);
        FileInputStream fis = new FileInputStream(orig);
        IOUtils.copy(fis, wos);
        IOUtils.closeQuietly(fis);
        IOUtils.closeQuietly(wos);
        return compressed;
    }
