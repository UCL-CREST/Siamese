    private void streamContains(String in, InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(stream, baos);
        byte[] bytes = baos.toByteArray();
        String cmp = new String(bytes, "UTF-8");
        assertTrue(cmp.contains(in));
        baos.close();
    }
