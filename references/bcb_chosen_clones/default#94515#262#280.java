    int findNagiosVersion() {
        try {
            String line = null;
            Process vercheck = Runtime.getRuntime().exec(NAG_COMMAND + " --version");
            BufferedReader verout = new BufferedReader(new InputStreamReader(vercheck.getInputStream()));
            Pattern nagVer = Pattern.compile("^Nagios\\s*(\\d*).*");
            while ((line = verout.readLine()) != null) {
                Matcher m = nagVer.matcher(line);
                if (m.find()) {
                    return (Integer.parseInt(m.group(1)));
                }
            }
        } catch (Exception ex) {
            System.out.println("Problem reading output from command " + NAG_COMMAND + "\n");
            System.out.println("You may need to set the -n flag\n");
            System.exit(0);
        }
        return (2);
    }
