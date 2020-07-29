    public static String getANewsLink() {
        String link = "";
        try {
            String urlStr = GlobalConfigs.NEWS_LIST_URL;
            if (urlStr == null || urlStr.trim().length() == 0) throw new Exception("No News Url is configured,please go to iask admin to set this value");
            URL url = new URL(urlStr);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.connect();
            InputStream urlStream = httpCon.getInputStream();
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(urlStream, "utf-8"));
            String htmlStr = "";
            String currentLine = "";
            while ((currentLine = bufferReader.readLine()) != null) {
                htmlStr = htmlStr + currentLine;
            }
            Pattern p = null;
            Matcher m = null;
            p = Pattern.compile("<tr><td>");
            m = p.matcher(htmlStr);
            int start = 0;
            int end = 0;
            int i = 0;
            int max = 5;
            int[] starts = new int[max];
            while (m.find()) {
                end = m.end();
                starts[i] = end;
                i = i + 1;
                if (i >= max) break;
            }
            i = 0;
            p = Pattern.compile("</td></tr>");
            m = p.matcher(htmlStr);
            int[] ends = new int[max];
            while (m.find()) {
                start = m.start();
                ends[i] = start;
                i = i + 1;
                if (i >= max) break;
            }
            int rand = new Random().nextInt();
            i = (rand >>> 1) % 10;
            if (i >= 5) i = i - 5;
            link = htmlStr.substring(starts[i], ends[i]);
        } catch (Exception e) {
            StackTraceElement[] element = e.getStackTrace();
            for (int i = 0; i < element.length; i++) {
                StackTraceElement stackTraceElement = element[i];
                log.error(stackTraceElement);
            }
        }
        return link;
    }
