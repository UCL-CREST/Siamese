    public static ArrayList<RoleName> importRoles(String urlString) {
        ArrayList<RoleName> results = new ArrayList<RoleName>();
        try {
            URL url = new URL(urlString);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buff = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                buff.append(line);
                if (line.equals("</RoleName>")) {
                    RoleName name = ProfileParser.parseRoleName(buff.toString());
                    results.add(name);
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
