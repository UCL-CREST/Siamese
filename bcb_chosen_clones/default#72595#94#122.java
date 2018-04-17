    private void execCommand(String command) throws Exception {
        Runtime r = Runtime.getRuntime();
        Process p = r.exec(command);
        InputStream p_i_s = p.getInputStream();
        OutputStream p_o_s = p.getOutputStream();
        InputStream p_e_s = p.getErrorStream();
        int status = -1;
        try {
            status = p.waitFor();
        } catch (InterruptedException err) {
        }
        if (0 != status) {
            return;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(p_i_s));
        String temp;
        temp = in.readLine();
        while ((temp = in.readLine()) != null) {
        }
        in.close();
        in = null;
        p_i_s.close();
        p_e_s.close();
        p_o_s.close();
        p_i_s = null;
        p_e_s = null;
        p_o_s = null;
        p = null;
    }
