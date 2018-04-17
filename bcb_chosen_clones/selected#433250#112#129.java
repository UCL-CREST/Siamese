    public static void zip(File[] files, String zipFilename) throws Exception {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(zipFilename));
        ZipOutputStream zos = new ZipOutputStream(os);
        byte data[] = new byte[BUFFER];
        for (int i = 0; i < files.length; i++) {
            log.debug("Adding: " + files[i]);
            InputStream is = new BufferedInputStream(new FileInputStream(files[i]), BUFFER);
            ZipEntry entry = new ZipEntry(files[i].getName());
            zos.putNextEntry(entry);
            int count;
            while ((count = is.read(data, 0, BUFFER)) != -1) {
                zos.write(data, 0, count);
            }
            is.close();
        }
        zos.close();
        log.debug("Zipped file: " + zipFilename);
    }
