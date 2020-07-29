    public static String executePost(String urlStr, Map paramsMap) throws IOException {
        StringBuffer result = new StringBuffer();
        HttpURLConnection connection = null;
        URL url = new URL(urlStr);
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        PrintWriter out = new PrintWriter(connection.getOutputStream());
        Iterator paramKeys = paramsMap.keySet().iterator();
        while (paramKeys.hasNext()) {
            String paramName = (String) paramKeys.next();
            out.print(paramName + "=" + paramsMap.get(paramName));
            if (paramKeys.hasNext()) {
                out.print('&');
            }
        }
        out.flush();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
        }
        in.close();
        out.close();
        connection.disconnect();
        String msg = result.toString();
        return stripOuterElement(msg);
    }
