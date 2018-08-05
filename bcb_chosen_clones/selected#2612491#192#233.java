    @Test
    public void testHandleMessageInvalidSignature() throws Exception {
        KeyPair keyPair = MiscTestUtils.generateKeyPair();
        DateTime notBefore = new DateTime();
        DateTime notAfter = notBefore.plusYears(1);
        X509Certificate certificate = MiscTestUtils.generateCertificate(keyPair.getPublic(), "CN=Test", notBefore, notAfter, null, keyPair.getPrivate(), true, 0, null, null);
        ServletConfig mockServletConfig = EasyMock.createMock(ServletConfig.class);
        Map<String, String> httpHeaders = new HashMap<String, String>();
        HttpSession mockHttpSession = EasyMock.createMock(HttpSession.class);
        HttpServletRequest mockServletRequest = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(mockServletConfig.getInitParameter("AuditService")).andStubReturn(null);
        EasyMock.expect(mockServletConfig.getInitParameter("AuditServiceClass")).andStubReturn(AuditTestService.class.getName());
        EasyMock.expect(mockServletConfig.getInitParameter("SignatureService")).andStubReturn(null);
        EasyMock.expect(mockServletConfig.getInitParameter("SignatureServiceClass")).andStubReturn(SignatureTestService.class.getName());
        EasyMock.expect(mockServletRequest.getRemoteAddr()).andStubReturn("remote-address");
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte[] document = "hello world".getBytes();
        byte[] digestValue = messageDigest.digest(document);
        EasyMock.expect(mockHttpSession.getAttribute(SignatureDataMessageHandler.DIGEST_VALUE_SESSION_ATTRIBUTE)).andStubReturn(digestValue);
        EasyMock.expect(mockHttpSession.getAttribute(SignatureDataMessageHandler.DIGEST_ALGO_SESSION_ATTRIBUTE)).andStubReturn("SHA-1");
        SignatureDataMessage message = new SignatureDataMessage();
        message.certificateChain = new LinkedList<X509Certificate>();
        message.certificateChain.add(certificate);
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(keyPair.getPrivate());
        signature.update("foobar-document".getBytes());
        byte[] signatureValue = signature.sign();
        message.signatureValue = signatureValue;
        EasyMock.replay(mockServletConfig, mockHttpSession, mockServletRequest);
        AppletServiceServlet.injectInitParams(mockServletConfig, this.testedInstance);
        this.testedInstance.init(mockServletConfig);
        try {
            this.testedInstance.handleMessage(message, httpHeaders, mockServletRequest, mockHttpSession);
            fail();
        } catch (ServletException e) {
            LOG.debug("expected exception: " + e.getMessage());
            EasyMock.verify(mockServletConfig, mockHttpSession, mockServletRequest);
            assertNull(SignatureTestService.getSignatureValue());
            assertEquals("remote-address", AuditTestService.getAuditSignatureRemoteAddress());
            assertEquals(certificate, AuditTestService.getAuditSignatureClientCertificate());
        }
    }
