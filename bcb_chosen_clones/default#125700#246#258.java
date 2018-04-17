    private String[] execSingleLineOutputCmd(String cmdWithParams) {
        String result = "";
        try {
            Process p = Runtime.getRuntime().exec(cmdWithParams.split(" "));
            BufferedReader sin = new BufferedReader(new InputStreamReader(p.getInputStream()));
            result = sin.readLine();
            sin.close();
            return result.split(" ");
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        }
    }
