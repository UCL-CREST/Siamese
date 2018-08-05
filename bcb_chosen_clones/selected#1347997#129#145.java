    public byte[] get() {
        if (content == null) {
            if (storeLocation != null) {
                content = new byte[(int) getSize()];
                try {
                    FileInputStream fis = new FileInputStream(storeLocation);
                    fis.read(content);
                } catch (Exception e) {
                    content = null;
                }
            } else {
                content = byteStream.toByteArray();
                byteStream = null;
            }
        }
        return content;
    }
