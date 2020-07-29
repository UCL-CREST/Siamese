    public static void addReaderField(final String fieldName, final Document document, final Store store, final TermVector termVector, final Reader reader) throws Exception {
        if (fieldName == null || reader == null) {
            LOGGER.warn("Field and reader can't be null : " + fieldName + ", " + reader);
            return;
        }
        Field field = document.getField(fieldName);
        if (field == null) {
            field = new Field(fieldName, reader, termVector);
            document.add(field);
        } else {
            Reader fieldReader = field.readerValue();
            if (fieldReader == null) {
                fieldReader = new StringReader(field.stringValue());
            }
            Reader finalReader = null;
            Writer writer = null;
            try {
                File tempFile = File.createTempFile(Long.toString(System.nanoTime()), IConstants.READER_FILE_SUFFIX);
                writer = new FileWriter(tempFile, false);
                char[] chars = new char[1024];
                int read = fieldReader.read(chars);
                while (read > -1) {
                    writer.write(chars, 0, read);
                    read = fieldReader.read(chars);
                }
                read = reader.read(chars);
                while (read > -1) {
                    writer.write(chars, 0, read);
                    read = reader.read(chars);
                }
                finalReader = new FileReader(tempFile);
                if (store.isStored()) {
                    document.removeField(fieldName);
                    field = new Field(fieldName, finalReader, termVector);
                    document.add(field);
                } else {
                    field.setValue(finalReader);
                }
            } catch (Exception e) {
                LOGGER.error("Exception writing the field value with the file writer : ", e);
            } finally {
                FileUtilities.close(writer);
                FileUtilities.close(finalReader);
                FileUtilities.close(fieldReader);
            }
        }
    }
