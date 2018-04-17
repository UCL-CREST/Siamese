    private static void addToZip(String zipPath, File srcFile, ZipOutputStream zos) throws IOException {
        if (srcFile.isDirectory()) {
            addFolderToZip(zipPath, srcFile, zos);
        } else {
            byte[] buff = new byte[BUFFER];
            int len;
            FileInputStream in = null;
            try {
                in = new FileInputStream(srcFile);
                zos.putNextEntry(new ZipEntry(buildZipPathPrefix(zipPath) + srcFile.getName()));
                while ((len = in.read(buff)) > 0) {
                    zos.write(buff, 0, len);
                }
                zos.closeEntry();
            } finally {
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }
