    public static File zip(File file, File zipFile) throws Exception {
        if (zipFile == null) zipFile = defaultZipFileName(file);
        if (!file.exists() || !file.canRead()) throw new RuntimeException(file + "doesn't exists or cannot read");
        if (file.isDirectory()) {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
            zipDirctory(out, file, "");
            out.close();
        } else {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] buf = new byte[1024];
            int len;
            FileOutputStream fos = new FileOutputStream(zipFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ZipOutputStream zos = new ZipOutputStream(bos);
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);
            while ((len = bis.read(buf)) != -1) {
                zos.write(buf, 0, len);
                zos.flush();
            }
            bis.close();
            zos.close();
        }
        return zipFile;
    }
