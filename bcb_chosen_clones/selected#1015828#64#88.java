    private static ArrayList<String> BingSearch(String query) {
        ArrayList<String> bingSearchResults = new ArrayList<String>();
        try {
            String request = "http://api.bing.net/xml.aspx?AppId=731DD1E61BE6DE4601A3008DC7A0EB379149EC29" + "&Version=2.2&Market=en-US&Query=" + URLEncoder.encode("Java example for " + query, "UTF-8") + "&Sources=web+spell&Web.Count=30";
            URL url = new URL(request);
            System.out.println("Host : " + url.getHost());
            url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            String finalContents = "";
            while ((inputLine = reader.readLine()) != null) {
                finalContents += "\n" + inputLine;
            }
            Document doc = Jsoup.parse(finalContents);
            Elements eles = doc.getElementsByTag("web:Url");
            for (Element ele : eles) {
                String urlText = ele.text();
                if (!urlText.endsWith(".pdf") && !urlText.endsWith(".doc") && !urlText.endsWith(".ppt") && !urlText.endsWith(".PDF") && !urlText.endsWith(".DOC") && !urlText.endsWith(".PPT")) bingSearchResults.add(ele.text());
                System.out.println("BingResult: " + ele.text());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bingSearchResults;
    }
