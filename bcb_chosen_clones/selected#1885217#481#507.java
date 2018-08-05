    public File getFile() throws IOException {
        if (file == null) {
            if (position > 0) {
                throw new IOException("Stream is already being read");
            } else {
                file = tmp.createTemporaryFile();
                OutputStream out = new FileOutputStream(file);
                try {
                    IOUtils.copy(in, out);
                } finally {
                    out.close();
                }
                FileInputStream newStream = new FileInputStream(file);
                tmp.addResource(newStream);
                final InputStream oldStream = in;
                in = new BufferedInputStream(newStream) {

                    @Override
                    public void close() throws IOException {
                        oldStream.close();
                    }
                };
                length = file.length();
            }
        }
        return file;
    }
