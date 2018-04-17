    private void dumpDocumentAndAttachments(ZipOutputStream zos, Database database, String id, String revision, boolean inlineAttachments) throws IOException {
        BaseDocument doc = database.getDocument(BaseDocument.class, id, revision, null);
        String path = idToRelPath(id);
        if (inlineAttachments) {
            Map<String, Attachment> attachments = doc.getAttachments();
            if (attachments != null && attachments.size() > 0) {
                for (Map.Entry<String, Attachment> e : attachments.entrySet()) {
                    String name = e.getKey();
                    Attachment attachment = e.getValue();
                    byte[] content = database.getAttachment(doc.getId(), name);
                    attachment.setStub(false);
                    String encodedData = Base64Util.encodeBase64(content);
                    attachment.setData(encodedData);
                }
            }
        }
        byte[] data = JSON.defaultJSON().forValue(doc).getBytes("UTF-8");
        ZipEntry entry = new ZipEntry(path + ".json");
        entry.setSize(data.length);
        zos.putNextEntry(entry);
        zos.write(data, 0, data.length);
        zos.flush();
        zos.closeEntry();
        if (!inlineAttachments) {
            Map<String, Attachment> attachments = doc.getAttachments();
            if (attachments != null && attachments.size() > 0) {
                String attachmentPath = path + "_attachments/";
                for (String name : attachments.keySet()) {
                    byte[] content = database.getAttachment(doc.getId(), name);
                    zos.putNextEntry(new ZipEntry(attachmentPath + idToRelPath(name)));
                    zos.write(content);
                    zos.closeEntry();
                }
            }
        }
    }
