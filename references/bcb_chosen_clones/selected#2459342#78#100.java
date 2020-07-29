    private void checkFilter(File file, Filter filter) throws IOException {
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            for (int i = 0; i < subFiles.length; i++) {
                checkFilter(subFiles[i], filter);
            }
        } else {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ByteArrayOutputStream encoded = new ByteArrayOutputStream();
            ByteArrayOutputStream decoded = new ByteArrayOutputStream();
            FileInputStream fin = new FileInputStream(file);
            int amountRead = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((amountRead = fin.read(buffer, 0, BUFFER_SIZE)) != -1) {
                output.write(buffer, 0, amountRead);
            }
            fin.close();
            byte[] original = output.toByteArray();
            filter.encode(new ByteArrayInputStream(original), encoded, EMPTY_DICTIONARY);
            filter.decode(new ByteArrayInputStream(encoded.toByteArray()), decoded, EMPTY_DICTIONARY);
            cmpArray(original, decoded.toByteArray(), filter, file);
        }
    }
