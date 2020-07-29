    public String excute(String targetUrl, String params, String type) {
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(targetUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(type);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(params.getBytes().length));
            connection.setRequestProperty("Content-Language", CHAR_SET);
            connection.setRequestProperty("Connection", "close");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            if (params != null) {
                if (params.length() > 0) {
                    DataOutputStream wr;
                    wr = new DataOutputStream(connection.getOutputStream());
                    wr.writeBytes(params);
                    wr.flush();
                    wr.close();
                }
            }
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, CHAR_SET));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append("\r\n");
            }
            rd.close();
            return response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
