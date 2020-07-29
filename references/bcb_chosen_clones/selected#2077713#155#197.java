    public String exportall(Map parameters) {
        HttpServletRequest req = (HttpServletRequest) parameters.get(WorkBookCommander.REQUEST);
        HttpServletResponse res = (HttpServletResponse) parameters.get(WorkBookCommander.REQUEST);
        User usr = (User) parameters.get(WorkBookCommander.USER);
        Serve sx = ((Serve) parameters.get(WorkBookCommander.SERVERCONFIG));
        try {
            Connection e360conn = sx.getExtenConfig().getE360Connection();
            final PreparedStatement getmemes = e360conn.prepareStatement("SELECT id,description FROM kb_memes WHERE meme_type=26" + " AND owner_id=?");
            getmemes.setInt(1, usr.getId());
            ResultSet rsx = getmemes.executeQuery();
            String wdx = ((ServeConnection) req).getRequestURI(true);
            String webdir = sx.getRealPath("/media/");
            webdir += usr.getId() + "/backups/";
            wdx = wdx + "/media/" + usr.getId() + "/backups/";
            String zipfnx = "data_export-" + System.currentTimeMillis() + ".zip";
            File tmpzip = new File(webdir + zipfnx);
            tmpzip.mkdirs();
            tmpzip.delete();
            ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(tmpzip)));
            final PreparedStatement getdesc = e360conn.prepareStatement("SELECT description2 FROM kb_memes WHERE id=?");
            while (rsx.next()) {
                try {
                    getdesc.setInt(1, rsx.getInt("id"));
                    ResultSet rsx2 = getdesc.executeQuery();
                    if (rsx2.next()) {
                        ZipEntry zx = new ZipEntry(rsx.getString("id") + "-" + rsx.getString("description") + ".xls");
                        zout.putNextEntry(zx);
                        byte[] content = ExtenXLS.getXLS(rsx2.getString("description2")).getBytes();
                        for (int t = 0; t < content.length; t++) zout.write(content[t]);
                        zout.closeEntry();
                    }
                } catch (Exception e) {
                    Logger.logErr("PluginSystem.exportall entry failed.", e);
                }
            }
            zout.close();
            res.sendRedirect(wdx + zipfnx);
            return null;
        } catch (Exception e) {
            Logger.logErr("Error exporting all data.", e);
            return WorkBookCommander.returnXMLErrorResponse("Error exporting all data." + e.toString());
        }
    }
