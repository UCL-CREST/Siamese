    public static void zipFilesInDirectory(File directory, String outname) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outname));
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                FileInputStream in = new FileInputStream(files[i]);
                out.putNextEntry(new ZipEntry(files[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException ex) {
            System.out.println("Exception: " + ex);
        }
    }
