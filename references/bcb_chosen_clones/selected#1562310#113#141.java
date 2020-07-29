    private static void zipDirectory(ZipOutputStream zos, File files, String entry) throws IOException {
        if (files.isDirectory()) {
            if (entry != null) {
                zos.putNextEntry(new ZipEntry(entry + "/"));
                entry = entry + "/";
            } else {
                zos.putNextEntry(new ZipEntry("/"));
                entry = "/";
            }
            String[] names = files.list();
            File[] files0 = files.listFiles();
            for (int i = 0; i < files0.length; i++) {
                zipDirectory(zos, files0[i], entry + names[i]);
            }
            zos.closeEntry();
        } else {
            if (entry != null) {
                zos.putNextEntry(new ZipEntry(entry));
            }
            byte[] data = new byte[1000];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(files), 1000);
            int count;
            while ((count = in.read(data, 0, 1000)) != -1) {
                zos.write(data, 0, count);
            }
            zos.closeEntry();
            in.close();
        }
    }
