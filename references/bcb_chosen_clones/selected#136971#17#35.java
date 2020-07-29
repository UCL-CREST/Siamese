    @SuppressWarnings("unchecked")
    private SwapHandler() {
        this.getSwappers = new HashMap<String, CS_Base>();
        final ArrayList<String> strings = new ArrayList<String>();
        strings.add("SwapPunkt");
        strings.add("SwapStrich");
        strings.add("SwapRoot");
        strings.add("SwapSquare");
        Class c;
        for (final String s : strings) {
            try {
                c = Class.forName("net.sourceforge.kas.cTree.cSwap." + s);
                final CS_Base a = (CS_Base) c.getConstructor().newInstance();
                a.register(this.getSwappers);
            } catch (final Exception e) {
                System.err.println("Error in SwapHandler");
            }
        }
    }
