    public static PipeID getPipeIDForService(ServiceDescriptor descriptor) {
        PipeID id = null;
        URI uri = descriptor.getUri();
        if (uri != null) {
            try {
                id = (PipeID) IDFactory.fromURI(uri);
            } catch (URISyntaxException e) {
                throw new RuntimeException("Error creating id for pipe " + uri, e);
            }
        }
        if (id == null) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
            }
            String idToHash = descriptor.getName();
            if (descriptor.getHost() != null) {
                idToHash += descriptor.getHost();
            }
            md.update(idToHash.getBytes());
            id = IDFactory.newPipeID(InfrastructurePeerGroupID, md.digest());
        }
        return id;
    }
