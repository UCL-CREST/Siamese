    public static String addTag(String tag_id, String tag_description, String tag_text, String tag_author, String application_code) {
        String so = OctopusErrorMessages.UNKNOWN_ERROR;
        if (tag_id == null || tag_id.trim().equals("")) {
            return OctopusErrorMessages.TAG_ID_CANT_BE_EMPTY;
        }
        if (tag_id.trim().equals(application_code)) {
            return OctopusErrorMessages.TAG_ID_TOO_SHORT;
        }
        if (!StringUtil.isAlphaNumerical(StringUtil.replace(StringUtil.replace(tag_id, "-", ""), "_", ""))) {
            return OctopusErrorMessages.TAG_ID_MUST_BE_ALPHANUMERIC;
        }
        if (!tag_id.startsWith(application_code)) {
            return OctopusErrorMessages.TAG_ID_MUST_START + " " + application_code;
        }
        String tag_exist = exist(tag_id);
        if (!tag_exist.equals(OctopusErrorMessages.DOESNT_ALREADY_EXIST)) {
            return tag_exist;
        }
        if (tag_description != null && !tag_description.trim().equals("")) {
            tag_description = StringUtil.replace(tag_description, "\n", " ");
            tag_description = StringUtil.replace(tag_description, "\r", " ");
            tag_description = StringUtil.replace(tag_description, "\t", " ");
            tag_description = StringUtil.replace(tag_description, "<", "&#60;");
            tag_description = StringUtil.replace(tag_description, ">", "&#62;");
            tag_description = StringUtil.replace(tag_description, "'", "&#39;");
        } else {
            return OctopusErrorMessages.DESCRIPTION_TEXT_EMPTY;
        }
        if (tag_text != null && !tag_text.trim().equals("")) {
            tag_text = StringUtil.replace(tag_text, "\n", " ");
            tag_text = StringUtil.replace(tag_text, "\r", " ");
            tag_text = StringUtil.replace(tag_text, "\t", " ");
            tag_text = StringUtil.replace(tag_text, "<", "&#60;");
            tag_text = StringUtil.replace(tag_text, ">", "&#62;");
            tag_text = StringUtil.replace(tag_text, "'", "&#39;");
        } else {
            return OctopusErrorMessages.TRANSLATION_TEXT_EMPTY;
        }
        if (tag_author == null || tag_author.trim().equals("")) {
            return OctopusErrorMessages.MAIN_PARAMETER_EMPTY;
        }
        DBConnection theConnection = null;
        try {
            theConnection = DBServiceManager.allocateConnection();
            theConnection.setAutoCommit(false);
            String query = "INSERT INTO tr_tag (tr_tag_id,tr_tag_applicationid,tr_tag_info,tr_tag_creationdate) ";
            query += "VALUES (?,?,'" + tag_description + "',getdate())";
            PreparedStatement state = theConnection.prepareStatement(query);
            state.setString(1, tag_id);
            state.setString(2, application_code);
            state.executeUpdate();
            String query2 = "INSERT INTO tr_translation (tr_translation_trtagid, tr_translation_language, tr_translation_text, tr_translation_version, tr_translation_lud, tr_translation_lun ) ";
            query2 += "VALUES(?,'" + OctopusApplication.MASTER_LANGUAGE + "','" + tag_text + "',0,getdate(),?)";
            PreparedStatement state2 = theConnection.prepareStatement(query2);
            state2.setString(1, tag_id);
            state2.setString(2, tag_author);
            state2.executeUpdate();
            theConnection.commit();
            so = OctopusErrorMessages.ACTION_DONE;
        } catch (SQLException e) {
            try {
                theConnection.rollback();
            } catch (SQLException ex) {
            }
            so = OctopusErrorMessages.ERROR_DATABASE;
        } finally {
            if (theConnection != null) {
                try {
                    theConnection.setAutoCommit(true);
                } catch (SQLException ex) {
                }
                theConnection.release();
            }
        }
        return so;
    }
