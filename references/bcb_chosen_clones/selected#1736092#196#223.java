    public static void unzip(File zipInFile, File outputDir) throws Exception {
        Enumeration<? extends ZipEntry> entries;
        ZipFile zipFile = new ZipFile(zipInFile);
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipInFile));
        ZipEntry entry = (ZipEntry) zipInputStream.getNextEntry();
        File curOutDir = outputDir;
        while (entry != null) {
            if (entry.isDirectory()) {
                curOutDir = new File(curOutDir, entry.getName());
                curOutDir.mkdirs();
                continue;
            }
            File outFile = new File(curOutDir, entry.getName());
            File tempDir = outFile.getParentFile();
            if (!tempDir.exists()) tempDir.mkdirs();
            outFile.createNewFile();
            BufferedOutputStream outstream = new BufferedOutputStream(new FileOutputStream(outFile));
            int n;
            byte[] buf = new byte[1024];
            while ((n = zipInputStream.read(buf, 0, 1024)) > -1) outstream.write(buf, 0, n);
            outstream.flush();
            outstream.close();
            zipInputStream.closeEntry();
            entry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
        zipFile.close();
    }
