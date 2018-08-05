    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.getWriter().println("Getting feed...");
        String googleFeed = "http://news.google.com/news?ned=us&topic=h&output=rss";
        String totalFeed = "";
        try {
            URL url = new URL(googleFeed);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                totalFeed += line;
            }
            reader.close();
            parseFeedandPersist(totalFeed, resp);
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    }
