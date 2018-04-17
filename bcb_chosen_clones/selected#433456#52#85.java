    @Override
    public String baiDuHotNews() {
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://news.baidu.com/z/wise_topic_processor/wise_hotwords_list.php?bd_page_type=1&tn=wapnews_hotwords_list&type=1&index=1&pfr=3-11-bdindex-top-3--");
        String hostNews = "";
        try {
            HttpResponse response = client.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
            String line = "";
            boolean todayNewsExist = false, firstNewExist = false;
            int newsCount = -1;
            while ((line = buffer.readLine()) != null) {
                if (todayNewsExist || line.contains("<div class=\"news_title\">")) todayNewsExist = true; else continue;
                if (firstNewExist || line.contains("<div class=\"list-item\">")) {
                    firstNewExist = true;
                    newsCount++;
                } else continue;
                if (todayNewsExist && firstNewExist && (newsCount == 1)) {
                    Pattern hrefPattern = Pattern.compile("<a.*>(.+?)</a>.*");
                    Matcher matcher = hrefPattern.matcher(line);
                    if (matcher.find()) {
                        hostNews = matcher.group(1);
                        break;
                    } else newsCount--;
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hostNews;
    }
