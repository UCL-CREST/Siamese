    public static void storeOpenDocumentSpreadsheetFile(File file, InputStream source) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        try {
            ZipEntry ze = new ZipEntry("mimetype");
            String mime = "application/vnd.oasis.opendocument.spreadsheet";
            ze.setMethod(ZipEntry.STORED);
            ze.setSize(mime.length());
            CRC32 crc = new CRC32();
            crc.update(mime.getBytes());
            ze.setCrc(crc.getValue());
            out.putNextEntry(ze);
            for (int i = 0; i < mime.length(); i++) {
                out.write(mime.charAt(i));
            }
            out.closeEntry();
            ZipEntry zipEntry = new ZipEntry("content.xml");
            out.putNextEntry(zipEntry);
            int c = -1;
            while ((c = source.read()) >= 0) {
                out.write(c);
            }
            out.closeEntry();
            addResourceFile("meta.xml", "/resource/ods/meta.xml", out);
            addResourceFile("META-INF/manifest.xml", "/resource/ods/manifest.xml", out);
        } finally {
            out.close();
        }
    }
