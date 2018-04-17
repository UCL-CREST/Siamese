    private String[] sendRequest(String url, String requestString) throws ClickatellException, IOException {
        String response = null;
        MessageFormat responseFormat = new MessageFormat("{0}: {1}");
        List idList = new LinkedList();
        try {
            log_.debug("sendRequest: posting : " + requestString + " to " + url);
            URL requestURL = new URL(url);
            URLConnection urlConn = requestURL.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            PrintWriter pw = new PrintWriter(urlConn.getOutputStream());
            pw.print(requestString);
            pw.flush();
            pw.close();
            InputStream is = urlConn.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            while ((response = responseReader.readLine()) != null) {
                Object[] objs = responseFormat.parse(response);
                if ("ERR".equalsIgnoreCase((String) objs[0])) {
                    MessageFormat errorFormat = new MessageFormat("{0}: {1}, {2}");
                    Object[] errObjs = errorFormat.parse(response);
                    String errorNo = (String) errObjs[1];
                    String description = (String) errObjs[2];
                    throw new ClickatellException("Clickatell error. Error " + errorNo + ", " + description, Integer.parseInt(errorNo));
                }
                log_.debug("sendRequest: Got ID : " + ((String) objs[1]));
                idList.add(objs[1]);
            }
            responseReader.close();
        } catch (ParseException ex) {
            throw new ClickatellException("Unexpected response from Clickatell. : " + response, ClickatellException.ERROR_UNKNOWN);
        }
        return (String[]) idList.toArray(new String[idList.size()]);
    }
