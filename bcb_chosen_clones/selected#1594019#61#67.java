    @SuppressWarnings("unchecked")
    private static List<Variavel> criarVariaveis(int tamanho, Class tipo) throws Exception {
        Variavel[] ret = new Variavel[tamanho];
        Constructor<Variavel> constr = tipo.getConstructor(String.class);
        for (int i = 0; i < tamanho; i++) ret[i] = constr.newInstance(String.valueOf(i + 1));
        return Arrays.asList(ret);
    }
