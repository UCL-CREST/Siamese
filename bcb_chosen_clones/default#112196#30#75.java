    public static void main(String[] args) {
        int pid = 0;
        String pidStr = "";
        String linuxPath = "/home/mohan/OSProject_05032009/";
        try {
            System.out.println("Into Process check:");
            Process P = Runtime.getRuntime().exec(linuxPath + "/Pid.sh");
            StringBuffer strBuf = new StringBuffer();
            String strLine = "";
            BufferedReader outCommand = new BufferedReader(new InputStreamReader(P.getInputStream()));
            while ((strLine = outCommand.readLine()) != null) {
                pidStr = strLine;
            }
            P.waitFor();
            pid = Integer.parseInt(pidStr);
            System.out.println("at Process check:" + pid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        System.out.println("after Process check:" + pid);
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url + dbName, userName, password);
            st = con.createStatement();
            int in1 = pid;
            String sql123 = "update os.LoadRequestResponse set processId=" + pid + " where requestId=" + args[0];
            int update = st.executeUpdate(sql123);
            System.out.println("into actual Load");
            int ins;
            int i;
            int j = 0;
            int k = 0;
            LoadId Obj = new LoadId();
            for (i = 0; i < 10000000; i++) {
                for (int l = 0; l < 20; l++) {
                    j = i + 1;
                    k = j - 1;
                    sql123 = "insert into os.Test(requestId,cnt) values (" + args[0] + "," + i + ")";
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
