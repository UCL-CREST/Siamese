    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            String content = "";
            URL url = new URL(request.getParameter("url"));
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                content += line + "\n";
            }
            in.close();
            String result = getResult(content);
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            out.println(result);
        } catch (Exception e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println(getErrorPage(e));
        }
    }
