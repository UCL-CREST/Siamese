    public static void compressFile(ZipOutputStream zos, File file, String dir) throws IOException {
        if (file.isDirectory()) {
            File[] fs = file.listFiles();
            for (File f : fs) {
                if (!dir.equals("")) {
                    compressFile(zos, f, dir + "\\" + file.getName());
                } else {
                    compressFile(zos, f, file.getName());
                }
            }
        } else {
            String entryName = null;
            if (!dir.equals("")) {
                entryName = dir + "\\" + file.getName();
            } else {
                entryName = file.getName();
            }
            ZipEntry entry = new ZipEntry(entryName);
            zos.putNextEntry(entry);
            InputStream is = new FileInputStream(file);
            int len = 0;
            while ((len = is.read()) != -1) {
                zos.write(len);
            }
            is.close();
        }
    }
