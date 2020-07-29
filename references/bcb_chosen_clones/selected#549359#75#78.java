    @Override
    protected void sendQuietly(HttpServletResponse response) throws Exception {
        IOUtils.copy(input, response.getOutputStream());
    }
