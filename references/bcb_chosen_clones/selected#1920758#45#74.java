    public boolean marshall(PackagePart part, OutputStream os) throws OpenXML4JException {
        if (!(os instanceof ZipOutputStream)) {
            logger.error("Unexpected class " + os.getClass().getName());
            throw new OpenXML4JException("ZipOutputStream expected !");
        }
        ZipOutputStream zos = (ZipOutputStream) os;
        ZipEntry partEntry = new ZipEntry(ZipHelper.getZipItemNameFromOPCName(part.getPartName().getURI().getPath()));
        try {
            zos.putNextEntry(partEntry);
            InputStream ins = part.getInputStream();
            byte[] buff = new byte[ZipHelper.READ_WRITE_FILE_BUFFER_SIZE];
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
            logger.error("Cannot write: " + part.getPartName() + ": in ZIP", ioe);
            return false;
        }
        if (part.hasRelationships()) {
            PackagePartName relationshipPartName = PackagingURIHelper.getRelationshipPartName(part.getPartName());
            marshallRelationshipPart(part.getRelationships(), relationshipPartName, zos);
        }
        return true;
    }
