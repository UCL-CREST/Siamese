    public static void storeOpenOfficeFile(File file, InputStream source) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        try {
            ZipEntry zipEntry = new ZipEntry("content.xml");
            out.putNextEntry(zipEntry);
            int c = -1;
            while ((c = source.read()) >= 0) {
                out.write(c);
            }
            out.closeEntry();
            addResourceFile("meta.xml", "/resource/openoffice/meta.xml", out);
            addResourceFile("mimetype", "/resource/openoffice/mimetype", out);
            addResourceFile("META-INF/manifest.xml", "/resource/openoffice/manifest.xml", out);
        } finally {
            out.close();
        }
    }
