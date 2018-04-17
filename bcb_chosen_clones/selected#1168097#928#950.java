    public List<DemandeChargement> getArtistToLoadFromWiki() throws Exception {
        URL fileURL = new URL("http://beastchild.free.fr/wiki/doku.php?id=music");
        URLConnection urlConnection = fileURL.openConnection();
        InputStream httpStream = urlConnection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(httpStream, "ISO-8859-1"));
        String ligne;
        List<DemandeChargement> dem = new ArrayList<DemandeChargement>();
        while ((ligne = br.readLine()) != null) {
            if (ligne.indexOf("&lt;@@@&gt;") != -1) {
                String maidS = ligne.substring(ligne.indexOf("&lt;@@@&gt;") + 11, ligne.indexOf("&lt;/@@@&gt;")).trim();
                try {
                    long maid = Long.parseLong(maidS);
                    log.info("MAID to load : " + maid);
                    dem.add(new DemandeChargement(maid));
                } catch (Exception e) {
                    log.error("Impossible de recuperer le MAID : " + maidS);
                }
            }
        }
        br.close();
        httpStream.close();
        return dem;
    }
