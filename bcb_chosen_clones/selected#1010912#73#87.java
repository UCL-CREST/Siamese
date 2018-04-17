    private static void writeEntry(ZipOutputStream zip, String name, InputStream in) throws IOException {
        zip.putNextEntry(new ZipEntry(name));
        try {
            byte[] buffer = new byte[4096];
            for (int n = in.read(buffer); n != -1; n = in.read(buffer)) {
                zip.write(buffer, 0, n);
            }
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
        zip.closeEntry();
    }
