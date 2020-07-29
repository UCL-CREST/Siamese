    public static byte[] getBytesFromFile(File file) {
        byte[] ret = null;
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            if (file == null) {
                return ret;
            }
            fileInputStream = new FileInputStream(file);
            byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] tmpByte = new byte[1024];
            int n = -1;
            while ((n = fileInputStream.read(tmpByte)) != -1) {
                byteArrayOutputStream.write(tmpByte, 0, n);
            }
            ret = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (Exception e) {
            }
        }
        return ret;
    }
