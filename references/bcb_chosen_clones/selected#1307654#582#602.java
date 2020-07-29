    private static final void zipDirectory(File inBaseDir, File inCurrentDir, ZipOutputStream inZos) throws IOException {
        File dirContents[] = inCurrentDir.listFiles();
        byte buffer[] = new byte[1024];
        for (int i = 0; i < dirContents.length; i++) {
            File nextFile = dirContents[i];
            if (nextFile.isFile()) {
                FileInputStream fis = new FileInputStream(nextFile);
                String entryName = nextFile.getAbsolutePath().substring(inBaseDir.getAbsolutePath().length() + 1);
                entryName = entryName.replace(File.separatorChar, '/');
                inZos.putNextEntry(new ZipEntry(entryName));
                int len = 0;
                while ((len = fis.read(buffer, 0, buffer.length)) != -1) {
                    inZos.write(buffer, 0, len);
                }
                inZos.closeEntry();
                fis.close();
            } else {
                zipDirectory(inBaseDir, nextFile, inZos);
            }
        }
    }
