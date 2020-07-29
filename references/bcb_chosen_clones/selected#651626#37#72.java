    public BasePolicy(String flaskPath) throws Exception {
        SWIGTYPE_p_p_policy p_p_pol = apol.new_policy_t_p_p();
        if (!flaskPath.endsWith("/")) flaskPath += "/";
        File tmpPolConf = File.createTempFile("tmpBasePolicy", ".conf");
        BufferedWriter tmpPolFile = new BufferedWriter(new FileWriter(tmpPolConf));
        BufferedReader secClassFile = new BufferedReader(new FileReader(flaskPath + "security_classes"));
        int bufSize = 1024;
        char[] buffer = new char[bufSize];
        int read;
        while ((read = secClassFile.read(buffer)) > 0) {
            tmpPolFile.write(buffer, 0, read);
        }
        secClassFile.close();
        BufferedReader sidsFile = new BufferedReader(new FileReader(flaskPath + "initial_sids"));
        while ((read = sidsFile.read(buffer)) > 0) {
            tmpPolFile.write(buffer, 0, read);
        }
        sidsFile.close();
        BufferedReader axxVecFile = new BufferedReader(new FileReader(flaskPath + "access_vectors"));
        while ((read = axxVecFile.read(buffer)) > 0) {
            tmpPolFile.write(buffer, 0, read);
        }
        axxVecFile.close();
        tmpPolFile.write("attribute ricka; \ntype rick_t; \nrole rick_r types rick_t; \nuser rick_u roles rick_r;\nsid kernel      rick_u:rick_r:rick_t\nfs_use_xattr ext3 rick_u:rick_r:rick_t;\ngenfscon proc /  rick_u:rick_r:rick_t\n");
        tmpPolFile.flush();
        tmpPolFile.close();
        if (apol.open_policy(tmpPolConf.getAbsolutePath(), p_p_pol) == 0) {
            Policy = apol.policy_t_p_p_value(p_p_pol);
            if (Policy == null) {
                throw new Exception("Failed to allocate memory for policy_t struct.");
            }
            tmpPolConf.delete();
        } else {
            throw new IOException("Failed to open/parse base policy file: " + tmpPolConf.getAbsolutePath());
        }
    }
