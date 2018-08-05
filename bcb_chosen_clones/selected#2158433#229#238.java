    private void addToZip(File file, ZipOutputStream zos) throws IOException {
        ZipEntry entry = new ZipEntry(file.getName());
        zos.putNextEntry(entry);
        FileInputStream is = new FileInputStream(file);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        is.read(bytes);
        is.close();
        zos.write(bytes, 0, size);
    }
