    private void sendFile(File file, HttpServletResponse response) throws IOException {
        response.setContentLength((int) file.length());
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            IOUtils.copy(inputStream, response.getOutputStream());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
