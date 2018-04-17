    private static void zipFile(ZipOutputStream out, String stripPath, File file, char pathSeparator) throws IOException {
        ZipEntry ze = new ZipEntry(processPath(file.getPath(), stripPath, pathSeparator));
        ze.setTime(file.lastModified());
        out.putNextEntry(ze);
        byte[] buffer = new byte[8 * 1024];
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file), buffer.length);
        try {
            int count = 0;
            while ((count = in.read(buffer, 0, buffer.length)) >= 0) {
                if (count != 0) {
                    out.write(buffer, 0, count);
                }
            }
        } finally {
            in.close();
        }
    }
