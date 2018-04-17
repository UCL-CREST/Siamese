    private void writeJar(File f, String relativePath) throws IOException {
        ZipEntry entry = new ZipEntry(relativePath);
        outputStream.putNextEntry(entry);
        FileInputStream in = new FileInputStream(f);
        int n;
        while (true) {
            n = in.read(buf);
            if (n <= 0) {
                break;
            }
            outputStream.write(buf, 0, n);
        }
        in.close();
        outputStream.flush();
        outputStream.closeEntry();
    }
