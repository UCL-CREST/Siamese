        private Vector convertColDataOracle_Oracle(String olddttype, String newdttype, Vector olddataVec) {
            Vector newdataVec = new Vector();
            Enumeration enm = olddataVec.elements();
            while (enm.hasMoreElements()) {
                olddata = enm.nextElement().toString();
                String newdata = "'" + olddata + "'";
                if ((olddttype.equalsIgnoreCase("Varchar") && newdttype.equalsIgnoreCase("Number"))) {
                    newdata = "0";
                }
                if (olddttype.equalsIgnoreCase(newdttype)) {
                    System.out.println("varchar in oracle");
                    newdata = "'" + olddata + "'";
                }
                if ((olddttype.equalsIgnoreCase("Char") && newdttype.equalsIgnoreCase("Number"))) {
                    newdata = "0";
                }
                if ((olddttype.equalsIgnoreCase("Varchar")) && newdttype.equalsIgnoreCase("Date")) {
                    newdata = "''";
                }
                if (olddttype.equalsIgnoreCase("Char") && (newdttype.equalsIgnoreCase("Timestamp") || newdttype.equalsIgnoreCase("Date"))) {
                    newdata = "''";
                }
                if ((olddttype.equalsIgnoreCase("Timestamp") || olddttype.equalsIgnoreCase("Date")) && newdttype.equalsIgnoreCase("Number")) {
                    newdata = "0";
                }
                if ((olddttype.equalsIgnoreCase("Varchar") && newdttype.equalsIgnoreCase("Timestamp"))) {
                    newdata = "''";
                }
                if (olddttype.equalsIgnoreCase("Timestamp") && newdttype.equalsIgnoreCase("Date")) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        Date d = formatter.parse(olddata);
                        formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String finalDate = formatter.format(d);
                        if (olddata.contains("0000-00-00")) newdata = "''"; else newdata = "to_date('" + finalDate + "','dd-MM-yyyy')";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (olddttype.equalsIgnoreCase("Date") && newdttype.equalsIgnoreCase("Timestamp")) {
                    if (olddata.contains("0000-00-00") || olddata.equals("null") || olddata.equals("")) newdata = "''"; else {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            Date d = formatter.parse(olddata);
                            formatter = new SimpleDateFormat("dd-MM-yyyy");
                            String finalDate = formatter.format(d);
                            newdata = "to_date('" + finalDate + "','dd-MM-yyyy')";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (olddttype.equalsIgnoreCase("Timestamp") && newdttype.equalsIgnoreCase("Timestamp")) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    if (olddata.contains("0000-00-00") || olddata.equals("null") || olddata.equals("")) newdata = "''"; else {
                        try {
                            Date d = formatter.parse(olddata);
                            formatter = new SimpleDateFormat("dd-MM-yyyy");
                            String finalDate = formatter.format(d);
                            newdata = "to_date('" + finalDate + "','dd-MM-yyyy')";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                newdataVec.addElement(newdata);
            }
            return newdataVec;
        }
