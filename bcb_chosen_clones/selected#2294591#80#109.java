    public static void encodeInstancia(InstanciaBean bean, OutputStream result) throws IOException {
        ZipOutputStream zipos = new ZipOutputStream(result);
        zipos.setMethod(ZipOutputStream.DEFLATED);
        for (Iterator iterAnexo = bean.getAnexos().keySet().iterator(); iterAnexo.hasNext(); ) {
            String key = (String) iterAnexo.next();
            Anexo anexo = (Anexo) bean.getAnexos().get(key);
            String entryName = ANEXO_SUBDIR + anexo.getName();
            ZipEntry zipEntry = new ZipEntry(entryName);
            zipEntry.setTime(System.currentTimeMillis());
            zipEntry.setSize(anexo.getData().length);
            zipEntry.setComment(anexo.getContentType());
            zipos.putNextEntry(zipEntry);
            zipos.write(anexo.getData(), 0, anexo.getData().length);
            zipos.closeEntry();
            bean.getAnexos().put(key, entryName);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(baos);
        encoder.setPersistenceDelegate(Locale.class, new DefaultPersistenceDelegate(new String[] { "language", "country", "variant" }));
        encoder.writeObject(bean);
        encoder.close();
        byte[] content = baos.toByteArray();
        ZipEntry zipEntry = new ZipEntry(FORM_DATA_FILE);
        zipEntry.setSize(baos.size());
        zipEntry.setTime(System.currentTimeMillis());
        zipos.putNextEntry(zipEntry);
        zipos.write(content, 0, content.length);
        zipos.closeEntry();
        zipos.close();
    }
