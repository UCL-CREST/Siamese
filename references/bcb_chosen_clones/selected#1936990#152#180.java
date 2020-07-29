    private boolean checkHashBack(Facade facade, HttpServletRequest req) {
        String txtTransactionID = req.getParameter("txtTransactionID");
        String txtOrderTotal = req.getParameter("txtOrderTotal");
        String txtShopId = facade.getSystemParameter(GlobalParameter.yellowPayMDMasterShopID);
        String txtArtCurrency = facade.getSystemParameter(GlobalParameter.yellowPayMDCurrency);
        String txtHashBack = req.getParameter("txtHashBack");
        String hashSeed = facade.getSystemParameter(GlobalParameter.yellowPayMDHashSeed);
        String securityValue = txtShopId + txtArtCurrency + txtOrderTotal + hashSeed + txtTransactionID;
        MessageDigest digest;
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(securityValue.getBytes());
            byte[] array = digest.digest();
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < array.length; ++j) {
                int b = array[j] & 0xFF;
                if (b < 0x10) sb.append('0');
                sb.append(Integer.toHexString(b));
            }
            String hash = sb.toString();
            System.out.println("com.eshop.http.servlets.PaymentController.checkHashBack: " + hash + " " + txtHashBack);
            if (txtHashBack.equals(hash)) {
                return true;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }
