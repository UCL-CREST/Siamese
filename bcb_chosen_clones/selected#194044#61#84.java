    public static ArrayList<Credential> importCredentials(String urlString) {
        ArrayList<Credential> results = new ArrayList<Credential>();
        try {
            URL url = new URL(urlString);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buff = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                buff.append(line);
                if (line.equals("-----END PGP SIGNATURE-----")) {
                    Credential credential = ProfileParser.parseCredential(buff.toString(), true);
                    results.add(credential);
                    buff = new StringBuffer();
                } else {
                    buff.append(NL);
                }
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } catch (ParsingException e) {
            System.err.println(e);
        }
        return results;
    }
