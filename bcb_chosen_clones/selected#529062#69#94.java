    public static byte[] getArtifact(String uuid) {
        if (uuid == null) {
            return new byte[] {};
        }
        InputStream input;
        try {
            input = new BufferedInputStream(new FileInputStream(uuid));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (; ; ) {
                int noBytesRead;
                try {
                    noBytesRead = input.read(buf);
                    if (noBytesRead == -1) {
                        return output.toByteArray();
                    }
                    output.write(buf, 0, noBytesRead);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return new byte[] {};
        }
    }
