    public String encodeXMLForm(XMLForm form) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ZipOutputStream zout = new ZipOutputStream(bout);
            zout.setLevel(9);
            if (form.getPhase() != null) {
                zout.putNextEntry(new ZipEntry("phase"));
                byte[] phase = form.getPhase().getBytes();
                zout.write(phase, 0, phase.length);
            }
            zout.putNextEntry(new ZipEntry("xml"));
            form.outputXML(zout);
            zout.close();
            byte[] enc = Base64.encode(bout.toByteArray());
            return new String(enc);
        } catch (IOException ex) {
            throw new XMLException("Unable to encode xml viewstate:" + ex);
        }
    }
