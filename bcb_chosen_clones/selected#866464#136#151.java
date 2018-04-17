    private void testBtnActionPerformed(java.awt.event.ActionEvent evt) {
        Process p = null;
        Runtime r = Runtime.getRuntime();
        try {
            p = r.exec("cmd.exe /c gammu identify");
            BufferedReader rd = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String temp = "";
            String s = "";
            while ((temp = rd.readLine()) != null) {
                s += temp + "\n";
            }
            txtTestConn.setText(s);
        } catch (IOException ex) {
            ex.getMessage();
        }
    }
