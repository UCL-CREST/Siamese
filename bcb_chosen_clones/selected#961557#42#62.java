    @Override
    public void render(Output output) throws IOException {
        output.setStatus(headersFile.getStatusCode(), headersFile.getStatusMessage());
        for (Entry<String, Set<String>> header : headersFile.getHeadersMap().entrySet()) {
            Set<String> values = header.getValue();
            for (String value : values) {
                output.addHeader(header.getKey(), value);
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
