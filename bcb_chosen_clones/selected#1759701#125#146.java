    private void genCustomCodeJar(Admin admin, String orgId, HttpServletRequest req, HttpServletResponse resp) throws IOException, AuthorizationDeniedException, EjbcaException, InvalidKeyException, UnrecoverableKeyException, NoSuchAlgorithmException, SignatureException, CertificateException, KeyStoreException, NoSuchProviderException, CertStoreException, InvalidAlgorithmParameterException, CMSException {
        BasicGlobalSettings bgs = HTMFManageGlobalPropertiesHelper.getBasicGlobalSettingsManager().getBasicGlobalSettings(orgId, application);
        ByteArrayOutputStream gpData = new ByteArrayOutputStream();
        bgs.getProperties().store(gpData, null);
        ByteArrayOutputStream customCodeJarData = new ByteArrayOutputStream();
        JarOutputStream jarOutputStream = new JarOutputStream(customCodeJarData);
        jarOutputStream.putNextEntry(new ZipEntry("global.properties"));
        jarOutputStream.write(gpData.toByteArray());
        HashMap<String, ResourceDataVO> resources = HTMFManageResourcesHelper.getCachedResources(orgId, application);
        for (ResourceDataVO res : resources.values()) {
            if (res.getType().equals(Constants.RESOURCE_TYPE_IMAGE) || res.getType().equals(Constants.RESOURCE_TYPE_LANGUAGEFILE) || res.getType().equals(Constants.RESOURCE_TYPE_PRINTTEMPLATE)) {
                jarOutputStream.putNextEntry(new ZipEntry(res.getName()));
                jarOutputStream.write(res.getData());
                jarOutputStream.closeEntry();
            }
        }
        jarOutputStream.close();
        resp.setContentType("application/java-archive");
        resp.setDateHeader("Last-Modified", System.currentTimeMillis());
        jarSigner.signJarFile(customCodeJarData.toByteArray(), resp.getOutputStream());
        resp.setStatus(HttpServletResponse.SC_OK);
    }
