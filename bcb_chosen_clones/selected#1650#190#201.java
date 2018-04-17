    @Test
    public void testGrantLicense() throws Exception {
        context.turnOffAuthorisationSystem();
        Item item = Item.create(context);
        String defaultLicense = ConfigurationManager.getDefaultSubmissionLicense();
        LicenseUtils.grantLicense(context, item, defaultLicense);
        StringWriter writer = new StringWriter();
        IOUtils.copy(item.getBundles("LICENSE")[0].getBitstreams()[0].retrieve(), writer);
        String license = writer.toString();
        assertThat("testGrantLicense 0", license, equalTo(defaultLicense));
        context.restoreAuthSystemState();
    }
