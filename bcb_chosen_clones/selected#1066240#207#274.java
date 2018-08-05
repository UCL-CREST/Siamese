    public static Set<Street> getVias(String pURL) {
        Set<Street> result = new HashSet<Street>();
        String iniCuerr = "<cuerr>";
        String finCuerr = "</cuerr>";
        String iniDesErr = "<des>";
        String finDesErr = "</des>";
        String iniVia = "<calle>";
        String finVia = "</calle>";
        String iniCodVia = "<cv>";
        String finCodVia = "</cv>";
        String iniTipoVia = "<tv>";
        String finTipoVia = "</tv>";
        String iniNomVia = "<nv>";
        String finNomVia = "</nv>";
        boolean error = false;
        int ini, fin;
        try {
            URL url = new URL(pURL);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            Street via;
            while ((str = br.readLine()) != null) {
                if (str.contains(iniCuerr)) {
                    ini = str.indexOf(iniCuerr) + iniCuerr.length();
                    fin = str.indexOf(finCuerr);
                    if (Integer.parseInt(str.substring(ini, fin)) > 0) error = true;
                }
                if (error) {
                    if (str.contains(iniDesErr)) {
                        ini = str.indexOf(iniDesErr) + iniDesErr.length();
                        fin = str.indexOf(finDesErr);
                        throw (new Exception(str.substring(ini, fin)));
                    }
                } else {
                    if (str.contains(iniVia)) {
                        via = new Street();
                        while ((str = br.readLine()) != null && !str.contains(finVia)) {
                            if (str.contains(iniCodVia)) {
                                ini = str.indexOf(iniCodVia) + iniCodVia.length();
                                fin = str.indexOf(finCodVia);
                                via.setCodeStreet(Integer.parseInt(str.substring(ini, fin)));
                            }
                            if (str.contains(iniTipoVia)) {
                                TypeStreet tipo = new TypeStreet();
                                if (!str.contains(finTipoVia)) tipo.setCodetpStreet(""); else {
                                    ini = str.indexOf(iniTipoVia) + iniTipoVia.length();
                                    fin = str.indexOf(finTipoVia);
                                    tipo.setCodetpStreet(str.substring(ini, fin));
                                }
                                tipo.setDescription(getDescripcionTipoVia(tipo.getCodetpStreet()));
                                via.setTypeStreet(tipo);
                            }
                            if (str.contains(iniNomVia)) {
                                ini = str.indexOf(iniNomVia) + iniNomVia.length();
                                fin = str.indexOf(finNomVia);
                                via.setStreetName(str.substring(ini, fin).trim());
                            }
                        }
                        result.add(via);
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return result;
    }
