    private void appendFile(String resourceName, InputStream inputStream) throws IOException {
        zos.putNextEntry(new ZipEntry(resourceName));
        int length;
        while ((length = inputStream.read(buffer)) != -1) zos.write(buffer, 0, length);
        zos.closeEntry();
    }
