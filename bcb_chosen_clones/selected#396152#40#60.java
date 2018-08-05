    private String sendQuery(String query) {
        File xmlServerResponse = null;
        String serverResponse = "";
        try {
            long start = Calendar.getInstance().getTimeInMillis();
            System.out.println("\n\n++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("    consulta de busqueda -> " + query);
            URL url = new URL(query);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                serverResponse += line;
            }
            long ahora = (Calendar.getInstance().getTimeInMillis() - start);
            System.out.println(" Tiempo transcurrido en la consulta (en milesimas) -> " + ahora);
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverResponse;
    }
