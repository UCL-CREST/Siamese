    public String useService(HashMap<String, String> input) {
        String output = "";
        if (input.size() < 1) {
            return "";
        }
        String data = "";
        try {
            for (String key : input.keySet()) {
                data += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(input.get(key), "UTF-8");
            }
            data = data.substring(1);
            URL url = new URL(serviceUrl);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                output += line;
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
