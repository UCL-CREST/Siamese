    public static void addJarContentsToZip(String library, ZipOutputStream zos) {
        try {
            FileInputStream fis = new FileInputStream(library);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.getName().contains("META-INF/") || entry.getName().contains("services")) {
                    try {
                        zos.putNextEntry(entry);
                        byte[] readBuffer = new byte[1024];
                        int bytesIn = 0;
                        while ((bytesIn = zis.read(readBuffer)) != -1) {
                            zos.write(readBuffer, 0, bytesIn);
                        }
                        zos.closeEntry();
                    } catch (ZipException e) {
                    }
                }
            }
            zis.close();
        } catch (Exception e) {
        }
    }
