    public static void zipFilesToZipFile(String dirPath, File file, ZipOutputStream zouts) {
        FileInputStream fin = null;
        ZipEntry entry = null;
        byte[] buf = new byte[4096];
        int readByte = 0;
        if (file.isFile()) {
            try {
                fin = new FileInputStream(file);
                entry = new ZipEntry(getEntryName(dirPath, file));
                zouts.putNextEntry(entry);
                while ((readByte = fin.read(buf)) != -1) {
                    zouts.write(buf, 0, readByte);
                }
                zouts.closeEntry();
                fin.close();
                System.out.println("����ļ�" + file.getAbsolutePath() + "��zip�ļ���!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
