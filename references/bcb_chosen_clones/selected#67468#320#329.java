    JcrFile createBody(Part part) throws IOException, MessagingException {
        JcrFile body = new JcrFile();
        body.setName("part");
        ByteArrayOutputStream pout = new ByteArrayOutputStream();
        IOUtils.copy(part.getInputStream(), pout);
        body.setDataProvider(new JcrDataProviderImpl(TYPE.BYTES, pout.toByteArray()));
        body.setMimeType(part.getContentType());
        body.setLastModified(java.util.Calendar.getInstance());
        return body;
    }
