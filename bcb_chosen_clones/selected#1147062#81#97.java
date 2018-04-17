    public void save(RequirementProject _requirementProject, OutputStream _out) throws IOException, RequirementDocumentFormatException {
        ZipOutputStream zout = new ZipOutputStream(_out);
        zout.setLevel(0);
        ByteArrayOutputStream xmlBuffer = new ByteArrayOutputStream();
        attachmentMarshaller.setZout(zout);
        writeXml(_requirementProject, xmlBuffer);
        attachmentMarshaller.setZout(null);
        if (DEBUG_ALSO_SAVE_SEPARATE_MODEL_XML) {
            IoHelper.write(xmlBuffer.toByteArray(), new File("model_debug.xml"));
        }
        zout.setLevel(9);
        ZipEntry zipEntry = new ZipEntry(MODEL_XML_FILE_NAME);
        zout.putNextEntry(zipEntry);
        zout.write(xmlBuffer.toByteArray());
        zout.closeEntry();
        zout.close();
    }
