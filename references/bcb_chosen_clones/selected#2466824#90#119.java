    private void zipFiles(File dir, String zipFilename) {
        byte[] buf = new byte[1024];
        try {
            File zipFile = new File(zipFilename);
            if (zipFile.exists()) {
                zipFile.delete();
            }
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilename));
            for (String zipThis : filesToZip) {
                File fileToZip = new File(dir, zipThis);
                if (fileToZip.exists() && fileToZip.canRead() && !fileToZip.isDirectory()) {
                    try {
                        FileInputStream in = new FileInputStream(fileToZip);
                        out.putNextEntry(new ZipEntry(fileToZip.getName()));
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        out.closeEntry();
                        in.close();
                    } catch (FileNotFoundException fnfe) {
                        logger.info("Couldn't open file to zip " + fnfe);
                    }
                }
            }
            out.close();
        } catch (IOException e) {
            logger.fatal("Error zipping generated XCCDF.", e);
        }
    }
