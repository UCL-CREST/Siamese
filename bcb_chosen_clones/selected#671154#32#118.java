    public TabelaVerdade(String tipoDeExercicio, String ordem, String ordem2, String[][] valores, String preposicao) {
        super(Usuario.statusDoExercicio(tipoDeExercicio, InterfaceDados.obtemNumeroDoExercicioCorrente()));
        pSuperior = new JPanel(new GridLayout(2, 1));
        pTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titulo = new JLabel(tipoDeExercicio);
        titulo.setFont(new Font("Arial", 0, 25));
        titulo.setForeground(new Color(0, 0, 128));
        pTitulo.add(titulo);
        pSuperior.add(pTitulo);
        preposicao = alteraString(preposicao);
        valores = alteraMatrizDeStrings(valores);
        pEnun = new JPanel(new GridLayout(2, 1));
        enunciado1 = new JLabel(this.retornaEspacoAjustado("    ") + ordem);
        enunciado1.setFont(new Font("Arial", 1, 14));
        enunciado1.setForeground(new Color(0, 0, 128));
        pEnun.add(enunciado1);
        pEnun2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        enunciado2 = new JLabel(this.retornaEspacoAjustado("    ") + ordem2);
        enunciado2.setFont(new Font("Arial", 1, 14));
        enunciado2.setForeground(new Color(0, 0, 128));
        pEnun2.add(enunciado2);
        enunciado2 = new JLabel(preposicao);
        enunciado2.setFont(new Font("Times", 1, 16));
        enunciado2.setForeground(new Color(0, 0, 128));
        pEnun2.add(enunciado2);
        pEnun.add(pEnun2);
        pSuperior.add(pEnun);
        getContentPane().add("North", pSuperior);
        pOeste = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pOeste.add(new JLabel(this.retornaEspacoAjustado("  ")));
        getContentPane().add("East", pOeste);
        pOeste.add(new JLabel(this.retornaEspacoAjustado("    ")));
        getContentPane().add("West", pOeste);
        pCentral = new JPanel(new GridLayout(1, valores[0].length + 1));
        pColuna = new JPanel(new GridLayout(valores.length, 1));
        String[][] valor = new String[valores[1].length][valores.length];
        for (int i = 0; i < valor.length; i++) for (int j = 0; j < valor[i].length; j++) {
            valor[i][j] = valores[j][i];
        }
        int total = 0;
        for (int i = 0; i < valor.length; i++) {
            if (valor[i][1].equals("nada")) total++;
        }
        preencher = new JTextField[total + 1][valor[1].length - 1];
        resposta = new String[total + 1][valor[1].length - 1];
        int aux = 0;
        for (int i = 0; i <= valor.length; i++) {
            pColuna = new JPanel(new GridLayout(valor[1].length, 1));
            for (int j = 0; j < valor[1].length; j++) {
                if (i == valor.length) {
                    if (j == 0) {
                        pAux = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JLabel prep = new JLabel(preposicao);
                        prep.setFont(new Font("Times", 0, 11));
                        prep.setForeground(new Color(0, 0, 128));
                        pAux.add(prep);
                    } else {
                        pAux = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        preencher[total][j - 1] = new JTextField("", 3);
                        preencher[total][j - 1].setDocument(new DefinePadraoDoTexto(1));
                        preencher[total][j - 1].addKeyListener(this);
                        pAux.add(preencher[total][j - 1]);
                    }
                } else {
                    if ((valor[i][j]).equals("nada")) {
                        pAux = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        preencher[aux][j - 1] = new JTextField("", 3);
                        preencher[aux][j - 1].setDocument(new DefinePadraoDoTexto(1));
                        preencher[aux][j - 1].addKeyListener(this);
                        pAux.add(preencher[aux][j - 1]);
                        if (j == valor[i].length - 1) aux++;
                    } else {
                        pAux = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JLabel vals = new JLabel(valor[i][j]);
                        if (j == 0) {
                            vals.setFont(new Font("Times", 0, 14));
                            vals.setForeground(new Color(0, 0, 128));
                        }
                        pAux.add(vals);
                    }
                }
                pColuna.add(pAux);
            }
            pCentral.add(pColuna);
        }
        getContentPane().add("Center", pCentral);
    }
