    private void doTask() {
        try {
            log("\n\n\n\n\n\n\n\n\n");
            log(" =================================================");
            log(" = Starting PSCafePOS                            =");
            log(" =================================================");
            log(" =   An open source point of sale system         =");
            log(" =   for educational organizations.              =");
            log(" =================================================");
            log(" = General Information                           =");
            log(" =   http://pscafe.sourceforge.net               =");
            log(" = Free Product Support                          =");
            log(" =   http://www.sourceforge.net/projects/pscafe  =");
            log(" =================================================");
            log(" = License Overview                              =");
            log(" =================================================");
            log(" = PSCafePOS is a POS System for Schools         =");
            log(" = Copyright (C) 2007 Charles Syperski           =");
            log(" =                                               =");
            log(" = This program is free software; you can        =");
            log(" = redistribute it and/or modify it under the    =");
            log(" = terms of the GNU General Public License as    =");
            log(" = published by the Free Software Foundation;    =");
            log(" = either version 2 of the License, or any later =");
            log(" = version.                                      =");
            log(" =                                               =");
            log(" = This program is distributed in the hope that  =");
            log(" = it will be useful, but WITHOUT ANY WARRANTY;  =");
            log(" = without even the implied warranty of          =");
            log(" = MERCHANTABILITY or FITNESS FOR A PARTICULAR   =");
            log(" = PURPOSE.                                      =");
            log(" =                                               =");
            log(" = See the GNU General Public License for more   =");
            log(" = details.                                      =");
            log(" =                                               =");
            log(" = You should have received a copy of the GNU    =");
            log(" = General Public License along with this        =");
            log(" = program; if not, write to the                 =");
            log(" =                                               =");
            log(" =      Free Software Foundation, Inc.           =");
            log(" =      59 Temple Place, Suite 330               =");
            log(" =      Boston, MA 02111-1307 USA                =");
            log(" =================================================");
            log(" = If you have any questions of comments please  =");
            log(" = let us know at http://pscafe.sourceforge.net  =");
            log(" =================================================");
            pause();
            File settings;
            if (blAltSettings) {
                System.out.println("\n  + Alternative path specified at run time:");
                System.out.println("    Path: " + strAltPath);
                settings = new File(strAltPath);
            } else {
                settings = new File("etc" + File.separatorChar + "settings.dbp");
            }
            System.out.print("\n  + Checking for existance of settings...");
            boolean blGo = false;
            if (settings.exists() && settings.canRead()) {
                log("[OK]");
                blGo = true;
                if (forceConfig) {
                    System.out.print("\n  + Running Config Wizard (at user request)...");
                    Process pp = Runtime.getRuntime().exec("java -cp . PSSettingWizard etc" + File.separatorChar + "settings.dbp");
                    InputStream stderr = pp.getErrorStream();
                    InputStream stdin = pp.getInputStream();
                    InputStreamReader isr = new InputStreamReader(stdin);
                    BufferedReader br = new BufferedReader(isr);
                    String ln = null;
                    while ((ln = br.readLine()) != null) System.out.println("  " + ln);
                    pp.waitFor();
                }
            } else {
                log("[FAILED]");
                settings = new File("etc" + File.separatorChar + "settings.dbp.firstrun");
                System.out.print("\n  + Checking if this is the first run... ");
                if (settings.exists() && settings.canRead()) {
                    log("[FOUND]");
                    File toFile = new File("etc" + File.separatorChar + "settings.dbp");
                    FileInputStream from = null;
                    FileOutputStream to = null;
                    try {
                        from = new FileInputStream(settings);
                        to = new FileOutputStream(toFile);
                        byte[] buffer = new byte[4096];
                        int bytes_read;
                        while ((bytes_read = from.read(buffer)) != -1) {
                            to.write(buffer, 0, bytes_read);
                        }
                        if (toFile.exists() && toFile.canRead()) {
                            settings = null;
                            settings = new File("etc" + File.separatorChar + "settings.dbp");
                        }
                        System.out.print("\n  + Running Settings Wizard... ");
                        try {
                            Process p = Runtime.getRuntime().exec("java PSSettingWizard etc" + File.separatorChar + "settings.dbp");
                            InputStream stderr = p.getErrorStream();
                            InputStream stdin = p.getInputStream();
                            InputStreamReader isr = new InputStreamReader(stdin);
                            BufferedReader br = new BufferedReader(isr);
                            String ln = null;
                            while ((ln = br.readLine()) != null) System.out.println("  " + ln);
                            p.waitFor();
                            log("[OK]");
                            if (p.exitValue() == 0) blGo = true;
                        } catch (InterruptedException i) {
                            System.err.println(i.getMessage());
                        }
                    } catch (Exception ex) {
                        System.err.println(ex.getMessage());
                    } finally {
                        if (from != null) try {
                            from.close();
                        } catch (IOException e) {
                            ;
                        }
                        if (to != null) try {
                            to.close();
                        } catch (IOException e) {
                            ;
                        }
                    }
                } else {
                    settings = null;
                    settings = new File("etc" + File.separatorChar + "settings.dbp");
                    DBSettingsWriter writ = new DBSettingsWriter();
                    writ.writeFile(new DBSettings(), settings);
                    blGo = true;
                }
            }
            if (blGo) {
                String cp = ".";
                try {
                    File classpath = new File("lib");
                    File[] subFiles = classpath.listFiles();
                    for (int i = 0; i < subFiles.length; i++) {
                        if (subFiles[i].isFile()) {
                            cp += File.pathSeparatorChar + "lib" + File.separatorChar + subFiles[i].getName() + "";
                        }
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                try {
                    boolean blExecutePOS = false;
                    System.out.print("\n  + Checking runtime settings...         ");
                    DBSettings info = null;
                    if (settings == null) settings = new File("etc" + File.separatorChar + "settings.dbp");
                    if (settings.exists() && settings.canRead()) {
                        DBSettingsWriter writ = new DBSettingsWriter();
                        info = (DBSettings) writ.loadSettingsDB(settings);
                        if (info != null) {
                            blExecutePOS = true;
                        }
                    }
                    if (blExecutePOS) {
                        log("[OK]");
                        String strSSL = "";
                        String strSSLDebug = "";
                        if (info != null) {
                            debug = info.get(DBSettings.MAIN_DEBUG).compareTo("1") == 0;
                            if (debug) log("       * Debug Mode is ON"); else log("       * Debug Mode is OFF");
                            if (info.get(DBSettings.POS_SSLENABLED).compareTo("1") == 0) {
                                strSSL = "-Djavax.net.ssl.keyStore=" + info.get(DBSettings.POS_SSLKEYSTORE) + " -Djavax.net.ssl.keyStorePassword=pscafe -Djavax.net.ssl.trustStore=" + info.get(DBSettings.POS_SSLTRUSTSTORE) + " -Djavax.net.ssl.trustStorePassword=pscafe";
                                log("       * Using SSL");
                                debug("            " + strSSL);
                                if (info.get(DBSettings.POS_SSLDEBUG).compareTo("1") == 0) {
                                    strSSLDebug = "-Djavax.net.debug=all";
                                    log("       * SSL Debugging enabled");
                                    debug("            " + strSSLDebug);
                                }
                            }
                        }
                        String strPOSRun = "java  -cp " + cp + " " + strSSL + " " + strSSLDebug + " POSDriver " + settings.getPath();
                        debug(strPOSRun);
                        System.out.print("\n  + Running PSCafePOS...                 ");
                        Process pr = Runtime.getRuntime().exec(strPOSRun);
                        System.out.print("[OK]\n\n");
                        InputStream stderr = pr.getErrorStream();
                        InputStream stdin = pr.getInputStream();
                        InputStreamReader isr = new InputStreamReader(stdin);
                        InputStreamReader isre = new InputStreamReader(stderr);
                        BufferedReader br = new BufferedReader(isr);
                        BufferedReader bre = new BufferedReader(isre);
                        String line = null;
                        String lineError = null;
                        log(" =================================================");
                        log(" =        Output from PSCafePOS System           =");
                        log(" =================================================");
                        while ((line = br.readLine()) != null || (lineError = bre.readLine()) != null) {
                            if (line != null) System.out.println(" [PSCafePOS]" + line);
                            if (lineError != null) System.out.println(" [ERR]" + lineError);
                        }
                        pr.waitFor();
                        log(" =================================================");
                        log(" =       End output from PSCafePOS System        =");
                        log(" =              PSCafePOS has exited             =");
                        log(" =================================================");
                    } else {
                        log("[Failed]");
                    }
                } catch (Exception i) {
                    log(i.getMessage());
                    i.printStackTrace();
                }
            }
        } catch (Exception e) {
            log(e.getMessage());
        }
    }
