    public List<Integer> retornarIDItensPlanilha() {
        ArrayList<Integer> lista = new ArrayList<Integer>();
        if (Util.vazio(this.expressao)) {
            return lista;
        }
        Pattern padrao = Pattern.compile((PREFIXO_ID_ITEM + "[0-9]+"));
        Matcher m = padrao.matcher(this.expressao);
        while (m.find()) {
            lista.add(new Integer(this.expressao.substring(m.start() + 3, m.end())));
        }
        return lista;
    }
