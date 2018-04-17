    private void readNormalButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            input = fc.getSelectedFile();
        } else return;
        try {
            FileInputStream fis = new FileInputStream(input);
            InputStreamReader isr = new InputStreamReader(fis, "UTF8");
            BufferedReader uni = new BufferedReader(isr);
            String line = "", s = "";
            while (line != null) {
                line = uni.readLine();
                if (line != null) {
                    s += line + "\n";
                }
            }
            jTextArea1.setText(s);
            jTextPane1.setText(s);
        } catch (Exception ex) {
            String oops = ex.toString();
        }
    }
