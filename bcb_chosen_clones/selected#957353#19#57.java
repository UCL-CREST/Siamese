    public static ArrayList<String> remoteCall(Map<String, String> dataDict) {
        ArrayList<String> result = new ArrayList<String>();
        String encodedData = "";
        for (String key : dataDict.keySet()) {
            String encodedSegment = "";
            String value = dataDict.get(key);
            if (value == null) continue;
            try {
                encodedSegment = key + "=" + URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (encodedData.length() > 0) {
                encodedData += "&";
            }
            encodedData += encodedSegment;
        }
        try {
            URL url = new URL(baseURL + encodedData);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
                System.out.println("GOT: " + line);
            }
            reader.close();
            result.remove(0);
            if (result.size() != 0) {
                if (!result.get(result.size() - 1).equals("DONE")) {
                    result.clear();
                } else {
                    result.remove(result.size() - 1);
                }
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return result;
    }
