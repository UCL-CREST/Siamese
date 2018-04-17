    public byte[] loadResource(String name) throws IOException {
        ClassPathResource cpr = new ClassPathResource(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(cpr.getInputStream(), baos);
        return baos.toByteArray();
    }
