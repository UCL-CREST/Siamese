    private void writeIntoZip(ZipOutputStream output, String name, InputStream input) throws Exception {
        output.putNextEntry(new ZipEntry(name));
        int size = 0;
        byte[] buffer = new byte[1024];
        while ((size = input.read(buffer, 0, buffer.length)) > 0) {
            output.write(buffer, 0, size);
        }
        output.closeEntry();
        input.close();
    }
