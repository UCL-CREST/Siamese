    public String buscaCDA() {
        Properties prop = new CargaProperties().Carga();
        URL url;
        BufferedReader in;
        String inputLine;
        String miLinea = null;
        try {
            url = new URL(prop.getProperty("CDA"));
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("cda-TRUNK-")) {
                    miLinea = inputLine;
                    miLinea = miLinea.substring(miLinea.indexOf("lastSuccessfulBuild/artifact/dist/cda-TRUNK"));
                    miLinea = miLinea.substring(0, miLinea.indexOf("\">"));
                    miLinea = url + miLinea;
                }
            }
        } catch (Throwable t) {
        }
        log.debug("Detetectado last build CDA: " + miLinea);
        return miLinea;
    }
