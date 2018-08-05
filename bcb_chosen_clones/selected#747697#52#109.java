    public static void exportCards(ArrayList<Card> cards, String zipFileName) {
        try {
            FileOutputStream outZipPic = new FileOutputStream(ZIP_PIC_FILE_NAME);
            ZipOutputStream zipOutPic = new ZipOutputStream(outZipPic);
            int numPic = 0;
            ArrayList<Card> newCards = new ArrayList<Card>();
            for (Iterator<Card> it = cards.iterator(); it.hasNext(); ) {
                try {
                    Card card = it.next();
                    Card newCard = new Card();
                    newCard.copy(card);
                    if (card.getImgFront() != null && !card.getImgFront().equals("")) {
                        File ff = new File(card.getImgFront());
                        if (ff.exists()) {
                            numPic++;
                            String newFileName = addFile(ff, zipOutPic, null);
                            newCard.setImgFront(newFileName);
                        }
                    }
                    if (card.getImgBack() != null && !card.getImgBack().equals("")) {
                        File ff = new File(card.getImgBack());
                        if (ff.exists()) {
                            numPic++;
                            String newFileName = addFile(ff, zipOutPic, null);
                            newCard.setImgBack(newFileName);
                        }
                    }
                    newCards.add(newCard);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (numPic > 0) {
                zipOutPic.finish();
                zipOutPic.close();
            }
            outZipPic.close();
            String xml = convertCardsToXML(newCards);
            FileOutputStream outZip = new FileOutputStream(zipFileName);
            ZipOutputStream zipOut = new ZipOutputStream(outZip);
            ZipEntry entry = new ZipEntry(XML_FILE_NAME);
            byte[] xmlBytes = xml.getBytes("UTF-8");
            int size = xmlBytes.length;
            System.out.println("size = " + size);
            zipOut.putNextEntry(entry);
            zipOut.write(xmlBytes);
            zipOut.closeEntry();
            File zippicF = new File(ZIP_PIC_FILE_NAME);
            if (zippicF.exists()) {
                addFile(zippicF, zipOut, ZIP_PIC_FILE_NAME);
            }
            zipOut.finish();
            zipOut.close();
            outZip.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
