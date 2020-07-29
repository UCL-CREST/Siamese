    @Test
    public void testPersistor() throws Exception {
        PreparedStatement ps;
        ps = connection.prepareStatement("delete from privatadresse");
        ps.executeUpdate();
        ps.close();
        ps = connection.prepareStatement("delete from adresse");
        ps.executeUpdate();
        ps.close();
        ps = connection.prepareStatement("delete from person");
        ps.executeUpdate();
        ps.close();
        Persistor p;
        Adresse csd = new LieferAdresse();
        csd.setStrasse("Amalienstrasse 68");
        modificationTracker.addNewParticipant(csd);
        Person markus = new Person();
        markus.setName("markus");
        modificationTracker.addNewParticipant(markus);
        markus.getPrivatAdressen().add(csd);
        Person martin = new Person();
        martin.setName("martin");
        modificationTracker.addNewParticipant(martin);
        p = new Persistor(getSchemaMapping(), idGenerator, modificationTracker);
        p.persist();
        Adresse bia = new LieferAdresse();
        modificationTracker.addNewParticipant(bia);
        bia.setStrasse("dr. boehringer gasse");
        markus.getAdressen().add(bia);
        bia.setPerson(martin);
        markus.setContactPerson(martin);
        p = new Persistor(getSchemaMapping(), idGenerator, modificationTracker);
        try {
            p.persist();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        }
    }
