    public RepositoryStorage getStorage(String storagename) {
        RepositoryStorage out = null;
        if (storagename == null) {
            throw new RuntimeException("Can not get storage when the given nickname is null.");
        }
        if (storagename.equals("")) {
            throw new RuntimeException("Can not get the storage when the given nickname is empty.");
        }
        Class targetclass = determineClassOf(storagename);
        if (targetclass != null) {
            Class[] cnstrargs = new Class[] { RepositoryConnectivity.class, String.class };
            Constructor cnstr = null;
            try {
                cnstr = targetclass.getConstructor(cnstrargs);
                if (cnstr == null) {
                    throw new RuntimeException("Constructor is null.");
                }
            } catch (Exception err) {
                throw new RuntimeException("Problem obtaining constructor for storage.", err);
            }
            try {
                out = (RepositoryStorage) cnstr.newInstance(this, storagename);
            } catch (Exception err) {
                throw new RuntimeException("Unable to construct.", err);
            }
        } else {
            if (storageExists(storagename)) {
                throw new RuntimeException("The storage " + storagename + " was indicated to exist but its class could not be determined.  Database may be in a corrupted state.");
            }
        }
        return (out);
    }
