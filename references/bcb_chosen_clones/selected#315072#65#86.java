    @Override
    public void render(Output output) throws IOException {
        output.setStatus(statusCode, statusMessage);
        if (headersMap != null) {
            Iterator<Entry<String, String>> iterator = headersMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> header = iterator.next();
                output.addHeader(header.getKey(), header.getValue());
            }
        }
        if (file != null) {
            InputStream inputStream = new FileInputStream(file);
            try {
                output.open();
                OutputStream out = output.getOutputStream();
                IOUtils.copy(inputStream, out);
            } finally {
                inputStream.close();
                output.close();
            }
        }
    }
