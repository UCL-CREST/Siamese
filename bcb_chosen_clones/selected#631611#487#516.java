    @Override
    public byte[] getDownloadBytes(String[] extraFields, String[] displayNames) {
        ArrayList<QueryField> fields = new ArrayList<QueryField>();
        if (getFacetFieldList() != null) {
            fields.add(data.get(0));
            fields.addAll(getFacetFieldList());
        }
        if (extraFields != null && extraFields.length > 0) {
            for (int i = 0; i < extraFields.length; i++) {
                String s = extraFields[i];
                if (displayNames == null) {
                    fields.add(new QueryField(s, CommonData.getFacetLayerDisplayName(s), QueryField.FieldType.AUTO));
                } else {
                    fields.add(new QueryField(s, displayNames[i], QueryField.FieldType.AUTO));
                }
            }
        }
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(bos);
            ZipEntry ze = new ZipEntry(getName() + ".csv");
            zos.putNextEntry(ze);
            zos.write(sample(fields).getBytes("UTF-8"));
            zos.close();
            return bos.toByteArray();
        } catch (Exception ex) {
            Logger.getLogger(UploadQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
