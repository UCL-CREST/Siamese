    public static void copyURLToFile(URL source, File destination) throws IOException {
        InputStream input = source.openStream();
        try {
            FileOutputStream output = openOutputStream(destination);
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.close(output);
            }
        } finally {
            IOUtils.close(input);
        }
    }
