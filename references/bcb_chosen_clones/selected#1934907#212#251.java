    private void compressResource(OutputStream os, DocumentImpl doc, boolean useHierarchy, String stripOffset, String method, String name) throws IOException, SAXException {
        Object entry = null;
        byte[] value = new byte[0];
        CRC32 chksum = new CRC32();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (name != null) {
            entry = newEntry(name);
        } else if (useHierarchy) {
            String docCollection = doc.getCollection().getURI().toString();
            XmldbURI collection = XmldbURI.create(removeLeadingOffset(docCollection, stripOffset));
            entry = newEntry(collection.append(doc.getFileURI()).toString());
        } else {
            entry = newEntry(doc.getFileURI().toString());
        }
        if (doc.getResourceType() == DocumentImpl.XML_FILE) {
            Serializer serializer = context.getBroker().getSerializer();
            serializer.setUser(context.getUser());
            serializer.setProperty("omit-xml-declaration", "no");
            String strDoc = serializer.serialize(doc);
            value = strDoc.getBytes();
        } else if (doc.getResourceType() == DocumentImpl.BINARY_FILE) {
            InputStream is = context.getBroker().getBinaryResource((BinaryDocument) doc);
            byte[] data = new byte[16384];
            int len = 0;
            while ((len = is.read(data, 0, data.length)) > 0) {
                baos.write(data, 0, len);
            }
            is.close();
            value = baos.toByteArray();
        }
        if (entry instanceof ZipEntry && "store".equals(method)) {
            ((ZipEntry) entry).setMethod(ZipOutputStream.STORED);
            chksum.update(value);
            ((ZipEntry) entry).setCrc(chksum.getValue());
            ((ZipEntry) entry).setSize(value.length);
        }
        putEntry(os, entry);
        os.write(value);
        closeEntry(os);
    }
