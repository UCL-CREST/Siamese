    public static void addToZip(String path, String srcFile, ZipOutputStream zip) throws ImportExportException {
        File file = new File(srcFile);
        if (!file.exists() || file.isDirectory()) {
            return;
        }
        String zipPath;
        if (!path.endsWith(File.separator)) {
            zipPath = path + File.separator + file.getName();
        } else {
            zipPath = path + file.getName();
        }
        byte[] buf = new byte[2048];
        int len;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            ZipEntry objEntry = new ZipEntry(zipPath);
            zip.putNextEntry(objEntry);
            while ((len = in.read(buf)) > 0) {
                zip.write(buf, 0, len);
            }
        } catch (Exception e) {
            ImportExportException ieo = new ImportExportException(ImportExportFault.UNZIP_STREAM_FAILURE, e);
            logger.throwing(ZipUtils.class.getName(), "addToZip", ieo);
            throw ieo;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException noop) {
                }
            }
        }
    }
