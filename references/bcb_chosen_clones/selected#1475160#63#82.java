    private void createPolicy(String policyName) throws SPLException {
        URL url = getClass().getResource(policyName + ".spl");
        StringBuffer contents = new StringBuffer();
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            while ((line = input.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
            input.close();
            System.out.println(policyName);
            System.out.println(contents.toString());
            boolean createReturn = jspl.createPolicy(policyName, contents.toString());
            System.out.println("Policy Created : " + policyName + " - " + createReturn);
            System.out.println("");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
