    public static Set<Province> getProvincias(String pURL) {
        Set<Province> result = new HashSet<Province>();
        String iniProv = "<prov>";
        String finProv = "</prov>";
        String iniNomProv = "<np>";
        String finNomProv = "</np>";
        String iniCodigo = "<cpine>";
        String finCodigo = "</cpine>";
        int ini, fin;
        try {
            URL url = new URL(pURL);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            Province provincia;
            while ((str = br.readLine()) != null) {
                if (str.contains(iniProv)) {
                    provincia = new Province();
                    while ((str = br.readLine()) != null && !str.contains(finProv)) {
                        if (str.contains(iniNomProv)) {
                            ini = str.indexOf(iniNomProv) + iniNomProv.length();
                            fin = str.indexOf(finNomProv);
                            provincia.setDescription(str.substring(ini, fin));
                        }
                        if (str.contains(iniCodigo)) {
                            ini = str.indexOf(iniCodigo) + iniCodigo.length();
                            fin = str.indexOf(finCodigo);
                            provincia.setCodeProvince(Integer.parseInt(str.substring(ini, fin)));
                        }
                    }
                    result.add(provincia);
                }
            }
            br.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return result;
    }
