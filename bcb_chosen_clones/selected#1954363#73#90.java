    private void addFile(final ZipOutputStream out, final File parent, final File file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        String name = null;
        if (parent != null) {
            name = parent.getName() + ZipWriter.DIR_SEPARATOR + file.getName();
        } else {
            name = file.getName();
        }
        out.putNextEntry(new ZipEntry(name));
        byte[] buf = new byte[1024];
        int len = input.read(buf);
        while (len > 0) {
            out.write(buf, 0, len);
            len = input.read(buf);
        }
        out.closeEntry();
        input.close();
    }
