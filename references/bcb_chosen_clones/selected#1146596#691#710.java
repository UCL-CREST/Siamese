    public static String getPagina(String strurl) {
        String resp = "";
        Authenticator.setDefault(new Autenticador());
        try {
            URL url = new URL(strurl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                resp += str;
            }
            in.close();
        } catch (MalformedURLException e) {
            resp = e.toString();
        } catch (IOException e) {
            resp = e.toString();
        } catch (Exception e) {
            resp = e.toString();
        }
        return resp;
    }
