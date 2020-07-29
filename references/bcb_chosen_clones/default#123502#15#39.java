    public String readLines() {
        StringBuffer lines = new StringBuffer();
        try {
            int HttpResult;
            URL url = new URL(address);
            URLConnection urlconn = url.openConnection();
            urlconn.connect();
            HttpURLConnection httpconn = (HttpURLConnection) urlconn;
            HttpResult = httpconn.getResponseCode();
            if (HttpResult != HttpURLConnection.HTTP_OK) {
                System.out.println("�޷����ӵ�" + address);
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) break;
                    lines.append(line + "\r\n");
                }
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines.toString();
    }
