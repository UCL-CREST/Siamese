    public void doEcho() {
        String cmd = Messages.getString("MyDCM.6");
        cmd = (new StringBuilder()).append(userDir).append(BIN_DCMECHO).append(aeTitle).append(Messages.getString("MyDCM.7")).append(address).append(Messages.getString("MyDCM.8")).append(port).toString();
        try {
            System.out.println(cmd);
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                parent.showMessage(line);
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
