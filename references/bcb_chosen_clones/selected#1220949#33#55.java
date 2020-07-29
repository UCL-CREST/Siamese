    public void run() {
        BufferedReader inp = null;
        try {
            String urlString = "http://www.hubtracker.com/query.php?action=add&username=" + user + "&password=" + pass + "&email=" + e_mail + "&address=" + Vars.Hub_Host;
            URL url = new URL(urlString);
            URLConnection conn;
            if (!Vars.Proxy_Host.equals("")) conn = url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Vars.Proxy_Host, Vars.Proxy_Port))); else conn = url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            inp = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String xx;
            while ((xx = inp.readLine()) != null) PluginMain.result += "\n" + xx;
            if (curCmd != null) this.curCmd.cur_client.sendFromBot("[hubtracker:] " + PluginMain.result); else PluginMain.curFrame.showMsg();
            inp.close();
            inp = null;
        } catch (MalformedURLException ue) {
            PluginMain.result = ue.toString();
        } catch (Exception e) {
            PluginMain.result = e.toString();
        }
        done = true;
    }
