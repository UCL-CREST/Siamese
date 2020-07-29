    public String sendRequestAndGetNormalStringOutPut(java.lang.String servletName, java.lang.String request) {
        String myurl = java.util.prefs.Preferences.systemRoot().get("serverurl", "");
        String myport = java.util.prefs.Preferences.systemRoot().get("portno", "8080");
        if (myport == null || myport.trim().equals("")) {
            myport = "80";
        }
        if (this.serverURL == null) {
            try {
                java.net.URL codebase = newgen.presentation.NewGenMain.getAppletInstance().getCodeBase();
                if (codebase != null) serverURL = codebase.getHost(); else serverURL = "localhost";
            } catch (Exception exp) {
                exp.printStackTrace();
                serverURL = "localhost";
            }
            newgen.presentation.component.IPAddressPortNoDialog ipdig = new newgen.presentation.component.IPAddressPortNoDialog(myurl, myport);
            ipdig.show();
            serverURL = myurl = ipdig.getIPAddress();
            myport = ipdig.getPortNo();
            java.util.prefs.Preferences.systemRoot().put("serverurl", serverURL);
            java.util.prefs.Preferences.systemRoot().put("portno", myport);
            System.out.println(serverURL);
        }
        String response = "";
        try {
            System.out.println("http://" + serverURL + ":" + myport + "/newgenlibctxt/" + servletName);
            java.net.URL url = new java.net.URL("http://" + serverURL + ":" + myport + "/newgenlibctxt/" + servletName);
            java.net.URLConnection urlconn = (java.net.URLConnection) url.openConnection();
            urlconn.setDoOutput(true);
            urlconn.setRequestProperty("Content-type", "text/xml; charset=UTF-8");
            java.io.OutputStream os = urlconn.getOutputStream();
            String req1xml = request;
            java.util.zip.CheckedOutputStream cos = new java.util.zip.CheckedOutputStream(os, new java.util.zip.Adler32());
            java.util.zip.GZIPOutputStream gop = new java.util.zip.GZIPOutputStream(cos);
            java.io.OutputStreamWriter dos = new java.io.OutputStreamWriter(gop, "UTF-8");
            System.out.println(req1xml);
            dos.write(req1xml);
            dos.flush();
            dos.close();
            System.out.println("url conn: " + urlconn.getContentEncoding() + "  " + urlconn.getContentType());
            java.io.InputStream ios = urlconn.getInputStream();
            java.util.zip.CheckedInputStream cis = new java.util.zip.CheckedInputStream(ios, new java.util.zip.Adler32());
            java.util.zip.GZIPInputStream gip = new java.util.zip.GZIPInputStream(cis);
            java.io.InputStreamReader br = new java.io.InputStreamReader(gip, "UTF-8");
            int n = -1;
            while ((n = br.read()) != -1) response += (char) n;
        } catch (java.net.ConnectException conexp) {
            javax.swing.JOptionPane.showMessageDialog(null, "<html>Could not establish connection with the NewGenLib server, " + "<br>These might be the possible reasons." + "<br><li>Check the network connectivity between this machine and the server." + "<br><li>Check whether NewGenLib server is running on the server machine." + "<br><li>NewGenLib server might not have initialized properly. In this case" + "<br>go to server machine, open NewGenLibDesktop Application," + "<br> utility ->Send log to NewGenLib Customer Support</html>", "Critical error", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception exp) {
            exp.printStackTrace(System.out);
            TroubleShootConnectivity troubleShoot = new TroubleShootConnectivity();
        }
        return response;
    }
