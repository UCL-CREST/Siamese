    private void addAuditDatastream() throws ObjectIntegrityException, StreamIOException {
        if (m_obj.getAuditRecords().size() == 0) {
            return;
        }
        String dsId = m_pid.toURI() + "/AUDIT";
        String dsvId = dsId + "/" + DateUtility.convertDateToString(m_obj.getCreateDate());
        Entry dsEntry = m_feed.addEntry();
        dsEntry.setId(dsId);
        dsEntry.setTitle("AUDIT");
        dsEntry.setUpdated(m_obj.getCreateDate());
        dsEntry.addCategory(MODEL.STATE.uri, "A", null);
        dsEntry.addCategory(MODEL.CONTROL_GROUP.uri, "X", null);
        dsEntry.addCategory(MODEL.VERSIONABLE.uri, "false", null);
        dsEntry.addLink(dsvId, Link.REL_ALTERNATE);
        Entry dsvEntry = m_feed.addEntry();
        dsvEntry.setId(dsvId);
        dsvEntry.setTitle("AUDIT.0");
        dsvEntry.setUpdated(m_obj.getCreateDate());
        ThreadHelper.addInReplyTo(dsvEntry, m_pid.toURI() + "/AUDIT");
        dsvEntry.addCategory(MODEL.FORMAT_URI.uri, AUDIT1_0.uri, null);
        dsvEntry.addCategory(MODEL.LABEL.uri, "Audit Trail for this object", null);
        if (m_format.equals(ATOM_ZIP1_1)) {
            String name = "AUDIT.0.xml";
            try {
                m_zout.putNextEntry(new ZipEntry(name));
                Reader r = new StringReader(DOTranslationUtility.getAuditTrail(m_obj));
                IOUtils.copy(r, m_zout, m_encoding);
                m_zout.closeEntry();
                r.close();
            } catch (IOException e) {
                throw new StreamIOException(e.getMessage(), e);
            }
            IRI iri = new IRI(name);
            dsvEntry.setSummary("AUDIT.0");
            dsvEntry.setContent(iri, "text/xml");
        } else {
            dsvEntry.setContent(DOTranslationUtility.getAuditTrail(m_obj), "text/xml");
        }
    }
