        private Vector convertColDataMSSQL_MSSQL(String olddttype, String newdttype, Vector olddataVec) {
            Vector newdataVec = new Vector();
            Enumeration enm = olddataVec.elements();
            while (enm.hasMoreElements()) {
                olddata = enm.nextElement().toString();
                String newdata = "'" + olddata + "'";
                if (olddttype.equals("nvarchar") || olddttype.equals("varchar")) {
                    if (olddata.contains("'")) {
                        olddata = olddata.replaceAll("'", "''");
                    }
                }
                if (olddttype.equalsIgnoreCase("Date")) {
                    if (olddata.equals("0")) {
                        newdata = "";
                    } else {
                        if (newdttype.equalsIgnoreCase("date")) {
                            olddata = olddata.replaceAll("'", "''");
                            if (desselcdriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver")) {
                                newdata = "to_date('" + olddata + "','dd-MM-yyyy')";
                            }
                        } else {
                            olddata = olddata.replaceAll("'", "''");
                            newdata = "'" + getDateFormat("yyyy-MM-dd", "dd-MMM-yy", olddata) + "'";
                        }
                    }
                }
                if (olddttype.equalsIgnoreCase(newdttype) && (!olddttype.equalsIgnoreCase("Date"))) {
                    newdata = "'" + olddata + "'";
                }
                if ((olddttype.equalsIgnoreCase("nvarchar") && newdttype.equalsIgnoreCase("numeric")) || (olddttype.equalsIgnoreCase("nvarchar") && newdttype.equalsIgnoreCase("int")) || (olddttype.equalsIgnoreCase("nvarchar") && newdttype.equalsIgnoreCase("bigint"))) {
                    newdata = "";
                    for (int i = 0; i < olddata.length(); i++) {
                        int j = (int) olddata.charAt(i);
                        if (j >= 48 && j <= 57) {
                            newdata += olddata.charAt(i);
                        }
                    }
                    if (newdata.equalsIgnoreCase("") || newdata.equalsIgnoreCase(" ")) newdata = "0";
                    newdata = "'" + newdata + "'";
                }
                if (olddttype.equalsIgnoreCase("nvarchar") && newdttype.equalsIgnoreCase("varchar")) {
                    newdata = "'" + olddata + "'";
                }
                if (olddttype.equalsIgnoreCase("int") && newdttype.equalsIgnoreCase("numeric")) {
                    newdata = "'" + olddata + "'";
                }
                if (olddttype.equalsIgnoreCase("float") && newdttype.equalsIgnoreCase("numeric")) {
                    newdata = "'" + olddata + "'";
                }
                if (olddttype.equalsIgnoreCase("numeric") && newdttype.equalsIgnoreCase("int")) {
                    if (olddata.equalsIgnoreCase("") || olddata.equalsIgnoreCase(" ") || olddata.equalsIgnoreCase("null")) newdata = "'0'"; else newdata = String.valueOf(Integer.parseInt(olddata));
                }
                if (olddttype.equalsIgnoreCase("bigint") && newdttype.equalsIgnoreCase("numeric")) {
                    newdata = "'" + olddata + "'";
                }
                if (olddttype.equalsIgnoreCase("nvarchar") && newdttype.equalsIgnoreCase("datetime")) {
                    if (olddata.equals("null") || olddata.equals("") || olddata.equals(" ")) {
                        newdata = "''";
                    } else {
                        ParsePosition p1 = new ParsePosition(0);
                        SimpleDateFormat dateFormat = null;
                        java.util.Date d1 = null;
                        SimpleDateFormat df = null;
                        try {
                            df = new SimpleDateFormat("dd.MM.yy");
                        } catch (Exception e) {
                            df = new SimpleDateFormat("MMM dd yyyy");
                        }
                        try {
                            d1 = df.parse(olddata);
                        } catch (Exception e) {
                            return null;
                        }
                        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        newdata = dateFormat.format(d1);
                        newdata = "'" + olddata + "'";
                    }
                }
                if ((olddttype.equalsIgnoreCase("datetime")) || (olddttype.equalsIgnoreCase("smalldatetime") && newdttype.equalsIgnoreCase("datetime"))) {
                    if (olddata.equals("null") || olddata.equals("") || olddata.equals(" ")) {
                        newdata = "''";
                    } else newdata = "'" + olddata + "'";
                }
                if ((olddttype.equalsIgnoreCase("real") && newdttype.equalsIgnoreCase("numeric")) || (olddttype.equalsIgnoreCase("float") && newdttype.equalsIgnoreCase("numeric"))) if (olddata.contains("E")) {
                    newdata = "ABS(" + olddata + ")";
                } else newdata = "'" + olddata + "'";
                if (newdttype.equalsIgnoreCase("int") || newdttype.equalsIgnoreCase("numeric") || newdttype.equalsIgnoreCase("real") || newdttype.equalsIgnoreCase("bigint") || newdttype.equalsIgnoreCase("float")) if (olddata.equalsIgnoreCase("") || olddata.equalsIgnoreCase(" ") || olddata.equalsIgnoreCase("null")) newdata = "'0'";
                newdataVec.addElement(newdata);
            }
            return newdataVec;
        }
