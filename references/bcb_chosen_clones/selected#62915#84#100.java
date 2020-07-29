    public static void zipAllFile(File[] inputFile, ZipOutputStream zipOutputStream) throws IOException {
        byte[] readBuffer = new byte[1024];
        int bytesIn = 0;
        for (int i = 0; i < inputFile.length; i++) {
            if (inputFile[i].isDirectory()) {
                zipDirectory(inputFile[i], zipOutputStream);
                continue;
            }
            FileInputStream fileInputStream = new FileInputStream(inputFile[i]);
            ZipEntry zipEntry = new ZipEntry(inputFile[i].getPath());
            zipOutputStream.putNextEntry(zipEntry);
            while ((bytesIn = fileInputStream.read(readBuffer)) != -1) {
                zipOutputStream.write(readBuffer, 0, bytesIn);
            }
            fileInputStream.close();
        }
    }
