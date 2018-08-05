    public OutputStream getAsOutputStream() throws IOException {
        OutputStream out;
        if (contentStream != null) {
            File tmp = File.createTempFile(getId(), null);
            out = new FileOutputStream(tmp);
            IOUtils.copy(contentStream, out);
        } else {
            out = new ByteArrayOutputStream();
            out.write(getContent());
        }
        return out;
    }
