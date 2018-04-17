    public void save(String path) throws CantSaveException, InvalidFileException, CantReplaceFileException {
        CRC32 crc = new CRC32();
        File file = new File(path + File.separator + filename);
        FileUtils.isFileValid(file);
        if (isNew) {
            if (FileUtils.fileExists(path, filename)) {
                throw new CantReplaceFileException(FSMessages.getMessage("material.alreadyExists"), "filename");
            }
        }
        if (!isNew && !originalFilename.equalsIgnoreCase(filename)) {
            if (FileUtils.fileExists(path, filename)) {
                throw new CantReplaceFileException(FSMessages.getMessage("material.alreadyExists"), "filename");
            }
        }
        Document doc = new Document();
        doc.addContent(new Comment("This file was created by FlySource. (www.flysource.net)"));
        doc.setRootElement(new Element("FlyTyingMaterial"));
        Element root = doc.getRootElement();
        root.setAttribute("version", FlyShareApp.MATERIAL_VERSION);
        Element el = new Element(NAME_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getName()));
        root.addContent(el);
        el = new Element(CATEGORY_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getCategory()));
        root.addContent(el);
        el = new Element(MANUFACTURER_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getManufacturer()));
        root.addContent(el);
        el = new Element(PART_NUM_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getPartNum()));
        root.addContent(el);
        el = new Element(REFERENCE_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getSupplier()));
        root.addContent(el);
        el = new Element(NOTES_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getNotes()));
        root.addContent(el);
        el = new Element(SIZE_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getSize()));
        root.addContent(el);
        el = new Element(COLOR_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getColor()));
        root.addContent(el);
        el = new Element(DATE_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getDate()));
        root.addContent(el);
        el = new Element(QTY_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getQty()));
        root.addContent(el);
        el = new Element(COST_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getCost()));
        root.addContent(el);
        el = new Element(SHAREABLE_ELEMENT);
        el.setText(isShareable() ? "Yes" : "No");
        root.addContent(el);
        crc.update(FlyShareApp.MATERIAL_VERSION.getBytes());
        crc.update(name.getBytes());
        crc.update(category.getBytes());
        if (manufacturer != null) crc.update(manufacturer.getBytes());
        if (notes != null) crc.update(notes.getBytes());
        if (supplier != null) crc.update(supplier.getBytes());
        setHasImage(false);
        if (image != null) {
            try {
                Base64 b64 = new Base64();
                Image i = image.getProcessor().createImage();
                BufferedImage bi = createBufferedImage(i);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bi, "jpg", bos);
                byte[] buffer = bos.toByteArray();
                crc.update(buffer);
                String s = new String(b64.encode(buffer));
                el = new Element(IMAGE_ELEMENT);
                el.setText(s);
                root.addContent(el);
                setHasImage(true);
            } catch (Exception e) {
                LOG.error(e);
            }
        }
        el = new Element(CRC_ELEMENT);
        el.setText("" + crc.getValue());
        root.addContent(el);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setTextMode(Format.TextMode.PRESERVE));
            outputter.output(doc, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new CantSaveException("Cannot save.\n\n" + e.getMessage());
        }
        if (!isNew && !originalFilename.equalsIgnoreCase(filename)) {
            File oldFile = new File(path + File.separator + originalFilename);
            oldFile.delete();
        }
        if (!isNew) setChangeSync(true);
        setSentToServer(false);
    }
