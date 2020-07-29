    public void writeCollections(List<Entry> collections, OutputStream os, boolean includeGranules) throws Exception {
        ZipOutputStream zos = new ZipOutputStream(os);
        Document doc = XmlUtil.makeDocument();
        Element root = XmlUtil.create(doc, EchoUtil.TAG_COLLECTIONMETADATAFILE, null, new String[] { "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance", "xsi:noNamespaceSchemaLocation", "http://www.echo.nasa.gov/ingest/schemas/operations/Collection.xsd" });
        Element collectionsNode = XmlUtil.create(root.getOwnerDocument(), EchoUtil.TAG_COLLECTIONS, root);
        for (Entry entry : collections) {
            makeCollectionNode(entry, collectionsNode);
        }
        String xml = XmlUtil.toString(root);
        System.err.println(xml);
        zos.putNextEntry(new ZipEntry("collections.xml"));
        byte[] bytes = xml.getBytes();
        zos.write(bytes, 0, bytes.length);
        zos.closeEntry();
        IOUtil.close(zos);
    }
