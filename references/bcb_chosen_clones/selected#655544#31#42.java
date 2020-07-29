    protected void writeToResponse(InputStream stream, HttpServletResponse response) throws IOException {
        OutputStream output = response.getOutputStream();
        try {
            IOUtils.copy(stream, output);
        } finally {
            try {
                stream.close();
            } finally {
                output.close();
            }
        }
    }
