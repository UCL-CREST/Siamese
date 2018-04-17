    public static void mergeZipAndDirToJar(String originZipFile, String originDir, ZipOutputStream zos) {
        try {
            FileInputStream fis = new FileInputStream(originZipFile);
            CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum));
            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null) {
                JarEntry newEntry = new JarEntry(entry.getName());
                zos.putNextEntry(newEntry);
                byte[] readBuffer = new byte[1024];
                int bytesIn = 0;
                while ((bytesIn = zis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                zos.closeEntry();
            }
            zis.close();
            File.zipDir(originDir, "", zos);
        } catch (Exception e) {
        }
    }
