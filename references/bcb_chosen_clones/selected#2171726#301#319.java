    public static void main(String[] arg) throws IOException {
        XmlPullParserFactory PULL_PARSER_FACTORY;
        try {
            PULL_PARSER_FACTORY = XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
            PULL_PARSER_FACTORY.setNamespaceAware(true);
            DasParser dp = new DasParser(PULL_PARSER_FACTORY);
            URL url = new URL("http://www.ebi.ac.uk/das-srv/uniprot/das/uniprot/features?segment=P05067");
            InputStream in = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String aLine, xml = "";
            while ((aLine = br.readLine()) != null) {
                xml += aLine;
            }
            WritebackDocument wbd = dp.parse(xml);
            System.out.println("FIN" + wbd);
        } catch (XmlPullParserException xppe) {
            throw new IllegalStateException("Fatal Exception thrown at initialisation.  Cannot initialise the PullParserFactory required to allow generation of the DAS XML.", xppe);
        }
    }
