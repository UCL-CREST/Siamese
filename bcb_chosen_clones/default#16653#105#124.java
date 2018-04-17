    @Override
    public String date(String var1) throws RemoteException {
        String response = "";
        try {
            Process p = Runtime.getRuntime().exec("date " + var1);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String s;
            while ((s = stdInput.readLine()) != null) {
                response += s;
            }
            while ((s = stdError.readLine()) != null) {
                response = "FALSE";
            }
        } catch (IOException e) {
            response = "Error in Execution";
            log.info("Cought exception: " + e.getMessage());
        }
        return response;
    }
