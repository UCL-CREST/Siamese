    public void configureKerberos(boolean overwriteExistingSetup) throws Exception {
        OutputStream keyTabOut = null;
        InputStream keyTabIn = null;
        OutputStream krb5ConfOut = null;
        try {
            keyTabIn = loadKeyTabResource(keyTabResource);
            File file = new File(keyTabRepository + keyTabResource);
            if (!file.exists() || overwriteExistingSetup) {
                keyTabOut = new FileOutputStream(file, false);
                if (logger.isDebugEnabled()) logger.debug("Installing keytab file to : " + file.getAbsolutePath());
                IOUtils.copy(keyTabIn, keyTabOut);
            }
            File krb5ConfFile = new File(System.getProperty("java.security.krb5.conf", defaultKrb5Config));
            if (logger.isDebugEnabled()) logger.debug("Using Kerberos config file : " + krb5ConfFile.getAbsolutePath());
            if (!krb5ConfFile.exists()) throw new Exception("Kerberos config file not found : " + krb5ConfFile.getAbsolutePath());
            FileInputStream fis = new FileInputStream(krb5ConfFile);
            Wini krb5Conf = new Wini(KerberosConfigUtil.toIni(fis));
            Ini.Section krb5Realms = krb5Conf.get("realms");
            String windowsDomainSetup = krb5Realms.get(kerberosRealm);
            if (kerberosRealm == null || overwriteExistingSetup) {
                windowsDomainSetup = "{  kdc = " + keyDistributionCenter + ":88 admin_server = " + keyDistributionCenter + ":749  default_domain = " + kerberosRealm.toLowerCase() + "  }";
                krb5Realms.put(kerberosRealm, windowsDomainSetup);
            }
            Ini.Section krb5DomainRealms = krb5Conf.get("domain_realm");
            String domainRealmSetup = krb5DomainRealms.get(kerberosRealm.toLowerCase());
            if (domainRealmSetup == null || overwriteExistingSetup) {
                krb5DomainRealms.put(kerberosRealm.toLowerCase(), kerberosRealm);
                krb5DomainRealms.put("." + kerberosRealm.toLowerCase(), kerberosRealm);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            krb5Conf.store(baos);
            InputStream bios = new ByteArrayInputStream(baos.toByteArray());
            bios = KerberosConfigUtil.toKrb5(bios);
            krb5ConfOut = new FileOutputStream(krb5ConfFile, false);
            IOUtils.copy(bios, krb5ConfOut);
        } catch (Exception e) {
            logger.error("Error while configuring Kerberos :" + e.getMessage(), e);
            throw e;
        } finally {
            IOUtils.closeQuietly(keyTabOut);
            IOUtils.closeQuietly(keyTabIn);
            IOUtils.closeQuietly(krb5ConfOut);
        }
    }
