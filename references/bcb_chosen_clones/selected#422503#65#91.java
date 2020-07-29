    public static OMElement createAttachments(Object[] objects, String[] locals, OMFactory factory, OMNamespace namespace) {
        if (objects.length != locals.length) {
            throw new IllegalArgumentException("GDPCAxis2Utils: createAttachments: should be same number of objects and locals.");
        }
        OMElement attachment = null;
        ZipOutputStream zos = null;
        ByteArrayOutputStream byteStream = null;
        ObjectOutputStream objectOutStream = null;
        try {
            byteStream = new ByteArrayOutputStream();
            zos = new ZipOutputStream(byteStream);
            for (int i = 0; i < objects.length; i++) {
                ZipEntry entry = new ZipEntry(locals[i]);
                zos.putNextEntry(entry);
                objectOutStream = new ObjectOutputStream(zos);
                objectOutStream.writeObject(objects[i]);
                zos.closeEntry();
            }
            attachment = factory.createOMElement("attachment", namespace);
            DataHandler dh = new DataHandler(new ByteArrayDataSource(byteStream.toByteArray()));
            OMText text = factory.createOMText(dh, true);
            attachment.addChild(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attachment;
    }
