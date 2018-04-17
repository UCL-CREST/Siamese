    public void shouldAllowClosingInputStreamTwice() throws IOException {
        OutputStream outputStream = fileSystem.createOutputStream(_("hello"), OutputMode.OVERWRITE);
        outputStream.write(new byte[] { 1, 2, 3 });
        outputStream.close();
        InputStream inputStream = fileSystem.createInputStream(_("hello"));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, buffer);
        inputStream.close();
        inputStream.close();
    }
