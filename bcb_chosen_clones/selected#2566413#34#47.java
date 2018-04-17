    public void updateChecksum() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            List<Parameter> sortedKeys = new ArrayList<Parameter>(parameter_instances.keySet());
            for (Parameter p : sortedKeys) {
                if (parameter_instances.get(p) != null && !(parameter_instances.get(p) instanceof OptionalDomain.OPTIONS) && !(parameter_instances.get(p).equals(FlagDomain.FLAGS.OFF))) {
                    md.update(parameter_instances.get(p).toString().getBytes());
                }
            }
            this.checksum = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
