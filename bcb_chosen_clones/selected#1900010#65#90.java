    public static void createZipFile(File zipFile, Collection includeFiles, final boolean verbose) throws IOException {
        if (verbose) {
            System.out.println("ZIP: " + zipFile.getAbsolutePath());
        }
        byte[] buf = new byte[BUFFER_SIZE];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
        for (Iterator iterator = includeFiles.iterator(); iterator.hasNext(); ) {
            File entryFile = (File) iterator.next();
            if (verbose) {
                System.out.println("Adding: " + entryFile.getAbsolutePath());
            }
            if (!entryFile.canRead()) {
                System.err.println("ZipBuilder: Could not read " + entryFile.getAbsolutePath());
                continue;
            }
            FileInputStream in = new FileInputStream(entryFile);
            out.putNextEntry(new ZipEntry(entryFile.getName()));
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
        out.close();
    }
