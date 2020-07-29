    public String getHtmlPage(URL url) {
        String html = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            html = sb.toString().replaceAll(HTML_FILTER_RE, " ");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }
