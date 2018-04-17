    public double probeProcessEnergy(int processId) throws RemoteException {
        float idle_energy = 19;
        double utiltopower_factor = 0.2394;
        double cpu_util = 10;
        String energy = "";
        double total_energy = 0;
        try {
            Process P = Runtime.getRuntime().exec(linuxPath + "/PidCPU.sh " + processId);
            StringBuffer strBuf = new StringBuffer();
            String strLine = "";
            BufferedReader outCommand = new BufferedReader(new InputStreamReader(P.getInputStream()));
            while ((strLine = outCommand.readLine()) != null) {
                energy = strLine;
            }
            P.waitFor();
            cpu_util = Double.parseDouble(energy);
            total_energy = utiltopower_factor * cpu_util;
        } catch (Exception e) {
            total_energy = -1;
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return total_energy;
    }
