    public String getXML(String servletURL, String request) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            String encodedRequest = URLEncoder.encode(request, "UTF-8");
            URL url = new URL(servletURL + request);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException ex) {
            return null;
        } catch (UnsupportedEncodingException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
        return stringBuffer.toString();
    }
