    public void addMetadata(Request request, Entry entry, ZipOutputStream zos, Metadata metadata, Element node) throws Exception {
        MetadataType type = getType(metadata.getType());
        if (type == null) {
            throw new IllegalStateException("Unknown metadata type:" + metadata.getType());
        }
        Document doc = node.getOwnerDocument();
        Element metadataNode = XmlUtil.create(doc, TAG_METADATA, node, new String[] { ATTR_TYPE, metadata.getType() });
        for (MetadataElement element : type.getChildren()) {
            int index = element.getIndex();
            String value = metadata.getAttr(index);
            if (value == null) {
                continue;
            }
            Element attrNode = XmlUtil.create(doc, Metadata.TAG_ATTR, metadataNode, new String[] { Metadata.ATTR_INDEX, "" + index });
            attrNode.appendChild(XmlUtil.makeCDataNode(doc, value, true));
            if (zos != null && element.getDataType().equals(element.DATATYPE_FILE)) {
                File f = type.getFile(entry, metadata, element);
                if (f == null || !f.exists()) continue;
                String fileName = repository.getGUID();
                attrNode.setAttribute("fileid", fileName);
                zos.putNextEntry(new ZipEntry(fileName));
                InputStream fis = getStorageManager().getFileInputStream(f.toString());
                try {
                    IOUtil.writeTo(fis, zos);
                } finally {
                    IOUtil.close(fis);
                    zos.closeEntry();
                }
            }
        }
    }
