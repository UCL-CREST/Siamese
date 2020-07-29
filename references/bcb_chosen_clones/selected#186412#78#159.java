    public void send(org.hibernate.Session hsession, Session session, String repositoryName, Vector files, int label, String charset) throws FilesException {
        ByteArrayInputStream bais = null;
        FileOutputStream fos = null;
        try {
            if ((files == null) || (files.size() <= 0)) {
                return;
            }
            if (charset == null) {
                charset = MimeUtility.javaCharset(Charset.defaultCharset().displayName());
            }
            Users user = getUser(hsession, repositoryName);
            Identity identity = getDefaultIdentity(hsession, user);
            InternetAddress _returnPath = new InternetAddress(identity.getIdeEmail(), identity.getIdeName());
            InternetAddress _from = new InternetAddress(identity.getIdeEmail(), identity.getIdeName());
            InternetAddress _replyTo = new InternetAddress(identity.getIdeReplyTo(), identity.getIdeName());
            InternetAddress _to = new InternetAddress(identity.getIdeEmail(), identity.getIdeName());
            for (int i = 0; i < files.size(); i++) {
                MultiPartEmail email = email = new MultiPartEmail();
                email.setCharset(charset);
                if (_from != null) {
                    email.setFrom(_from.getAddress(), _from.getPersonal());
                }
                if (_returnPath != null) {
                    email.addHeader("Return-Path", _returnPath.getAddress());
                    email.addHeader("Errors-To", _returnPath.getAddress());
                    email.addHeader("X-Errors-To", _returnPath.getAddress());
                }
                if (_replyTo != null) {
                    email.addReplyTo(_replyTo.getAddress(), _replyTo.getPersonal());
                }
                if (_to != null) {
                    email.addTo(_to.getAddress(), _to.getPersonal());
                }
                MailPartObj obj = (MailPartObj) files.get(i);
                email.setSubject("Files-System " + obj.getName());
                Date now = new Date();
                email.setSentDate(now);
                File dir = new File(System.getProperty("user.home") + File.separator + "tmp");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File file = new File(dir, obj.getName());
                bais = new ByteArrayInputStream(obj.getAttachent());
                fos = new FileOutputStream(file);
                IOUtils.copy(bais, fos);
                IOUtils.closeQuietly(bais);
                IOUtils.closeQuietly(fos);
                EmailAttachment attachment = new EmailAttachment();
                attachment.setPath(file.getPath());
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setDescription("File Attachment: " + file.getName());
                attachment.setName(file.getName());
                email.attach(attachment);
                String mid = getId();
                email.addHeader(RFC2822Headers.IN_REPLY_TO, "<" + mid + ".JavaMail.duroty@duroty" + ">");
                email.addHeader(RFC2822Headers.REFERENCES, "<" + mid + ".JavaMail.duroty@duroty" + ">");
                email.addHeader("X-DBox", "FILES");
                email.addHeader("X-DRecent", "false");
                email.setMailSession(session);
                email.buildMimeMessage();
                MimeMessage mime = email.getMimeMessage();
                int size = MessageUtilities.getMessageSize(mime);
                if (!controlQuota(hsession, user, size)) {
                    throw new MailException("ErrorMessages.mail.quota.exceded");
                }
                messageable.storeMessage(mid, mime, user);
            }
        } catch (FilesException e) {
            throw e;
        } catch (Exception e) {
            throw new FilesException(e);
        } catch (java.lang.OutOfMemoryError ex) {
            System.gc();
            throw new FilesException(ex);
        } catch (Throwable e) {
            throw new FilesException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
            IOUtils.closeQuietly(bais);
            IOUtils.closeQuietly(fos);
        }
    }
