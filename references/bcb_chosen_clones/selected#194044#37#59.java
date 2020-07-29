    public static ArrayList<Principal> importPrincipals(String urlString) {
        ArrayList<Principal> results = new ArrayList<Principal>();
        try {
            URL url = new URL(urlString);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buff = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                buff.append(line);
                if (line.equals("</Principal>")) {
                    Principal principal = ProfileParser.parsePrincipal(buff.toString());
                    results.add(principal);
                    buff = new StringBuffer();
                } else {
                    buff.append(NL);
                }
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } catch (ParsingException e) {
        }
        return results;
    }
