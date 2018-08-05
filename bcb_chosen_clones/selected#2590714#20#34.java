    public String getIpAddress() {
        try {
            URL url = new URL("http://checkip.dyndns.org");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String linha;
            String rtn = "";
            while ((linha = in.readLine()) != null) rtn += linha;
            ;
            in.close();
            return filtraRetorno(rtn);
        } catch (IOException ex) {
            Logger.getLogger(ExternalIp.class.getName()).log(Level.SEVERE, null, ex);
            return "ERRO.";
        }
    }
