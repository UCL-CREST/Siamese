    public byte[] encode() {
        byte[] result = null;
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(bao);
            zipOut.putNextEntry(new ZipEntry("root-Job"));
            zipOut.setLevel(9);
            ObjectOutputStream oos = new ObjectOutputStream(zipOut);
            oos.writeObject(this);
            oos.flush();
            bao.flush();
            oos.close();
            result = bao.toByteArray();
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Encoding job " + this + " failed.", ex);
        }
        return result;
    }
