    public static byte[] readFileAsBytes(String path, Integer start, Integer length) {
        byte[] byteData = null;
        try {
            File file = new File(path);
            DataInputStream dis;
            dis = new DataInputStream(new FileInputStream(file));
            if (dis.available() > Integer.MAX_VALUE) {
                throw new OopsException("Arquivo muito grande pra manipulação, não exagere: " + path);
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream(length);
            byte[] bytes = new byte[length];
            dis.skipBytes(start);
            int readBytes = dis.read(bytes, 0, length);
            os.write(bytes, 0, readBytes);
            byteData = os.toByteArray();
            dis.close();
            os.close();
        } catch (Exception e) {
            throw new OopsException(e, "Problemas ao ler o arquivo [" + path + "].");
        }
        return byteData;
    }
