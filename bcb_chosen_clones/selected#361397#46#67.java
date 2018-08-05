    @Test
    public void GetBingSearchResult() throws UnsupportedEncodingException {
        String query = "Scanner Java example";
        String request = "http://api.bing.net/xml.aspx?AppId=731DD1E61BE6DE4601A3008DC7A0EB379149EC29" + "&Version=2.2&Market=en-US&Query=" + URLEncoder.encode(query, "UTF-8") + "&Sources=web+spell&Web.Count=50";
        try {
            URL url = new URL(request);
            System.out.println("Host : " + url.getHost());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            String finalContents = "";
            while ((inputLine = reader.readLine()) != null) {
                finalContents += "\n" + inputLine;
            }
            Document doc = Jsoup.parse(finalContents);
            Elements eles = doc.getElementsByTag("web:Url");
            for (Element ele : eles) {
                System.out.println(ele.text());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
