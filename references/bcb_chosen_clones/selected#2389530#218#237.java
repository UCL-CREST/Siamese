    protected void addFileResourceToArchive(ZipOutputStream zos, File f, String zipEntryName) throws IOException {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                addFileResourceToArchive(zos, files[i], zipEntryName + File.separator + files[i].getName());
            }
        } else {
            FileInputStream fi = new FileInputStream(f);
            BufferedInputStream buffi = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(zipEntryName);
            zos.putNextEntry(entry);
            byte data[] = new byte[BUFFER];
            int count;
            while ((count = buffi.read(data, 0, BUFFER)) != -1) {
                zos.write(data, 0, count);
            }
            zos.closeEntry();
            buffi.close();
        }
    }
