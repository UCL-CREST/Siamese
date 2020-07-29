    public static Set<Municipality> getMunicipios(String pURL) {
        Set<Municipality> result = new HashSet<Municipality>();
        String iniCuerr = "<cuerr>";
        String finCuerr = "</cuerr>";
        String iniDesErr = "<des>";
        String finDesErr = "</des>";
        String iniMun = "<muni>";
        String finMun = "</muni>";
        String iniNomMun = "<nm>";
        String finNomMun = "</nm>";
        String iniCarto = "<carto>";
        String iniCodDelMEH = "<cd>";
        String finCodDelMEH = "</cd>";
        String iniCodMunMEH = "<cmc>";
        String finCodMunMEH = "</cmc>";
        String iniCodProvINE = "<cp>";
        String finCodProvINE = "</cp>";
        String iniCodMunINE = "<cm>";
        String finCodMunINE = "</cm>";
        boolean error = false;
        int ini, fin;
        try {
            URL url = new URL(pURL);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            Municipality municipio;
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
                    if (str.contains(iniMun)) {
                        municipio = new Municipality();
                        municipio.setCodemunicipalityine(0);
                        municipio.setCodemunicipalitydgc(0);
                        while ((str = br.readLine()) != null && !str.contains(finMun)) {
                            if (str.contains(iniNomMun)) {
                                ini = str.indexOf(iniNomMun) + iniNomMun.length();
                                fin = str.indexOf(finNomMun);
                                municipio.setMuniName(str.substring(ini, fin).trim());
                            }
                            if (str.contains(iniCarto)) {
                                if (str.contains("URBANA")) municipio.setIsurban(true);
                                if (str.contains("RUSTICA")) municipio.setIsrustic(true);
                            }
                            if (str.contains(iniCodDelMEH)) {
                                ini = str.indexOf(iniCodDelMEH) + iniCodDelMEH.length();
                                fin = str.indexOf(finCodDelMEH);
                                municipio.setCodemunicipalitydgc(municipio.getCodemunicipalitydgc() + Integer.parseInt(str.substring(ini, fin)) * 1000);
                            }
                            if (str.contains(iniCodMunMEH)) {
                                ini = str.indexOf(iniCodMunMEH) + iniCodMunMEH.length();
                                fin = str.indexOf(finCodMunMEH);
                                municipio.setCodemunicipalitydgc(municipio.getCodemunicipalitydgc() + Integer.parseInt(str.substring(ini, fin)));
                            }
                            if (str.contains(iniCodProvINE)) {
                                ini = str.indexOf(iniCodProvINE) + iniCodProvINE.length();
                                fin = str.indexOf(finCodProvINE);
                                municipio.setCodemunicipalityine(municipio.getCodemunicipalityine() + Integer.parseInt(str.substring(ini, fin)) * 1000);
                            }
                            if (str.contains(iniCodMunINE)) {
                                ini = str.indexOf(iniCodMunINE) + iniCodMunINE.length();
                                fin = str.indexOf(finCodMunINE);
                                municipio.setCodemunicipalityine(municipio.getCodemunicipalityine() + Integer.parseInt(str.substring(ini, fin)));
                            }
                            municipio.setDescription();
                        }
                        result.add(municipio);
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return result;
    }
