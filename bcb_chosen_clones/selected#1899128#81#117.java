    private static void zipFiles(final File rootDir, final File folderToZip, final ZipOutputStream zipOutputStream) {
        final byte[] buf = new byte[1024];
        final String relativePath = folderToZip.toString().substring(rootDir.toString().length());
        FileInputStream in;
        final File[] files = folderToZip.listFiles();
        for (final File file : files) {
            final String filename = file.getName();
            if (file.isDirectory()) {
                final String dirName = relativePath + File.separator + filename;
                LOG.debug("adding dir [" + dirName + "]");
                zipFiles(rootDir, file, zipOutputStream);
            } else {
                String filePath = relativePath + File.separator + filename;
                if (filePath.charAt(0) == File.separatorChar) {
                    filePath = filePath.substring(1);
                }
                LOG.debug("adding file [" + filePath + "]");
                try {
                    in = new FileInputStream(new File(folderToZip, filename));
                    try {
                        zipOutputStream.putNextEntry(new ZipEntry(filePath.replace(File.separatorChar, '/')));
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            zipOutputStream.write(buf, 0, len);
                        }
                        zipOutputStream.closeEntry();
                    } finally {
                        in.close();
                    }
                } catch (IOException ioe) {
                    final String message = "Error occured while zipping file " + filePath;
                    LOG.error(message, ioe);
                    throw new RuntimeException(message, ioe);
                }
            }
        }
    }
