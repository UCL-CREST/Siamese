    private void addProjectXmlToZip(ZipOutputStream out, org.jdom.Document doc) throws IOException {
        ZipEntry entry = new ZipEntry(FDDPMA.PROJECT_ENTRY);
        out.putNextEntry(entry);
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        ByteArrayOutputStream xmlBuf = new ByteArrayOutputStream();
        outputter.output(doc, xmlBuf);
        out.write(xmlBuf.toByteArray());
    }
