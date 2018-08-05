    private GmailContact convertContactToGmailContact(Contact contact) throws GmailManagerException {
        boolean homePhone = false, homePhone2 = false, homeFax = false, homeMobile = false, homePager = false;
        boolean businessPhone = false, businessPhone2 = false, businessFax = false, businessMobile = false, businessPager = false;
        boolean otherPhone = false, otherFax = false;
        if (log.isTraceEnabled()) log.trace("Converting Foundation contact to Gmail contact: Name:" + contact.getName().getFirstName().getPropertyValueAsString());
        try {
            GmailContact gmailContact = new GmailContact();
            gmailContact.setId(contact.getUid());
            Name name = contact.getName();
            if (name != null) if (name.getFirstName() != null && name.getFirstName().getPropertyValueAsString() != null) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(name.getFirstName().getPropertyValueAsString()).append(" ");
                if (name.getMiddleName() != null && name.getMiddleName().getPropertyValueAsString() != null) buffer.append(name.getMiddleName().getPropertyValueAsString()).append(" ");
                if (name.getLastName() != null && name.getLastName().getPropertyValueAsString() != null) buffer.append(name.getLastName().getPropertyValueAsString()).append(" ");
                if (log.isDebugEnabled()) log.debug("NAME: " + buffer.toString().trim());
                gmailContact.setName(buffer.toString().trim());
            }
            if (contact.getPersonalDetail() != null) {
                if (contact.getPersonalDetail().getEmails() != null && contact.getPersonalDetail().getEmails().size() > 0) {
                    if (contact.getPersonalDetail().getEmails().get(0) != null) {
                        Email email1 = (Email) contact.getPersonalDetail().getEmails().get(0);
                        if (email1.getPropertyValueAsString() != null && email1.getPropertyValueAsString().equals("") == false) {
                            if (log.isDebugEnabled()) log.debug("EMAIL1: " + email1.getPropertyValueAsString());
                            gmailContact.setEmail(email1.getPropertyValueAsString());
                        }
                    }
                    if (contact.getPersonalDetail().getEmails().size() > 1 && contact.getPersonalDetail().getEmails().get(1) != null) {
                        Email email2 = (Email) contact.getPersonalDetail().getEmails().get(1);
                        if (email2.getPropertyValueAsString() != null && email2.getPropertyValueAsString().equals("") == false) {
                            if (log.isDebugEnabled()) log.debug("EMAIL2: " + email2.getPropertyValueAsString());
                            gmailContact.setEmail2(email2.getPropertyValueAsString());
                        }
                    }
                }
                Address address = contact.getPersonalDetail().getAddress();
                if (address != null) if (address.getStreet() != null) if (address.getStreet().getPropertyValueAsString() != null) {
                    StringBuffer addressBuffer = new StringBuffer();
                    addressBuffer.append(address.getStreet().getPropertyValueAsString()).append(" ");
                    addressBuffer.append(address.getPostalCode().getPropertyValueAsString()).append(" ");
                    addressBuffer.append(address.getCity().getPropertyValueAsString()).append(" ");
                    addressBuffer.append(address.getState().getPropertyValueAsString()).append(" ");
                    addressBuffer.append(address.getCountry().getPropertyValueAsString());
                    if (log.isDebugEnabled()) log.debug("HOME_ADDRESS: " + addressBuffer.toString());
                    gmailContact.setHomeAddress(addressBuffer.toString());
                }
                Address addressOther = contact.getPersonalDetail().getOtherAddress();
                if (addressOther != null) if (addressOther.getStreet() != null) if (addressOther.getStreet().getPropertyValueAsString() != null) {
                    StringBuffer addressBuffer = new StringBuffer();
                    addressBuffer.append(addressOther.getStreet().getPropertyValueAsString()).append(" ");
                    addressBuffer.append(addressOther.getPostalCode().getPropertyValueAsString()).append(" ");
                    addressBuffer.append(addressOther.getCity().getPropertyValueAsString()).append(" ");
                    addressBuffer.append(addressOther.getState().getPropertyValueAsString()).append(" ");
                    addressBuffer.append(addressOther.getCountry().getPropertyValueAsString());
                    if (log.isDebugEnabled()) log.debug("OTHER_ADDRESS: " + addressBuffer.toString());
                    gmailContact.setOtherAddress(addressBuffer.toString());
                }
                if (contact.getPersonalDetail().getPhones() != null && contact.getPersonalDetail().getPhones().size() > 0) {
                    for (int i = 0; i < contact.getPersonalDetail().getPhones().size(); i++) {
                        Phone phone = (Phone) contact.getPersonalDetail().getPhones().get(i);
                        if (log.isDebugEnabled()) log.debug("PERSONAL_PHONE: " + phone.getPropertyValueAsString() + " type:" + phone.getPhoneType());
                        if (phone.getPhoneType().equals(SIFC.HOME_TELEPHONE_NUMBER) && homePhone == false) {
                            gmailContact.setHomePhone(phone.getPropertyValueAsString());
                            homePhone = true;
                        } else if (phone.getPhoneType().equals(SIFC.HOME2_TELEPHONE_NUMBER) && homePhone2 == false) {
                            gmailContact.setHomePhone2(phone.getPropertyValueAsString());
                            homePhone2 = true;
                        } else if (phone.getPhoneType().equals(SIFC.HOME_FAX_NUMBER) && homeFax == false) {
                            gmailContact.setHomeFax(phone.getPropertyValueAsString());
                            homeFax = true;
                        } else if ((phone.getPhoneType().equals(SIFC.MOBILE_TELEPHONE_NUMBER) || phone.getPhoneType().equals(SIFC.MOBILE_HOME_TELEPHONE_NUMBER)) && homeMobile == false) {
                            gmailContact.setMobilePhone(phone.getPropertyValueAsString());
                            homeMobile = true;
                        } else if (phone.getPhoneType().equals(SIFC.PAGER_NUMBER) && homePager == false) {
                            gmailContact.setPager(phone.getPropertyValueAsString());
                            homePager = true;
                        } else if (phone.getPhoneType().equals(SIFC.OTHER_TELEPHONE_NUMBER) && otherPhone == false) {
                            gmailContact.setOtherPhone(phone.getPropertyValueAsString());
                            otherPhone = true;
                        } else if (phone.getPhoneType().equals(SIFC.OTHER_FAX_NUMBER) && otherFax == false) {
                            gmailContact.setOtherFax(phone.getPropertyValueAsString());
                            otherFax = true;
                        } else {
                            if (log.isDebugEnabled()) log.debug("GOOGLE - Whoops - Personal Phones UNKNOWN TYPE:" + phone.getPhoneType() + " VALUE:" + phone.getPropertyValueAsString());
                        }
                    }
                }
            }
            if (contact.getBusinessDetail() != null) {
                if (contact.getBusinessDetail().getEmails() != null && contact.getBusinessDetail().getEmails().size() > 0) {
                    if (contact.getBusinessDetail().getEmails().get(0) != null) {
                        Email email3 = (Email) contact.getBusinessDetail().getEmails().get(0);
                        if (email3.getPropertyValueAsString() != null && email3.getPropertyValueAsString().equals("") == false) {
                            if (log.isDebugEnabled()) log.debug("EMAIL3: " + email3.getPropertyValueAsString());
                            gmailContact.setEmail3(email3.getPropertyValueAsString());
                        }
                    }
                }
                Address address = contact.getBusinessDetail().getAddress();
                if (address != null) if (address.getStreet() != null) if (address.getStreet().getPropertyValueAsString() != null) {
                    StringBuffer addressBuffer = new StringBuffer();
                    addressBuffer.append(address.getStreet().getPropertyValueAsString()).append(" ");
                    addressBuffer.append(address.getPostalCode().getPropertyValueAsString()).append(" ");
                    addressBuffer.append(address.getCity().getPropertyValueAsString()).append(" ");
                    addressBuffer.append(address.getState().getPropertyValueAsString()).append(" ");
                    addressBuffer.append(address.getCountry().getPropertyValueAsString());
                    if (log.isDebugEnabled()) log.debug("BUSINESS_ADDRESS: " + addressBuffer.toString());
                    gmailContact.setBusinessAddress(addressBuffer.toString());
                }
                if (contact.getBusinessDetail().getPhones() != null && contact.getBusinessDetail().getPhones().size() > 0) {
                    for (int i = 0; i < contact.getBusinessDetail().getPhones().size(); i++) {
                        Phone phone = (Phone) contact.getBusinessDetail().getPhones().get(i);
                        if (log.isDebugEnabled()) log.debug("BUSINESS_PHONE: " + phone.getPropertyValueAsString() + " type:" + phone.getPhoneType());
                        if (phone.getPhoneType().equals(SIFC.BUSINESS_TELEPHONE_NUMBER) && businessPhone == false) {
                            gmailContact.setBusinessPhone(phone.getPropertyValueAsString());
                            businessPhone = true;
                        } else if (phone.getPhoneType().equals(SIFC.BUSINESS2_TELEPHONE_NUMBER) && businessPhone2 == false) {
                            gmailContact.setBusinessPhone2(phone.getPropertyValueAsString());
                            businessPhone2 = true;
                        } else if (phone.getPhoneType().equals(SIFC.BUSINESS_FAX_NUMBER) && businessFax == false) {
                            gmailContact.setBusinessFax(phone.getPropertyValueAsString());
                            businessFax = true;
                        } else if (phone.getPhoneType().equals(SIFC.MOBILE_BUSINESS_TELEPHONE_NUMBER) && homeMobile == false && businessMobile == false) {
                            gmailContact.setMobilePhone(phone.getPropertyValueAsString());
                            businessMobile = true;
                        } else if (phone.getPhoneType().equals(SIFC.PAGER_NUMBER) && homePager == false && businessPager == false) {
                            gmailContact.setPager(phone.getPropertyValueAsString());
                            businessPager = true;
                        } else {
                            if (log.isDebugEnabled()) log.debug("GOOGLE - Whoops - Business Phones UNKNOWN TYPE:" + phone.getPhoneType() + " VALUE:" + phone.getPropertyValueAsString());
                        }
                    }
                }
                if (contact.getBusinessDetail().getCompany() != null) if (contact.getBusinessDetail().getCompany().getPropertyValueAsString() != null) {
                    if (log.isDebugEnabled()) log.debug("COMPANY: " + contact.getBusinessDetail().getCompany().getPropertyValueAsString());
                    gmailContact.setCompany(contact.getBusinessDetail().getCompany().getPropertyValueAsString());
                }
                if (contact.getBusinessDetail().getTitles() != null && contact.getBusinessDetail().getTitles().size() > 0) {
                    if (contact.getBusinessDetail().getTitles().get(0) != null) {
                        Title title = (Title) contact.getBusinessDetail().getTitles().get(0);
                        if (log.isDebugEnabled()) log.debug("TITLE: " + title.getPropertyValueAsString());
                        gmailContact.setJobTitle(title.getPropertyValueAsString());
                    }
                }
            }
            if (contact.getNotes() != null && contact.getNotes().size() > 0) {
                if (contact.getNotes().get(0) != null) {
                    Note notes = (Note) contact.getNotes().get(0);
                    if (notes.getPropertyValueAsString() != null && notes.getPropertyValueAsString().equals("") == false) {
                        if (log.isDebugEnabled()) log.debug("NOTES: " + notes.getPropertyValueAsString());
                        gmailContact.setNotes(notes.getPropertyValueAsString());
                    }
                }
            }
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(contact.toString().getBytes());
            gmailContact.setMd5Hash(new BigInteger(m.digest()).toString());
            return gmailContact;
        } catch (Exception e) {
            throw new GmailManagerException("GOOGLE Gmail - convertContactToGmailContact error: " + e.getMessage());
        }
    }
