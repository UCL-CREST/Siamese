    @Transactional(readOnly = false)
    public void saveOrUpdateProduct(Product product, File[] doc, String[] docFileName, String[] docContentType) throws IOException {
        logger.info("addOrUpdateProduct()");
        List<Images> imgList = new ArrayList<Images>();
        InputStream in = null;
        OutputStream out = null;
        String saveDirectory = ServletActionContext.getServletContext().getRealPath("common/userfiles/image/");
        if (doc != null && doc.length > 0) {
            File uploadPath = new File(saveDirectory);
            if (!uploadPath.exists()) uploadPath.mkdirs();
            for (int i = 0; i < doc.length; i++) {
                Images img = new Images();
                in = new FileInputStream(doc[i]);
                img.setName(docFileName[i].substring(0, docFileName[i].lastIndexOf(".")));
                img.setRenameAs(docFileName[i]);
                imgList.add(img);
                out = new FileOutputStream(saveDirectory + "/" + img.getRenameAs());
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) out.write(buffer, 0, len);
                out.flush();
            }
        }
        product.setImagesCollection(imgList);
        productDao.saveOrUpdateProduct(product);
        if (null != in) {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (null != out) {
            try {
                out.close();
            } catch (Exception e) {
                logger.info("addOrUpdateProduct() **********" + e.getStackTrace());
                e.printStackTrace();
            }
        }
    }
