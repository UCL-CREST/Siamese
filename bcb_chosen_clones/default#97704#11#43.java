    public static String post(String strUrl, String strPostString) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(true);
            conn.setAllowUserInteraction(true);
            conn.setFollowRedirects(true);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(strPostString);
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String s = "";
            String sRet = "";
            while ((s = in.readLine()) != null) {
                sRet += s;
            }
            in.close();
            return sRet;
        } catch (MalformedURLException e) {
            System.out.println("Internal Error. Malformed URL.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Internal I/O Error.");
            e.printStackTrace();
        }
        return "";
    }
