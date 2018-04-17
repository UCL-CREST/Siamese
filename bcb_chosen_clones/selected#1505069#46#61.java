    @Override
    protected void handleFile(File file, int depth, Collection results) throws IOException {
        String path = "";
        File parent = file.getParentFile();
        for (int i = 0; i < depth - 1; i++) {
            path = parent.getName() + "/" + path;
            parent = parent.getParentFile();
        }
        ZipEntry entry = new ZipEntry(path + file.getName());
        output.putNextEntry(entry);
        int size = (int) file.length();
        byte[] buffer = new byte[size];
        FileInputStream fis = new FileInputStream(file);
        fis.read(buffer);
        output.write(buffer);
    }
