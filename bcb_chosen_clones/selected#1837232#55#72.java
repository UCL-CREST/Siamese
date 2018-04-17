    public byte[] getFile(final String file) throws IOException {
        if (this.files.contains(file)) {
            ZipInputStream input = new ZipInputStream(new ByteArrayInputStream(this.bytes));
            ZipEntry entry = input.getNextEntry();
            while (entry != null) {
                entry = input.getNextEntry();
                if ((entry.getName().equals(file)) && (!entry.isDirectory())) {
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    IOUtils.copy(input, output);
                    output.close();
                    input.close();
                    return output.toByteArray();
                }
            }
            input.close();
        }
        return null;
    }
