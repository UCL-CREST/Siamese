    public static void zipFile(String filename, ZipOutputStream zos, String zipname) {
        try {
            byte[] readBuffer = new byte[1024];
            int bytesIn = 0;
            File f = new File(filename);
            if (f.isDirectory()) {
                return;
            }
            FileInputStream fis = new FileInputStream(f);
            ZipEntry anEntry = new ZipEntry(zipname);
            zos.putNextEntry(anEntry);
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            fis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
