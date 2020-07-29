    public static void addFileToZip(File file, String entryName, ZipOutputStream zOut) throws IOException {
        if (file.isDirectory()) {
            return;
        }
        int bytesRead;
        final int bufSize = 8192;
        byte buf[] = new byte[bufSize];
        ZipEntry zipEntry = new ZipEntry(entryName);
        zipEntry.setTime(file.lastModified());
        try {
            zOut.putNextEntry(zipEntry);
        } catch (IOException ex) {
            return;
        }
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file), bufSize);
        while ((bytesRead = in.read(buf)) != -1) {
            zOut.write(buf, 0, bytesRead);
        }
        zOut.closeEntry();
        in.close();
    }
