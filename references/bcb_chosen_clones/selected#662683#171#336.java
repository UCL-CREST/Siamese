    public int procesar() {
        int mas = 0;
        String uriOntologia = "", source = "", uri = "";
        String fichOrigenHTML = "", fichOrigenLN = "";
        String ficheroOutOWL = "";
        md5 firma = null;
        StringTokenV2 entra = null, entra2 = null, entra3 = null;
        FileInputStream lengNat = null;
        BufferedInputStream lengNat2 = null;
        DataInputStream entradaLenguajeNatural = null;
        FileWriter salOWL = null;
        BufferedWriter salOWL2 = null;
        PrintWriter salidaOWL = null;
        String sujeto = "", verbo = "", CD = "", CI = "", fraseOrigen = "";
        StringTokenV2 token2;
        boolean bandera = false;
        OntClass c = null;
        OntClass cBak = null;
        String claseTrabajo = "";
        String nombreClase = "", nombrePropiedad = "", variasPalabras = "";
        int incre = 0, emergencia = 0;
        String lineaSalida = "";
        String[] ontologia = new String[5];
        ontologia[0] = "http://www.criado.info/owl/vertebrados_es.owl#";
        ontologia[1] = "http://www.w3.org/2001/sw/WebOnt/guide-src/wine#";
        ontologia[2] = "http://www.co-ode.org/ontologies/pizza/2005/10/18/pizza.owl#";
        ontologia[3] = "http://www.w3.org/2001/sw/WebOnt/guide-src/food#";
        ontologia[4] = "http://www.daml.org/2001/01/gedcom/gedcom#";
        String[] ontologiaSource = new String[5];
        ontologiaSource[0] = this.directorioMapeo + "\\" + "mapeo_vertebrados_es.xml";
        ontologiaSource[1] = this.directorioMapeo + "\\" + "mapeo_wine_es.xml";
        ontologiaSource[2] = this.directorioMapeo + "\\" + "mapeo_pizza_es.xml";
        ontologiaSource[3] = this.directorioMapeo + "\\" + "mapeo_food_es.xml";
        ontologiaSource[4] = this.directorioMapeo + "\\" + "mapeo_parentesco_es.xml";
        mapeoIdiomas clasesOntologias;
        try {
            if ((entrada = entradaFichero.readLine()) != null) {
                if (entrada.trim().length() > 10) {
                    entrada2 = new StringTokenV2(entrada.trim(), "\"");
                    if (entrada2.isIncluidaSubcadena("<fichero ontologia=")) {
                        ontologiaOrigen = entrada2.getToken(2);
                        fichOrigenHTML = entrada2.getToken(4);
                        fichOrigenLN = entrada2.getToken(6);
                        if (ontologiaOrigen.equals("VERTEBRADOS")) {
                            source = ontologiaSource[0];
                            uriOntologia = ontologia[0];
                        }
                        if (ontologiaOrigen.equals("WINE")) {
                            source = ontologiaSource[1];
                            uriOntologia = ontologia[1];
                        }
                        if (ontologiaOrigen.equals("PIZZA")) {
                            source = ontologiaSource[2];
                            uriOntologia = ontologia[2];
                        }
                        if (ontologiaOrigen.equals("FOOD")) {
                            source = ontologiaSource[3];
                            uriOntologia = ontologia[3];
                        }
                        if (ontologiaOrigen.equals("PARENTESCOS")) {
                            source = ontologiaSource[4];
                            uriOntologia = ontologia[4];
                        }
                        firma = new md5(uriOntologia, false);
                        clasesOntologias = new mapeoIdiomas(source);
                        uri = "";
                        ficheroOutOWL = "";
                        entra2 = new StringTokenV2(fichOrigenHTML, "\\");
                        int numToken = entra2.getNumeroTokenTotales();
                        entra = new StringTokenV2(fichOrigenHTML, " ");
                        if (entra.isIncluidaSubcadena(directorioLocal)) {
                            entra = new StringTokenV2(entra.getQuitar(directorioLocal) + "", " ");
                            uri = entra.getCambiar("\\", "/");
                            uri = entra.getQuitar(entra2.getToken(numToken)) + "";
                            entra3 = new StringTokenV2(entra2.getToken(numToken), ".");
                            ficheroOutOWL = entra3.getToken(1) + "_" + firma.toString() + ".owl";
                            uri = urlPatron + uri + ficheroOutOWL;
                        }
                        entra3 = new StringTokenV2(fichOrigenHTML, ".");
                        ficheroOutOWL = entra3.getToken(1) + "_" + firma.toString() + ".owl";
                        lineaSalida = "<vistasemantica origen=\"" + fichOrigenLN + "\" destino=\"" + uri + "\" />";
                        lengNat = new FileInputStream(fichOrigenLN);
                        lengNat2 = new BufferedInputStream(lengNat);
                        entradaLenguajeNatural = new DataInputStream(lengNat2);
                        salOWL = new FileWriter(ficheroOutOWL);
                        salOWL2 = new BufferedWriter(salOWL);
                        salidaOWL = new PrintWriter(salOWL2);
                        while ((entradaInstancias = entradaLenguajeNatural.readLine()) != null) {
                            sujeto = "";
                            verbo = "";
                            CD = "";
                            CI = "";
                            fraseOrigen = "";
                            if (entradaInstancias.trim().length() > 10) {
                                entrada2 = new StringTokenV2(entradaInstancias.trim(), "\"");
                                if (entrada2.isIncluidaSubcadena("<oracion sujeto=")) {
                                    sujeto = entrada2.getToken(2).trim();
                                    verbo = entrada2.getToken(4).trim();
                                    CD = entrada2.getToken(6).trim();
                                    CI = entrada2.getToken(8).trim();
                                    fraseOrigen = entrada2.getToken(10).trim();
                                    if (sujeto.length() > 0 & verbo.length() > 0 & CD.length() > 0) {
                                        bandera = false;
                                        c = null;
                                        cBak = null;
                                        nombreClase = clasesOntologias.getClaseInstancia(CD);
                                        if (nombreClase.length() > 0) {
                                            bandera = true;
                                        }
                                        if (bandera) {
                                            if (incre == 0) {
                                                salidaOWL.write(" <rdf:RDF        " + "\n");
                                                salidaOWL.write("     xmlns:j.0=\"" + uriOntologia + "\"" + "\n");
                                                salidaOWL.write("     xmlns:protege=\"http://protege.stanford.edu/plugins/owl/protege#\"" + "\n");
                                                salidaOWL.write("     xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"" + "\n");
                                                salidaOWL.write("     xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"" + "\n");
                                                salidaOWL.write("     xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"" + "\n");
                                                salidaOWL.write("     xmlns:owl=\"http://www.w3.org/2002/07/owl#\"    " + "\n");
                                                salidaOWL.write("     xmlns=\"" + uri + "#\"" + "\n");
                                                salidaOWL.write("   xml:base=\"" + uri + "\">" + "\n");
                                                salidaOWL.write("   <owl:Ontology rdf:about=\"\">" + "\n");
                                                salidaOWL.write("     <owl:imports rdf:resource=\"" + uriOntologia + "\"/>" + "\n");
                                                salidaOWL.write("   </owl:Ontology>" + "\n");
                                                salidaOWL.flush();
                                                salida.write(lineaSalida + "\n");
                                                salida.flush();
                                                incre = 1;
                                            }
                                            salidaOWL.write("    <j.0:" + nombreClase + " rdf:ID=\"" + sujeto.toUpperCase() + "\"/>" + "\n");
                                            salidaOWL.write("    <owl:AllDifferent>" + "\n");
                                            salidaOWL.write("      <owl:distinctMembers rdf:parseType=\"Collection\">" + "\n");
                                            salidaOWL.write("        <" + nombreClase + " rdf:about=\"#" + sujeto.toUpperCase() + "\"/>" + "\n");
                                            salidaOWL.write("      </owl:distinctMembers>" + "\n");
                                            salidaOWL.write("    </owl:AllDifferent>" + "\n");
                                            salidaOWL.flush();
                                            bandera = false;
                                        }
                                    }
                                }
                            }
                        }
                        salidaOWL.write(" </rdf:RDF>" + "\n" + "\n");
                        salidaOWL.write("<!-- Creado por [html2ws]  http://www.luis.criado.org -->" + "\n");
                        salidaOWL.flush();
                    }
                }
                mas = 1;
            } else {
                salida.write("</listaVistasSemanticas>\n");
                salida.flush();
                salida.close();
                bw2.close();
                fw2.close();
                salidaOWL.close();
                entradaFichero.close();
                ent2.close();
                ent1.close();
                mas = -1;
            }
        } catch (Exception e) {
            mas = -2;
            salida.write("No se encuentra: " + fichOrigen + "\n");
            salida.flush();
        }
        return mas;
    }
