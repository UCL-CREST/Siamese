    public static String getMAC() {
        try {
            Process process = Runtime.getRuntime().exec(IP_CONFIG_QUERY);
            StreamReader reader = new StreamReader(process.getInputStream());
            reader.start();
            process.waitFor();
            reader.join();
            String result = reader.getResult();
            String rval = result;
            Pattern macPattern = Pattern.compile("[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0 -9a-fA-F]{2}-[0-9a-fA-F]{2}");
            Matcher m = macPattern.matcher(result);
            rval = m.group();
            return rval;
        } catch (Exception e) {
            return "null";
        }
    }
