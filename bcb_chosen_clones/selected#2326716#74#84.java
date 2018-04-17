    public void reset(int currentPilot) {
        try {
            PreparedStatement psta = jdbc.prepareStatement("DELETE FROM component_prop " + "WHERE pilot_id = ? ");
            psta.setInt(1, currentPilot);
            psta.executeUpdate();
            jdbc.commit();
        } catch (SQLException e) {
            jdbc.rollback();
            log.debug(e);
        }
    }
