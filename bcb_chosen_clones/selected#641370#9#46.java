    public String[] execute(String[] args) {
        if (args.length == 2) {
            if (System.getProperty("os.name").toLowerCase().contains(args[1])) {
            } else {
                return new String[] { "Skipping program, requires OS " + args[1] };
            }
        }
        ArrayList<String> result = new ArrayList<String>();
        Process pro = null;
        try {
            pro = Runtime.getRuntime().exec(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
        BufferedReader err = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
        try {
            String input = in.readLine();
            boolean read = false;
            while (input != null) {
                read = true;
                result.add(input);
                input = in.readLine();
            }
            if (!read) {
                input = err.readLine();
                while (input != null) {
                    read = true;
                    result.add(input);
                    input = err.readLine();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toArray(new String[result.size()]);
    }
