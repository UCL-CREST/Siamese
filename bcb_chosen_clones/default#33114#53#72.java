    public int bindInstall() {
        String cmd = "apt-get install bind9";
        System.out.println("in action performed 1");
        System.out.println("in action performed 2");
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            System.out.println("in try");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        this.dispose();
        BindConfig config_bind = new BindConfig();
        config_bind.show();
        return 0;
    }
