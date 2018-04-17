    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        if ((this.jTree2.getSelectionPath() == null) || !(this.jTree2.getSelectionPath().getLastPathComponent() instanceof StyleTreeNode)) {
            Msg.showMsg("Devi selezionare lo stile sotto il quale caricare la ricetta!", this);
            return;
        }
        if ((this.txtUser.getText() == null) || (this.txtUser.getText().length() == 0)) {
            Msg.showMsg("Il nome utente è obbligatorio!", this);
            return;
        }
        if ((this.txtPwd.getPassword() == null) || (this.txtPwd.getPassword().length == 0)) {
            Msg.showMsg("La password è obbligatoria!", this);
            return;
        }
        this.nomeRicetta = this.txtNome.getText();
        if ((this.nomeRicetta == null) || (this.nomeRicetta.length() == 0)) {
            Msg.showMsg("Il nome della ricetta è obbligatorio!", this);
            return;
        }
        StyleTreeNode node = null;
        if (this.jTree2.getSelectionPath().getLastPathComponent() instanceof StyleTreeNode) {
            node = (StyleTreeNode) this.jTree2.getSelectionPath().getLastPathComponent();
        }
        try {
            String data = URLEncoder.encode("nick", "UTF-8") + "=" + URLEncoder.encode(this.txtUser.getText(), "UTF-8");
            data += "&" + URLEncoder.encode("pwd", "UTF-8") + "=" + URLEncoder.encode(new String(this.txtPwd.getPassword()), "UTF-8");
            data += "&" + URLEncoder.encode("id_stile", "UTF-8") + "=" + URLEncoder.encode(node.getIdStile(), "UTF-8");
            data += "&" + URLEncoder.encode("nome_ricetta", "UTF-8") + "=" + URLEncoder.encode(this.nomeRicetta, "UTF-8");
            data += "&" + URLEncoder.encode("xml_ricetta", "UTF-8") + "=" + URLEncoder.encode(this.xml, "UTF-8");
            URL url = new URL("http://" + Main.config.getRemoteServer() + "/upload_ricetta.asp?" + data);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String str = "";
            while ((line = rd.readLine()) != null) {
                str += line;
            }
            rd.close();
            Msg.showMsg(str, this);
            doDefaultCloseAction();
        } catch (Exception e) {
            Utils.showException(e, "Errore in upload", this);
        }
        reloadTree();
    }
