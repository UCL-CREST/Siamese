    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathTranslated().substring(0, request.getPathTranslated().length() - request.getPathInfo().length()) + request.getServletPath() + request.getPathInfo();
        File file = new File(path);
        if (file.exists()) {
            FileInputStream in = new FileInputStream(file);
            IOUtils.copyLarge(in, response.getOutputStream());
            in.close();
        }
    }
