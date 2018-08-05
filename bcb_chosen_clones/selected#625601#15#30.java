    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String zOntoJsonApiUrl = getInitParameter("zOntoJsonApiServletUrl");
        URL url = new URL(zOntoJsonApiUrl + "?" + req.getQueryString());
        resp.setContentType("text/html");
        InputStreamReader bf = new InputStreamReader(url.openStream());
        BufferedReader bbf = new BufferedReader(bf);
        String response = "";
        String line = bbf.readLine();
        PrintWriter out = resp.getWriter();
        while (line != null) {
            response += line;
            line = bbf.readLine();
        }
        out.print(response);
        out.close();
    }
