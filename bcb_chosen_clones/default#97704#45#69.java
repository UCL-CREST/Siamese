    public static String get(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(true);
            conn.setAllowUserInteraction(true);
            conn.setFollowRedirects(true);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("User-Agent:", "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; de-de) AppleWebKit/523.12.2 (KHTML, like Gecko) Version/3.0.4 Safari/523.12.2");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String s = "";
            String sRet = "";
            while ((s = in.readLine()) != null) {
                sRet += '\n' + s;
            }
            return sRet;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
