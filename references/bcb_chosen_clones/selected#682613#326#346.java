    public static void copyFile(String oldPathFile, String newPathFile) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPathFile);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPathFile);
                FileOutputStream fs = new FileOutputStream(newPathFile);
                byte[] buffer = new byte[2048];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            } else {
                System.err.println(FileUtil.class.toString() + ":不存在file" + oldPathFile);
            }
        } catch (Exception e) {
            System.err.println(FileUtil.class.toString() + ":复制file" + oldPathFile + "到" + newPathFile + "出错!");
        }
    }
