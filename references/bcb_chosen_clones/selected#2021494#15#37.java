    public static void main(String[] args) {
        try {
            String data = URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode("admin", "UTF-8");
            data += "&" + URLEncoder.encode("nick", "UTF-8") + "=" + URLEncoder.encode("k", "UTF-8");
            data += "&" + URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode("asdf", "UTF-8");
            data += "&" + URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            URL url = new URL("http://localhost:80/test/index.php");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
