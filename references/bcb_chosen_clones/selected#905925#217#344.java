    @SuppressWarnings("unchecked")
    public ArrayList<GmailContact> getAllContacts() throws GmailException {
        String query = properties.getString("export_page");
        query = query.replace("[RANDOM_INT]", "" + random.nextInt());
        int statusCode = -1;
        GetMethod get = new GetMethod(query);
        if (log.isInfoEnabled()) log.info("getting all contacts ...");
        try {
            statusCode = client.executeMethod(get);
            if (statusCode != 200) throw new GmailException("In contacts export page: Status code expected: 200 -> Status code returned: " + statusCode);
        } catch (HttpException e) {
            throw new GmailException("HttpException in contacts export page:" + e.getMessage());
        } catch (IOException e) {
            throw new GmailException("IOException in contacts export page:" + e.getMessage());
        } finally {
            get.releaseConnection();
        }
        if (log.isTraceEnabled()) log.trace("accessing contacts export page successful...");
        String query_post = properties.getString("outlook_export_page");
        PostMethod post = new PostMethod(query_post);
        post.addRequestHeader("Accept-Encoding", "gzip,deflate");
        post.addRequestHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.8");
        NameValuePair[] data = { new NameValuePair("at", getCookie("GMAIL_AT")), new NameValuePair("ecf", "o"), new NameValuePair("ac", "Export Contacts") };
        post.setRequestBody(data);
        if (log.isTraceEnabled()) log.trace("getting contacts csv file...");
        try {
            statusCode = client.executeMethod(post);
            if (statusCode != 200) throw new GmailException("In csv file post: Status code expected: 200 -> Status code returned: " + statusCode);
            if (log.isTraceEnabled()) log.trace("Gmail: csv charset: " + post.getResponseCharSet());
            GMAIL_OUTPUT_CHARSET = post.getResponseCharSet();
            InputStreamReader isr = new InputStreamReader(new GZIPInputStream(post.getResponseBodyAsStream()), post.getResponseCharSet());
            CSVReader reader = new CSVReader(isr);
            List csvEntries = reader.readAll();
            reader.close();
            ArrayList<GmailContact> contacts = new ArrayList<GmailContact>();
            MessageDigest m = MessageDigest.getInstance("MD5");
            if (log.isTraceEnabled()) log.trace("creating Gmail contacts...");
            for (int i = 1; i < csvEntries.size(); i++) {
                GmailContact contact = new GmailContact();
                String[] value = (String[]) csvEntries.get(i);
                for (int j = 0; j < value.length; j++) {
                    switch(j) {
                        case 0:
                            contact.setName(value[j]);
                            break;
                        case 1:
                            contact.setEmail(value[j]);
                            if (contact.getName() == null) contact.setIdName(value[j]); else contact.setIdName(contact.getName() + value[j]);
                            break;
                        case 2:
                            contact.setNotes(value[j]);
                            break;
                        case 3:
                            contact.setEmail2(value[j]);
                            break;
                        case 4:
                            contact.setEmail3(value[j]);
                            break;
                        case 5:
                            contact.setMobilePhone(value[j]);
                            break;
                        case 6:
                            contact.setPager(value[j]);
                            break;
                        case 7:
                            contact.setCompany(value[j]);
                            break;
                        case 8:
                            contact.setJobTitle(value[j]);
                            break;
                        case 9:
                            contact.setHomePhone(value[j]);
                            break;
                        case 10:
                            contact.setHomePhone2(value[j]);
                            break;
                        case 11:
                            contact.setHomeFax(value[j]);
                            break;
                        case 12:
                            contact.setHomeAddress(value[j]);
                            break;
                        case 13:
                            contact.setBusinessPhone(value[j]);
                            break;
                        case 14:
                            contact.setBusinessPhone2(value[j]);
                            break;
                        case 15:
                            contact.setBusinessFax(value[j]);
                            break;
                        case 16:
                            contact.setBusinessAddress(value[j]);
                            break;
                        case 17:
                            contact.setOtherPhone(value[j]);
                            break;
                        case 18:
                            contact.setOtherFax(value[j]);
                            break;
                        case 19:
                            contact.setOtherAddress(value[j]);
                            break;
                    }
                }
                m.update(contact.toString().getBytes());
                if (log.isTraceEnabled()) log.trace("setting Md5 Hash...");
                contact.setMd5Hash(new BigInteger(m.digest()).toString());
                contacts.add(contact);
            }
            if (log.isTraceEnabled()) log.trace("Mapping contacts uid...");
            Collections.sort(contacts);
            ArrayList<GmailContact> idList = getAllContactsID();
            for (int i = 0; i < idList.size(); i++) {
                contacts.get(i).setId(idList.get(i).getId());
            }
            if (log.isInfoEnabled()) log.info("getting all contacts info successful...");
            return contacts;
        } catch (HttpException e) {
            throw new GmailException("HttpException in csv file post:" + e.getMessage());
        } catch (IOException e) {
            throw new GmailException("IOException in csv file post:" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new GmailException("No such md5 algorithm " + e.getMessage());
        } finally {
            post.releaseConnection();
        }
    }
