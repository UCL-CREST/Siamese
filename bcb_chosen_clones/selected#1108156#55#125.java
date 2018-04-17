    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
        Writer out = null;
        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {
            AuthSession auth_ = (AuthSession) renderRequest.getUserPrincipal();
            if (auth_ == null || !auth_.isUserInRole("webmill.upload_image")) {
                throw new PortletSecurityException("You have not enough right");
            }
            if (log.isDebugEnabled()) log.debug("Start commit new image from file");
            dbDyn = DatabaseAdapter.getInstance();
            String index_page = PortletService.url("mill.image.index", renderRequest, renderResponse);
            if (log.isDebugEnabled()) log.debug("right to commit image - " + auth_.isUserInRole("webmill.upload_image"));
            PortletSession sess = renderRequest.getPortletSession(true);
            if ((sess.getAttribute("MILL.IMAGE.ID_MAIN") == null) || (sess.getAttribute("MILL.IMAGE.DESC_IMAGE") == null)) {
                out.write("Not all parametrs initialized");
                return;
            }
            Long id_main = (Long) sess.getAttribute("MILL.IMAGE.ID_MAIN");
            String desc = ((String) sess.getAttribute("MILL.IMAGE.DESC_IMAGE"));
            if (log.isDebugEnabled()) log.debug("image description " + desc);
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_image_number_file");
            seq.setTableName("MAIN_FORUM_THREADS");
            seq.setColumnName("ID_THREAD");
            Long currID = dbDyn.getSequenceNextValue(seq);
            String storage_ = portletConfig.getPortletContext().getRealPath("/") + File.separatorChar + "image";
            String fileName = storage_ + File.separator + StringTools.appendString("" + currID, '0', 7, true) + "-";
            if (log.isDebugEnabled()) log.debug("image fileName " + fileName);
            String newFileName = "";
            String supportExtension[] = { ".jpg", ".jpeg", ".gif", ".png" };
            try {
                if (true) throw new UploadFileException("Todo need fix");
            } catch (UploadFileException e) {
                log.error("Error save image to disk", e);
                out.write("<html><head></head<body>" + "Error while processing this page:<br>" + ExceptionTools.getStackTrace(e, 20, "<br>") + "<br>" + "<p><a href=\"" + index_page + "\">continue</a></p>" + "</body></html>");
                return;
            }
            if (log.isDebugEnabled()) log.debug("newFileName " + newFileName);
            UserInfo userInfo = auth_.getUserInfo();
            CustomSequenceType seqImageDir = new CustomSequenceType();
            seqImageDir.setSequenceName("seq_WM_image_dir");
            seqImageDir.setTableName("WM_IMAGE_DIR");
            seqImageDir.setColumnName("ID_IMAGE_DIR");
            Long seqValue = dbDyn.getSequenceNextValue(seqImageDir);
            ps = dbDyn.prepareStatement("insert into WM_IMAGE_DIR " + "( ID_IMAGE_DIR, ID_FIRM, is_group, id, id_main, name_file, description )" + "(?, ?, 0, ?, ?, ?, ?");
            RsetTools.setLong(ps, 1, seqValue);
            RsetTools.setLong(ps, 2, userInfo.getCompanyId());
            RsetTools.setLong(ps, 3, currID);
            RsetTools.setLong(ps, 4, id_main);
            ps.setString(5, "/image/" + newFileName);
            ps.setString(6, desc);
            ps.executeUpdate();
            dbDyn.commit();
            if (log.isDebugEnabled()) log.debug("redirect to indexPage - " + index_page);
            out.write("Image successful uploaded");
            return;
        } catch (Exception e) {
            try {
                dbDyn.rollback();
            } catch (SQLException e1) {
            }
            final String es = "Error upload image";
            log.error(es, e);
            throw new PortletException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }
