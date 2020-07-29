    public boolean copyTo(String targetFilePath) {
        try {
            FileInputStream srcFile = new FileInputStream(filePath);
            FileOutputStream target = new FileOutputStream(targetFilePath);
            byte[] buff = new byte[1024];
            int readed = -1;
            while ((readed = srcFile.read(buff)) > 0) target.write(buff, 0, readed);
            srcFile.close();
            target.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
