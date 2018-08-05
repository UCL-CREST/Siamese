    public static Board readStream(InputStream is) throws IOException {
        StringWriter stringWriter = new StringWriter();
        IOUtils.copy(is, stringWriter);
        String s = stringWriter.getBuffer().toString();
        Board board = read(s);
        return board;
    }
