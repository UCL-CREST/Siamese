    private boolean setupDatabase() {
        if (DoInstallationTasks.DEBUG_DB) {
            System.out.println("About to setup database");
        }
        if (installer.getRootDBUsername() != null && installer.getRootDBUsername().length() > 1 && installer.getRootDBPassword() != null && installer.getRootDBPassword().length() > 1) {
            if (DoInstallationTasks.DEBUG_DB) {
                System.out.println("Going to call doDBRootPortions");
            }
            if (!doDBRootPortions(installer.getPachyDBHost(), installer.getPachyDBPort(), installer.getPachyDBName(), installer.getPachyDBUsername(), installer.getPachyDBPassword(), installer.getRootDBUsername(), installer.getRootDBPassword())) {
                System.err.println("Root work not able to be completed, " + "not continuing.");
                return false;
            }
            if (DoInstallationTasks.DEBUG_DB) {
                System.out.println("Back from call to doDBRootPortions");
            }
        }
        if (DoInstallationTasks.DEBUG_DB) {
            System.out.println("Going to open SQL files");
        }
        Connection conn = getDBConnection(installer.getPachyDBHost(), installer.getPachyDBPort(), installer.getPachyDBName(), installer.getPachyDBUsername(), installer.getPachyDBPassword());
        if (conn == null) {
            return false;
        }
        Statement stmt = null;
        boolean havePachy20 = false;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRESENTATION LIMIT 1");
            if (rs.next()) {
                havePachy20 = true;
            }
            if (!havePachy20) {
                rs = stmt.executeQuery("SELECT * FROM SCREEN LIMIT 1");
                if (rs.next()) {
                    havePachy20 = true;
                }
            }
        } catch (SQLException sqle) {
            System.err.println("Error doing check for presentation, means " + "2.0 doesn't exist");
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("SQLState: " + sqle.getSQLState());
            System.out.println("VendorError: " + sqle.getErrorCode());
        } finally {
            try {
                stmt.close();
            } catch (SQLException sqlex) {
            }
        }
        if (havePachy20) {
            Object[] options = { Pachyderm21Installer.ISTRINGS.getString("dialog.abort"), Pachyderm21Installer.ISTRINGS.getString("dialog.keep"), Pachyderm21Installer.ISTRINGS.getString("dialog.overwrite") };
            int result = JOptionPane.showOptionDialog(this.installer, Pachyderm21Installer.ISTRINGS.getString("dit.pachy20msg"), Pachyderm21Installer.ISTRINGS.getString("dit.pachy20title"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (result == 0) {
                System.out.println("Aborted by user before doing database");
                return false;
            } else if (result == 2) {
                havePachy20 = false;
            }
        } else {
            Object[] options = { Pachyderm21Installer.ISTRINGS.getString("dialog.continue"), Pachyderm21Installer.ISTRINGS.getString("dialog.abort") };
            int result = JOptionPane.showOptionDialog(this.installer, Pachyderm21Installer.ISTRINGS.getString("dit.nopachy20msg"), Pachyderm21Installer.ISTRINGS.getString("dit.nopachy20title"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (result == 1) {
                System.out.println("Aborted by user before doing database");
                return false;
            }
        }
        boolean dbError = false;
        if (havePachy20) {
            try {
                java.util.Date d = new java.util.Date();
                stmt = conn.createStatement();
                stmt.executeUpdate("RENAME TABLE APDEFAULT TO APDEFAULT_2_0_" + d.getTime());
            } catch (SQLException sqle) {
                System.err.println("Error doing check for presentation, " + "means 2.0 doesn't exist");
                System.out.println("SQLException: " + sqle.getMessage());
                System.out.println("SQLState: " + sqle.getSQLState());
                System.out.println("VendorError: " + sqle.getErrorCode());
                dbError = true;
            } finally {
                try {
                    stmt.close();
                } catch (SQLException sqlex) {
                }
            }
        }
        if (dbError) {
            return false;
        }
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("START TRANSACTION");
            InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream("apdefaults.sql"));
            LineNumberReader lnr = new LineNumberReader(isr);
            String linein;
            while ((linein = lnr.readLine()) != null) {
                if (linein.trim().length() > 0) {
                    String lineout = replaceTemplateVariables(linein);
                    stmt.executeUpdate(lineout);
                }
            }
            stmt.executeUpdate("COMMIT");
            lnr.close();
        } catch (SQLException sqle) {
            System.err.println("error doing apdefaults.sql template");
            System.out.println("sqlexception: " + sqle.getMessage());
            System.out.println("sqlstate: " + sqle.getSQLState());
            System.out.println("vendorerror: " + sqle.getErrorCode());
            if (stmt != null) {
                try {
                    stmt.executeUpdate("ROLLBACK");
                } catch (SQLException sqlex) {
                }
            }
            dbError = true;
        } catch (Exception e) {
            System.err.println("Error doing apdefaults.sql template");
            e.printStackTrace(System.err);
            if (stmt != null) {
                try {
                    stmt.executeUpdate("ROLLBACK");
                } catch (SQLException sqlex) {
                }
            }
            dbError = true;
        } finally {
            try {
                stmt.close();
            } catch (SQLException sqlex) {
            }
        }
        if (dbError) {
            return false;
        }
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("START TRANSACTION");
            InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream("Pachyderm21.sql"));
            LineNumberReader lnr = new LineNumberReader(isr);
            String linein;
            while ((linein = lnr.readLine()) != null) {
                if (linein.trim().length() > 0) {
                    String lineout = replaceTemplateVariables(linein);
                    if (DoInstallationTasks.DEBUG) {
                        System.out.println("line #" + lnr.getLineNumber());
                    }
                    stmt.executeUpdate(lineout);
                }
            }
            stmt.executeUpdate("COMMIT");
            lnr.close();
        } catch (SQLException sqle) {
            System.err.println("error doing pachyderm21.sql template");
            System.out.println("sqlexception: " + sqle.getMessage());
            System.out.println("sqlstate: " + sqle.getSQLState());
            System.out.println("vendorerror: " + sqle.getErrorCode());
            if (stmt != null) {
                try {
                    stmt.executeUpdate("ROLLBACK");
                } catch (SQLException sqlex) {
                }
            }
            dbError = true;
        } catch (Exception e) {
            System.err.println("error doing pachyderm21.sql template");
            e.printStackTrace(System.err);
            if (stmt != null) {
                try {
                    stmt.executeUpdate("ROLLBACK");
                } catch (SQLException sqlex) {
                }
            }
            dbError = true;
        } finally {
            try {
                stmt.close();
            } catch (SQLException sqlex) {
            }
        }
        if (dbError) {
            return false;
        }
        if (!havePachy20) {
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate("START TRANSACTION");
                InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream("Pachyderm" + "21new.sql"));
                LineNumberReader lnr = new LineNumberReader(isr);
                String linein;
                while ((linein = lnr.readLine()) != null) {
                    if (linein.trim().length() > 0) {
                        String lineout = replaceTemplateVariables(linein);
                        if (DoInstallationTasks.DEBUG) {
                            System.out.println("Line #" + lnr.getLineNumber());
                        }
                        stmt.executeUpdate(lineout);
                    }
                }
                stmt.executeUpdate("COMMIT");
                lnr.close();
            } catch (SQLException sqle) {
                System.err.println("Error doing Pachyderm21new.sql template");
                System.out.println("SQLException: " + sqle.getMessage());
                System.out.println("SQLState: " + sqle.getSQLState());
                System.out.println("VendorError: " + sqle.getErrorCode());
                if (stmt != null) {
                    try {
                        stmt.executeUpdate("ROLLBACK");
                    } catch (SQLException sqlex) {
                    }
                }
                dbError = true;
            } catch (Exception e) {
                System.err.println("Error doing Pachyderm21.sql template");
                e.printStackTrace(System.err);
                if (stmt != null) {
                    try {
                        stmt.executeUpdate("ROLLBACK");
                    } catch (SQLException sqlex) {
                    }
                }
                dbError = true;
            } finally {
                try {
                    stmt.close();
                } catch (SQLException sqlex) {
                }
            }
        }
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        try {
            String adminPassword = installer.getAdminPassword();
            MessageDigest _md = MessageDigest.getInstance("MD5");
            _md.update(adminPassword.getBytes("UTF-8"));
            byte[] md5 = _md.digest();
            ps = conn.prepareStatement("UPDATE AUTHRECORD set PASSWORD=? " + "WHERE USERNAME='administrator'");
            ps.setBytes(1, md5);
            int numupdates = ps.executeUpdate();
            if (DEBUG) System.out.println("Changing admin password, " + "numUpdates = " + numupdates);
            Vector<AdminData> v = installer.getAdditionalAdminAccounts();
            String customPropertiesSPFPre = "{\n \"CXMultiValueArchive\" = {" + "\n  \"class\" = " + "\"ca.ucalgary.apollo.core." + "CXMutableMultiValue\";\n  " + "\"values\" = (\n   " + "{\n    \"class\" = \"ca.ucalgary." + "apollo.core.CXMultiValue$Value\";\n" + "    \"identifier\" = \"0\";\n    " + "\"label\" = \"work\";\n    " + "\"value\" = \"";
            String customPropertiesSPFPost = "\";\n   }\n  );\n  \"identCounter\" = \"1\";\n };\n}";
            if (v.size() > 0) {
                ps = conn.prepareStatement("INSERT INTO `APPERSON` VALUES " + "(NULL,NULL,NULL,NOW(),NULL,NULL," + "?,?,NULL,NULL,NULL,NULL,NULL," + "NULL,?,NULL,NULL,NULL,NULL,NOW()," + "NULL,NULL,NULL,NULL,NULL,NULL,?," + "NULL,NULL,NULL,NULL,NULL)");
                ps1 = conn.prepareStatement("INSERT INTO `AUTHRECORD` VALUES " + "(?,'pachyderm',?,NULL)");
                ps2 = conn.prepareStatement("INSERT INTO `AUTHMAP` " + "(external_id,external_realm," + "map_id,person_id) " + "VALUES (?,'pachyderm',?,?)");
                ps3 = conn.prepareStatement("INSERT INTO `GROUPPERSONJOIN` " + "(group_id,person_id) " + "VALUES(1, ?)");
            }
            for (int i = 0; i < v.size(); ++i) {
                AdminData ad = (AdminData) v.elementAt(i);
                _md = MessageDigest.getInstance("MD5");
                _md.update(ad.getPassword().getBytes("UTF-8"));
                md5 = _md.digest();
                ps.setString(1, customPropertiesSPFPre + ad.getEmail() + customPropertiesSPFPost);
                ps.setString(2, ad.getFirstName());
                ps.setString(3, ad.getLastName());
                ps.setInt(4, i + 2);
                numupdates = ps.executeUpdate();
                if (numupdates == 1) {
                    ps1.setBytes(1, md5);
                    ps1.setString(2, ad.getUsername());
                    ps1.executeUpdate();
                    ps2.setString(1, ad.getUsername() + "@pachyderm");
                    ps2.setInt(2, i + 2);
                    ps2.setInt(3, i + 2);
                    ps2.executeUpdate();
                    ps3.setInt(1, i + 2);
                    ps3.executeUpdate();
                }
            }
        } catch (SQLException sqle) {
            System.err.println("Error doing Pachyderm21new.sql template");
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("SQLState: " + sqle.getSQLState());
            System.out.println("VendorError: " + sqle.getErrorCode());
            dbError = true;
        } catch (Exception e) {
            System.err.println("Error doing Pachyderm21.sql template");
            e.printStackTrace(System.err);
            dbError = true;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException sqlex) {
                }
            }
            if (ps1 != null) {
                try {
                    ps1.close();
                } catch (SQLException sqlex) {
                }
            }
            if (ps2 != null) {
                try {
                    ps2.close();
                } catch (SQLException sqlex) {
                }
            }
        }
        return true;
    }
