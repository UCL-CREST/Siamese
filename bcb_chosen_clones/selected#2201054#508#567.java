    private List<Document> storeDocuments(List<Document> documents) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Document> newDocuments = new ArrayList<Document>();
        try {
            session.beginTransaction();
            Preference preference = new PreferenceModel();
            preference = (Preference) preference.doList(preference).get(0);
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            if (documents != null && !documents.isEmpty()) {
                for (Iterator<Document> iter = documents.iterator(); iter.hasNext(); ) {
                    Document document = iter.next();
                    if (AppConstants.STATUS_ACTIVE.equals(document.getStatus())) {
                        try {
                            document = (Document) preAdd(document, getParams());
                            File fileIn = new File(preference.getScanLocation() + File.separator + document.getName());
                            File fileOut = new File(preference.getStoreLocation() + File.separator + document.getName());
                            FileInputStream in = new FileInputStream(fileIn);
                            FileOutputStream out = new FileOutputStream(fileOut);
                            int c;
                            while ((c = in.read()) != -1) out.write(c);
                            in.close();
                            out.close();
                            document.doAdd(document);
                            boolean isDeleted = fileIn.delete();
                            System.out.println("Deleted scan folder file: " + document.getName() + ":" + isDeleted);
                            if (isDeleted) {
                                document.setStatus(AppConstants.STATUS_PROCESSING);
                                int uploadCount = 0;
                                if (document.getUploadCount() != null) {
                                    uploadCount = document.getUploadCount();
                                }
                                uploadCount++;
                                document.setUploadCount(uploadCount);
                                newDocuments.add(document);
                            }
                        } catch (Exception add_ex) {
                            add_ex.printStackTrace();
                        }
                    } else if (AppConstants.STATUS_PROCESSING.equals(document.getStatus())) {
                        int uploadCount = document.getUploadCount();
                        if (uploadCount < 5) {
                            uploadCount++;
                            document.setUploadCount(uploadCount);
                            System.out.println("increase upload count: " + document.getName() + ":" + uploadCount);
                            newDocuments.add(document);
                        } else {
                            System.out.println("delete from documents list: " + document.getName());
                        }
                    } else if (AppConstants.STATUS_INACTIVE.equals(document.getStatus())) {
                        document.setFixFlag(AppConstants.FLAG_NO);
                        newDocuments.add(document);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return newDocuments;
    }
