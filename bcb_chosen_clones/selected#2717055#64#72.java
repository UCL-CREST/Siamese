    private static void adicionaSubEngine(String NomeSubEngine) throws Exception {
        Class classe = Class.forName(NomeSubEngine);
        try {
            Constructor construtor = classe.getConstructor();
            subEngines.add((SubEngine) construtor.newInstance());
        } catch (Exception e) {
            throw new Exception("Impossivel criar o subEngine " + NomeSubEngine, e);
        }
    }
