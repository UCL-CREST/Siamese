    @Override
    public void doHandler(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String directURL = request.getRequestURL().toString();
        response.setCharacterEncoding("gbk");
        PrintWriter out = response.getWriter();
        try {
            directURL = urlTools.urlFilter(directURL, true);
            URL url = new URL(directURL);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "gbk"));
            String line;
            while ((line = in.readLine()) != null) {
                out.println(line);
            }
            in.close();
        } catch (Exception e) {
            out.println("file not find");
        }
        out.flush();
    }
