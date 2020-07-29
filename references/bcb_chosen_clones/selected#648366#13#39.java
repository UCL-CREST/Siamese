    public static Boolean decompress(File source, File destination) {
        FileOutputStream outputStream;
        ZipInputStream inputStream;
        try {
            outputStream = null;
            inputStream = new ZipInputStream(new FileInputStream(source));
            int read;
            byte buffer[] = new byte[BUFFER_SIZE];
            ZipEntry zipEntry;
            while ((zipEntry = inputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) new File(destination, zipEntry.getName()).mkdirs(); else {
                    File fileEntry = new File(destination, zipEntry.getName());
                    fileEntry.getParentFile().mkdirs();
                    outputStream = new FileOutputStream(fileEntry);
                    while ((read = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }
                    outputStream.flush();
                    outputStream.close();
                }
            }
            inputStream.close();
        } catch (Exception oException) {
            return false;
        }
        return true;
    }
