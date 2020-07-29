    public static void zipFile(String from, String to) throws IOException {
        FileInputStream in = new FileInputStream(from);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(to));
        out.putNextEntry(new ZipEntry(getFilename(from)));
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        in.close();
        out.closeEntry();
        out.close();
    }
