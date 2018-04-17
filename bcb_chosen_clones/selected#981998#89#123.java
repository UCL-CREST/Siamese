    private File sendQuery(String query) throws MusicBrainzException {
        File xmlServerResponse = null;
        try {
            xmlServerResponse = new File(SERVER_RESPONSE_FILE);
            long start = Calendar.getInstance().getTimeInMillis();
            System.out.println("\n\n++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("    consulta de busqueda -> " + query);
            URL url = new URL(query);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String response = "";
            String line = "";
            System.out.println("    Respuesta del servidor: \n");
            while ((line = in.readLine()) != null) {
                response += line;
            }
            xmlServerResponse = new File(SERVER_RESPONSE_FILE);
            System.out.println("    Ruta del archivo XML -> " + xmlServerResponse.getAbsolutePath());
            BufferedWriter out = new BufferedWriter(new FileWriter(xmlServerResponse));
            out.write(response);
            out.close();
            System.out.println("Tamanho del xmlFile -> " + xmlServerResponse.length());
            long ahora = (Calendar.getInstance().getTimeInMillis() - start);
            System.out.println(" Tiempo transcurrido en la consulta (en milesimas) -> " + ahora);
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
        } catch (IOException e) {
            e.printStackTrace();
            String msg = e.getMessage();
            if (e instanceof FileNotFoundException) {
                msg = "ERROR: MusicBrainz URL used is not found:\n" + msg;
            } else {
            }
            throw new MusicBrainzException(msg);
        }
        return xmlServerResponse;
    }
