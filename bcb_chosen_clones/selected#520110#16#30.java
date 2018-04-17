    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setHeader("Content-type", "application/force-download");
        response.setHeader("Content-disposition", "attachment");
        response.setHeader("filename", "export.txt");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        InputStream x = baseRequest.getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(x, writer);
        String theString = writer.toString();
        System.out.println(theString);
        response.getWriter().println(request.getParameter("file").replace("*", "\n"));
    }
