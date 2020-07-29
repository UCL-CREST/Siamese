    private void updateUser(AddEditUserForm addform, HttpServletRequest request) throws Exception {
        Session hbsession = HibernateUtil.currentSession();
        try {
            Transaction tx = hbsession.beginTransaction();
            NvUsers user = (NvUsers) hbsession.load(NvUsers.class, addform.getLogin());
            if (!addform.getPassword().equalsIgnoreCase("")) {
                MessageDigest md = (MessageDigest) MessageDigest.getInstance("MD5").clone();
                md.update(addform.getPassword().getBytes("UTF-8"));
                byte[] pd = md.digest();
                StringBuffer app = new StringBuffer();
                for (int i = 0; i < pd.length; i++) {
                    String s2 = Integer.toHexString(pd[i] & 0xFF);
                    app.append((s2.length() == 1) ? "0" + s2 : s2);
                }
                user.setPassword(app.toString());
            }
            ActionErrors errors = new ActionErrors();
            HashMap cAttrs = addform.getCustomAttrs();
            Query q1 = hbsession.createQuery("from org.nodevision.portal.hibernate.om.NvCustomAttrs as a");
            Iterator attrs = q1.iterate();
            HashMap attrInfos = new HashMap();
            while (attrs.hasNext()) {
                NvCustomAttrs element = (NvCustomAttrs) attrs.next();
                attrInfos.put(element.getAttrName(), element.getAttrType());
                NvCustomValuesId id = new NvCustomValuesId();
                id.setNvUsers(user);
                NvCustomValues value = new NvCustomValues();
                id.setNvCustomAttrs(element);
                value.setId(id);
                if (element.getAttrType().equalsIgnoreCase("String")) {
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    ObjectOutputStream serializer = new ObjectOutputStream(bout);
                    serializer.writeObject(cAttrs.get(element.getAttrName()).toString());
                    value.setAttrValue(Hibernate.createBlob(bout.toByteArray()));
                } else if (element.getAttrType().equalsIgnoreCase("Boolean")) {
                    Boolean valueBoolean = Boolean.FALSE;
                    if (cAttrs.get(element.getAttrName()) != null) {
                        valueBoolean = Boolean.TRUE;
                    }
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    ObjectOutputStream serializer = new ObjectOutputStream(bout);
                    serializer.writeObject(valueBoolean);
                    value.setAttrValue(Hibernate.createBlob(bout.toByteArray()));
                } else if (element.getAttrType().equalsIgnoreCase("Date")) {
                    Date date = new Date(0);
                    if (!cAttrs.get(element.getAttrName()).toString().equalsIgnoreCase("")) {
                        String bdate = cAttrs.get(element.getAttrName()).toString();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        date = df.parse(bdate);
                    }
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    ObjectOutputStream serializer = new ObjectOutputStream(bout);
                    serializer.writeObject(date);
                    value.setAttrValue(Hibernate.createBlob(bout.toByteArray()));
                }
                hbsession.saveOrUpdate(value);
                hbsession.flush();
            }
            String bdate = addform.getUser_bdate();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Date parsedDate = df.parse(bdate);
            user.setTimezone(addform.getTimezone());
            user.setLocale(addform.getLocale());
            user.setBdate(new BigDecimal(parsedDate.getTime()));
            user.setGender(addform.getUser_gender());
            user.setEmployer(addform.getEmployer());
            user.setDepartment(addform.getDepartment());
            user.setJobtitle(addform.getJobtitle());
            user.setNamePrefix(addform.getName_prefix());
            user.setNameGiven(addform.getName_given());
            user.setNameFamily(addform.getName_famliy());
            user.setNameMiddle(addform.getName_middle());
            user.setNameSuffix(addform.getName_suffix());
            user.setHomeName(addform.getHome_name());
            user.setHomeStreet(addform.getHome_street());
            user.setHomeStateprov(addform.getHome_stateprov());
            user.setHomePostalcode(addform.getHome_postalcode().equalsIgnoreCase("") ? new Integer(0) : new Integer(addform.getHome_postalcode()));
            user.setHomeOrganization(addform.getHome_organization_name());
            user.setHomeCountry(addform.getHome_country());
            user.setHomeCity(addform.getHome_city());
            user.setHomePhoneIntcode((addform.getHome_phone_intcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_phone_intcode()));
            user.setHomePhoneLoccode((addform.getHome_phone_loccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_phone_loccode()));
            user.setHomePhoneNumber((addform.getHome_phone_number().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_phone_number()));
            user.setHomePhoneExt((addform.getHome_phone_ext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_phone_ext()));
            user.setHomePhoneComment(addform.getHome_phone_commment());
            user.setHomeFaxIntcode((addform.getHome_fax_intcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_fax_intcode()));
            user.setHomeFaxLoccode((addform.getHome_fax_loccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_fax_loccode()));
            user.setHomeFaxNumber((addform.getHome_fax_number().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_fax_number()));
            user.setHomeFaxExt((addform.getHome_fax_ext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_fax_ext()));
            user.setHomeFaxComment(addform.getHome_fax_commment());
            user.setHomeMobileIntcode((addform.getHome_mobile_intcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_mobile_intcode()));
            user.setHomeMobileLoccode((addform.getHome_mobile_loccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_mobile_loccode()));
            user.setHomeMobileNumber((addform.getHome_mobile_number().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_mobile_number()));
            user.setHomeMobileExt((addform.getHome_mobile_ext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_mobile_ext()));
            user.setHomeMobileComment(addform.getHome_mobile_commment());
            user.setHomePagerIntcode((addform.getHome_pager_intcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_pager_intcode()));
            user.setHomePagerLoccode((addform.getHome_pager_loccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_pager_loccode()));
            user.setHomePagerNumber((addform.getHome_pager_number().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_pager_number()));
            user.setHomePagerExt((addform.getHome_pager_ext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_pager_ext()));
            user.setHomePagerComment(addform.getHome_pager_commment());
            user.setHomeUri(addform.getHome_uri());
            user.setHomeEmail(addform.getHome_email());
            user.setBusinessName(addform.getBusiness_name());
            user.setBusinessStreet(addform.getBusiness_street());
            user.setBusinessStateprov(addform.getBusiness_stateprov());
            user.setBusinessPostalcode((addform.getBusiness_postalcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_postalcode()));
            user.setBusinessOrganization(addform.getBusiness_organization_name());
            user.setBusinessCountry(addform.getBusiness_country());
            user.setBusinessCity(addform.getBusiness_city());
            user.setBusinessPhoneIntcode((addform.getBusiness_phone_intcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_phone_intcode()));
            user.setBusinessPhoneLoccode((addform.getBusiness_phone_loccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_phone_loccode()));
            user.setBusinessPhoneNumber((addform.getBusiness_phone_number().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_phone_number()));
            user.setBusinessPhoneExt((addform.getBusiness_phone_ext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_phone_ext()));
            user.setBusinessPhoneComment(addform.getBusiness_phone_commment());
            user.setBusinessFaxIntcode((addform.getBusiness_fax_intcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_fax_intcode()));
            user.setBusinessFaxLoccode((addform.getBusiness_fax_loccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_fax_loccode()));
            user.setBusinessFaxNumber((addform.getBusiness_fax_number().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_fax_number()));
            user.setBusinessFaxExt((addform.getBusiness_fax_ext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_fax_ext()));
            user.setBusinessFaxComment(addform.getBusiness_fax_commment());
            user.setBusinessMobileIntcode((addform.getBusiness_mobile_intcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_mobile_intcode()));
            user.setBusinessMobileLoccode((addform.getBusiness_mobile_loccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_mobile_loccode()));
            user.setBusinessMobileNumber((addform.getBusiness_mobile_number().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_mobile_number()));
            user.setBusinessMobileExt((addform.getBusiness_mobile_ext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_mobile_ext()));
            user.setBusinessMobileComment(addform.getBusiness_mobile_commment());
            user.setBusinessPagerIntcode((addform.getBusiness_pager_intcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_pager_intcode()));
            user.setBusinessPagerLoccode((addform.getBusiness_pager_loccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_pager_loccode()));
            user.setBusinessPagerNumber((addform.getBusiness_pager_number().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_pager_number()));
            user.setBusinessPagerExt((addform.getBusiness_pager_ext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_pager_ext()));
            user.setBusinessPagerComment(addform.getBusiness_pager_commment());
            user.setBusinessUri(addform.getBusiness_uri());
            user.setBusinessEmail(addform.getBusiness_email());
            String hqlDelete = "delete org.nodevision.portal.hibernate.om.NvUserRoles where login = :login";
            int deletedEntities = hbsession.createQuery(hqlDelete).setString("login", user.getLogin()).executeUpdate();
            String[] selectedGroups = addform.getSelectedGroups();
            Set newGroups = new HashSet();
            for (int i = 0; i < selectedGroups.length; i++) {
                NvUserRolesId userroles = new NvUserRolesId();
                userroles.setNvUsers(user);
                userroles.setNvRoles((NvRoles) hbsession.load(NvRoles.class, selectedGroups[i]));
                NvUserRoles newRole = new NvUserRoles();
                newRole.setId(userroles);
                newGroups.add(newRole);
            }
            user.setSetOfNvUserRoles(newGroups);
            hbsession.update(user);
            hbsession.flush();
            if (!hbsession.connection().getAutoCommit()) {
                tx.commit();
            }
        } finally {
            HibernateUtil.closeSession();
        }
    }
