    public String buscaSAIKU() {
        URL url;
        Properties prop = new CargaProperties().Carga();
        BufferedReader in;
        String inputLine;
        String miLinea = null;
        try {
            url = new URL(prop.getProperty("SAIKU"));
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("lastSuccessfulBuild/artifact/saiku-bi-platform-plugin/target")) {
                    miLinea = inputLine;
                    log.debug(miLinea);
                    miLinea = miLinea.substring(miLinea.indexOf("lastSuccessfulBuild/artifact/saiku-bi-platform-plugin/target"));
                    miLinea = miLinea.substring(0, miLinea.indexOf("\">"));
                    miLinea = url + miLinea;
                }
            }
        } catch (Throwable t) {
        }
        log.debug("Detetectado last build SAIKU: " + miLinea);
        return miLinea;
    }
