    protected int authenticate(long companyId, String login, String password, String authType, Map headerMap, Map parameterMap) throws PortalException, SystemException {
        login = login.trim().toLowerCase();
        long userId = GetterUtil.getLong(login);
        if (authType.equals(CompanyImpl.AUTH_TYPE_EA)) {
            if (!Validator.isEmailAddress(login)) {
                throw new UserEmailAddressException();
            }
        } else if (authType.equals(CompanyImpl.AUTH_TYPE_SN)) {
            if (Validator.isNull(login)) {
                throw new UserScreenNameException();
            }
        } else if (authType.equals(CompanyImpl.AUTH_TYPE_ID)) {
            if (Validator.isNull(login)) {
                throw new UserIdException();
            }
        }
        if (Validator.isNull(password)) {
            throw new UserPasswordException(UserPasswordException.PASSWORD_INVALID);
        }
        int authResult = Authenticator.FAILURE;
        String[] authPipelinePre = PropsUtil.getArray(PropsUtil.AUTH_PIPELINE_PRE);
        if (authType.equals(CompanyImpl.AUTH_TYPE_EA)) {
            authResult = AuthPipeline.authenticateByEmailAddress(authPipelinePre, companyId, login, password, headerMap, parameterMap);
        } else if (authType.equals(CompanyImpl.AUTH_TYPE_SN)) {
            authResult = AuthPipeline.authenticateByScreenName(authPipelinePre, companyId, login, password, headerMap, parameterMap);
        } else if (authType.equals(CompanyImpl.AUTH_TYPE_ID)) {
            authResult = AuthPipeline.authenticateByUserId(authPipelinePre, companyId, userId, password, headerMap, parameterMap);
        }
        User user = null;
        try {
            if (authType.equals(CompanyImpl.AUTH_TYPE_EA)) {
                user = UserUtil.findByC_EA(companyId, login);
            } else if (authType.equals(CompanyImpl.AUTH_TYPE_SN)) {
                user = UserUtil.findByC_SN(companyId, login);
            } else if (authType.equals(CompanyImpl.AUTH_TYPE_ID)) {
                user = UserUtil.findByC_U(companyId, GetterUtil.getLong(login));
            }
        } catch (NoSuchUserException nsue) {
            return Authenticator.DNE;
        }
        if (user.isDefaultUser()) {
            _log.error("The default user should never be allowed to authenticate");
            return Authenticator.DNE;
        }
        if (!user.isPasswordEncrypted()) {
            user.setPassword(PwdEncryptor.encrypt(user.getPassword()));
            user.setPasswordEncrypted(true);
            UserUtil.update(user);
        }
        checkLockout(user);
        checkPasswordExpired(user);
        if (authResult == Authenticator.SUCCESS) {
            if (GetterUtil.getBoolean(PropsUtil.get(PropsUtil.AUTH_PIPELINE_ENABLE_LIFERAY_CHECK))) {
                String encPwd = PwdEncryptor.encrypt(password, user.getPassword());
                if (user.getPassword().equals(encPwd)) {
                    authResult = Authenticator.SUCCESS;
                } else if (GetterUtil.getBoolean(PropsUtil.get(PropsUtil.AUTH_MAC_ALLOW))) {
                    try {
                        MessageDigest digester = MessageDigest.getInstance(PropsUtil.get(PropsUtil.AUTH_MAC_ALGORITHM));
                        digester.update(login.getBytes("UTF8"));
                        String shardKey = PropsUtil.get(PropsUtil.AUTH_MAC_SHARED_KEY);
                        encPwd = Base64.encode(digester.digest(shardKey.getBytes("UTF8")));
                        if (password.equals(encPwd)) {
                            authResult = Authenticator.SUCCESS;
                        } else {
                            authResult = Authenticator.FAILURE;
                        }
                    } catch (NoSuchAlgorithmException nsae) {
                        throw new SystemException(nsae);
                    } catch (UnsupportedEncodingException uee) {
                        throw new SystemException(uee);
                    }
                } else {
                    authResult = Authenticator.FAILURE;
                }
            }
        }
        if (authResult == Authenticator.SUCCESS) {
            String[] authPipelinePost = PropsUtil.getArray(PropsUtil.AUTH_PIPELINE_POST);
            if (authType.equals(CompanyImpl.AUTH_TYPE_EA)) {
                authResult = AuthPipeline.authenticateByEmailAddress(authPipelinePost, companyId, login, password, headerMap, parameterMap);
            } else if (authType.equals(CompanyImpl.AUTH_TYPE_SN)) {
                authResult = AuthPipeline.authenticateByScreenName(authPipelinePost, companyId, login, password, headerMap, parameterMap);
            } else if (authType.equals(CompanyImpl.AUTH_TYPE_ID)) {
                authResult = AuthPipeline.authenticateByUserId(authPipelinePost, companyId, userId, password, headerMap, parameterMap);
            }
        }
        if (authResult == Authenticator.FAILURE) {
            try {
                String[] authFailure = PropsUtil.getArray(PropsUtil.AUTH_FAILURE);
                if (authType.equals(CompanyImpl.AUTH_TYPE_EA)) {
                    AuthPipeline.onFailureByEmailAddress(authFailure, companyId, login, headerMap, parameterMap);
                } else if (authType.equals(CompanyImpl.AUTH_TYPE_SN)) {
                    AuthPipeline.onFailureByScreenName(authFailure, companyId, login, headerMap, parameterMap);
                } else if (authType.equals(CompanyImpl.AUTH_TYPE_ID)) {
                    AuthPipeline.onFailureByUserId(authFailure, companyId, userId, headerMap, parameterMap);
                }
                if (!PortalLDAPUtil.isPasswordPolicyEnabled(user.getCompanyId())) {
                    PasswordPolicy passwordPolicy = user.getPasswordPolicy();
                    int failedLoginAttempts = user.getFailedLoginAttempts();
                    int maxFailures = passwordPolicy.getMaxFailure();
                    if ((failedLoginAttempts >= maxFailures) && (maxFailures != 0)) {
                        String[] authMaxFailures = PropsUtil.getArray(PropsUtil.AUTH_MAX_FAILURES);
                        if (authType.equals(CompanyImpl.AUTH_TYPE_EA)) {
                            AuthPipeline.onMaxFailuresByEmailAddress(authMaxFailures, companyId, login, headerMap, parameterMap);
                        } else if (authType.equals(CompanyImpl.AUTH_TYPE_SN)) {
                            AuthPipeline.onMaxFailuresByScreenName(authMaxFailures, companyId, login, headerMap, parameterMap);
                        } else if (authType.equals(CompanyImpl.AUTH_TYPE_ID)) {
                            AuthPipeline.onMaxFailuresByUserId(authMaxFailures, companyId, userId, headerMap, parameterMap);
                        }
                    }
                }
            } catch (Exception e) {
                _log.error(e, e);
            }
        }
        return authResult;
    }
