    @Override
    public CElement doIt(final CD_Event message) {
        CElement old = this.getEvent().getFirst();
        try {
            int n = ((CNum) old).getValue();
            final List<PairOfInt> list = new ArrayList<PairOfInt>();
            int i = 2;
            while (i <= n) {
                if (n % i == 0) {
                    int exp = 0;
                    while (n % i == 0) {
                        exp++;
                        n = n / i;
                    }
                    list.add(new PairOfInt(i, exp));
                }
                i++;
            }
            for (final PairOfInt p : list) {
                System.out.println(p.base + " ^ " + p.exp);
            }
            final ArrayList<CElement> pL = new ArrayList<CElement>();
            for (final PairOfInt p : list) {
                if (p.exp == 1) {
                    pL.add(CNum.createNum(old, "" + p.base));
                } else {
                    final CNum cB = CNum.createNum(old, "" + p.base);
                    pL.add(CPot.createPot(cB, p.exp));
                }
            }
            CElement result;
            if (pL.size() > 1) {
                final CTimesRow cTR = CTimesRow.createRow(pL);
                cTR.correctInternalPraefixesAndRolle();
                result = cTR;
            } else {
                result = pL.get(0);
            }
            final CFences cF = CFences.createFenced(result);
            final CElement parent = old.getParent();
            if (parent instanceof CFences) {
                final CElement grandparent = parent.getParent();
                grandparent.replaceChild(cF, parent, true, true);
                old = cF;
            } else {
                parent.replaceChild(cF, old, true, true);
                final CD_Event e = new CD_Event(cF);
                final C_Changer c = DefHandler.getInst().getChanger(e);
                old = c.doIt(null);
            }
        } catch (final NumberFormatException e) {
            System.out.println("CA_Primfaktorzerlegung: ParseFehler");
        }
        return old;
    }
