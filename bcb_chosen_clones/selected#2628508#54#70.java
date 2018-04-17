    private static void zipFunc(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ZipEntry fileEntry = new ZipEntry(filePath.substring(Config.getDataDir().length() + 1));
            zos.putNextEntry(fileEntry);
            byte[] data = new byte[1024];
            int byteCount;
            while ((byteCount = bis.read(data, 0, 1024)) > -1) {
                zos.write(data, 0, byteCount);
            }
            bis.close();
            fis.close();
        } catch (IOException e) {
        }
        System.out.println(filePath);
    }
