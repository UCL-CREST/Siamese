    public static void extractFile(String jarArchive, String fileToExtract, String destination) {
        FileWriter writer = null;
        ZipInputStream zipStream = null;
        try {
            FileInputStream inputStream = new FileInputStream(jarArchive);
            BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
            zipStream = new ZipInputStream(bufferedStream);
            writer = new FileWriter(new File(destination));
            ZipEntry zipEntry = null;
            while ((zipEntry = zipStream.getNextEntry()) != null) {
                if (zipEntry.getName().equals(fileToExtract)) {
                    int size = (int) zipEntry.getSize();
                    for (int i = 0; i < size; i++) {
                        writer.write(zipStream.read());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipStream != null) try {
                zipStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (writer != null) try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
