    private void zipReference(Problem p, String fileName, ReferenceType type, ZipOutputStream out) throws Exception {
        ReferencePersistence referencePersistence = PersistenceManager.getInstance().getReferencePersistence();
        List<Reference> refs = referencePersistence.getProblemReferences(p.getId(), type);
        if (refs.size() == 0) {
            return;
        }
        Reference ref = refs.get(0);
        if (type == ReferenceType.CHECKER_SOURCE || type == ReferenceType.JUDGE_SOLUTION) {
            String contentType = ref.getContentType();
            if (contentType == null) {
                contentType = "cc";
            }
            fileName += "." + contentType;
        }
        out.putNextEntry(new ZipEntry(p.getCode() + "/" + fileName));
        byte[] data = ref.getContent();
        out.write(data);
        out.closeEntry();
    }
