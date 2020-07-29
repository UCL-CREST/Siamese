    public static OMElement createAttachment(Object obj, String local, OMFactory factory, OMNamespace namespace) {
        OMElement attachment = null;
        ZipOutputStream zos = null;
        ByteArrayOutputStream byteStream = null;
        ObjectOutputStream objectOutStream = null;
        try {
            byteStream = new ByteArrayOutputStream();
            zos = new ZipOutputStream(byteStream);
            ZipEntry entry = new ZipEntry(local);
            zos.putNextEntry(entry);
            objectOutStream = new ObjectOutputStream(zos);
            objectOutStream.writeObject(obj);
            zos.closeEntry();
            attachment = factory.createOMElement(local, namespace);
            DataHandler dh = new DataHandler(new ByteArrayDataSource(byteStream.toByteArray()));
            OMText text = factory.createOMText(dh, true);
            attachment.addChild(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attachment;
    }
