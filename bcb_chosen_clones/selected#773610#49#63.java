    public void zipit(String outString, String name) throws Exception {
        File saveFile;
        ZipEntry ze;
        if (m_zipOut == null) {
            saveFile = new File(m_destination, name + ".gz");
            DataOutputStream dout = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(saveFile)));
            dout.writeBytes(outString);
            dout.close();
        } else {
            ze = new ZipEntry(name);
            m_zs.putNextEntry(ze);
            m_zipOut.writeBytes(outString);
            m_zs.closeEntry();
        }
    }
