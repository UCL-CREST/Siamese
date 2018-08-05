    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullUrl = req.getRequestURL().toString();
        if (fullUrl.indexOf(ip) != -1) {
            fullUrl = fullUrl.replaceAll(ip, "a.tbcdn.cn");
        }
        URL url = new URL(fullUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        PrintWriter out = resp.getWriter();
        String line;
        while ((line = in.readLine()) != null) {
            out.println(line);
        }
        in.close();
        out.flush();
    }
