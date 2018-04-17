        private Vector convertColDataMYSQL_ORACLE(String olddttype, String newdttype, Vector olddataVec) {
            Vector newdataVec = new Vector();
            Enumeration enm = olddataVec.elements();
            while (enm.hasMoreElements()) {
                olddata = enm.nextElement().toString();
                String newdata = "'" + olddata + "'";
                if ((olddttype.equalsIgnoreCase("Float") || olddttype.equalsIgnoreCase("Integer") || olddttype.equalsIgnoreCase("bigint") || olddttype.equalsIgnoreCase("tinyint") || olddttype.equalsIgnoreCase("Double") || olddttype.equalsIgnoreCase("Varchar")) && newdttype.equalsIgnoreCase("Date")) {
                    newdata = "''";
                }
                if ((olddttype.equalsIgnoreCase("Char") && newdttype.equalsIgnoreCase("Number"))) {
                    newdata = "'0'";
                }
                if (olddttype.equalsIgnoreCase("Char") && (newdttype.equalsIgnoreCase("Timestamp") || newdttype.equalsIgnoreCase("DateTime") || newdttype.equalsIgnoreCase("Date"))) {
                    newdata = "''";
                }
                if ((olddttype.equalsIgnoreCase("DateTime") || olddttype.equalsIgnoreCase("Timestamp") || olddttype.equalsIgnoreCase("Date")) && newdttype.equalsIgnoreCase("Number")) {
                    newdata = "'0'";
                }
                if (olddttype.equalsIgnoreCase("DateTime") && newdttype.equalsIgnoreCase("Timestamp")) {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date d = sf.parse(olddata);
                        sf = new SimpleDateFormat("dd-MM-yyyy");
                        String finalDate = sf.format(d);
                        newdata = "to_date('" + finalDate + "','dd-MM-yyyy')";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if ((olddttype.equalsIgnoreCase("Varchar") && newdttype.equalsIgnoreCase("Timestamp"))) {
                    newdata = "''";
                }
                if ((olddttype.equalsIgnoreCase("Varchar") && newdttype.equalsIgnoreCase("Number"))) {
                    newdata = "'0'";
                }
                if (olddttype.equalsIgnoreCase("Date")) {
                    if (olddata.equals("0")) {
                        newdata = "";
                    } else {
                        if (newdttype.equalsIgnoreCase("date")) {
                            olddata = olddata.replaceAll("'", "''");
                            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date d = sf.parse(olddata);
                                sf = new SimpleDateFormat("dd-MM-yyyy");
                                String finalDate = sf.format(d);
                                if (olddata.contains("0000-00-00")) newdata = "''"; else newdata = "to_date('" + finalDate + "','dd-MM-yyyy')";
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (olddttype.equalsIgnoreCase(newdttype) && (!olddttype.equalsIgnoreCase("Date"))) {
                    newdata = "'" + olddata + "'";
                }
                if (olddttype.equalsIgnoreCase("DateTime") && newdttype.equalsIgnoreCase("Date")) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date d = formatter.parse(olddata);
                        formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String finalDate = formatter.format(d);
                        if (olddata.contains("0000-00-00")) newdata = "''"; else newdata = "to_date('" + finalDate + "','dd-MM-yyyy')";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (olddttype.equalsIgnoreCase("Time") && newdttype.equalsIgnoreCase("Date")) {
                    newdata = "''";
                }
                if (olddttype.equalsIgnoreCase("Year") && newdttype.equalsIgnoreCase("Date")) {
                    newdata = "''";
                }
                newdataVec.addElement(newdata);
            }
            return newdataVec;
        }
