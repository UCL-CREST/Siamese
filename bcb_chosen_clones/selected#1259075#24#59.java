    public static String translate(String s, String type) {
        try {
            String result = null;
            URL url = new URL("http://www.excite.co.jp/world/english/");
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
            connection.setDoOutput(true);
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print("before=" + URLEncoder.encode(s, "SJIS") + "&wb_lp=");
            out.print(type);
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "SJIS"));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                int textPos = inputLine.indexOf("name=\"after\"");
                if (textPos >= 0) {
                    int ltrPos = inputLine.indexOf(">", textPos + 11);
                    if (ltrPos >= 0) {
                        int closePos = inputLine.indexOf("<", ltrPos + 1);
                        if (closePos >= 0) {
                            result = inputLine.substring(ltrPos + 1, closePos);
                            break;
                        } else {
                            result = inputLine.substring(ltrPos + 1);
                            break;
                        }
                    }
                }
            }
            in.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
