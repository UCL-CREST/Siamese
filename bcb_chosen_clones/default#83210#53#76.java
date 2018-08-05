    public double probeEnergy() throws RemoteException {
        float idle_energy1 = Float.parseFloat(idle_energy);
        double utiltopower_factor1 = Double.parseDouble(utiltopower_factor);
        double cpu_util = 10;
        String energy = "";
        double total_energy = 0;
        try {
            Process P = Runtime.getRuntime().exec(linuxPath + "/you.sh");
            StringBuffer strBuf = new StringBuffer();
            String strLine = "";
            BufferedReader outCommand = new BufferedReader(new InputStreamReader(P.getInputStream()));
            while ((strLine = outCommand.readLine()) != null) {
                energy = strLine;
            }
            P.waitFor();
            cpu_util = Double.parseDouble(energy);
            total_energy = idle_energy1 + utiltopower_factor1 * cpu_util;
        } catch (Exception e) {
            total_energy = -1;
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return total_energy;
    }
