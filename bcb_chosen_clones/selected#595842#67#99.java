    private static List<String> loadServicesImplementations(final Class ofClass) {
        List<String> result = new ArrayList<String>();
        String override = System.getProperty(ofClass.getName());
        if (override != null) {
            result.add(override);
        }
        ClassLoader loader = ServiceLib.class.getClassLoader();
        URL url = loader.getResource("META-INF/services/" + ofClass.getName());
        if (url == null) {
            return result;
        }
        InputStream inStream = null;
        InputStreamReader reader = null;
        BufferedReader bReader = null;
        try {
            inStream = url.openStream();
            reader = new InputStreamReader(inStream);
            bReader = new BufferedReader(reader);
            String line;
            while ((line = bReader.readLine()) != null) {
                if (!line.matches("\\s*(#.*)?")) {
                    result.add(line.trim());
                }
            }
        } catch (IOException iox) {
            LOG.log(Level.WARNING, "Could not load services descriptor: " + url.toString(), iox);
        } finally {
            finalClose(bReader);
            finalClose(reader);
            finalClose(inStream);
        }
        return result;
    }
