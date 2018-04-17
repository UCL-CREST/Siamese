    public Reader transform(Reader reader, Map<String, Object> parameterMap) {
        try {
            File file = File.createTempFile("srx2", ".srx");
            file.deleteOnExit();
            Writer writer = getWriter(getFileOutputStream(file.getAbsolutePath()));
            transform(reader, writer, parameterMap);
            writer.close();
            Reader resultReader = getReader(getFileInputStream(file.getAbsolutePath()));
            return resultReader;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
