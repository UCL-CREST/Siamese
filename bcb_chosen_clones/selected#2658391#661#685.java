    private long writeFileToZip(String filename, String uspaceName, String uplDirectory, boolean isAscii, org.unicore.utility.ZipOutputStream zos) throws FileNotFoundException, IOException {
        String zipName = uplDirectory + "/" + uspaceName;
        logger.info("Writing ZIP entry: " + zipName);
        zos.putNextEntry(new ZipEntry(zipName));
        FileInputStream fis = new FileInputStream(filename);
        long length = new File(filename).length();
        if (!isAscii) {
            byte[] buffer = new byte[4096];
            int read;
            while ((read = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, read);
                logger.fine("next chunk: " + read);
            }
        } else {
            byte read;
            while ((read = (byte) fis.read()) > 0) {
                if (read != 13 && read != 26) {
                    zos.write((int) read);
                }
            }
        }
        zos.closeEntry();
        fis.close();
        return length;
    }
