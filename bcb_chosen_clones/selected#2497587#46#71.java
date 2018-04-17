    @Override
    public byte[] getAvatar() throws IOException {
        HttpUriRequest request;
        try {
            request = new HttpGet(mUrl);
        } catch (IllegalArgumentException e) {
            IOException ioe = new IOException("Invalid url " + mUrl);
            ioe.initCause(e);
            throw ioe;
        }
        HttpResponse response = mClient.execute(request);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            byte[] data = new byte[1024];
            int nbread;
            while ((nbread = in.read(data)) != -1) {
                os.write(data, 0, nbread);
            }
        } finally {
            in.close();
            os.close();
        }
        return os.toByteArray();
    }
