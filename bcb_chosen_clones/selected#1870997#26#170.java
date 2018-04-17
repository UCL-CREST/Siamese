    public ArrayList parseFile(File newfile) throws IOException {
        String s;
        String firstName;
        String header;
        String name = null;
        Integer PVLoggerID = new Integer(0);
        String[] tokens;
        int nvalues = 0;
        double num1, num2, num3;
        double xoffset = 1.0;
        double xdelta = 1.0;
        double yoffset = 1.0;
        double ydelta = 1.0;
        double zoffset = 1.0;
        double zdelta = 1.0;
        boolean readfit = false;
        boolean readraw = false;
        boolean zerodata = false;
        boolean baddata = false;
        boolean harpdata = false;
        ArrayList fitparams = new ArrayList();
        ArrayList xraw = new ArrayList();
        ArrayList yraw = new ArrayList();
        ArrayList zraw = new ArrayList();
        ArrayList sraw = new ArrayList();
        ArrayList sxraw = new ArrayList();
        ArrayList syraw = new ArrayList();
        ArrayList szraw = new ArrayList();
        URL url = newfile.toURI().toURL();
        InputStream is = url.openStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        while ((s = br.readLine()) != null) {
            tokens = s.split("\\s+");
            nvalues = tokens.length;
            firstName = (String) tokens[0];
            if (((String) tokens[0]).length() == 0) {
                readraw = false;
                readfit = false;
                continue;
            }
            if ((nvalues == 4) && (!firstName.startsWith("---"))) {
                if ((Double.parseDouble(tokens[1]) == 0.) && (Double.parseDouble(tokens[2]) == 0.) && (Double.parseDouble(tokens[3]) == 0.)) {
                    zerodata = true;
                } else {
                    zerodata = false;
                }
                if (tokens[1].equals("NaN") || tokens[2].equals("NaN") || tokens[3].equals("NaN")) {
                    baddata = true;
                } else {
                    baddata = false;
                }
            }
            if (firstName.startsWith("start")) {
                header = s;
            }
            if (firstName.indexOf("WS") > 0) {
                if (name != null) {
                    dumpData(name, fitparams, sraw, sxraw, syraw, szraw, yraw, zraw, xraw);
                }
                name = tokens[0];
                readraw = false;
                readfit = false;
                zerodata = false;
                baddata = false;
                harpdata = false;
                fitparams.clear();
                xraw.clear();
                yraw.clear();
                zraw.clear();
                sraw.clear();
                sxraw.clear();
                syraw.clear();
                szraw.clear();
            }
            if (firstName.startsWith("Area")) ;
            if (firstName.startsWith("Ampl")) ;
            if (firstName.startsWith("Mean")) ;
            if (firstName.startsWith("Sigma")) {
                fitparams.add(new Double(Double.parseDouble(tokens[3])));
                fitparams.add(new Double(Double.parseDouble(tokens[1])));
                fitparams.add(new Double(Double.parseDouble(tokens[5])));
            }
            if (firstName.startsWith("Offset")) ;
            if (firstName.startsWith("Slope")) ;
            if ((firstName.equals("Position")) && (((String) tokens[2]).equals("Raw"))) {
                readraw = true;
                continue;
            }
            if ((firstName.equals("Position")) && (((String) tokens[2]).equals("Fit"))) {
                readfit = true;
                continue;
            }
            if ((firstName.contains("Harp"))) {
                xraw.clear();
                yraw.clear();
                zraw.clear();
                sraw.clear();
                sxraw.clear();
                syraw.clear();
                szraw.clear();
                harpdata = true;
                readraw = true;
                name = tokens[0];
                continue;
            }
            if (firstName.startsWith("---")) continue;
            if (harpdata == true) {
                if (((String) tokens[0]).length() != 0) {
                    if (firstName.startsWith("PVLogger")) {
                        try {
                            PVLoggerID = new Integer(Integer.parseInt(tokens[2]));
                        } catch (NumberFormatException e) {
                        }
                    } else {
                        sxraw.add(new Double(Double.parseDouble(tokens[0])));
                        xraw.add(new Double(Double.parseDouble(tokens[1])));
                        syraw.add(new Double(Double.parseDouble(tokens[2])));
                        yraw.add(new Double(Double.parseDouble(tokens[3])));
                        szraw.add(new Double(Double.parseDouble(tokens[4])));
                        zraw.add(new Double(Double.parseDouble(tokens[5])));
                    }
                }
                continue;
            }
            if (readraw && (!zerodata) && (!baddata)) {
                sraw.add(new Double(Double.parseDouble(tokens[0]) / Math.sqrt(2.0)));
                sxraw.add(new Double(Double.parseDouble(tokens[0]) / Math.sqrt(2.0)));
                syraw.add(new Double(Double.parseDouble(tokens[0]) / Math.sqrt(2.0)));
                szraw.add(new Double(Double.parseDouble(tokens[0])));
                yraw.add(new Double(Double.parseDouble(tokens[1])));
                zraw.add(new Double(Double.parseDouble(tokens[2])));
                xraw.add(new Double(Double.parseDouble(tokens[3])));
            }
            if (firstName.startsWith("PVLogger")) {
                try {
                    PVLoggerID = new Integer(Integer.parseInt(tokens[2]));
                } catch (NumberFormatException e) {
                }
            }
        }
        dumpData(name, fitparams, sraw, sxraw, syraw, szraw, yraw, zraw, xraw);
        wiredata.add((Integer) PVLoggerID);
        return wiredata;
    }
