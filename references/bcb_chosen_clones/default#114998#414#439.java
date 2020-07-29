    protected String getTcpdumpData(String ndttraceFilename) {
        if (ndttraceFilename == null) {
            return "";
        }
        String[] cmdarray = new String[] { snaplogFrame.getTcptrace(), "-l", ndttraceFilename.startsWith("/") ? ndttraceFilename : snaplogFrame.getSnaplogs().endsWith("/") ? snaplogFrame.getSnaplogs() + ndttraceFilename : snaplogFrame.getSnaplogs() + "/" + ndttraceFilename };
        Process tcptraceProcess;
        try {
            tcptraceProcess = Runtime.getRuntime().exec(cmdarray);
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(tcptraceProcess.getInputStream()));
        StringBuffer toReturn = new StringBuffer();
        try {
            String line = in.readLine();
            while (line != null) {
                toReturn.append(line + "\n");
                line = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        tcptraceProcess.destroy();
        return toReturn.toString();
    }
