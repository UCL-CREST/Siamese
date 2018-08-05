    public void close() {
        logger.debug("Closing output file.");
        outfile.close();
        tempFile.renameTo(new File(fileName));
        if (toZIP) {
            logger.debug("ZIPping output file.");
            try {
                ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(fileName + ".zip"));
                zipout.setLevel(9);
                String outfilezipname = fileName.substring(fileName.lastIndexOf(System.getProperty("file.separator")) + 1);
                zipout.putNextEntry(new ZipEntry(outfilezipname));
                FileInputStream fis = new FileInputStream(fileName);
                byte[] buffer = new byte[65536];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zipout.write(buffer, 0, len);
                }
                zipout.close();
                fis.close();
                logger.debug("ZIPping output file ok.");
                logger.debug("Removing " + fileName);
                (new File(fileName)).delete();
            } catch (IOException ex) {
                logger.debug("Error when zipping file", ex);
            }
        }
    }
