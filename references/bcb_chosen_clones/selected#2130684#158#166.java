    public void copy(String original, String copy) throws SQLException {
        try {
            OutputStream out = openFileOutputStream(copy, false);
            InputStream in = openFileInputStream(original);
            IOUtils.copyAndClose(in, out);
        } catch (IOException e) {
            throw Message.convertIOException(e, "Can not copy " + original + " to " + copy);
        }
    }
