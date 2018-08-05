    @Override
    protected void copy(Reader reader, OutputStream outputs) throws IOException {
        if (outputs == null) {
            throw new NullPointerException();
        }
        if (reader == null) {
            throw new NullPointerException();
        }
        ZipOutputStream zipoutputs = null;
        try {
            zipoutputs = new ZipOutputStream(outputs);
            zipoutputs.putNextEntry(new ZipEntry("default"));
            IOUtils.copy(reader, zipoutputs);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (zipoutputs != null) {
                zipoutputs.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }
