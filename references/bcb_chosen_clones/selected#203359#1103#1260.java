    public void setOldData(String strn, String paCat, String phypreform, String libname, String wef1) {
        refreshData();
        cbPatronCategory.setSelectedItem(paCat);
        cbLibrary.setSelectedItem(libname);
        cbPhyform.setSelectedItem(phypreform);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dt = sdf.parse(wef1);
            SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
            String st = format.format(dt);
            wef.setDate(st);
            JSONObject jo = new JSONObject(strn);
            jo.put("OperationId", "25");
            String req = jo.toString();
            System.out.println(req);
            String res = ServletConnector.getInstance().sendJSONRequest("JSONServlet", req);
            System.out.println(res);
            JSONObject jon1 = new JSONObject(res);
            String opac = jon1.getString("renewal_through_opac");
            String lonlt = jon1.getString("loan_limit");
            String renlt = jon1.getString("renewal_limit");
            String max_cel = jon1.getString("max_ceil_on_fine");
            String otrdtl = jon1.getString("other_details");
            int overLoanLimit = Integer.parseInt(jon1.getString("OverallLoanLimit"));
            String mulCop = jon1.getString("AllowMultipleCopies");
            String selectData = "";
            if (opac.equals("Y")) {
                cbRenewal.setSelected(true);
            } else if (opac.equals("N")) {
                cbRenewal.setSelected(false);
            }
            if (mulCop.equals("Y")) {
                cbAllowMultipleCopies.setSelected(true);
            } else if (mulCop.equals("N")) {
                cbAllowMultipleCopies.setSelected(false);
            }
            int on = 0;
            if (lonlt != null && !lonlt.equals("")) {
                on = Integer.parseInt(lonlt);
            }
            int rn = 0;
            if (renlt != null && !renlt.equals("")) {
                rn = Integer.parseInt(renlt);
            }
            double ce = 0.0;
            if (max_cel != null && !max_cel.equals("")) {
                Double.parseDouble(max_cel);
            }
            jFormattedTextField3.setValue(on);
            jFormattedTextField4.setValue(rn);
            jFormattedTextField5.setValue(ce);
            try {
                SAXBuilder sb = new SAXBuilder();
                Document doc = sb.build(new java.io.StringReader(otrdtl));
                String ovrlonlt = " ";
                String lonpty = " ";
                String durton = " ";
                String ocren = " ";
                String day = " ";
                String mont = " ";
                String lpinclude = " ";
                String odinclude = "";
                String oduron = "";
                if (doc != null) {
                    ovrlonlt = doc.getRootElement().getChildText("OverallLoanLimit");
                    int ol = 0;
                    try {
                        ol = Integer.parseInt(ovrlonlt);
                    } catch (Exception ex) {
                    }
                    jsGlobalLoanlt.setValue(overLoanLimit);
                    int dutn = 0;
                    try {
                        durton = doc.getRootElement().getChild("LoanPeriod").getChildText("Duration");
                        dutn = Integer.parseInt(durton);
                    } catch (Exception ex) {
                    }
                    lonpty = doc.getRootElement().getChild("LoanPeriod").getChildText("DurationType");
                    if (lonpty.trim().toUpperCase().equals("DAY")) {
                        rbIndays.setSelected(true);
                        ((java.awt.CardLayout) this.jPanel8.getLayout()).show(jPanel8, "card2");
                        jFormattedTextField1.setValue(dutn);
                    } else if (lonpty.trim().toUpperCase().equals("HOUR")) {
                        rbInhours.setSelected(true);
                        ((java.awt.CardLayout) this.jPanel8.getLayout()).show(jPanel8, "card3");
                        jFormattedTextField2.setValue(dutn);
                    } else if (lonpty.trim().toUpperCase().equals("NEXTOCCURRING")) {
                        rbNextOccuring.setSelected(true);
                        ((java.awt.CardLayout) this.jPanel8.getLayout()).show(jPanel8, "card4");
                    }
                }
                if (rbNextOccuring.isSelected()) {
                    List ls = doc.getRootElement().getChild("LoanPeriod").getChild("Occurrances").getChildren("Occurrance");
                    if (ls.size() > 0) {
                        for (int i = 0; i < ls.size(); i++) {
                            Object[][] row = new Object[1][1];
                            day = ((org.jdom.Element) ls.get(i)).getChildText("day");
                            mont = ((org.jdom.Element) ls.get(i)).getChildText("month");
                            int day1 = Integer.parseInt(day);
                            int mont1 = Integer.parseInt(mont);
                            modelAdvance.addRow(row);
                            if (modelAdvance.getRowCount() == 1) {
                                modelAdvance.setValueAt(day1, modelAdvance.getRowCount() - 1, 0);
                                modelAdvance.setValueAt(mont1, modelAdvance.getRowCount() - 1, 1);
                            } else {
                                modelAdvance.setValueAt(day1, modelAdvance.getRowCount() - 1, 0);
                                modelAdvance.setValueAt(mont1, modelAdvance.getRowCount() - 1, 1);
                            }
                        }
                    }
                }
                oduron = doc.getRootElement().getChild("OverDue").getChildText("DurationType");
                lpinclude = doc.getRootElement().getChild("LoanPeriod").getChildText("Holidays");
                odinclude = doc.getRootElement().getChild("OverDue").getChildText("Holidays");
                if (lpinclude.toUpperCase().equals("INCLUDE")) {
                    jRadioButton1.setSelected(true);
                } else if (lpinclude.toUpperCase().equals("EXCLUDE")) {
                    jRadioButton2.setSelected(true);
                }
                if (oduron.trim().toUpperCase().equals("DAYS")) {
                    jRadioButton3.setSelected(true);
                } else if (oduron.trim().toUpperCase().equals("HOURS")) {
                    jRadioButton4.setSelected(true);
                }
                if (odinclude.toUpperCase().equals("INCLUDE")) {
                    jRadioButton6.setSelected(true);
                } else if (odinclude.toUpperCase().equals("EXCLUDE")) {
                    jRadioButton5.setSelected(true);
                }
                try {
                    List lst = doc.getRootElement().getChild("OverDue").getChild("Charges").getChildren("Charge");
                    if (lst.size() > 0) {
                        for (int i = 0; i < lst.size(); i++) {
                            String frm = ((org.jdom.Element) lst.get(i)).getChildText("From");
                            String to = ((org.jdom.Element) lst.get(i)).getChildText("To");
                            String amont = ((org.jdom.Element) lst.get(i)).getChildText("Amount");
                            Object[][] row = new Object[1][1];
                            jtab2.addRow(row);
                            if (jtab2.getRowCount() == 1) {
                                jtab2.setValueAt(frm, jtab2.getRowCount() - 1, 0);
                                jtab2.setValueAt(to, jtab2.getRowCount() - 1, 1);
                                jtab2.setValueAt(amont, jtab2.getRowCount() - 1, 2);
                            } else {
                                jtab2.setValueAt(frm, jtab2.getRowCount() - 1, 0);
                                jtab2.setValueAt(to, jtab2.getRowCount() - 1, 1);
                                jtab2.setValueAt(amont, jtab2.getRowCount() - 1, 2);
                            }
                        }
                    }
                } catch (Exception ex) {
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
