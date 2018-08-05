    public static void zip(String baseDir, String zipFilename, String[] filenames, String[] archFilenames) throws IOException {
        ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFilename)));
        byte[] data = new byte[512];
        int bc;
        for (int i = 0; i < filenames.length; i++) {
            try {
                InputStream fin = new BufferedInputStream(new FileInputStream(filenames[i]));
                ZipEntry entry = new ZipEntry(archFilenames[i].substring(baseDir.length() + 1));
                zout.putNextEntry(entry);
                while ((bc = fin.read(data, 0, 512)) != -1) {
                    zout.write(data, 0, bc);
                }
                zout.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        zout.close();
    }
