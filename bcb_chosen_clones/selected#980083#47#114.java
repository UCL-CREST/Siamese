    public double getDistanceFromDOTGraph(String sCorrectString, String sDotString) {
        File fCorrectTmp = null;
        if (sCorrectString != null) {
            try {
                fCorrectTmp = File.createTempFile("tmpCorrect", ".dot");
                FileWriter fwOut = new FileWriter(fCorrectTmp);
                BufferedWriter bf = new BufferedWriter(fwOut);
                bf.write(sCorrectString);
                bf.flush();
                bf.close();
                fwOut.close();
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
                return Double.NEGATIVE_INFINITY;
            }
        }
        ArrayList<String> alParams = new ArrayList();
        alParams.add(PathToCasc);
        alParams.add(fCorrectTmp.getPath());
        File fTmp = null;
        if (sDotString != null) {
            try {
                fTmp = File.createTempFile("tmp", ".dot");
                FileWriter fwOut = new FileWriter(fTmp);
                BufferedWriter bf = new BufferedWriter(fwOut);
                alParams.add(fTmp.getPath());
                bf.write(sDotString);
                bf.flush();
                bf.close();
                fwOut.close();
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
                return Double.NEGATIVE_INFINITY;
            }
        }
        String[] saCmd = new String[alParams.size()];
        saCmd = alParams.toArray(saCmd);
        ProcessBuilder pbP = new ProcessBuilder(saCmd);
        Process p;
        try {
            p = pbP.start();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            return Double.NEGATIVE_INFINITY;
        }
        InputStream isIn = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(isIn));
        String sStr = "";
        String sStrPrv = "";
        try {
            while ((sStr = br.readLine()) != null) {
                if (!sStr.matches("\\d+[:]\\d+\\s+[:]+\\s+\\d+\\s+\\(\\d+[.]\\d+[%]\\)")) {
                    sStrPrv = sStr;
                    continue;
                }
                String sRes = sStr.substring(sStr.lastIndexOf("(") + 1, sStr.lastIndexOf("%)"));
                return Double.valueOf(sRes).doubleValue() / 100.0;
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            return Double.NEGATIVE_INFINITY;
        }
        if (sDotString != null) {
            fCorrectTmp.delete();
            fTmp.delete();
        }
        return Double.NEGATIVE_INFINITY;
    }
