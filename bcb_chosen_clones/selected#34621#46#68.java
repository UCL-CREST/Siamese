    private static void recurseAndZip(File file, ZipOutputStream zos) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File file1 = files[i];
                    recurseAndZip(file1, zos);
                }
            }
        } else {
            byte[] buf = new byte[1024];
            int len;
            ZipEntry entry = new ZipEntry(file.getPath());
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            zos.putNextEntry(entry);
            while ((len = bis.read(buf)) >= 0) {
                zos.write(buf, 0, len);
            }
            bis.close();
            zos.closeEntry();
        }
    }
