    public void close() throws IOException {
        ZipEntry ze;
        ze = new ZipEntry("mimetype");
        ze.setMethod(ZipEntry.STORED);
        ze.setSize(20);
        ze.setCrc(0x2cab616f);
        zos = new ZipOutputStream(new FileOutputStream(epubFilename));
        zos.putNextEntry(ze);
        zos.write("application/epub+zip".getBytes());
        ze = new ZipEntry("META-INF/container.xml");
        zos.putNextEntry(ze);
        String container = "<?xml version=\"1.0\"?>\n<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\n  <rootfiles>\n    <rootfile full-path=\"OEBPS/content.opf\" media-type=\"application/oebps-package+xml\"/>\n  </rootfiles>\n</container>";
        zos.write(container.getBytes());
        Content c = new Content(this);
        addMemoryContent("OEBPS/content.opf", c.dump(), 5);
        TOC t = new TOC(this);
        addMemoryContent("OEBPS/toc.ncx", t.dump(), 5);
        tmpzos.close();
        ZipInputStream zis = new ZipInputStream(new FileInputStream(tmpOutF));
        for (ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
            zos.putNextEntry(ze);
            Utils.writeTo(zis, zos);
        }
        zis.close();
        zos.close();
        tmpOutF.delete();
    }
