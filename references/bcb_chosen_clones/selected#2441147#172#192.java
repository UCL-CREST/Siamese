    private void getServiceReponse(String service, NameSpaceDefinition nsDefinition) throws Exception {
        Pattern pattern = Pattern.compile("(?i)(?:.*(xmlns(?:\\:\\w+)?=\\\"http\\:\\/\\/www\\.ivoa\\.net\\/.*" + service + "[^\\\"]*\\\").*)");
        pattern = Pattern.compile(".*xmlns(?::\\w+)?=(\"[^\"]*(?i)(?:" + service + ")[^\"]*\").*");
        logger.debug("read " + this.url + service);
        BufferedReader in = new BufferedReader(new InputStreamReader((new URL(this.url + service)).openStream()));
        String inputLine;
        BufferedWriter bfw = new BufferedWriter(new FileWriter(this.baseDirectory + service + ".xml"));
        boolean found = false;
        while ((inputLine = in.readLine()) != null) {
            if (!found) {
                Matcher m = pattern.matcher(inputLine);
                if (m.matches()) {
                    nsDefinition.init("xmlns:vosi=" + m.group(1));
                    found = true;
                }
            }
            bfw.write(inputLine + "\n");
        }
        in.close();
        bfw.close();
    }
