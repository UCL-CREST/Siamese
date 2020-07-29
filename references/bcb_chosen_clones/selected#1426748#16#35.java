    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        FileResourceManager frm = CommonsTransactionContext.configure(new File("C:/tmp"));
        try {
            frm.start();
        } catch (ResourceManagerSystemException e) {
            throw new RuntimeException(e);
        }
        FileInputStream is = new FileInputStream("C:/Alfresco/WCM_Eval_Guide2.0.pdf");
        CommonsTransactionOutputStream os = new CommonsTransactionOutputStream(new Ownerr());
        IOUtils.copy(is, os);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
        try {
            frm.stop(FileResourceManager.SHUTDOWN_MODE_NORMAL);
        } catch (ResourceManagerSystemException e) {
            throw new RuntimeException(e);
        }
        System.out.println(System.currentTimeMillis() - start);
    }
