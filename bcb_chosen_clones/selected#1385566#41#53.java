    public File createTemporaryFile() throws IOException {
        URL url = clazz.getResource(resource);
        if (url == null) {
            throw new IOException("No resource available from '" + clazz.getName() + "' for '" + resource + "'");
        }
        String extension = getExtension(resource);
        String prefix = "resource-temporary-file-creator";
        File file = File.createTempFile(prefix, extension);
        InputStream input = url.openConnection().getInputStream();
        FileOutputStream output = new FileOutputStream(file);
        com.volantis.synergetics.io.IOUtils.copyAndClose(input, output);
        return file;
    }
