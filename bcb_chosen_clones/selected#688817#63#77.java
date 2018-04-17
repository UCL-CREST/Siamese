    private void addFiletoPackage(String fileToAdd, ZipOutputStream packageOutputStream) throws FileNotFoundException, IOException {
        LOG.log(Level.INFO, "Adding: {0} to {1}", new Object[] { fileToAdd, targetFileName });
        File f = new File(pathToData, fileToAdd);
        if (!fileToAdd.startsWith(File.pathSeparator)) {
            fileToAdd = File.pathSeparator + fileToAdd;
        }
        BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(f), BUFFER_SIZE);
        ZipEntry entry = new ZipEntry(fileToAdd);
        packageOutputStream.putNextEntry(entry);
        int count;
        while ((count = fileInputStream.read(dataBuffer, 0, BUFFER_SIZE)) != -1) {
            packageOutputStream.write(dataBuffer, 0, count);
        }
        IOUtil.closeGracefully(fileInputStream);
    }
