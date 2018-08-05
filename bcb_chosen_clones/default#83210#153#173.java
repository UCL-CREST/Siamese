    public int runLoad(int requestId, int loadId, int server) throws RemoteException {
        int ret = 0;
        try {
            Process P;
            System.out.println("LINUX: " + linuxPath + "/Load1 " + requestId);
            System.out.println("java " + linuxPath + "/LoadId " + requestId);
            if (loadId == 1) P = Runtime.getRuntime().exec("cr_run java LoadId " + requestId); else P = Runtime.getRuntime().exec("cr_run java LoadId " + requestId);
            StringBuffer strBuf = new StringBuffer();
            String strLine = "";
            String strLine1 = "";
            BufferedReader outCommand = new BufferedReader(new InputStreamReader(P.getInputStream()));
            while ((strLine = outCommand.readLine()) != null) {
                strLine1 = strLine;
            }
            P.waitFor();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
