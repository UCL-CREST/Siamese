    public static void addFileToZipStream(ZipOutputStream zos, String baseDir, String relDir, String filename) throws IOException {
        File f = new File(baseDir + File.separatorChar + relDir + filename);
        if (f.isDirectory()) {
            String[] files = f.list();
            for (int i = 0; i < files.length; i++) {
                addFileToZipStream(zos, baseDir, relDir + filename + File.separatorChar, files[i]);
            }
        } else {
            ZipEntry e = new ZipEntry(relDir + filename);
            zos.putNextEntry(e);
            zos.write(new FileContents(f.getPath()).contents);
            zos.closeEntry();
        }
    }
