    public boolean renameTo(Folder f) throws MessagingException, StoreClosedException, NullPointerException {
        String[] aLabels = new String[] { "en", "es", "fr", "de", "it", "pt", "ca", "ja", "cn", "tw", "fi", "ru", "pl", "nl", "xx" };
        PreparedStatement oUpdt = null;
        if (!((DBStore) getStore()).isConnected()) throw new StoreClosedException(getStore(), "Store is not connected");
        if (oCatg.isNull(DB.gu_category)) throw new NullPointerException("Folder is closed");
        try {
            oUpdt = getConnection().prepareStatement("DELETE FROM " + DB.k_cat_labels + " WHERE " + DB.gu_category + "=?");
            oUpdt.setString(1, oCatg.getString(DB.gu_category));
            oUpdt.executeUpdate();
            oUpdt.close();
            oUpdt.getConnection().prepareStatement("INSERT INTO " + DB.k_cat_labels + " (" + DB.gu_category + "," + DB.id_language + "," + DB.tr_category + "," + DB.url_category + ") VALUES (?,?,?,NULL)");
            oUpdt.setString(1, oCatg.getString(DB.gu_category));
            for (int l = 0; l < aLabels.length; l++) {
                oUpdt.setString(2, aLabels[l]);
                oUpdt.setString(3, f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1).toLowerCase());
                oUpdt.executeUpdate();
            }
            oUpdt.close();
            oUpdt = null;
            getConnection().commit();
        } catch (SQLException sqle) {
            try {
                if (null != oUpdt) oUpdt.close();
            } catch (SQLException ignore) {
            }
            try {
                getConnection().rollback();
            } catch (SQLException ignore) {
            }
            throw new MessagingException(sqle.getMessage(), sqle);
        }
        return true;
    }
