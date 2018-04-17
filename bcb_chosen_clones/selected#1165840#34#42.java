    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] path = StringUtils.split(request.getRequestURI(), "/");
        String file = path[path.length - 1];
        File f = new File(pathToImages + "/" + file);
        response.setContentType(getServletContext().getMimeType(f.getName()));
        FileInputStream fis = new FileInputStream(f);
        IOUtils.copy(fis, response.getOutputStream());
        fis.close();
    }
