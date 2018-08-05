    public void setRemoteConfig(String s) {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            URL url = new URL(s);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("#")) continue;
                String[] split = line.split("=");
                if (split.length >= 2) {
                    map.put(split[0], split[1]);
                }
            }
            MethodAndFieldSetter.setMethodsAndFields(this, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
