    public void transferTo(File file) throws IOException, IllegalStateException {
        OutputStream out = new FileOutputStream(file);
        IOUtils.copy(getInputStream(), out);
        out.close();
    }
