    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = null;
        ServletOutputStream outstream = null;
        try {
            String action = req.getParameter("nmrshiftdbaction");
            String relativepath = ServletUtils.expandRelative(this.getServletConfig(), "/WEB-INF");
            TurbineConfig tc = new TurbineConfig(relativepath + "..", relativepath + getServletConfig().getInitParameter("properties"));
            tc.init();
            int spectrumId = -1;
            DBSpectrum spectrum = null;
            Export export = null;
            String format = req.getParameter("format");
            if (action.equals("test")) {
                try {
                    res.setContentType("text/plain");
                    out = res.getWriter();
                    List l = DBSpectrumPeer.executeQuery("select SPECTRUM_ID from SPECTRUM limit 1");
                    if (l.size() > 0) spectrumId = ((Record) l.get(0)).getValue(1).asInt();
                    out.write("success");
                } catch (Exception ex) {
                    out.write("failure");
                }
            } else if (action.equals("rss")) {
                int numbertoexport = 10;
                out = res.getWriter();
                if (req.getParameter("numbertoexport") != null) {
                    try {
                        numbertoexport = Integer.parseInt(req.getParameter("numbertoexport"));
                        if (numbertoexport < 1 || numbertoexport > 20) throw new NumberFormatException("Number to small/large");
                    } catch (NumberFormatException ex) {
                        out.println("The parameter <code>numbertoexport</code>must be an integer from 1 to 20");
                    }
                }
                res.setContentType("text/xml");
                RssWriter rssWriter = new RssWriter();
                rssWriter.setWriter(res.getWriter());
                AtomContainerSet soac = new AtomContainerSet();
                String query = "select distinct MOLECULE.MOLECULE_ID from MOLECULE, SPECTRUM where SPECTRUM.MOLECULE_ID = MOLECULE.MOLECULE_ID and SPECTRUM.REVIEW_FLAG =\"true\" order by MOLECULE.DATE desc;";
                List l = NmrshiftdbUserPeer.executeQuery(query);
                for (int i = 0; i < numbertoexport; i++) {
                    if (i == l.size()) break;
                    DBMolecule mol = DBMoleculePeer.retrieveByPK(new NumberKey(((Record) l.get(i)).getValue(1).asInt()));
                    IMolecule cdkmol = mol.getAsCDKMoleculeAsEntered(1);
                    soac.addAtomContainer(cdkmol);
                    rssWriter.getLinkmap().put(cdkmol, mol.getEasylink(req));
                    rssWriter.getDatemap().put(cdkmol, mol.getDate());
                    rssWriter.getTitlemap().put(cdkmol, mol.getChemicalNamesAsOneStringWithFallback());
                    rssWriter.getCreatormap().put(cdkmol, mol.getNmrshiftdbUser().getUserName());
                    rssWriter.setCreator(GeneralUtils.getAdminEmail(getServletConfig()));
                    Vector v = mol.getDBCanonicalNames();
                    for (int k = 0; k < v.size(); k++) {
                        DBCanonicalName canonName = (DBCanonicalName) v.get(k);
                        if (canonName.getDBCanonicalNameType().getCanonicalNameType() == "INChI") {
                            rssWriter.getInchimap().put(cdkmol, canonName.getName());
                            break;
                        }
                    }
                    rssWriter.setTitle("NMRShiftDB");
                    rssWriter.setLink("http://www.nmrshiftdb.org");
                    rssWriter.setDescription("NMRShiftDB is an open-source, open-access, open-submission, open-content web database for chemical structures and their nuclear magnetic resonance data");
                    rssWriter.setPublisher("NMRShiftDB.org");
                    rssWriter.setImagelink("http://www.nmrshiftdb.org/images/nmrshift-logo.gif");
                    rssWriter.setAbout("http://www.nmrshiftdb.org/NmrshiftdbServlet?nmrshiftdbaction=rss");
                    Collection coll = new ArrayList();
                    Vector spectra = mol.selectSpectra(null);
                    for (int k = 0; k < spectra.size(); k++) {
                        Element el = ((DBSpectrum) spectra.get(k)).getCmlSpect();
                        Element el2 = el.getChildElements().get(0);
                        el.removeChild(el2);
                        coll.add(el2);
                    }
                    rssWriter.getMultiMap().put(cdkmol, coll);
                }
                rssWriter.write(soac);
            } else if (action.equals("getattachment")) {
                res.setContentType("application/zip");
                outstream = res.getOutputStream();
                DBSample sample = DBSamplePeer.retrieveByPK(new NumberKey(req.getParameter("sampleid")));
                outstream.write(sample.getAttachment());
            } else if (action.equals("createreport")) {
                res.setContentType("application/pdf");
                outstream = res.getOutputStream();
                boolean yearly = req.getParameter("style").equals("yearly");
                int yearstart = Integer.parseInt(req.getParameter("yearstart"));
                int yearend = Integer.parseInt(req.getParameter("yearend"));
                int monthstart = 0;
                int monthend = 0;
                if (!yearly) {
                    monthstart = Integer.parseInt(req.getParameter("monthstart"));
                    monthend = Integer.parseInt(req.getParameter("monthend"));
                }
                int type = Integer.parseInt(req.getParameter("type"));
                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(relativepath + "/reports/" + (yearly ? "yearly" : "monthly") + "_report_" + type + ".jasper");
                Map parameters = new HashMap();
                if (yearly) parameters.put("HEADER", "Report for years " + yearstart + " - " + yearend); else parameters.put("HEADER", "Report for " + monthstart + "/" + yearstart + " - " + monthend + "/" + yearend);
                DBConnection dbconn = TurbineDB.getConnection();
                Connection conn = dbconn.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = null;
                if (type == 1) {
                    rs = stmt.executeQuery("select YEAR(DATE) as YEAR, " + (yearly ? "" : " MONTH(DATE) as MONTH, ") + "AFFILIATION_1, AFFILIATION_2, MACHINE.NAME as NAME, count(*) as C, sum(WISHED_SPECTRUM like '%13C%' or WISHED_SPECTRUM like '%variable temperature%' or WISHED_SPECTRUM like '%ID sel. NOE%' or WISHED_SPECTRUM like '%solvent suppression%' or WISHED_SPECTRUM like '%standard spectrum%') as 1_D, sum(WISHED_SPECTRUM like '%H,H-COSY%' or WISHED_SPECTRUM like '%NOESY%' or WISHED_SPECTRUM like '%HMQC%' or WISHED_SPECTRUM like '%HMBC%') as 2_D, sum(OTHER_WISHED_SPECTRUM!='') as SPECIAL, sum(OTHER_NUCLEI!='') as HETERO, sum(PROCESS='self') as SELF, sum(PROCESS='robot') as ROBOT, sum(PROCESS='worker') as OPERATOR from (SAMPLE join TURBINE_USER using (USER_ID)) join MACHINE on MACHINE.MACHINE_ID=SAMPLE.MACHINE where YEAR(DATE)>=" + yearstart + " and YEAR(DATE)<=" + yearend + " and LOGIN_NAME<>'testuser' group by YEAR, " + (yearly ? "" : "MONTH, ") + "AFFILIATION_1, AFFILIATION_2, MACHINE.NAME");
                } else if (type == 2) {
                    rs = stmt.executeQuery("select YEAR(DATE) as YEAR, " + (yearly ? "" : " MONTH(DATE) as MONTH, ") + "MACHINE.NAME as NAME, count(*) as C, sum(WISHED_SPECTRUM like '%13C%' or WISHED_SPECTRUM like '%variable temperature%' or WISHED_SPECTRUM like '%ID sel. NOE%' or WISHED_SPECTRUM like '%solvent suppression%' or WISHED_SPECTRUM like '%standard spectrum%') as 1_D, sum(WISHED_SPECTRUM like '%H,H-COSY%' or WISHED_SPECTRUM like '%NOESY%' or WISHED_SPECTRUM like '%HMQC%' or WISHED_SPECTRUM like '%HMBC%') as 2_D, sum(OTHER_WISHED_SPECTRUM!='') as SPECIAL, sum(OTHER_NUCLEI!='') as HETERO, sum(PROCESS='self') as SELF, sum(PROCESS='robot') as ROBOT, sum(PROCESS='worker') as OPERATOR from (SAMPLE join TURBINE_USER using (USER_ID)) join MACHINE on MACHINE.MACHINE_ID=SAMPLE.MACHINE group by YEAR, " + (yearly ? "" : "MONTH, ") + "MACHINE.NAME");
                }
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRResultSetDataSource(rs));
                JasperExportManager.exportReportToPdfStream(jasperPrint, outstream);
                dbconn.close();
            } else if (action.equals("exportcmlbyinchi")) {
                res.setContentType("text/xml");
                out = res.getWriter();
                String inchi = req.getParameter("inchi");
                String spectrumtype = req.getParameter("spectrumtype");
                Criteria crit = new Criteria();
                crit.add(DBCanonicalNamePeer.NAME, inchi);
                crit.addJoin(DBCanonicalNamePeer.MOLECULE_ID, DBSpectrumPeer.MOLECULE_ID);
                crit.addJoin(DBSpectrumPeer.SPECTRUM_TYPE_ID, DBSpectrumTypePeer.SPECTRUM_TYPE_ID);
                crit.add(DBSpectrumTypePeer.NAME, spectrumtype);
                try {
                    GeneralUtils.logToSql(crit.toString(), null);
                } catch (Exception ex) {
                }
                Vector spectra = DBSpectrumPeer.doSelect(crit);
                if (spectra.size() == 0) {
                    out.write("No such molecule or spectrum");
                } else {
                    Element cmlElement = new Element("cml");
                    cmlElement.addAttribute(new Attribute("convention", "nmrshiftdb-convention"));
                    cmlElement.setNamespaceURI("http://www.xml-cml.org/schema");
                    Element parent = ((DBSpectrum) spectra.get(0)).getDBMolecule().getCML(1);
                    nu.xom.Node cmldoc = parent.getChild(0);
                    ((Element) cmldoc).setNamespaceURI("http://www.xml-cml.org/schema");
                    parent.removeChildren();
                    cmlElement.appendChild(cmldoc);
                    for (int k = 0; k < spectra.size(); k++) {
                        Element parentspec = ((DBSpectrum) spectra.get(k)).getCmlSpect();
                        Node spectrumel = parentspec.getChild(0);
                        parentspec.removeChildren();
                        cmlElement.appendChild(spectrumel);
                        ((Element) spectrumel).setNamespaceURI("http://www.xml-cml.org/schema");
                    }
                    out.write(cmlElement.toXML());
                }
            } else if (action.equals("namelist")) {
                res.setContentType("application/zip");
                outstream = res.getOutputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zipout = new ZipOutputStream(baos);
                Criteria crit = new Criteria();
                crit.addJoin(DBMoleculePeer.MOLECULE_ID, DBSpectrumPeer.MOLECULE_ID);
                crit.add(DBSpectrumPeer.REVIEW_FLAG, "true");
                Vector v = DBMoleculePeer.doSelect(crit);
                for (int i = 0; i < v.size(); i++) {
                    if (i % 500 == 0) {
                        if (i != 0) {
                            zipout.write(new String("<p>The list is continued <a href=\"nmrshiftdb.names." + i + ".html\">here</a></p></body></html>").getBytes());
                            zipout.closeEntry();
                        }
                        zipout.putNextEntry(new ZipEntry("nmrshiftdb.names." + i + ".html"));
                        zipout.write(new String("<html><body><h1>This is a list of strcutures in <a href=\"http://www.nmrshiftdb.org\">NMRShiftDB</a>, starting at " + i + ", Its main purpose is to be found by search engines</h1>").getBytes());
                    }
                    DBMolecule mol = (DBMolecule) v.get(i);
                    zipout.write(new String("<p><a href=\"" + mol.getEasylink(req) + "\">").getBytes());
                    Vector cannames = mol.getDBCanonicalNames();
                    for (int k = 0; k < cannames.size(); k++) {
                        zipout.write(new String(((DBCanonicalName) cannames.get(k)).getName() + " ").getBytes());
                    }
                    Vector chemnames = mol.getDBChemicalNames();
                    for (int k = 0; k < chemnames.size(); k++) {
                        zipout.write(new String(((DBChemicalName) chemnames.get(k)).getName() + " ").getBytes());
                    }
                    zipout.write(new String("</a>. Information we have got: NMR spectra").getBytes());
                    Vector spectra = mol.selectSpectra();
                    for (int k = 0; k < spectra.size(); k++) {
                        zipout.write(new String(((DBSpectrum) spectra.get(k)).getDBSpectrumType().getName() + ", ").getBytes());
                    }
                    if (mol.hasAny3d()) zipout.write(new String("3D coordinates, ").getBytes());
                    zipout.write(new String("File formats: CML, mol, png, jpeg").getBytes());
                    zipout.write(new String("</p>").getBytes());
                }
                zipout.write(new String("</body></html>").getBytes());
                zipout.closeEntry();
                zipout.close();
                InputStream is = new ByteArrayInputStream(baos.toByteArray());
                byte[] buf = new byte[32 * 1024];
                int nRead = 0;
                while ((nRead = is.read(buf)) != -1) {
                    outstream.write(buf, 0, nRead);
                }
            } else if (action.equals("predictor")) {
                if (req.getParameter("symbol") == null) {
                    res.setContentType("text/plain");
                    out = res.getWriter();
                    out.write("please give the symbol to create the predictor for in the request with symbol=X (e. g. symbol=C");
                }
                res.setContentType("application/zip");
                outstream = res.getOutputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zipout = new ZipOutputStream(baos);
                String filename = "org/openscience/nmrshiftdb/PredictionTool.class";
                zipout.putNextEntry(new ZipEntry(filename));
                JarInputStream jip = new JarInputStream(new FileInputStream(ServletUtils.expandRelative(getServletConfig(), "/WEB-INF/lib/nmrshiftdb-lib.jar")));
                JarEntry entry = jip.getNextJarEntry();
                while (entry.getName().indexOf("PredictionTool.class") == -1) {
                    entry = jip.getNextJarEntry();
                }
                for (int i = 0; i < entry.getSize(); i++) {
                    zipout.write(jip.read());
                }
                zipout.closeEntry();
                zipout.putNextEntry(new ZipEntry("nmrshiftdb.csv"));
                int i = 0;
                org.apache.turbine.util.db.pool.DBConnection conn = TurbineDB.getConnection();
                HashMap mapsmap = new HashMap();
                while (true) {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("select HOSE_CODE, VALUE, SYMBOL from HOSE_CODES where CONDITION_TYPE='m' and WITH_RINGS=0 and SYMBOL='" + req.getParameter("symbol") + "' limit " + (i * 1000) + ", 1000");
                    int m = 0;
                    while (rs.next()) {
                        String code = rs.getString(1);
                        Double value = new Double(rs.getString(2));
                        String symbol = rs.getString(3);
                        if (mapsmap.get(symbol) == null) {
                            mapsmap.put(symbol, new HashMap());
                        }
                        for (int spheres = 6; spheres > 0; spheres--) {
                            StringBuffer hoseCodeBuffer = new StringBuffer();
                            StringTokenizer st = new StringTokenizer(code, "()/");
                            for (int k = 0; k < spheres; k++) {
                                if (st.hasMoreTokens()) {
                                    String partcode = st.nextToken();
                                    hoseCodeBuffer.append(partcode);
                                }
                                if (k == 0) {
                                    hoseCodeBuffer.append("(");
                                } else if (k == 3) {
                                    hoseCodeBuffer.append(")");
                                } else {
                                    hoseCodeBuffer.append("/");
                                }
                            }
                            String hoseCode = hoseCodeBuffer.toString();
                            if (((HashMap) mapsmap.get(symbol)).get(hoseCode) == null) {
                                ((HashMap) mapsmap.get(symbol)).put(hoseCode, new ArrayList());
                            }
                            ((ArrayList) ((HashMap) mapsmap.get(symbol)).get(hoseCode)).add(value);
                        }
                        m++;
                    }
                    i++;
                    stmt.close();
                    if (m == 0) break;
                }
                Set keySet = mapsmap.keySet();
                Iterator it = keySet.iterator();
                while (it.hasNext()) {
                    String symbol = (String) it.next();
                    HashMap hosemap = ((HashMap) mapsmap.get(symbol));
                    Set keySet2 = hosemap.keySet();
                    Iterator it2 = keySet2.iterator();
                    while (it2.hasNext()) {
                        String hoseCode = (String) it2.next();
                        ArrayList list = ((ArrayList) hosemap.get(hoseCode));
                        double[] values = new double[list.size()];
                        for (int k = 0; k < list.size(); k++) {
                            values[k] = ((Double) list.get(k)).doubleValue();
                        }
                        zipout.write(new String(symbol + "|" + hoseCode + "|" + Statistics.minimum(values) + "|" + Statistics.average(values) + "|" + Statistics.maximum(values) + "\r\n").getBytes());
                    }
                }
                zipout.closeEntry();
                zipout.close();
                InputStream is = new ByteArrayInputStream(baos.toByteArray());
                byte[] buf = new byte[32 * 1024];
                int nRead = 0;
                i = 0;
                while ((nRead = is.read(buf)) != -1) {
                    outstream.write(buf, 0, nRead);
                }
            } else if (action.equals("exportspec") || action.equals("exportmol")) {
                if (spectrumId > -1) spectrum = DBSpectrumPeer.retrieveByPK(new NumberKey(spectrumId)); else spectrum = DBSpectrumPeer.retrieveByPK(new NumberKey(req.getParameter("spectrumid")));
                export = new Export(spectrum);
            } else if (action.equals("exportmdl")) {
                res.setContentType("text/plain");
                outstream = res.getOutputStream();
                DBMolecule mol = DBMoleculePeer.retrieveByPK(new NumberKey(req.getParameter("moleculeid")));
                outstream.write(mol.getStructureFile(Integer.parseInt(req.getParameter("coordsetid")), false).getBytes());
            } else if (action.equals("exportlastinputs")) {
                format = action;
            } else if (action.equals("printpredict")) {
                res.setContentType("text/html");
                out = res.getWriter();
                HttpSession session = req.getSession();
                VelocityContext context = PredictPortlet.getContext(session, true, true, new StringBuffer(), getServletConfig(), req, true);
                StringWriter w = new StringWriter();
                Velocity.mergeTemplate("predictprint.vm", "ISO-8859-1", context, w);
                out.println(w.toString());
            } else {
                res.setContentType("text/html");
                out = res.getWriter();
                out.println("No valid action");
            }
            if (format == null) format = "";
            if (format.equals("pdf") || format.equals("rtf")) {
                res.setContentType("application/" + format);
                out = res.getWriter();
            }
            if (format.equals("docbook")) {
                res.setContentType("application/zip");
                outstream = res.getOutputStream();
            }
            if (format.equals("svg")) {
                res.setContentType("image/x-svg");
                out = res.getWriter();
            }
            if (format.equals("tiff")) {
                res.setContentType("image/tiff");
                outstream = res.getOutputStream();
            }
            if (format.equals("jpeg")) {
                res.setContentType("image/jpeg");
                outstream = res.getOutputStream();
            }
            if (format.equals("png")) {
                res.setContentType("image/png");
                outstream = res.getOutputStream();
            }
            if (format.equals("mdl") || format.equals("txt") || format.equals("cml") || format.equals("cmlboth") || format.indexOf("exsection") == 0) {
                res.setContentType("text/plain");
                out = res.getWriter();
            }
            if (format.equals("simplehtml") || format.equals("exportlastinputs")) {
                res.setContentType("text/html");
                out = res.getWriter();
            }
            if (action.equals("exportlastinputs")) {
                int numbertoexport = 4;
                if (req.getParameter("numbertoexport") != null) {
                    try {
                        numbertoexport = Integer.parseInt(req.getParameter("numbertoexport"));
                        if (numbertoexport < 1 || numbertoexport > 20) throw new NumberFormatException("Number to small/large");
                    } catch (NumberFormatException ex) {
                        out.println("The parameter <code>numbertoexport</code>must be an integer from 1 to 20");
                    }
                }
                NmrshiftdbUser user = null;
                try {
                    user = NmrshiftdbUserPeer.getByName(req.getParameter("username"));
                } catch (NmrshiftdbException ex) {
                    out.println("Seems <code>username</code> is not OK: " + ex.getMessage());
                }
                if (user != null) {
                    List l = NmrshiftdbUserPeer.executeQuery("SELECT LAST_DOWNLOAD_DATE FROM TURBINE_USER  where LOGIN_NAME=\"" + user.getUserName() + "\";");
                    Date lastDownloadDate = ((Record) l.get(0)).getValue(1).asDate();
                    if (((new Date().getTime() - lastDownloadDate.getTime()) / 3600000) < 24) {
                        out.println("Your last download was at " + lastDownloadDate + ". You may download your last inputs only once a day. Sorry for this, but we need to be carefull with resources. If you want to put your last inputs on your homepage best use some sort of cache (e. g. use wget for downlaod with crond and link to this static resource))!");
                    } else {
                        NmrshiftdbUserPeer.executeStatement("UPDATE TURBINE_USER SET LAST_DOWNLOAD_DATE=NOW() where LOGIN_NAME=\"" + user.getUserName() + "\";");
                        Vector<String> parameters = new Vector<String>();
                        String query = "select distinct MOLECULE.MOLECULE_ID from MOLECULE, SPECTRUM where SPECTRUM.MOLECULE_ID = MOLECULE.MOLECULE_ID and SPECTRUM.REVIEW_FLAG =\"true\" and SPECTRUM.USER_ID=" + user.getUserId() + " order by MOLECULE.DATE desc;";
                        l = NmrshiftdbUserPeer.executeQuery(query);
                        String url = javax.servlet.http.HttpUtils.getRequestURL(req).toString();
                        url = url.substring(0, url.length() - 17);
                        for (int i = 0; i < numbertoexport; i++) {
                            if (i == l.size()) break;
                            DBMolecule mol = DBMoleculePeer.retrieveByPK(new NumberKey(((Record) l.get(i)).getValue(1).asInt()));
                            parameters.add(new String("<a href=\"" + url + "/portal/pane0/Results?nmrshiftdbaction=showDetailsFromHome&molNumber=" + mol.getMoleculeId() + "\"><img src=\"" + javax.servlet.http.HttpUtils.getRequestURL(req).toString() + "?nmrshiftdbaction=exportmol&spectrumid=" + ((DBSpectrum) mol.getDBSpectrums().get(0)).getSpectrumId() + "&format=jpeg&size=150x150&backcolor=12632256\"></a>"));
                        }
                        VelocityContext context = new VelocityContext();
                        context.put("results", parameters);
                        StringWriter w = new StringWriter();
                        Velocity.mergeTemplate("lateststructures.vm", "ISO-8859-1", context, w);
                        out.println(w.toString());
                    }
                }
            }
            if (action.equals("exportspec")) {
                if (format.equals("txt")) {
                    String lastsearchtype = req.getParameter("lastsearchtype");
                    if (lastsearchtype.equals(NmrshiftdbConstants.TOTALSPECTRUM) || lastsearchtype.equals(NmrshiftdbConstants.SUBSPECTRUM)) {
                        List l = ParseUtils.parseSpectrumFromSpecFile(req.getParameter("lastsearchvalues"));
                        spectrum.initSimilarity(l, lastsearchtype.equals(NmrshiftdbConstants.SUBSPECTRUM));
                    }
                    Vector v = spectrum.getOptions();
                    DBMolecule mol = spectrum.getDBMolecule();
                    out.print(mol.getChemicalNamesAsOneString(false) + mol.getMolecularFormula(false) + "; " + mol.getMolecularWeight() + " Dalton\n\r");
                    out.print("\n\rAtom\t");
                    if (spectrum.getDBSpectrumType().getElementSymbol() == ("H")) out.print("Mult.\t");
                    out.print("Meas.");
                    if (lastsearchtype.equals(NmrshiftdbConstants.TOTALSPECTRUM) || lastsearchtype.equals(NmrshiftdbConstants.SUBSPECTRUM)) {
                        out.print("\tInput\tDiff");
                    }
                    out.print("\n\r");
                    out.print("No.\t");
                    if (spectrum.getDBSpectrumType().getElementSymbol() == ("H")) out.print("\t");
                    out.print("Shift");
                    if (lastsearchtype.equals(NmrshiftdbConstants.TOTALSPECTRUM) || lastsearchtype.equals(NmrshiftdbConstants.SUBSPECTRUM)) {
                        out.print("\tShift\tM-I");
                    }
                    out.print("\n\r");
                    for (int i = 0; i < v.size(); i++) {
                        out.print(((ValuesForVelocityBean) v.get(i)).getDisplayText() + "\t" + ((ValuesForVelocityBean) v.get(i)).getRange());
                        if (lastsearchtype.equals(NmrshiftdbConstants.TOTALSPECTRUM) || lastsearchtype.equals(NmrshiftdbConstants.SUBSPECTRUM)) {
                            out.print("\t" + ((ValuesForVelocityBean) v.get(i)).getNameForElements() + "\t" + ((ValuesForVelocityBean) v.get(i)).getDelta());
                        }
                        out.print("\n\r");
                    }
                }
                if (format.equals("simplehtml")) {
                    String i1 = export.getImage(false, "jpeg", ServletUtils.expandRelative(this.getServletConfig(), "/nmrshiftdbhtml") + "/tmp/" + System.currentTimeMillis(), true);
                    export.pictures[0] = new File(i1).getName();
                    String i2 = export.getImage(true, "jpeg", ServletUtils.expandRelative(this.getServletConfig(), "/nmrshiftdbhtml") + "/tmp/" + System.currentTimeMillis(), true);
                    export.pictures[1] = new File(i2).getName();
                    String docbook = export.getHtml();
                    out.print(docbook);
                }
                if (format.equals("pdf") || format.equals("rtf")) {
                    String svgSpec = export.getSpecSvg(400, 200);
                    String svgspecfile = relativepath + "/tmp/" + System.currentTimeMillis() + "s.svg";
                    new FileOutputStream(svgspecfile).write(svgSpec.getBytes());
                    export.pictures[1] = svgspecfile;
                    String molSvg = export.getMolSvg(true);
                    String svgmolfile = relativepath + "/tmp/" + System.currentTimeMillis() + "m.svg";
                    new FileOutputStream(svgmolfile).write(molSvg.getBytes());
                    export.pictures[0] = svgmolfile;
                    String docbook = export.getDocbook("pdf", "SVG");
                    TransformerFactory tFactory = TransformerFactory.newInstance();
                    Transformer transformer = tFactory.newTransformer(new StreamSource("file:" + GeneralUtils.getNmrshiftdbProperty("docbookxslpath", getServletConfig()) + "/fo/docbook.xsl"));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    transformer.transform(new StreamSource(new StringReader(docbook)), new StreamResult(baos));
                    FopFactory fopFactory = FopFactory.newInstance();
                    FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
                    OutputStream out2 = new ByteArrayOutputStream();
                    Fop fop = fopFactory.newFop(format.equals("rtf") ? MimeConstants.MIME_RTF : MimeConstants.MIME_PDF, foUserAgent, out2);
                    TransformerFactory factory = TransformerFactory.newInstance();
                    transformer = factory.newTransformer();
                    Source src = new StreamSource(new StringReader(baos.toString()));
                    Result res2 = new SAXResult(fop.getDefaultHandler());
                    transformer.transform(src, res2);
                    out.print(out2.toString());
                }
                if (format.equals("docbook")) {
                    String i1 = relativepath + "/tmp/" + System.currentTimeMillis() + ".svg";
                    new FileOutputStream(i1).write(export.getSpecSvg(300, 200).getBytes());
                    export.pictures[0] = new File(i1).getName();
                    String i2 = relativepath + "/tmp/" + System.currentTimeMillis() + ".svg";
                    new FileOutputStream(i2).write(export.getMolSvg(true).getBytes());
                    export.pictures[1] = new File(i2).getName();
                    String docbook = export.getDocbook("pdf", "SVG");
                    String docbookfile = relativepath + "/tmp/" + System.currentTimeMillis() + ".xml";
                    new FileOutputStream(docbookfile).write(docbook.getBytes());
                    ByteArrayOutputStream baos = export.makeZip(new String[] { docbookfile, i1, i2 });
                    outstream.write(baos.toByteArray());
                }
                if (format.equals("svg")) {
                    out.print(export.getSpecSvg(400, 200));
                }
                if (format.equals("tiff") || format.equals("jpeg") || format.equals("png")) {
                    InputStream is = new FileInputStream(export.getImage(false, format, relativepath + "/tmp/" + System.currentTimeMillis(), true));
                    byte[] buf = new byte[32 * 1024];
                    int nRead = 0;
                    while ((nRead = is.read(buf)) != -1) {
                        outstream.write(buf, 0, nRead);
                    }
                }
                if (format.equals("cml")) {
                    out.print(spectrum.getCmlSpect().toXML());
                }
                if (format.equals("cmlboth")) {
                    Element cmlElement = new Element("cml");
                    cmlElement.addAttribute(new Attribute("convention", "nmrshiftdb-convention"));
                    cmlElement.setNamespaceURI("http://www.xml-cml.org/schema");
                    Element parent = spectrum.getDBMolecule().getCML(1, spectrum.getDBSpectrumType().getName().equals("1H"));
                    nu.xom.Node cmldoc = parent.getChild(0);
                    ((Element) cmldoc).setNamespaceURI("http://www.xml-cml.org/schema");
                    parent.removeChildren();
                    cmlElement.appendChild(cmldoc);
                    Element parentspec = spectrum.getCmlSpect();
                    Node spectrumel = parentspec.getChild(0);
                    parentspec.removeChildren();
                    cmlElement.appendChild(spectrumel);
                    ((Element) spectrumel).setNamespaceURI("http://www.xml-cml.org/schema");
                    out.write(cmlElement.toXML());
                }
                if (format.indexOf("exsection") == 0) {
                    StringTokenizer st = new StringTokenizer(format, "-");
                    st.nextToken();
                    String template = st.nextToken();
                    Criteria crit = new Criteria();
                    crit.add(DBSpectrumPeer.USER_ID, spectrum.getUserId());
                    Vector v = spectrum.getDBMolecule().getDBSpectrums(crit);
                    VelocityContext context = new VelocityContext();
                    context.put("spectra", v);
                    context.put("molecule", spectrum.getDBMolecule());
                    StringWriter w = new StringWriter();
                    Velocity.mergeTemplate("exporttemplates/" + template, "ISO-8859-1", context, w);
                    out.write(w.toString());
                }
            }
            if (action.equals("exportmol")) {
                int width = -1;
                int height = -1;
                if (req.getParameter("size") != null) {
                    StringTokenizer st = new StringTokenizer(req.getParameter("size"), "x");
                    width = Integer.parseInt(st.nextToken());
                    height = Integer.parseInt(st.nextToken());
                }
                boolean shownumbers = true;
                if (req.getParameter("shownumbers") != null && req.getParameter("shownumbers").equals("false")) {
                    shownumbers = false;
                }
                if (req.getParameter("backcolor") != null) {
                    export.backColor = new Color(Integer.parseInt(req.getParameter("backcolor")));
                }
                if (req.getParameter("markatom") != null) {
                    export.selected = Integer.parseInt(req.getParameter("markatom")) - 1;
                }
                if (format.equals("svg")) {
                    out.print(export.getMolSvg(true));
                }
                if (format.equals("tiff") || format.equals("jpeg") || format.equals("png")) {
                    InputStream is = new FileInputStream(export.getImage(true, format, relativepath + "/tmp/" + System.currentTimeMillis(), width, height, shownumbers, null));
                    byte[] buf = new byte[32 * 1024];
                    int nRead = 0;
                    while ((nRead = is.read(buf)) != -1) {
                        outstream.write(buf, 0, nRead);
                    }
                }
                if (format.equals("mdl")) {
                    out.println(spectrum.getDBMolecule().getStructureFile(1, false));
                }
                if (format.equals("cml")) {
                    out.println(spectrum.getDBMolecule().getCMLString(1));
                }
            }
            if (out != null) out.flush(); else outstream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            out.print(GeneralUtils.logError(ex, "NmrshiftdbServlet", null, true));
            out.flush();
        }
    }
