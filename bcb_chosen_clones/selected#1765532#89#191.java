    public void setup(String sym, String dayOrWeek) {
        String useGoogle = System.getProperty("useGoogle");
        boolean bUseGoogle = (useGoogle != null && (useGoogle.equalsIgnoreCase("yes") || useGoogle.equalsIgnoreCase("true") || useGoogle.equalsIgnoreCase("1")));
        String googDailyOrWeekly = "daily";
        if (dayOrWeek.equals("d")) ; else if (dayOrWeek.equals("w")) googDailyOrWeekly = "weekly"; else if (dayOrWeek.equals("$")) ; else {
            System.out.println("day or week not d or w or $");
            goodData = false;
            return;
        }
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        String startDate = "&d=" + c.get(Calendar.MONTH) + "&e=" + c.get(Calendar.DAY_OF_MONTH) + "&f=" + c.get(Calendar.YEAR);
        String googEndDate = "&enddate=" + sdfMonth.format(c.getTime()) + "+" + sdfDay.format(c.getTime()) + "%2C+" + sdfYear.format(c.getTime());
        if (dayOrWeek.equals("d")) c.add(Calendar.DAY_OF_MONTH, defaultDaysToGoBack); else if (dayOrWeek.equals("w")) c.add(Calendar.DAY_OF_MONTH, defaultDaysToGoBack); else {
            c.add(Calendar.DAY_OF_MONTH, defaultDaysToGoBack);
            dayOrWeek = "d";
        }
        String stopDate = "&a=" + c.get(Calendar.MONTH) + "&b=" + c.get(Calendar.DAY_OF_MONTH) + "&c=" + c.get(Calendar.YEAR);
        String googStartDate = "&startdate=" + sdfMonth.format(c.getTime()) + "+" + sdfDay.format(c.getTime()) + "%2C+" + sdfYear.format(c.getTime());
        ArrayList<Double> op = new ArrayList<Double>();
        ArrayList<Double> hi = new ArrayList<Double>();
        ArrayList<Double> lo = new ArrayList<Double>();
        ArrayList<Double> cl = new ArrayList<Double>();
        ArrayList<String> dt = new ArrayList<String>();
        ArrayList<Double> vol = new ArrayList<Double>();
        ArrayList<Double> adjClose = new ArrayList<Double>();
        String sr;
        int cnt = 0;
        long vtot = 0;
        int vcnt = vol.size();
        try {
            URL url;
            if (bUseGoogle == false) url = new URL("http://ichart.finance.yahoo.com/table.csv?s=" + sym + "&g=" + dayOrWeek + stopDate + startDate + "&ignore=.csv"); else url = new URL("http://www.google.com/finance/historical?q=" + sym.toUpperCase() + "&histperiod=" + googDailyOrWeekly + googStartDate + googEndDate + "&output=csv");
            url.openConnection();
            InputStream inputstream = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
            SimpleDateFormat df2 = new SimpleDateFormat("dd-MMM-yy");
            SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
            while ((sr = br.readLine()) != null) {
                cnt++;
                if (cnt < 2) continue;
                StringTokenizer st = new StringTokenizer(sr, ",");
                if (bUseGoogle) {
                    String idate = (String) st.nextElement();
                    df2.parse(idate);
                    String open = (String) st.nextElement();
                    String high = (String) st.nextElement();
                    String low = (String) st.nextElement();
                    String close = (String) st.nextElement();
                    String volm = (String) st.nextElement();
                    if (open.equals(close) && open.equals(high) && open.equals(low) && volm.equals("0")) ; else {
                        dt.add(df3.format(df2.parse(idate)));
                        op.add(new Double(open));
                        hi.add(new Double(high));
                        lo.add(new Double(low));
                        cl.add(new Double(close));
                        vol.add(new Double(volm));
                        vtot += Long.parseLong(volm);
                        vcnt++;
                    }
                } else {
                    dt.add((String) st.nextElement());
                    op.add(new Double((String) st.nextElement()));
                    hi.add(new Double((String) st.nextElement()));
                    lo.add(new Double((String) st.nextElement()));
                    cl.add(new Double((String) st.nextElement()));
                    vol.add(new Double((String) st.nextElement()));
                    vtot += vol.get(vcnt).longValue();
                    vcnt++;
                    adjClose.add(new Double((String) st.nextElement()));
                }
            }
            inputstream.close();
        } catch (Exception e) {
            e.printStackTrace();
            goodData = false;
            return;
        }
        if (cnt < 5) {
            goodData = false;
            return;
        }
        if (!bUseGoogle) {
            for (int i = 0; i < dt.size(); i++) {
                double adj = cl.get(i).doubleValue() / adjClose.get(i);
                op.set(i, op.get(i).doubleValue() / adj);
                cl.set(i, cl.get(i).doubleValue() / adj);
                hi.set(i, hi.get(i).doubleValue() / adj);
                lo.set(i, lo.get(i).doubleValue() / adj);
                vol.set(i, vol.get(i).doubleValue() / adj);
            }
        }
        moveD(dt);
        moveA(op, "inOpen");
        moveA(hi, "inHigh");
        moveA(lo, "inLow");
        moveA(cl, "inClose");
        moveA(vol, "inVolume");
        if (!bUseGoogle) moveA(adjClose, "inAdjustedClose");
        goodData = true;
    }
