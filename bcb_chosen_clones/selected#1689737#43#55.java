    private Long queryServer(OWLOntology ontologyURI) throws IOException {
        String requestString = "http://" + InetAddress.getLocalHost().getHostName() + ":8080/ChangeServer";
        requestString += "?query=" + URLEncoder.encode(ontologyURI.getURI().toString(), "UTF-8");
        URL url = new URL(requestString);
        BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer returned = new StringBuffer();
        String str;
        while (null != ((str = input.readLine()))) {
            returned.append(str);
        }
        input.close();
        return new Long(returned.toString());
    }
