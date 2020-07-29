    public static IGraph exec2graph(String command) throws Exception {
        Process p = Runtime.getRuntime().exec(command);
        Reader r = new InputStreamReader(p.getInputStream());
        Graph g = new Graph("exec");
        IParserHandler h = new Ogdl2Graph(g);
        OgdlParser parser = new OgdlParser(r, h);
        parser.parse();
        return g;
    }
