    public boolean marshall(PackagePart part, OutputStream os) throws OpenXML4JException {
        if (!(os instanceof ZipOutputStream)) {
            logger.error("unexpected class " + os.getClass().getName());
            throw new OpenXML4JException("ZipOutputStream expected !");
        }
        ZipOutputStream zos = (ZipOutputStream) os;
        ZipEntry partEntry = new ZipEntry(part.uri.getPath());
        try {
            zos.putNextEntry(partEntry);
            InputStream ins = part.getInputStream();
            byte[] buff = new byte[OpenXMLDocument.READ_WRITE_FILE_BUFFER_SIZE];
            while (ins.available() > 0) {
                int resultRead = ins.read(buff);
                if (resultRead == -1) {
                    break;
                } else {
                    zos.write(buff, 0, resultRead);
                }
            }
            zos.closeEntry();
        } catch (IOException ioe) {
            logger.error("cannot write:" + part.uri + ": in ZIP", ioe);
            return false;
        }
        if (part.hasRelationships()) {
            marshallRelationshipPart(part.getRelationships(), PackageURIHelper.getRelationshipPartUri(part.getUri()), zos);
        }
        return true;
    }
