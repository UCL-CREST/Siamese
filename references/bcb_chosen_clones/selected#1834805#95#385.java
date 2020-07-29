    public void checkFilesAndCopyValid(String filename) {
        downloadResults();
        loadResults();
        File tmpFolderF = new File(tmpFolder);
        deleteFileFromTMPFolder(tmpFolderF);
        ZipReader zr = new ZipReader();
        zr.UnzipFile(filename);
        try {
            LogManager.getInstance().log("Ov��uji odevzdan� soubory a kop�ruji validovan�:");
            LogManager.getInstance().log("");
            JAXBElement<?> element = ElementJAXB.getJAXBElement();
            Ppa1VysledkyCviceniType pvct = (Ppa1VysledkyCviceniType) element.getValue();
            File zipFolder = new File(tmpFolder).listFiles()[0].listFiles()[0].listFiles()[0];
            File[] zipFolderList = zipFolder.listFiles();
            for (File studentDirectory : zipFolderList) {
                if (studentDirectory.isDirectory()) {
                    String osobniCisloZeSlozky = studentDirectory.getName().split("-")[0];
                    LogManager.getInstance().changeLog("Prov��ov�n� soubor� studenta s ��slem: " + osobniCisloZeSlozky);
                    List<StudentType> students = (List<StudentType>) pvct.getStudent();
                    for (StudentType student : students) {
                        if (student.getOsobniCislo().equals(osobniCisloZeSlozky)) {
                            int pzp = student.getDomaciUlohy().getPosledniZpracovanyPokus().getCislo().intValue();
                            DomaciUlohyType dut = student.getDomaciUlohy();
                            ChybneOdevzdaneType chot = dut.getChybneOdevzdane();
                            ObjectFactory of = new ObjectFactory();
                            File[] pokusyDirectories = studentDirectory.listFiles();
                            NodeList souboryNL = result.getElementsByTagName("soubor");
                            int start = souboryNL.getLength() - 1;
                            boolean samostatnaPrace = false;
                            for (int i = (pokusyDirectories.length - 1); i >= 0; i--) {
                                if ((pokusyDirectories[i].isDirectory()) && (pzp < Integer.parseInt(pokusyDirectories[i].getName().split("_")[1].trim()))) {
                                    File testedFile = pokusyDirectories[i].listFiles()[0];
                                    if ((testedFile.exists()) && (testedFile.isFile())) {
                                        String[] partsOfFilename = testedFile.getName().split("_");
                                        String osobniCisloZeSouboru = "", priponaSouboru = "";
                                        String[] posledniCastSouboru = null;
                                        if (partsOfFilename.length == 4) {
                                            posledniCastSouboru = partsOfFilename[3].split("[.]");
                                            osobniCisloZeSouboru = posledniCastSouboru[0];
                                            if (posledniCastSouboru.length <= 1) priponaSouboru = ""; else priponaSouboru = posledniCastSouboru[1];
                                        }
                                        String samostatnaPraceNazev = Konfigurace.getInstance().getSamostatnaPraceNazev();
                                        List<SouborType> lst = chot.getSoubor();
                                        if (testedFile.getName().startsWith(samostatnaPraceNazev)) {
                                            samostatnaPrace = true;
                                        } else {
                                            samostatnaPrace = false;
                                            if (partsOfFilename.length != 4) {
                                                SouborType st = new SouborType();
                                                st.setJmeno(testedFile.getName());
                                                st.setDuvod("�patn� struktura jm�na souboru.");
                                                lst.add(st);
                                                continue;
                                            } else if (!testedFile.getName().startsWith("Ppa1_cv")) {
                                                SouborType st = new SouborType();
                                                st.setJmeno(testedFile.getName());
                                                st.setDuvod("�patn� za��tek jm�na souboru.");
                                                lst.add(st);
                                                continue;
                                            } else if (!priponaSouboru.equals("java")) {
                                                SouborType st = new SouborType();
                                                st.setJmeno(testedFile.getName());
                                                st.setDuvod("�patn� p��pona souboru.");
                                                lst.add(st);
                                                continue;
                                            } else if (!osobniCisloZeSouboru.equals(osobniCisloZeSlozky)) {
                                                SouborType st = new SouborType();
                                                st.setJmeno(testedFile.getName());
                                                st.setDuvod("Nesouhlas� osobn� ��sla.");
                                                lst.add(st);
                                                continue;
                                            } else if (partsOfFilename[3].split("[.]").length > 2) {
                                                SouborType st = new SouborType();
                                                st.setJmeno(testedFile.getName());
                                                st.setDuvod("V�ce p��pon souboru.");
                                                lst.add(st);
                                                continue;
                                            } else {
                                                long cisloCviceni, cisloUlohy;
                                                try {
                                                    if (partsOfFilename[1].length() == 4) {
                                                        String cisloS = partsOfFilename[1].substring(2);
                                                        long cisloL = Long.parseLong(cisloS);
                                                        cisloCviceni = cisloL;
                                                    } else {
                                                        throw new NumberFormatException();
                                                    }
                                                } catch (NumberFormatException e) {
                                                    SouborType st = new SouborType();
                                                    st.setJmeno(testedFile.getName());
                                                    st.setDuvod("Chyb� (nebo je chybn�) ��slo cvi�en�");
                                                    lst.add(st);
                                                    continue;
                                                }
                                                try {
                                                    if (partsOfFilename[2].length() > 0) {
                                                        String cisloS = partsOfFilename[2];
                                                        long cisloL = Long.parseLong(cisloS);
                                                        cisloUlohy = cisloL;
                                                    } else {
                                                        throw new NumberFormatException();
                                                    }
                                                } catch (NumberFormatException e) {
                                                    SouborType st = new SouborType();
                                                    st.setJmeno(testedFile.getName());
                                                    st.setDuvod("Chyb� (nebo je chybn�) ��slo �lohy");
                                                    lst.add(st);
                                                    continue;
                                                }
                                                CislaUloh ci = new CislaUloh();
                                                List<long[]> cviceni = ci.getSeznamCviceni();
                                                boolean nalezenoCv = false, nalezenaUl = false;
                                                for (long[] c : cviceni) {
                                                    if (c[0] == cisloCviceni) {
                                                        for (int j = 1; j < c.length; j++) {
                                                            if (c[j] == cisloUlohy) {
                                                                nalezenaUl = true;
                                                                break;
                                                            }
                                                        }
                                                        nalezenoCv = true;
                                                        break;
                                                    }
                                                }
                                                if (!nalezenoCv) {
                                                    SouborType st = new SouborType();
                                                    st.setJmeno(testedFile.getName());
                                                    st.setDuvod("Neplatn� ��slo cvi�en�");
                                                    lst.add(st);
                                                    continue;
                                                }
                                                if (!nalezenaUl) {
                                                    SouborType st = new SouborType();
                                                    st.setJmeno(testedFile.getName());
                                                    st.setDuvod("Neplatn� ��slo �lohy");
                                                    lst.add(st);
                                                    continue;
                                                }
                                            }
                                        }
                                    }
                                    Calendar dateFromZipFile = null;
                                    File zipFile = new File(filename);
                                    if (zipFile.exists()) {
                                        String[] s = zipFile.getName().split("_");
                                        if (s.length >= 3) {
                                            String[] date = s[1].split("-"), time = s[2].split("-");
                                            dateFromZipFile = new GregorianCalendar();
                                            dateFromZipFile.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]), Integer.parseInt(time[0]), Integer.parseInt(time[1]), 0);
                                        }
                                    }
                                    boolean shodaJmenaSouboru = false;
                                    String vysledekValidaceSouboru = "";
                                    for (int j = start; j >= 0; j--) {
                                        NodeList vlastnostiSouboruNL = souboryNL.item(j).getChildNodes();
                                        for (int k = 0; k < vlastnostiSouboruNL.getLength(); k++) {
                                            if (vlastnostiSouboruNL.item(k).getNodeName().equals("cas")) {
                                                String[] obsahElementuCas = vlastnostiSouboruNL.item(k).getTextContent().split(" ");
                                                String[] datumZElementu = obsahElementuCas[0].split("-"), casZElementu = obsahElementuCas[1].split("-");
                                                Calendar datumACasZElementu = new GregorianCalendar();
                                                datumACasZElementu.set(Integer.parseInt(datumZElementu[0]), Integer.parseInt(datumZElementu[1]) - 1, Integer.parseInt(datumZElementu[2]), Integer.parseInt(casZElementu[0]), Integer.parseInt(casZElementu[1]), Integer.parseInt(casZElementu[2]));
                                                if ((dateFromZipFile != null) && (datumACasZElementu.compareTo(dateFromZipFile) > 0)) {
                                                    shodaJmenaSouboru = false;
                                                    break;
                                                }
                                            }
                                            if (vlastnostiSouboruNL.item(k).getNodeName().equals("nazev")) {
                                                shodaJmenaSouboru = vlastnostiSouboruNL.item(k).getTextContent().equals(testedFile.getName());
                                            }
                                            if (vlastnostiSouboruNL.item(k).getNodeName().equals("vysledek")) {
                                                vysledekValidaceSouboru = vlastnostiSouboruNL.item(k).getTextContent();
                                            }
                                        }
                                        if (shodaJmenaSouboru) {
                                            start = --j;
                                            break;
                                        }
                                    }
                                    if (shodaJmenaSouboru && !samostatnaPrace) {
                                        boolean odevzdanoVcas = false;
                                        String cisloCviceniS = testedFile.getName().split("_")[1].substring(2);
                                        int cisloCviceniI = Integer.parseInt(cisloCviceniS);
                                        String cisloUlohyS = testedFile.getName().split("_")[2];
                                        int cisloUlohyI = Integer.parseInt(cisloUlohyS);
                                        List<CviceniType> lct = student.getDomaciUlohy().getCviceni();
                                        for (CviceniType ct : lct) {
                                            if (ct.getCislo().intValue() == cisloCviceniI) {
                                                MezniTerminOdevzdaniVcasType mtovt = ct.getMezniTerminOdevzdaniVcas();
                                                Calendar mtovGC = new GregorianCalendar();
                                                mtovGC.set(mtovt.getDatum().getYear(), mtovt.getDatum().getMonth() - 1, mtovt.getDatum().getDay(), 23, 59, 59);
                                                Calendar fileTimeStamp = new GregorianCalendar();
                                                fileTimeStamp.setTimeInMillis(testedFile.lastModified());
                                                String[] datumSouboru = String.format("%tF", fileTimeStamp).split("-");
                                                String[] casSouboru = String.format("%tT", fileTimeStamp).split(":");
                                                XMLGregorianCalendar xmlGCDate = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(Integer.parseInt(datumSouboru[0]), Integer.parseInt(datumSouboru[1]), Integer.parseInt(datumSouboru[2]), DatatypeConstants.FIELD_UNDEFINED);
                                                XMLGregorianCalendar xmlGCTime = DatatypeFactory.newInstance().newXMLGregorianCalendarTime(Integer.parseInt(casSouboru[0]), Integer.parseInt(casSouboru[1]), Integer.parseInt(casSouboru[2]), DatatypeConstants.FIELD_UNDEFINED);
                                                if (fileTimeStamp.compareTo(mtovGC) <= 0) odevzdanoVcas = true; else odevzdanoVcas = false;
                                                List<UlohaType> lut = ct.getUloha();
                                                for (UlohaType ut : lut) {
                                                    if (ut.getCislo().intValue() == cisloUlohyI) {
                                                        List<OdevzdanoType> lot = ut.getOdevzdano();
                                                        OdevzdanoType ot = of.createOdevzdanoType();
                                                        ot.setDatum(xmlGCDate);
                                                        ot.setCas(xmlGCTime);
                                                        OdevzdanoVcasType ovt = of.createOdevzdanoVcasType();
                                                        ovt.setVysledek(odevzdanoVcas);
                                                        ValidatorType vt = of.createValidatorType();
                                                        vt.setVysledek(vysledekValidaceSouboru.equals("true"));
                                                        ot.setOdevzdanoVcas(ovt);
                                                        ot.setValidator(vt);
                                                        lot.add(ot);
                                                        if (vt.isVysledek()) {
                                                            String test = String.format("%s%s%02d", validovane, File.separator, ct.getCislo().intValue());
                                                            if (!(new File(test).exists())) {
                                                                LogManager.getInstance().log("Nebyla provedena p��prava soubor�. Chyb� slo�ka " + test.substring(Ppa1Cviceni.USER_DIR.length()) + ".");
                                                                return;
                                                            } else {
                                                                copyValidFile(testedFile, ct.getCislo().intValue());
                                                            }
                                                        }
                                                        break;
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                    } else if (shodaJmenaSouboru && samostatnaPrace) {
                                        String[] partsOfFilename = testedFile.getName().split("_");
                                        String[] partsOfLastPartOfFilename = partsOfFilename[partsOfFilename.length - 1].split("[.]");
                                        String osobniCisloZeSouboru = partsOfLastPartOfFilename[0];
                                        String priponaZeSouboru = partsOfLastPartOfFilename[partsOfLastPartOfFilename.length - 1];
                                        if ((partsOfLastPartOfFilename.length == 2) && (priponaZeSouboru.equals("java"))) {
                                            if (student.getOsobniCislo().equals(osobniCisloZeSouboru)) {
                                                Calendar fileTimeStamp = new GregorianCalendar();
                                                fileTimeStamp.setTimeInMillis(testedFile.lastModified());
                                                String[] datumSouboru = String.format("%tF", fileTimeStamp).split("-");
                                                String[] casSouboru = String.format("%tT", fileTimeStamp).split(":");
                                                XMLGregorianCalendar xmlGCDate = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(Integer.parseInt(datumSouboru[0]), Integer.parseInt(datumSouboru[1]), Integer.parseInt(datumSouboru[2]), DatatypeConstants.FIELD_UNDEFINED);
                                                XMLGregorianCalendar xmlGCTime = DatatypeFactory.newInstance().newXMLGregorianCalendarTime(Integer.parseInt(casSouboru[0]), Integer.parseInt(casSouboru[1]), Integer.parseInt(casSouboru[2]), DatatypeConstants.FIELD_UNDEFINED);
                                                List<UlozenoType> lut = student.getSamostatnaPrace().getUlozeno();
                                                if (lut.isEmpty()) {
                                                    File samostatnaPraceNewFile = new File(sp + File.separator + testedFile.getName());
                                                    if (samostatnaPraceNewFile.exists()) {
                                                        samostatnaPraceNewFile.delete();
                                                        samostatnaPraceNewFile.createNewFile();
                                                    }
                                                    String EOL = "" + (char) 0x0D + (char) 0x0A;
                                                    FileReader fr = new FileReader(testedFile);
                                                    BufferedReader br = new BufferedReader(fr);
                                                    FileWriter fw = new FileWriter(samostatnaPraceNewFile);
                                                    String line;
                                                    while ((line = br.readLine()) != null) fw.write(line + EOL);
                                                    br.close();
                                                    fw.close();
                                                    samostatnaPraceNewFile.setLastModified(testedFile.lastModified());
                                                }
                                                UlozenoType ut = of.createUlozenoType();
                                                ut.setDatum(xmlGCDate);
                                                ut.setCas(xmlGCTime);
                                                lut.add(0, ut);
                                            }
                                        }
                                    }
                                }
                            }
                            PosledniZpracovanyPokusType pzpt = new PosledniZpracovanyPokusType();
                            String[] slozkaPoslednihoPokusu = pokusyDirectories[pokusyDirectories.length - 1].getName().split("_");
                            int cisloPokusu = Integer.parseInt(slozkaPoslednihoPokusu[slozkaPoslednihoPokusu.length - 1].trim());
                            pzpt.setCislo(new BigInteger(String.valueOf(cisloPokusu)));
                            student.getDomaciUlohy().setPosledniZpracovanyPokus(pzpt);
                            break;
                        }
                    }
                }
            }
            ElementJAXB.setJAXBElement(element);
            LogManager.getInstance().log("Ov��ov�n� a kop�rov�n� odevzdan�ch soubor� dokon�eno.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogManager.getInstance().log("Nastala chyba p�i ov��ov�n� a kop�rov�n�.");
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
            LogManager.getInstance().log("Nastala chyba p�i ov��ov�n� a kop�rov�n�.");
        } catch (IOException e) {
            e.printStackTrace();
            LogManager.getInstance().log("Nastala chyba p�i ov��ov�n� a kop�rov�n�.");
        }
        LogManager.getInstance().log("Maz�n� rozbalen�ch soubor� ...");
        deleteFileFromTMPFolder(tmpFolderF);
        LogManager.getInstance().changeLog("Maz�n� rozbalen�ch soubor� ... OK");
    }
