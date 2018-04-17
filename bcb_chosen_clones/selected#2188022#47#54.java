    private void copyFile(URL from, File to) {
        try {
            InputStream is = from.openStream();
            IOUtils.copy(is, new FileOutputStream(to));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
