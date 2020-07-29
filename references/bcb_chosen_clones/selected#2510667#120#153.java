    public String getTags(String content) {
        StringBuffer xml = new StringBuffer();
        OutputStreamWriter osw = null;
        BufferedReader br = null;
        try {
            String reqData = URLEncoder.encode(paramName, "UTF-8") + "=" + URLEncoder.encode(content, "UTF-8");
            URL service = new URL(cmdUrl);
            URLConnection urlConn = service.openConnection();
            urlConn.setDoOutput(true);
            urlConn.connect();
            osw = new OutputStreamWriter(urlConn.getOutputStream());
            osw.write(reqData);
            osw.flush();
            br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                xml.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (osw != null) {
                    osw.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return xml.toString();
    }
