    protected String readFileUsingHttp(String fileUrlName) {
        String response = "";
        try {
            URL url = new URL(fileUrlName);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;
            httpConn.setRequestProperty("Content-Type", "text/html");
            httpConn.setRequestProperty("Content-Length", "0");
            httpConn.setRequestMethod("GET");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setAllowUserInteraction(false);
            InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
                response += inputLine + "\n";
            }
            if (response.endsWith("\n")) {
                response = response.substring(0, response.length() - 1);
            }
            in.close();
        } catch (Exception x) {
            x.printStackTrace();
        }
        return response;
    }
