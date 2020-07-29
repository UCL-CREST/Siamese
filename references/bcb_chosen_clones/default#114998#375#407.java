    protected String getSnaplogData(String snaplogFilename, String variables, int numOfLines, boolean trim) {
        if (snaplogFilename == null) {
            return "";
        }
        String[] cmdarray = new String[] { snaplogFrame.getGenplot(), "-tm", variables == null ? snaplogFrame.getVariables() : variables, snaplogFilename.startsWith("/") ? snaplogFilename : snaplogFrame.getSnaplogs().endsWith("/") ? snaplogFrame.getSnaplogs() + snaplogFilename : snaplogFrame.getSnaplogs() + "/" + snaplogFilename };
        Process genplotProcess;
        try {
            genplotProcess = Runtime.getRuntime().exec(cmdarray);
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(genplotProcess.getInputStream()));
        StringBuffer toReturn = new StringBuffer();
        try {
            int counter = 1;
            if (trim) {
                in.readLine();
                in.readLine();
                in.readLine();
            }
            String line = in.readLine();
            while (line != null && counter != numOfLines) {
                toReturn.append(line + "\n");
                line = in.readLine();
                counter += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        genplotProcess.destroy();
        return toReturn.toString();
    }
