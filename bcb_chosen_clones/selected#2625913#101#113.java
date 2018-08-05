    private static void addFile(ZipOutputStream out, File parent, File file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        String name = (parent != null ? parent.getName() + ZipWriter.SEPARATOR : ZipWriter.BLANK) + file.getName();
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
