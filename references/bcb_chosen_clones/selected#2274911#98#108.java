    protected void addFile(String path, File file, ZipOutputStream zos) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ZipEntry fileEntry = new ZipEntry(path);
        zos.putNextEntry(fileEntry);
        byte[] data = new byte[1024];
        int byteCount;
        while ((byteCount = bis.read(data, 0, 1024)) > -1) {
            zos.write(data, 0, byteCount);
        }
    }
