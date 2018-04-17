    private void process(String zipFileName, String directory, String db) throws SQLException {
        InputStream in = null;
        try {
            if (!FileUtils.exists(zipFileName)) {
                throw new IOException("File not found: " + zipFileName);
            }
            String originalDbName = null;
            int originalDbLen = 0;
            if (db != null) {
                originalDbName = getOriginalDbName(zipFileName, db);
                if (originalDbName == null) {
                    throw new IOException("No database named " + db + " found");
                }
                if (originalDbName.startsWith(File.separator)) {
                    originalDbName = originalDbName.substring(1);
                }
                originalDbLen = originalDbName.length();
            }
            in = FileUtils.openFileInputStream(zipFileName);
            ZipInputStream zipIn = new ZipInputStream(in);
            while (true) {
                ZipEntry entry = zipIn.getNextEntry();
                if (entry == null) {
                    break;
                }
                String fileName = entry.getName();
                fileName = fileName.replace('\\', File.separatorChar);
                fileName = fileName.replace('/', File.separatorChar);
                if (fileName.startsWith(File.separator)) {
                    fileName = fileName.substring(1);
                }
                boolean copy = false;
                if (db == null) {
                    copy = true;
                } else if (fileName.startsWith(originalDbName + ".")) {
                    fileName = db + fileName.substring(originalDbLen);
                    copy = true;
                }
                if (copy) {
                    OutputStream out = null;
                    try {
                        out = FileUtils.openFileOutputStream(directory + File.separator + fileName, false);
                        IOUtils.copy(zipIn, out);
                        out.close();
                    } finally {
                        IOUtils.closeSilently(out);
                    }
                }
                zipIn.closeEntry();
            }
            zipIn.closeEntry();
            zipIn.close();
        } catch (IOException e) {
            throw Message.convertIOException(e, zipFileName);
        } finally {
            IOUtils.closeSilently(in);
        }
    }
