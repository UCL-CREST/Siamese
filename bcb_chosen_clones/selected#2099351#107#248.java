    @SuppressWarnings("unchecked")
    private int syncCustomers() throws RemoteException, BasicException {
        dlintegration.syncCustomersBefore();
        ArrayList<String> notToSync = new ArrayList<String>();
        int step = 0;
        User[] remoteUsers;
        int cpt = 0;
        do {
            remoteUsers = externalsales.getUsersBySteps(step);
            step++;
            if (remoteUsers == null) {
                throw new BasicException(AppLocal.getIntString("message.returnnull") + " > Customers null");
            }
            if (remoteUsers.length > 0) {
                String perms;
                for (User remoteUser : remoteUsers) {
                    if (notToSync.contains(remoteUser.getLogin())) continue;
                    cpt++;
                    String name = externalsales.encodeString((remoteUser.getFirstname().trim() + " " + remoteUser.getLastname()).trim());
                    String firstname = externalsales.encodeString(remoteUser.getFirstname());
                    String lastname = externalsales.encodeString(remoteUser.getLastname());
                    String description = externalsales.encodeString(remoteUser.getDescription());
                    String address = externalsales.encodeString(remoteUser.getAddress());
                    String address2 = externalsales.encodeString(remoteUser.getAddress2());
                    String city = externalsales.encodeString(remoteUser.getCity());
                    String country = externalsales.encodeString(remoteUser.getCountry());
                    String phone = externalsales.encodeString(remoteUser.getPhone());
                    String mobile = externalsales.encodeString(remoteUser.getMobile());
                    String zipcode = externalsales.encodeString(remoteUser.getZipcode());
                    CustomerSync copyCustomer = new CustomerSync(remoteUser.getId());
                    if (firstname == null || firstname.equals("")) firstname = " ";
                    copyCustomer.setFirstname(firstname.toUpperCase());
                    if (lastname == null || lastname.equals("")) lastname = " ";
                    copyCustomer.setLastname(lastname.toUpperCase());
                    copyCustomer.setTaxid(remoteUser.getLogin());
                    copyCustomer.setSearchkey(remoteUser.getLogin() + name.toUpperCase());
                    if (name == null || name.equals("")) name = " ";
                    copyCustomer.setName(name.toUpperCase());
                    if (description == null || description.equals("")) description = " ";
                    copyCustomer.setNotes(description);
                    copyCustomer.setEmail(remoteUser.getEmail());
                    if (address == null || address.equals("")) address = " ";
                    copyCustomer.setAddress(address);
                    if (address2 == null || address2.equals("")) address2 = " ";
                    copyCustomer.setAddress2(address2);
                    if (city == null || city.equals("")) city = "Brussels";
                    copyCustomer.setCity(city);
                    if (country == null || country.equals("")) country = "Belgium";
                    copyCustomer.setCountry(country);
                    copyCustomer.setMaxdebt(10000.0);
                    if (phone == null || phone.equals("")) phone = " ";
                    copyCustomer.setPhone(phone);
                    if (mobile == null || mobile.equals("")) mobile = " ";
                    copyCustomer.setPhone2(mobile);
                    if (zipcode == null || zipcode.equals("")) zipcode = " ";
                    copyCustomer.setPostal(zipcode);
                    if (TicketInfo.isWS() && TicketInfo.getPayID() == 2 && remoteUser.getEmail().contains("@DONOTSENDME")) {
                        notToSync.add(copyCustomer.getTaxid());
                        continue;
                    }
                    dlintegration.syncCustomer(copyCustomer);
                    notToSync.add(copyCustomer.getTaxid());
                }
            }
        } while (remoteUsers.length > 0);
        List<CustomerSync> localList = dlintegration.getCustomers();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (CustomerSync localCustomer : localList) {
            Date now = new Date();
            if (notToSync.contains(localCustomer.getTaxid())) {
                continue;
            }
            cpt++;
            User userAdd = new User();
            userAdd.setLogin(localCustomer.getTaxid());
            userAdd.setId(localCustomer.getTaxid());
            userAdd.setFirstname(" ");
            String tmpName = localCustomer.getName().trim();
            tmpName = tmpName.replace("'", "");
            while (tmpName.charAt(0) == ' ') {
                tmpName = tmpName.substring(1);
            }
            userAdd.setLastname(tmpName);
            char[] pw = new char[8];
            int c = 'A';
            int r1 = 0;
            for (int i = 0; i < 8; i++) {
                r1 = (int) (Math.random() * 3);
                switch(r1) {
                    case 0:
                        c = '0' + (int) (Math.random() * 10);
                        break;
                    case 1:
                        c = 'a' + (int) (Math.random() * 26);
                        break;
                    case 2:
                        c = 'A' + (int) (Math.random() * 26);
                        break;
                }
                pw[i] = (char) c;
            }
            String clave = new String(pw);
            byte[] password = { 00 };
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(clave.getBytes());
                password = md5.digest();
                userAdd.setPassword(password.toString());
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(UsersSync.class.getName()).log(Level.SEVERE, null, ex);
                userAdd.setPassword(clave);
            }
            userAdd.setTitle("M");
            if (localCustomer.getEmail() == null || localCustomer.getEmail().trim().equals("") || localCustomer.getEmail().indexOf('@') <= 0) userAdd.setEmail(localCustomer.getTaxid() + defaultEmail); else userAdd.setEmail(localCustomer.getEmail());
            userAdd.setDescription(localCustomer.getNotes() + "");
            userAdd.setAddress(localCustomer.getAddress() + "");
            userAdd.setAddress2(localCustomer.getAddress2() + "");
            userAdd.setState_region(localCustomer.getRegion() + "");
            userAdd.setCity(localCustomer.getCity() + "");
            userAdd.setCountry(localCustomer.getCountry() + "");
            userAdd.setZipcode(localCustomer.getPostal() + "");
            userAdd.setPhone(localCustomer.getPhone() + "");
            userAdd.setMobile(localCustomer.getPhone2() + "");
            userAdd.setFax(" ");
            try {
                userAdd.setCdate(df.format(localCustomer.getCurdate()));
            } catch (NullPointerException nu) {
                userAdd.setCdate(df.format(now));
            }
            userAdd.setPerms("shopper");
            userAdd.setBank_account_nr("");
            userAdd.setBank_account_holder("");
            userAdd.setBank_account_type("");
            userAdd.setBank_iban("");
            userAdd.setBank_name("");
            userAdd.setBank_sort_code("");
            userAdd.setMdate(df.format(now));
            userAdd.setShopper_group_id("1");
            externalsales.addUser(userAdd);
        }
        return cpt;
    }
