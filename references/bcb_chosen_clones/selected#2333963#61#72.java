    public void reset(String componentName, int currentPilot) {
        try {
            PreparedStatement psta = jdbc.prepareStatement("DELETE FROM component_prop " + "WHERE pilot_id = ? " + "AND component_name = ?");
            psta.setInt(1, currentPilot);
            psta.setString(2, componentName);
            psta.executeUpdate();
            jdbc.commit();
        } catch (SQLException e) {
            jdbc.rollback();
            log.debug(e);
        }
    }
