    private static String loadUrlToString(String a_url) throws IOException {
        URL l_url1 = new URL(a_url);
        BufferedReader br = new BufferedReader(new InputStreamReader(l_url1.openStream()));
        String l_content = "";
        String l_ligne = null;
        l_content = br.readLine();
        while ((l_ligne = br.readLine()) != null) {
            l_content += AA.SL + l_ligne;
        }
        return l_content;
    }
