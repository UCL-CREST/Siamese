        String digest(final UserAccountEntity account) {
            try {
                final MessageDigest digest = MessageDigest.getInstance("SHA-1");
                digest.reset();
                digest.update(account.getUserId().getBytes("UTF-8"));
                digest.update(account.getLastLogin().toString().getBytes("UTF-8"));
                digest.update(account.getPerson().getGivenName().getBytes("UTF-8"));
                digest.update(account.getPerson().getSurname().getBytes("UTF-8"));
                digest.update(account.getPerson().getEmail().getBytes("UTF-8"));
                digest.update(m_random);
                return new String(Base64.altEncode(digest.digest()));
            } catch (final Exception e) {
                LOG.error("Exception", e);
                throw new RuntimeException(e);
            }
        }
