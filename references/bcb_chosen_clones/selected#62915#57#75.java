    public static void zipDirectory(File inputFile, ZipOutputStream zipOutputStream) throws IOException {
        String[] dirList = inputFile.list();
        byte[] readBuffer = new byte[1024];
        int bytesIn = 0;
        for (int i = 0; i < dirList.length; i++) {
            File file = new File(inputFile, dirList[i]);
            if (file.isDirectory()) {
                zipDirectory(file, zipOutputStream);
                continue;
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(file.getPath());
            zipOutputStream.putNextEntry(zipEntry);
            while ((bytesIn = fileInputStream.read(readBuffer)) != -1) {
                zipOutputStream.write(readBuffer, 0, bytesIn);
            }
            fileInputStream.close();
        }
    }
