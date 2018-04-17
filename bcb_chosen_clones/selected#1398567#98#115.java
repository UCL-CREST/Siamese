    private static void extract(ZipFile zipFile) throws Exception {
        FileUtils.deleteQuietly(WEBKIT_DIR);
        WEBKIT_DIR.mkdirs();
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                new File(WEBKIT_DIR, entry.getName()).mkdirs();
                continue;
            }
            InputStream inputStream = zipFile.getInputStream(entry);
            File outputFile = new File(WEBKIT_DIR, entry.getName());
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            IOUtils.copy(inputStream, outputStream);
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }
