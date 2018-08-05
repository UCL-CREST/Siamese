    public static void writeFileToZipFile(File file, String zippedName, ZipOutputStream zipOut) throws ZipIOException {
        try {
            byte data[] = new byte[BUFFER_SIZE];
            BufferedInputStream fileInput = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);
            ZipEntry entry = new ZipEntry(zippedName);
            zipOut.putNextEntry(entry);
            int count;
            while ((count = fileInput.read(data, 0, BUFFER_SIZE)) != -1) {
                zipOut.write(data, 0, count);
            }
            fileInput.close();
        } catch (FileNotFoundException e) {
            throw new ZipIOException(e.getMessage(), e);
        } catch (IOException e) {
            throw new ZipIOException(e.getMessage(), e);
        }
    }
