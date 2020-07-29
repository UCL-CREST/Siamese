    public void write(Archivable arcObj, OutputStream out) throws IOException, ArchiveException {
        ZipOutputStream zip = new ZipOutputStream(out);
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new ArchiveException("parser config error", ex);
        }
        Document doc = builder.newDocument();
        WriterContext context = new WriterContext(doc);
        Element root = arcObj.write(context);
        doc.appendChild(root);
        ZipEntry xmlEntry = new ZipEntry(ArchiveEntry.XML_ENTRY_NAME);
        zip.putNextEntry(xmlEntry);
        Result result = new StreamResult(new OutputStreamWriter(zip, "utf-8"));
        DOMSource source = new DOMSource(doc);
        TransformerFactory factory = TransformerFactory.newInstance();
        try {
            factory.setAttribute("indent-number", new Integer(4));
        } catch (IllegalArgumentException exc) {
        }
        try {
            Transformer xformer = factory.newTransformer();
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(source, result);
        } catch (TransformerException ex) {
            throw new ArchiveException("error transforming document", ex);
        }
        zip.closeEntry();
        byte[] buf = new byte[1024];
        for (int i = 0; i < context.entries.size(); i++) {
            ArchiveEntry entry = context.entries.get(i);
            InputStream in = entry.openStream();
            zip.putNextEntry(entry);
            int len;
            while ((len = in.read(buf)) > 0) {
                zip.write(buf, 0, len);
            }
            zip.closeEntry();
            in.close();
        }
        zip.close();
    }
