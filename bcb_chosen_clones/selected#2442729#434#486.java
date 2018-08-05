    private void readParameterTable() {
        if (this.parameters != null) return;
        parameters = new GribPDSParameter[NPARAMETERS];
        int center;
        int subcenter;
        int number;
        try {
            BufferedReader br;
            if (filename != null && filename.length() > 0) {
                GribPDSParamTable tab = (GribPDSParamTable) fileTabMap.get(filename);
                if (tab != null) {
                    this.parameters = tab.parameters;
                    return;
                }
            }
            if (url != null) {
                InputStream is = url.openStream();
                InputStreamReader isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
            } else {
                br = new BufferedReader(new FileReader("tables\\" + filename));
            }
            String line = br.readLine();
            String[] tableDefArr = SmartStringArray.split(":", line);
            center = Integer.parseInt(tableDefArr[1].trim());
            subcenter = Integer.parseInt(tableDefArr[2].trim());
            number = Integer.parseInt(tableDefArr[3].trim());
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0 || line.startsWith("//")) continue;
                GribPDSParameter parameter = new GribPDSParameter();
                tableDefArr = SmartStringArray.split(":", line);
                parameter.number = Integer.parseInt(tableDefArr[0].trim());
                parameter.name = tableDefArr[1].trim();
                if (tableDefArr[2].indexOf('[') == -1) {
                    parameter.description = parameter.unit = tableDefArr[2].trim();
                } else {
                    String[] arr2 = SmartStringArray.split("[", tableDefArr[2]);
                    parameter.description = arr2[0].trim();
                    parameter.unit = arr2[1].substring(0, arr2[1].lastIndexOf(']')).trim();
                }
                if (!this.setParameter(parameter)) {
                    System.err.println("Warning, bad parameter ignored (" + filename + "): " + parameter.toString());
                }
            }
            if (filename != null && filename.length() > 0) {
                GribPDSParamTable loadedTable = new GribPDSParamTable(filename, center, subcenter, number, this.parameters);
                fileTabMap.put(filename, loadedTable);
            }
        } catch (IOException ioError) {
            System.err.println("An error occurred in GribPDSParamTable while " + "trying to open the parameter table " + filename + " : " + ioError);
        }
    }
