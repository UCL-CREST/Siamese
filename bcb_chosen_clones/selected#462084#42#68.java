    private static void addZipEntry(ZipOutputStream zipOutputStream, File entryFile, String parentPath) throws IOException {
        if (entryFile.isDirectory()) {
            String dirPath = parentPath + entryFile.getName() + PATH_SEPARATOR;
            File[] files = entryFile.listFiles();
            if (files.length == 0) {
                zipOutputStream.putNextEntry(new ZipEntry(dirPath));
            } else {
                for (int i = 0; i < files.length; i++) {
                    addZipEntry(zipOutputStream, files[i], dirPath);
                }
            }
        } else {
            InputStream entryFileStream = new FileInputStream(entryFile);
            try {
                ZipEntry entry = new ZipEntry(parentPath + entryFile.getName());
                zipOutputStream.putNextEntry(entry);
                byte[] buf = new byte[BUF_SIZE];
                int bufSize = 0;
                while ((bufSize = entryFileStream.read(buf, 0, BUF_SIZE)) != -1) {
                    zipOutputStream.write(buf, 0, bufSize);
                }
                zipOutputStream.closeEntry();
            } finally {
                entryFileStream.close();
            }
        }
    }
