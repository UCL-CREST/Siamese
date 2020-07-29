    @SuppressWarnings("unchecked")
    protected Message createMessage(Representation xmlMessage, Session session) throws IOException, AddressException, MessagingException {
        final String representationMessageClassName = getRepresentationMessageClass();
        if (representationMessageClassName == null) {
            return new RepresentationMessage(xmlMessage, session);
        }
        try {
            final Class<? extends RepresentationMessage> representationMessageClass = (Class<? extends RepresentationMessage>) Class.forName(representationMessageClassName);
            return representationMessageClass.getConstructor(Representation.class, Session.class).newInstance(xmlMessage, session);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Unable to create a new instance of " + representationMessageClassName, e);
            return new RepresentationMessage(xmlMessage, session);
        }
    }
