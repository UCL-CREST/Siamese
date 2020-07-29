    private static String getTextFromURL(HttpServletRequest req, String urlString) {
        StringBuffer buffer = new StringBuffer();
        if (!urlString.startsWith("http")) {
            String requestURL = req.getRequestURL().toString();
            urlString = requestURL.substring(0, requestURL.lastIndexOf("/")) + urlString;
        }
        try {
            URL url = new URL(urlString);
            BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                buffer.append(line);
                buffer.append(Constants.LF);
            }
        } catch (FileNotFoundException nf) {
            log.error("File not found: " + urlString, nf);
        } catch (Exception e) {
            log.error("Exception while reading file: " + urlString, e);
        }
        return buffer.toString();
    }
