    private static void recursiveCompress(File file, URI relateTo, ZipOutputStream zipOutputStream) throws Exception {
        byte[] buffer = new byte[BUFFER];
        int len = 0;
        if (!file.isDirectory()) {
            URI relativePath = relateTo.relativize(file.toURI());
            ZipEntry entry = new ZipEntry(relativePath.getPath());
            zipOutputStream.putNextEntry(entry);
            FileInputStream fis = new FileInputStream(file);
            while ((len = fis.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
            }
            fis.close();
            zipOutputStream.closeEntry();
        } else {
            File[] children = file.listFiles();
            for (int i = 0; i < children.length; i++) {
                File child = children[i];
                recursiveCompress(child, relateTo, zipOutputStream);
            }
        }
    }
