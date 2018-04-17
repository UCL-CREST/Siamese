    private final void addFileToZip(ZipOutputStream out, String filename, boolean delete) throws Exception {
        File file = new File(filename);
        FileInputStream fin = new FileInputStream(file);
        ZipEntry entry = new ZipEntry(filename);
        entry.setTime(file.lastModified());
        out.putNextEntry(entry);
        byte[] buffer = new byte[16384];
        int count;
        while ((count = fin.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }
        out.closeEntry();
        fin.close();
        if (delete) {
            file.delete();
        }
    }
