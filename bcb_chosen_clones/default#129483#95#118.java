    public static byte[] getBytesFromFile(File file) throws Exception {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8192);
        try {
            fis = new FileInputStream(file);
            copy(fis, bos);
            return bos.toByteArray();
        } catch (Exception ex) {
            throw new Exception("Excepcion obteniendo bytes del fichero: " + file.getName(), ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ex2) {
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception ex2) {
                }
            }
        }
    }
