    public DataSet(String name, String type, URL docBase, String plotDir) {
        sitename = name.toUpperCase();
        data = new Vector[3];
        data[0] = new Vector();
        data[1] = new Vector();
        data[2] = new Vector();
        if (type == null) return;
        plottype = type.toLowerCase();
        String filename;
        filename = plotDir + sitename + "_" + plottype + ".plt.gz";
        try {
            double total = 0;
            URL dataurl = new URL(docBase, filename);
            BufferedReader readme = new BufferedReader(new InputStreamReader(new GZIPInputStream(dataurl.openStream())));
            while (true) {
                String myline = readme.readLine();
                if (myline == null) break;
                myline = myline.toLowerCase();
                if (myline.startsWith("fit:")) {
                    if (haveFit) {
                        continue;
                    }
                    StringTokenizer st = new StringTokenizer(myline.replace('\n', ' '));
                    fit = new Double[5];
                    String bye = (String) st.nextToken();
                    fit[0] = new Double((String) st.nextToken());
                    fit[1] = new Double((String) st.nextToken());
                    fit[2] = new Double((String) st.nextToken());
                    fit[3] = new Double((String) st.nextToken());
                    fit[4] = new Double((String) st.nextToken());
                    haveFit = true;
                    continue;
                }
                if (myline.startsWith("decyear:")) {
                    StringTokenizer st = new StringTokenizer(myline.replace('\n', ' '));
                    String bye = (String) st.nextToken();
                    decYear = new Double((String) st.nextToken());
                    haveDate = true;
                    continue;
                }
                StringTokenizer st = new StringTokenizer(myline.replace('\n', ' '));
                boolean ok = true;
                String tmp;
                Double[] mydbl = new Double[3];
                for (int i = 0; i < 3 && ok; i++) {
                    if (st.hasMoreTokens()) {
                        tmp = (String) st.nextToken();
                        if (tmp.startsWith("X") || tmp.startsWith("x")) {
                            ok = false;
                            break;
                        } else {
                            mydbl[i] = new Double(tmp);
                        }
                    } else {
                        mydbl[i] = new Double(0.0);
                    }
                }
                if (ok) {
                    if (mydbl[2].doubleValue() > 100) continue;
                    total = mydbl[1].doubleValue() + total;
                    for (int i = 0; i < 3; i++) {
                        data[i].addElement(mydbl[i]);
                    }
                }
            }
            average = total / length();
        } catch (FileNotFoundException e) {
            System.err.println("PlotApplet: file not found: " + e);
        } catch (IOException e) {
            System.err.println("PlotApplet: error reading input file: " + e);
        }
    }
