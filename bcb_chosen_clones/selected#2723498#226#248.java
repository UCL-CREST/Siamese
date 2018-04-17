    protected void validate(long googcmId, long reservePrice, String description, String category, int days, String status, String title, byte[] imgBytes) throws PortalException, SystemException {
        if (Validator.isNull(description)) throw new AuctionDescriptionException(); else if (Validator.isNull(title)) throw new AuctionTitleException(); else if (Validator.isNull(category)) throw new CategoryIdException();
        if (googcmId < 1000000000l | googcmId > 999999999999999l) throw new AuctionGoogCMIdException();
        long imgMaxSize = 1048576l;
        if ((imgBytes == null) || (imgBytes.length > ((int) imgMaxSize))) throw new AuctionImageSizeException();
        if (days != 3 & days != 7 & days != 10) throw new AuctionEndeDateException();
        if ((reservePrice < 0) || (reservePrice > 10000)) throw new AuctionReservePriceException();
        try {
            URL url = new URL("https://checkout.google.com/api/checkout/v2/checkoutForm/Merchant/" + googcmId);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            boolean sellerExists = true;
            String line;
            while ((line = rd.readLine()) != null) {
                if (line.contains("" + googcmId)) {
                    throw new AuctionGoogCMAccountException();
                }
            }
            rd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
