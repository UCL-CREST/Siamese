    protected void writeSchema(DataSet set, ZipOutputStream zipout, MetaFile metafile) throws Exception, IOException {
        XMLSchemaStorage xSchema = new XMLSchemaStorage();
        Document xmldocSchema = xSchema.store(set);
        String xmlSchema = xmldocSchema == null ? null : XMLUtil.toXMLString(xmldocSchema);
        if (xmlSchema == null) {
            throw new Exception("Не возможно сохранить XML схему");
        }
        ZipEntry zeSchema = new ZipEntry(schemaFileName());
        zipout.putNextEntry(zeSchema);
        byte[] bSchema = xmlSchema.getBytes(TextUtil.UTF8());
        zipout.write(bSchema, 0, bSchema.length);
        zipout.closeEntry();
        metafile.setSchemaFile(schemaFileName());
    }
