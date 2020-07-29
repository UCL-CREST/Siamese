    public void writeData(String name, int items, int mznum, int mzscale, long tstart, long tdelta, int[] peaks) {
        PrintWriter file = getWriter(name + ".txt");
        file.println("999 9999");
        file.println("Doe, John");
        file.println("TEST Lab");
        if (mzscale == 1) file.println("PALMS Positive Ion Data"); else if (mzscale == -1) file.println("PALMS Negative Ion Data"); else file.println("PALMS GJIFJIGJ Ion Data");
        file.println("TEST Mission");
        file.println("1 1");
        file.println("1970 01 01 2008 07 09");
        file.println("0");
        file.println("TIME (UT SECONDS)");
        file.println(mznum + 4);
        for (int i = 0; i < mznum + 4; i++) file.println("1.0");
        for (int i = 0; i < mznum + 4; i++) file.println("9.9E29");
        file.println("TOTION total MCP signal (electron units)");
        file.println("HMASS high mass integral (fraction)");
        file.println("UNLIST (unlisted low mass peaks (fraction)");
        file.println("UFO unidentified peaks (fraction)");
        for (int i = 1; i <= mznum; i++) file.println("MS" + i + " (fraction)");
        int header2length = 13;
        file.println(header2length);
        for (int i = 0; i < header2length; i++) file.println("1.0");
        for (int i = 0; i < header2length; i++) file.println("9.9E29");
        file.println("AirCraftTime aircraft time (s)");
        file.println("INDEX index ()");
        file.println("SCAT scatter (V)");
        file.println("JMETER joule meter ()");
        file.println("ND neutral density (fraction)");
        file.println("SCALEA Mass scale intercept (us)");
        file.println("SCALEB mass scale slope (us)");
        file.println("NUMPKS number of peaks ()");
        file.println("CONF confidence (coded)");
        file.println("CAT preliminary category ()");
        file.println("AeroDiam aerodynamic diameter (um)");
        file.println("AeroDiam1p7 aero diam if density=1.7 (um)");
        file.println("TOTBACK total background subtracted (electron units)");
        file.println("0");
        file.println("0");
        String nothing = "0.000000";
        for (int i = 0; i < items; i++) {
            file.println(tstart + (tdelta * i));
            file.println(tstart + (tdelta * i) - 3);
            file.println(i + 1);
            for (int j = 0; j < 15; j++) file.println(Math.random());
            boolean peaked = false;
            for (int k = 1; k <= mznum; k++) {
                for (int j = 0; j < peaks.length && !peaked; j++) if (k == peaks[j]) {
                    double randData = (int) (1000000 * (j + 1));
                    file.println(randData / 1000000);
                    peaked = true;
                }
                if (!peaked) file.println(nothing);
                peaked = false;
            }
        }
        try {
            Scanner test = new Scanner(f);
            while (test.hasNext()) {
                System.out.println(test.nextLine());
            }
            System.out.println("test");
        } catch (Exception e) {
        }
        file.close();
    }
