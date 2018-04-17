    public static String postRequest(String urlString, HashMap data) {
        String returnData = "";
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            Object[] keySet = data.keySet().toArray();
            Object[] values = data.values().toArray();
            for (int count = 0; count < keySet.length; count++) {
                out.print(URLEncoder.encode((String) keySet[count]) + "=" + URLEncoder.encode((String) values[count]));
                if ((count + 1) < keySet.length) out.print("&");
            }
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                returnData += inputLine;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            returnData = null;
        }
        return (returnData);
    }
