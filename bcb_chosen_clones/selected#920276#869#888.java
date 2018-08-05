    public static void zipCompress(File sourceFile) throws Exception {
        final int BUFFER_SIZE = 100000;
        if (sourceFile.getName().endsWith(".zip")) {
        } else {
            File targetFile = new File(sourceFile.getAbsolutePath() + ".zip");
            FileInputStream fis = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(targetFile);
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));
            BufferedInputStream bis = new BufferedInputStream(fis, BUFFER_SIZE);
            ZipEntry entry = new ZipEntry(sourceFile.getName());
            zos.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER_SIZE];
            while ((count = bis.read(data, 0, BUFFER_SIZE)) != -1) {
                zos.write(data, 0, count);
            }
            bis.close();
            zos.close();
        }
    }
