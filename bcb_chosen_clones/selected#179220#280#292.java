    private static void addFileToJar(BufferedInputStream origin, ZipOutputStream out, byte[] data, String fillFullPath) throws FileNotFoundException, IOException {
        logger.debug("Begining to add file '" + fillFullPath + "' to jar file.");
        FileInputStream fi = new FileInputStream(fillFullPath);
        origin = new BufferedInputStream(fi, COMPRESS_OUTPUTSTREAM_BUFFER_SIZE);
        ZipEntry entry = new ZipEntry(fillFullPath);
        out.putNextEntry(entry);
        int count;
        while ((count = origin.read(data, 0, COMPRESS_OUTPUTSTREAM_BUFFER_SIZE)) != -1) {
            out.write(data, 0, count);
        }
        origin.close();
        logger.debug("File '" + fillFullPath + "' added succesfully to jar file.");
    }
