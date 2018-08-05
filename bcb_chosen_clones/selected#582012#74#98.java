    public CircleDisplay(ExpFile exp, GrpFile grp) {
        this.exp = exp;
        this.grp = grp;
        if (grp == null || grp.getNumGenes() == 0) names = exp.getGeneVector(); else names = grp.getGroup();
        namePlaces = new int[names.length];
        dissims = new float[names.length][names.length];
        for (int i = 0; i < names.length; i++) {
            namePlaces[i] = exp.findGeneName(names[i].toString());
        }
        this.setBackground(Color.white);
        for (int i = 0; i < names.length; i++) {
            for (int j = i; j < names.length; j++) {
                if (namePlaces[i] != -1 && namePlaces[j] != -1) dissims[i][j] = exp.correlation(namePlaces[i], namePlaces[j]); else dissims[i][j] = 100;
                dissims[j][i] = dissims[i][j];
            }
        }
        thresh = .2f;
        boxes = new GeneBox[names.length];
        this.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                this_mouseClicked(e);
            }
        });
    }
