    public HashMap<String, String> prepareDataFile(String fileName) {
        File originalDataFile = new File(fileName);
        String tableName = new String();
        String line = new String();
        BufferedReader br;
        BufferedWriter bw;
        long recordsNumber;
        File tempDataFile;
        HashMap<String, String> tableFileMap = new HashMap<String, String>();
        String filePath = new String();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(originalDataFile)));
            if (br.ready()) {
                if ((line = br.readLine()) != null) {
                    do {
                        tableName = "temp_" + getTableName(line);
                        tempDataFile = new File("./Data/" + tableName + ".txt");
                        tempDataFile.createNewFile();
                        tableFileMap.put(tableName, tempDataFile.getCanonicalPath());
                        recordsNumber = getRecordNumber(line);
                        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempDataFile)));
                        for (long i = 0; i < recordsNumber; i++) {
                            bw.write(br.readLine() + "\r\n");
                        }
                        bw.close();
                    } while ((line = br.readLine()) != null);
                }
                br.close();
                return tableFileMap;
            }
        } catch (Exception e) {
            ExceptionHandler.handleExcptin(e);
        }
        return null;
    }
