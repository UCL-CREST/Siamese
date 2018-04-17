    public List<String> getLinks(String url) {
        List<String> links = new ArrayList<String>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            String s;
            StringBuilder builder = new StringBuilder();
            while ((s = bufferedReader.readLine()) != null) {
                builder.append(s);
            }
            Matcher tagmatch = htmltag.matcher(builder.toString());
            while (tagmatch.find()) {
                Matcher matcher = link.matcher(tagmatch.group());
                matcher.find();
                String link = matcher.group().replaceFirst("href=\"", "").replaceFirst("\">", "");
                if (valid(link)) {
                    links.add(makeAbsolute(url, link));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return links;
    }
