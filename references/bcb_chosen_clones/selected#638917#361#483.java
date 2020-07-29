    public void open(int mode) throws MessagingException {
        final int ALL_OPTIONS = READ_ONLY | READ_WRITE | MODE_MBOX | MODE_BLOB;
        if (DebugFile.trace) {
            DebugFile.writeln("DBFolder.open(" + String.valueOf(mode) + ")");
            DebugFile.incIdent();
        }
        if ((0 == (mode & READ_ONLY)) && (0 == (mode & READ_WRITE))) {
            if (DebugFile.trace) DebugFile.decIdent();
            throw new MessagingException("Folder must be opened in either READ_ONLY or READ_WRITE mode");
        } else if (ALL_OPTIONS != (mode | ALL_OPTIONS)) {
            if (DebugFile.trace) DebugFile.decIdent();
            throw new MessagingException("Invalid DBFolder open() option mode");
        } else {
            if ((0 == (mode & MODE_MBOX)) && (0 == (mode & MODE_BLOB))) mode = mode | MODE_MBOX;
            iOpenMode = mode;
            oConn = ((DBStore) getStore()).getConnection();
            if ((iOpenMode & MODE_MBOX) != 0) {
                String sFolderUrl;
                try {
                    sFolderUrl = Gadgets.chomp(getStore().getURLName().getFile(), File.separator) + oCatg.getPath(oConn);
                    if (DebugFile.trace) DebugFile.writeln("mail folder directory is " + sFolderUrl);
                    if (sFolderUrl.startsWith("file://")) sFolderDir = sFolderUrl.substring(7); else sFolderDir = sFolderUrl;
                } catch (SQLException sqle) {
                    iOpenMode = 0;
                    oConn = null;
                    if (DebugFile.trace) DebugFile.decIdent();
                    throw new MessagingException(sqle.getMessage(), sqle);
                }
                try {
                    File oDir = new File(sFolderDir);
                    if (!oDir.exists()) {
                        FileSystem oFS = new FileSystem();
                        oFS.mkdirs(sFolderUrl);
                    }
                } catch (IOException ioe) {
                    iOpenMode = 0;
                    oConn = null;
                    if (DebugFile.trace) DebugFile.decIdent();
                    throw new MessagingException(ioe.getMessage(), ioe);
                } catch (SecurityException se) {
                    iOpenMode = 0;
                    oConn = null;
                    if (DebugFile.trace) DebugFile.decIdent();
                    throw new MessagingException(se.getMessage(), se);
                } catch (Exception je) {
                    iOpenMode = 0;
                    oConn = null;
                    if (DebugFile.trace) DebugFile.decIdent();
                    throw new MessagingException(je.getMessage(), je);
                }
                JDCConnection oConn = getConnection();
                PreparedStatement oStmt = null;
                ResultSet oRSet = null;
                boolean bHasFilePointer;
                try {
                    oStmt = oConn.prepareStatement("SELECT NULL FROM " + DB.k_x_cat_objs + " WHERE " + DB.gu_category + "=? AND " + DB.id_class + "=15", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                    oStmt.setString(1, getCategory().getString(DB.gu_category));
                    oRSet = oStmt.executeQuery();
                    bHasFilePointer = oRSet.next();
                    oRSet.close();
                    oRSet = null;
                    oStmt.close();
                    oStmt = null;
                    if (!bHasFilePointer) {
                        oConn.setAutoCommit(false);
                        Product oProd = new Product();
                        oProd.put(DB.gu_owner, oCatg.getString(DB.gu_owner));
                        oProd.put(DB.nm_product, oCatg.getString(DB.nm_category));
                        oProd.store(oConn);
                        ProductLocation oLoca = new ProductLocation();
                        oLoca.put(DB.gu_product, oProd.getString(DB.gu_product));
                        oLoca.put(DB.gu_owner, oCatg.getString(DB.gu_owner));
                        oLoca.put(DB.pg_prod_locat, 1);
                        oLoca.put(DB.id_cont_type, 1);
                        oLoca.put(DB.id_prod_type, "MBOX");
                        oLoca.put(DB.len_file, 0);
                        oLoca.put(DB.xprotocol, "file://");
                        oLoca.put(DB.xhost, "localhost");
                        oLoca.put(DB.xpath, Gadgets.chomp(sFolderDir, File.separator));
                        oLoca.put(DB.xfile, oCatg.getString(DB.nm_category) + ".mbox");
                        oLoca.put(DB.xoriginalfile, oCatg.getString(DB.nm_category) + ".mbox");
                        oLoca.store(oConn);
                        oStmt = oConn.prepareStatement("INSERT INTO " + DB.k_x_cat_objs + " (" + DB.gu_category + "," + DB.gu_object + "," + DB.id_class + ") VALUES (?,?,15)");
                        oStmt.setString(1, oCatg.getString(DB.gu_category));
                        oStmt.setString(2, oProd.getString(DB.gu_product));
                        oStmt.executeUpdate();
                        oStmt.close();
                        oStmt = null;
                        oConn.commit();
                    }
                } catch (SQLException sqle) {
                    if (DebugFile.trace) {
                        DebugFile.writeln("SQLException " + sqle.getMessage());
                        DebugFile.decIdent();
                    }
                    if (oStmt != null) {
                        try {
                            oStmt.close();
                        } catch (SQLException ignore) {
                        }
                    }
                    if (oConn != null) {
                        try {
                            oConn.rollback();
                        } catch (SQLException ignore) {
                        }
                    }
                    throw new MessagingException(sqle.getMessage(), sqle);
                }
            } else {
                sFolderDir = null;
            }
            if (DebugFile.trace) {
                DebugFile.decIdent();
                String sMode = "";
                if ((iOpenMode & READ_WRITE) != 0) sMode += " READ_WRITE ";
                if ((iOpenMode & READ_ONLY) != 0) sMode += " READ_ONLY ";
                if ((iOpenMode & MODE_BLOB) != 0) sMode += " MODE_BLOB ";
                if ((iOpenMode & MODE_MBOX) != 0) sMode += " MODE_MBOX ";
                DebugFile.writeln("End DBFolder.open() :");
            }
        }
    }
