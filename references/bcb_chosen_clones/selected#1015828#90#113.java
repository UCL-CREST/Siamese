    private static ArrayList<String> YahooSearch(String query) {
        ArrayList<String> yahooSearchResults = new ArrayList<String>();
        try {
            String request = "http://boss.yahooapis.com/ysearch/web/v1/" + URLEncoder.encode("Java example for " + query, "UTF-8") + "?appid=zfau5aPV34ETbq9mWU0ui5e04y0rIewg1zwvzHb1tGoBFK2nSCU1SKS2D4zphh2rd3Wf" + "&format=xml&count=30&type=-msoffice,-pdf";
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
            Elements eles = doc.getElementsByTag("url");
            for (Element ele : eles) {
                yahooSearchResults.add(ele.text());
                System.out.println("YahooResult: " + ele.text());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return yahooSearchResults;
    }
