    private static String[] readConfigFile(String inputFileName, String configFilename) throws IOException {
        String[] result = { inputFileName, "", "", "", "", "", "", "" };
        useAgencyShortname = false;
        BufferedReader in = new BufferedReader(new FileReader(configFilename));
        String line;
        int tokenCount;
        String configValues[] = { "", "", "" };
        String txcMode = null;
        while ((line = in.readLine()) != null) {
            tokenCount = 0;
            StringTokenizer st = new StringTokenizer(line, "=");
            while (st.hasMoreTokens() && tokenCount <= 2) {
                configValues[tokenCount] = st.nextToken();
                if (tokenCount == 1) {
                    configValues[0] = configValues[0].trim().toLowerCase();
                    if (configValues[0].equals("url")) result[1] = new String(configValues[1]);
                    if (configValues[0].equals("timezone")) result[2] = new String(configValues[1]);
                    if (configValues[0].equals("default-route-type")) result[3] = new String(configValues[1]);
                    if (configValues[0].equals("lang")) result[6] = new String(configValues[1]);
                    if (configValues[0].equals("phone")) result[7] = new String(configValues[1]);
                    if (configValues[0].equals("output-directory")) result[4] = new String(configValues[1]);
                    if (configValues[0].equals("stopfile")) {
                        result[5] = new String(configValues[1]);
                        if (naptanStopnames == null) naptanStopnames = NaPTANHelper.readStopfile(configValues[1]);
                    }
                    if (configValues[0].equals("naptanstopcolumn")) {
                        if (stopColumns == null) stopColumns = new ArrayList();
                        stopColumns.add(configValues[1]);
                    }
                    if (configValues[0].equals("naptanstophelper")) if (stopColumns == null) naptanHelperStopColumn = 0; else naptanHelperStopColumn = stopColumns.size();
                    if (configValues[0].equals("stopfilecolumnseparator")) stopfilecolumnseparator = new String(configValues[1]);
                    if (configValues[0].equals("useagencyshortname") && configValues[1] != null && configValues[1].trim().toLowerCase().equals("true")) useAgencyShortname = true;
                    if (configValues[0].equals("skipemptyservice") && configValues[1] != null && configValues[1].trim().toLowerCase().equals("true")) skipEmptyService = true;
                    if (configValues[0].equals("skiporphanstops") && configValues[1] != null && configValues[1].trim().toLowerCase().equals("true")) skipOrphanStops = true;
                    if (configValues[0].equals("geocodemissingstops") && configValues[1] != null && configValues[1].trim().toLowerCase().equals("true")) geocodeMissingStops = true;
                    if (txcMode != null) if (txcMode.length() > 0 && configValues[1].length() > 0) {
                        if (modeList == null) modeList = new HashMap();
                        modeList.put(txcMode, configValues[1]);
                    }
                    txcMode = null;
                }
                if (configValues[0].length() >= 7 && configValues[0].substring(0, 7).equals("agency:")) {
                    configValues[1] = configValues[0].substring(7, configValues[0].length());
                    configValues[0] = "agency";
                    tokenCount++;
                }
                if (tokenCount == 2) {
                    if (configValues[0].equals("agency")) {
                        if (agencyMap == null) agencyMap = new HashMap();
                        agencyMap.put(configValues[1], configValues[2]);
                    }
                }
                if (configValues[0].length() >= 5 && configValues[0].substring(0, 5).equals("mode:")) txcMode = configValues[0].substring(5, configValues[0].length());
                tokenCount++;
            }
        }
        in.close();
        return result;
    }
