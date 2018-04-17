    public String sendMessage(String message, boolean log) {
        StringBuilder ret;
        try {
            URL url = new URL(this.stringURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("User-Agent", serverName);
            urlConnection.setRequestProperty("Host", ip);
            urlConnection.setRequestProperty("Content-type", "text/xml");
            urlConnection.setRequestProperty("Content-length", Integer.toString(message.length()));
            PrintWriter _out = new PrintWriter(urlConnection.getOutputStream());
            if (log) {
                CampaignData.mwlog.infoLog("Sending Message: " + MWCyclopsUtils.formatMessage(message));
            } else CampaignData.mwlog.infoLog("Sending Message: " + message);
            _out.println(message);
            _out.flush();
            _out.close();
            ret = new StringBuilder();
            if (log) {
                BufferedReader _in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String input;
                while ((input = _in.readLine()) != null) ret.append(input + "\n");
                CampaignData.mwlog.infoLog(ret.toString());
                _in.close();
            } else {
                BufferedReader _in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while (_in.readLine() != null) {
                }
                _in.close();
            }
            _out.close();
            urlConnection.disconnect();
            return ret.toString();
        } catch (Exception ex) {
            CampaignData.mwlog.errLog(ex);
        }
        return "";
    }
