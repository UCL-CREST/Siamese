    public static List<Instance> analyze(File f) throws Exception {
        FileReader r = new FileReader(f);
        StringBuilder data = new StringBuilder();
        char[] cbuf = new char[4096];
        int read;
        while ((read = r.read(cbuf)) != -1) {
            data.append(cbuf, 0, read);
        }
        r.close();
        LOGGER.debug("{} characters read from input file", data.length());
        Pattern p = Pattern.compile("performing benchmark on graph \\(ID=[^\\|]*,\\|V\\|=(\\d+),\\|E\\|=(\\d+),[^\\)]*\\)", Pattern.MULTILINE | Pattern.DOTALL);
        List<Instance> instances = new LinkedList<Instance>();
        String input = data.toString();
        int from = 0;
        Matcher m = p.matcher(input);
        while (m.find()) {
            Instance i = new Instance();
            i.vertices = Integer.parseInt(m.group(1));
            i.edges = Integer.parseInt(m.group(2));
            i.solutions = new HashMap<String, List<Solution>>();
            LOGGER.debug("found run with {} vertices and {} edges", new Object[] { i.vertices, i.edges });
            instances.add(i);
            if (instances.size() > 1) {
                Instance k = instances.get(instances.size() - 2);
                String text = input.substring(from, m.start());
                extractSolutions(k, text);
            }
            from = m.end();
        }
        Instance k = instances.get(instances.size() - 1);
        String text = input.substring(from, data.length());
        extractSolutions(k, text);
        return instances;
    }
