    private void setManagedContent(Entry entry, Datastream vds) throws StreamIOException {
        if (m_transContext == DOTranslationUtility.SERIALIZE_EXPORT_ARCHIVE && !m_format.equals(ATOM_ZIP1_1)) {
            String mimeType = vds.DSMIME;
            if (MimeTypeHelper.isText(mimeType) || MimeTypeHelper.isXml(mimeType)) {
                try {
                    entry.setContent(IOUtils.toString(vds.getContentStream(), m_encoding), mimeType);
                } catch (IOException e) {
                    throw new StreamIOException(e.getMessage(), e);
                }
            } else {
                entry.setContent(vds.getContentStream(), mimeType);
            }
        } else {
            String dsLocation;
            IRI iri;
            if (m_format.equals(ATOM_ZIP1_1) && m_transContext != DOTranslationUtility.AS_IS) {
                dsLocation = vds.DSVersionID + "." + MimeTypeUtils.fileExtensionForMIMEType(vds.DSMIME);
                try {
                    m_zout.putNextEntry(new ZipEntry(dsLocation));
                    InputStream is = vds.getContentStream();
                    IOUtils.copy(is, m_zout);
                    is.close();
                    m_zout.closeEntry();
                } catch (IOException e) {
                    throw new StreamIOException(e.getMessage(), e);
                }
            } else {
                dsLocation = StreamUtility.enc(DOTranslationUtility.normalizeDSLocationURLs(m_obj.getPid(), vds, m_transContext).DSLocation);
            }
            iri = new IRI(dsLocation);
            entry.setSummary(vds.DSVersionID);
            entry.setContent(iri, vds.DSMIME);
        }
    }
