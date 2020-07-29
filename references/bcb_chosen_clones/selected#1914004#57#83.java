    public byte[] createZipFile(final Collection<ZipEintrag> zipEintraege) throws IOException {
        final ByteArrayOutputStream dest = new ByteArrayOutputStream();
        final ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(dest));
        try {
            final List<String> dokumentNamen = new ArrayList<String>();
            for (final ZipEintrag zipEintrag : zipEintraege) {
                final String dokumentName = zipEintrag.getFilename();
                if (dokumentNamen.contains(dokumentName)) {
                    this.facesMessages.add(FacesMessage.SEVERITY_WARN, "Das Dokument " + dokumentName + " ist doppelt und wird nicht ber�cksichtigt");
                    continue;
                }
                final byte[] bytes = zipEintrag.getDaten();
                if (bytes == null) {
                    this.facesMessages.add(FacesMessage.SEVERITY_WARN, "Das Dokument " + dokumentName + " ist leer und wird nicht ber�cksichtigt");
                    continue;
                }
                dokumentNamen.add(dokumentName);
                final ZipEntry entry = new ZipEntry(dokumentName);
                zipStream.putNextEntry(entry);
                zipStream.write(bytes, 0, bytes.length);
            }
        } finally {
            zipStream.close();
            dest.close();
        }
        return dest.toByteArray();
    }
