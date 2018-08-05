    public static String[] getHints(String query) {
        try {
            URL url = new URL("http://www.ebi.ac.uk/integr8/OrganismSearch.do?action=orgNames&orgName=" + query);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            String dest = new String();
            while ((inputLine = in.readLine()) != null) dest = dest.concat(inputLine);
            in.close();
            Matcher m = taxonPattern.matcher(dest);
            ArrayList<String> strings = new ArrayList<String>();
            while (m.find()) {
                strings.add(m.group(1));
            }
            return strings.toArray(new String[] {});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[0];
    }
