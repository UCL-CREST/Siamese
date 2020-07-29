    public static void track(String strUserName, String strShortDescription, String strLongDescription, String strPriority, String strComponent) {
        String strFromToken = "";
        try {
            URL url = new URL(getTracUrl() + "newticket");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String buffer = reader.readLine();
            while (buffer != null) {
                if (buffer.contains("__FORM_TOKEN")) {
                    Pattern pattern = Pattern.compile("value=\"[^\"]*\"");
                    Matcher matcher = pattern.matcher(buffer);
                    int start = 0;
                    matcher.find(start);
                    int von = matcher.start() + 7;
                    int bis = matcher.end() - 1;
                    strFromToken = buffer.substring(von, bis);
                }
                buffer = reader.readLine();
            }
            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod(getTracUrl() + "newticket");
            method.setRequestHeader("Cookie", "trac_form_token=" + strFromToken);
            method.addParameter("__FORM_TOKEN", strFromToken);
            method.addParameter("reporter", strUserName);
            method.addParameter("summary", strShortDescription);
            method.addParameter("type", "Fehler");
            method.addParameter("description", strLongDescription);
            method.addParameter("action", "create");
            method.addParameter("status", "new");
            method.addParameter("priority", strPriority);
            method.addParameter("milestone", "");
            method.addParameter("component", strComponent);
            method.addParameter("keywords", "BugReporter");
            method.addParameter("cc", "");
            method.addParameter("version", "");
            client.executeMethod(method);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
