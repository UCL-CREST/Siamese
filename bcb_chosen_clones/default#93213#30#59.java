    public static void main(String[] args) throws Exception {
        String urlString = "http://php.tech.sina.com.cn/download/d_load.php?d_id=7877&down_id=151542";
        urlString = EncodeUtils.encodeURL(urlString);
        URL url = new URL(urlString);
        System.out.println("第一次：" + url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        HttpURLConnection.setFollowRedirects(true);
        Map req = conn.getRequestProperties();
        System.out.println("第一次请求头：");
        printMap(req);
        conn.connect();
        System.out.println("第一次响应：");
        System.out.println(conn.getResponseMessage());
        int code = conn.getResponseCode();
        System.out.println("第一次code:" + code);
        printMap(conn.getHeaderFields());
        System.out.println(conn.getURL().getFile());
        if (code == 404 && !(conn.getURL() + "").equals(urlString)) {
            System.out.println(conn.getURL());
            String tmp = URLEncoder.encode(conn.getURL().toString(), "gbk");
            System.out.println(URLEncoder.encode("在线音乐播放脚本", "GBK"));
            System.out.println(tmp);
            url = new URL(tmp);
            System.out.println("第二次：" + url);
            conn = (HttpURLConnection) url.openConnection();
            System.out.println("第二次响应：");
            System.out.println("code:" + code);
            printMap(conn.getHeaderFields());
        }
    }
