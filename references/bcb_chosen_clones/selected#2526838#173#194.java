    protected static void createZipFile(String zipName, List<File> files) {
        byte[] buf = new byte[1024];
        try {
            if (files.size() > 0) {
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipName));
                int len;
                for (File file : files) {
                    FileInputStream in = new FileInputStream(file);
                    ZipEntry entry = new ZipEntry(file.getPath());
                    out.putNextEntry(entry);
                    while ((len = in.read(buf)) != -1) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                }
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
