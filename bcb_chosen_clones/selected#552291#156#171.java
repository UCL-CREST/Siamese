    private long getCompressedSize(File file) throws IOException {
        ZipEntry entry = new ZipEntry(file.getName());
        entry.setMethod(ZipEntry.DEFLATED);
        ZipOutputStream out = new ZipOutputStream(new IdleOutputStream());
        out.setLevel(COMPRESS_LEVEL);
        out.putNextEntry(entry);
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
        int b;
        while ((b = input.read()) != -1) {
            out.write(b);
        }
        input.close();
        out.closeEntry();
        out.close();
        return entry.getCompressedSize();
    }
