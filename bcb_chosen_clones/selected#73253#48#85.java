    private static void zip(Logger logger, File[] files, ZipOutputStream zos) throws IOException {
        byte[] buf = new byte[1024];
        try {
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f.isDirectory()) {
                    if (relative) {
                        logger.debug("New dir entry: " + f.getAbsolutePath().substring(relativeDir.length() + 1, f.getAbsolutePath().length()) + NIX_FILEPATH_SEP);
                        zos.putNextEntry(new ZipEntry(f.getAbsolutePath().substring(relativeDir.length() + 1, f.getAbsolutePath().length()) + NIX_FILEPATH_SEP));
                    } else {
                        logger.debug("New dir entry: " + f.getAbsolutePath() + NIX_FILEPATH_SEP);
                        zos.putNextEntry(new ZipEntry(f.getAbsolutePath() + NIX_FILEPATH_SEP));
                    }
                    File[] dirList = f.listFiles();
                    zip(logger, dirList, zos);
                    continue;
                }
                FileInputStream in = new FileInputStream(f.getAbsolutePath());
                if (relative) {
                    logger.debug("New file entry: " + f.getAbsolutePath().substring(relativeDir.length(), f.getAbsolutePath().length()));
                    zos.putNextEntry(new ZipEntry(f.getAbsolutePath().substring(relativeDir.length(), f.getAbsolutePath().length())));
                } else {
                    logger.debug(f.getAbsolutePath());
                    zos.putNextEntry(new ZipEntry(f.getAbsolutePath()));
                }
                int len;
                while ((len = in.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }
                in.close();
                zos.closeEntry();
            }
        } catch (IOException e) {
            logger.error("IO Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
