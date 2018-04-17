    public static String getAnalysisServletOutput(String inputXml) {
        java.io.BufferedWriter bWriter = null;
        URLConnection connection = null;
        String resultString = "";
        bWriter = null;
        connection = null;
        String target = ServletConstant.ANALYSIS_SERVLET;
        String message = "\nTHIS MESSAGE IS SENT FROM THE CLIENT APPLET   \n\r";
        try {
            URL url = new URL(target);
            connection = (HttpURLConnection) url.openConnection();
            ((HttpURLConnection) connection).setRequestMethod("POST");
            connection.setDoOutput(true);
            bWriter = new java.io.BufferedWriter(new java.io.OutputStreamWriter(connection.getOutputStream()));
            bWriter.write(message);
            bWriter.flush();
            bWriter.close();
            java.io.BufferedReader bReader = null;
            bReader = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = bReader.readLine()) != null) {
                sb.append(line);
            }
            resultString = sb.toString();
            bReader.close();
            ((HttpURLConnection) connection).disconnect();
        } catch (java.io.IOException ex) {
            resultString += ex.toString();
        } finally {
            if (bWriter != null) {
                try {
                    bWriter.close();
                } catch (Exception ex) {
                    resultString += ex.toString();
                }
            }
            if (connection != null) {
                try {
                    ((HttpURLConnection) connection).disconnect();
                } catch (Exception ex) {
                    resultString += ex.toString();
                }
            }
        }
        return resultString;
    }
