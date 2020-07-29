    public byte[] readFile(File file) {
        byte result[] = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                baos.write(buf, 0, len);
            }
            fis.close();
            baos.close();
            result = baos.toByteArray();
        } catch (FileNotFoundException ex) {
            logger.error(ex);
        } catch (IOException ex) {
            logger.error(ex);
        }
        return result;
    }
