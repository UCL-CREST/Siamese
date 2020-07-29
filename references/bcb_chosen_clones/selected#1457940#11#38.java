    public StringBuffer getReturn(String url_address) {
        StringBuffer message = new StringBuffer();
        try {
            URL url = new URL(url_address);
            try {
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.connect();
                InputStreamReader insr = new InputStreamReader(httpConnection.getInputStream());
                BufferedReader in = new BufferedReader(insr);
                String temp = in.readLine();
                while (temp != null) {
                    message.append(temp + "\n");
                    temp = in.readLine();
                }
                in.close();
            } catch (IOException e) {
                System.out.println("httpConnecter:Error[" + e + "]");
                message.append("Connect error [" + url_address + "]");
            }
        } catch (MalformedURLException e) {
            message.append("Connect error [" + url_address + "]");
            System.out.println("httpConneter:Error[" + e.getMessage() + "]");
        } catch (Exception e) {
            message.append("Connect error [" + url_address + "]");
            System.out.println("httpConneter:Error[" + e.getMessage() + "]");
        }
        return message;
    }
