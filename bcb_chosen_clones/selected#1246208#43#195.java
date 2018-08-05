    public void run(ServletConfig servcon) {
        Log.info("bulding sdf file");
        DBConnection dbconn = null;
        try {
            List l = DBMoleculePeer.executeQuery("select distinct MOLECULE_ID from SPECTRUM where REVIEW_FLAG=\"true\";");
            File outputFile = new File(ServletUtils.expandRelative(servcon, "/nmrshiftdbhtml/Nmrshiftdb.sdf.zip.new"));
            File outputFileCml = new File(ServletUtils.expandRelative(servcon, "/nmrshiftdbhtml/Nmrshiftdb.xml.zip.new"));
            File outputFileWithSignals = new File(ServletUtils.expandRelative(servcon, "/nmrshiftdbhtml/NmrshiftdbWithSignals.sdf.zip.new"));
            File outputFile3d = new File(ServletUtils.expandRelative(servcon, "/nmrshiftdbhtml/Nmrshiftdb3d.sdf.zip.new"));
            File outputFileCml3d = new File(ServletUtils.expandRelative(servcon, "/nmrshiftdbhtml/Nmrshiftdb3d.xml.zip.new"));
            File outputFileWithSignals3d = new File(ServletUtils.expandRelative(servcon, "/nmrshiftdbhtml/NmrshiftdbWithSignals3d.sdf.zip.new"));
            FileOutputStream baos = new FileOutputStream(outputFile);
            ZipOutputStream zipout = new ZipOutputStream(baos);
            zipout.putNextEntry(new ZipEntry("nmrshiftdb.sdf"));
            FileOutputStream baossig = new FileOutputStream(outputFileWithSignals);
            ZipOutputStream zipoutwithsignals = new ZipOutputStream(baossig);
            zipoutwithsignals.putNextEntry(new ZipEntry("nmrshiftdb.sdf"));
            FileOutputStream baoscml = new FileOutputStream(outputFileCml);
            ZipOutputStream zipoutcml = new ZipOutputStream(baoscml);
            FileOutputStream baos3d = new FileOutputStream(outputFile3d);
            ZipOutputStream zipout3d = new ZipOutputStream(baos3d);
            zipout3d.putNextEntry(new ZipEntry("nmrshiftdb3d.sdf"));
            FileOutputStream baossig3d = new FileOutputStream(outputFileWithSignals3d);
            ZipOutputStream zipoutwithsignals3d = new ZipOutputStream(baossig3d);
            zipoutwithsignals3d.putNextEntry(new ZipEntry("nmrshiftdb3d.sdf"));
            FileOutputStream baoscml3d = new FileOutputStream(outputFileCml3d);
            ZipOutputStream zipoutcml3d = new ZipOutputStream(baoscml3d);
            zipoutcml.putNextEntry(new ZipEntry("nmrshiftdb.xml"));
            zipoutcml3d.putNextEntry(new ZipEntry("nmrshiftdb3d.xml"));
            zipoutcml.write(new String("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<cml>").getBytes());
            zipoutcml3d.write(new String("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<cml>").getBytes());
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            Vector conditiontypes = DBConditionTypePeer.doSelect(new Criteria());
            dbconn = TurbineDB.getConnection();
            Statement stmt = dbconn.createStatement();
            for (int i = 0; i < l.size(); i++) {
                try {
                    DBMolecule mol = DBMoleculePeer.retrieveByPK(new NumberKey(((Record) l.get(i)).getValue(1).asString()));
                    zipout.write(mol.getStructureFile(1, true).getBytes());
                    zipout.write(new String("$$$$\r\n").getBytes());
                    IMolecule cdkmol = mol.getAsCDKMolecule(1);
                    HashMap map = new HashMap();
                    Vector spectra = mol.selectSpectra();
                    for (int m = 0; m < conditiontypes.size(); m++) {
                        String ctid = ((DBConditionType) conditiontypes.get(m)).getConditionTypeId().toString();
                        String ctname = ((DBConditionType) conditiontypes.get(m)).getConditionName();
                        String sql = "select VALUE from SPECTRUM join SPECTRUM_CONDITION using (SPECTRUM_ID) join CONDITION using (CONDITION_ID)  where MOLECULE_ID =" + mol.getMoleculeId() + " and CONDITION_TYPE_ID=" + ctid + " order by SPECTRUM.SPECTRUM_TYPE_ID";
                        ResultSet rs = stmt.executeQuery(sql);
                        StringBuffer valuesline = new StringBuffer();
                        int n = 0;
                        while (rs.next()) {
                            valuesline.append(n + ":" + rs.getString(1) + " ");
                            n++;
                        }
                        if (!valuesline.toString().equals("")) map.put(ctname, valuesline.toString());
                    }
                    StringTokenizer st = new StringTokenizer(mol.getCML(1).getChild(0).toXML(), "<");
                    while (st.hasMoreTokens()) zipoutcml.write(new String("<" + st.nextToken()).getBytes());
                    for (int k = 0; k < spectra.size(); k++) {
                        DBSpectrum spectrum = ((DBSpectrum) spectra.get(k));
                        if (spectrum.getMark() > spectrum.getDisplaymark()) {
                            st = new StringTokenizer(spectrum.getCmlSpect().getChild(0).toXML(), "<");
                            while (st.hasMoreTokens()) zipoutcml.write(new String("<" + st.nextToken()).getBytes());
                            map.put("Spectrum " + spectrum.getDBSpectrumType().getName() + " " + k, spectrum.getSpecfile());
                        }
                    }
                    zipoutwithsignals.write(mol.getStructureFile(1, map, true).getBytes());
                    zipoutwithsignals.write(new String("$$$$\r\n").getBytes());
                    if (mol.hasCoordinates(2)) {
                        zipout3d.write(mol.getStructureFile(2, true).getBytes());
                        zipout3d.write(new String("$$$$\r\n").getBytes());
                        map = new HashMap();
                        spectra = mol.selectSpectra();
                        for (int m = 0; m < conditiontypes.size(); m++) {
                            String ctid = ((DBConditionType) conditiontypes.get(m)).getConditionTypeId().toString();
                            String ctname = ((DBConditionType) conditiontypes.get(m)).getConditionName();
                            String sql = "select VALUE from SPECTRUM join SPECTRUM_CONDITION using (SPECTRUM_ID) join CONDITION using (CONDITION_ID)  where MOLECULE_ID =" + mol.getMoleculeId() + " and CONDITION_TYPE_ID=" + ctid + " order by SPECTRUM.SPECTRUM_TYPE_ID";
                            ResultSet rs = stmt.executeQuery(sql);
                            StringBuffer valuesline = new StringBuffer();
                            int n = 0;
                            while (rs.next()) {
                                valuesline.append(n + ":" + rs.getString(1) + " ");
                                n++;
                            }
                            if (!valuesline.toString().equals("")) map.put(ctname, valuesline.toString());
                        }
                        st = new StringTokenizer(mol.getCML(2).getChild(0).toXML(), "<");
                        st.nextToken();
                        while (st.hasMoreTokens()) zipoutcml3d.write(new String("<" + st.nextToken()).getBytes());
                        for (int k = 0; k < spectra.size(); k++) {
                            DBSpectrum spectrum = ((DBSpectrum) spectra.get(k));
                            if (spectrum.getMark() > spectrum.getDisplaymark()) {
                                st = new StringTokenizer(spectrum.getCmlSpect().getChild(0).toXML(), "<");
                                st.nextToken();
                                while (st.hasMoreTokens()) zipoutcml3d.write(new String("<" + st.nextToken()).getBytes());
                                map.put("Spectrum " + spectrum.getDBSpectrumType().getName() + " " + k, spectrum.getSpecfile());
                            }
                        }
                        zipoutwithsignals3d.write(mol.getStructureFile(2, map, true).getBytes());
                        zipoutwithsignals3d.write(new String("$$$$\r\n").getBytes());
                    }
                } catch (Exception ex) {
                    GeneralUtils.logError(ex, "loop in buildsdf", null, false);
                }
            }
            zipoutcml.write(new String("</cml>").getBytes());
            zipout.closeEntry();
            zipout.close();
            zipoutwithsignals.closeEntry();
            zipoutwithsignals.close();
            zipoutcml.closeEntry();
            zipoutcml.close();
            zipoutcml3d.write(new String("</cml>").getBytes());
            zipout3d.closeEntry();
            zipout3d.close();
            zipoutwithsignals3d.closeEntry();
            zipoutwithsignals3d.close();
            zipoutcml3d.closeEntry();
            zipoutcml3d.close();
            File oldFile = new File(ServletUtils.expandRelative(servcon, "/nmrshiftdbhtml/Nmrshiftdb.sdf.zip"));
            File oldFilesig = new File(ServletUtils.expandRelative(servcon, "/nmrshiftdbhtml/NmrshiftdbWithSignals.sdf.zip"));
            File oldFileCml = new File(ServletUtils.expandRelative(servcon, "/nmrshiftdbhtml/Nmrshiftdb.xml.zip"));
            File oldFile3d = new File(ServletUtils.expandRelative(servcon, "/nmrshiftdbhtml/Nmrshiftdb3d.sdf.zip"));
            File oldFilesig3d = new File(ServletUtils.expandRelative(servcon, "/nmrshiftdbhtml/NmrshiftdbWithSignals3d.sdf.zip"));
            File oldFileCml3d = new File(ServletUtils.expandRelative(servcon, "/nmrshiftdbhtml/Nmrshiftdb3d.xml.zip"));
            try {
                oldFile.delete();
                oldFileCml.delete();
                oldFilesig.delete();
                oldFile3d.delete();
                oldFileCml3d.delete();
                oldFilesig3d.delete();
            } catch (Exception ex) {
            }
            outputFile.renameTo(oldFile);
            outputFileCml.renameTo(oldFileCml);
            outputFileWithSignals.renameTo(oldFilesig);
            outputFile3d.renameTo(oldFile3d);
            outputFileCml3d.renameTo(oldFileCml3d);
            outputFileWithSignals3d.renameTo(oldFilesig3d);
        } catch (Exception ex) {
            GeneralUtils.logError(ex, "build sdf", null, false);
        } finally {
            try {
                TurbineDB.releaseConnection(dbconn);
            } catch (Exception ex) {
                Log.error("BuildHome3" + ex.getMessage());
            }
        }
        this.setResult(Daemon.RESULT_PROCESSING);
        this.setResult(Daemon.RESULT_SUCCESS);
    }
