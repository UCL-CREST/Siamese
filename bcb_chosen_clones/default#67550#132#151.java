    public int runLoad(int requestId, int loadId, int server) throws RemoteException {
        int ret = 0;
        try {
            Process P;
            System.out.println("LINUX: " + linuxPath + "/Load1 " + requestId);
            if (loadId == 1) P = Runtime.getRuntime().exec(linuxPath + "/Load1 " + requestId); else if (loadId == 2) P = Runtime.getRuntime().exec(linuxPath + "/Load1 " + requestId); else P = Runtime.getRuntime().exec(linuxPath + "/Load1 " + requestId);
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
