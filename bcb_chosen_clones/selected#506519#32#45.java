    public static byte[] getBytesFromFile(String fileName) throws FileNotFoundException, IOException {
        byte refBytes[] = null;
        {
            FileInputStream fisRef = new FileInputStream(fileName);
            UnsyncByteArrayOutputStream baos = new UnsyncByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int len;
            while ((len = fisRef.read(buf)) > 0) {
                baos.write(buf, 0, len);
            }
            refBytes = baos.toByteArray();
        }
        return refBytes;
    }
