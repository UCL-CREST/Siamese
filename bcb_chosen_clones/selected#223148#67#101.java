    public static synchronized File makeZip(File[] files, String fileName) throws IOException {
        byte[] buffer = new byte[18024];
        if (Files.exists(fileName)) {
            throw new IOException("The file [" + fileName + "] already exists.");
        }
        if (!fileName.endsWith(".zip")) {
            fileName += ".zip";
        }
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        ZipOutputStream zipOutputStream = null;
        InputStream inputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileName);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            zipOutputStream = new ZipOutputStream(bufferedOutputStream);
            zipOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
            for (File file : files) {
                inputStream = new BufferedInputStream(new FileInputStream(file));
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }
                zipOutputStream.closeEntry();
                inputStream.close();
            }
        } finally {
            Streams.close(zipOutputStream);
            Streams.close(bufferedOutputStream);
            Streams.close(fileOutputStream);
            Streams.close(inputStream);
        }
        return new File(fileName);
    }
