    public static void addZipEntry(ZipOutputStream zout, BufferedInputStream bin, String zipEntryName) throws FileNotFoundException, IOException {
        byte[] bytes = new byte[4096];
        int bytesRead = 0;
        zipEntryName = LibJarAjar.replaceSubstring(zipEntryName, File.separator, "/");
        ZipEntry entry = new ZipEntry(zipEntryName);
        zout.putNextEntry(entry);
        System.out.println("Adding: " + zipEntryName);
        while ((bytesRead = bin.read(bytes)) != -1) {
            zout.write(bytes, 0, bytesRead);
        }
        if (bin != null) bin.close();
        zout.closeEntry();
    }
