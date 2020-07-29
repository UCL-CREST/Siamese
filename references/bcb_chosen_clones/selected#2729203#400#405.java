    public void putFile(String path, InputStream is) throws IOException {
        File explodedFile = putFileImpl(path);
        FileOutputStream fos = new FileOutputStream(explodedFile);
        IOUtils.copy(is, fos);
        fos.close();
    }
