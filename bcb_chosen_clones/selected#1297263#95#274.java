            public void run() {
                m.status.setStatusOn();
                StringBuilder sb_file = new StringBuilder("");
                try {
                    File[] files = layout.getFile().listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isDirectory()) {
                            if (!(files[i].getName().substring(0, 1).equals("."))) copyDir(files[i], new File(directory, files[i].getName()));
                        } else {
                            if (!(files[i].getName().equals("preview.jpg")) && !(files[i].getName().equals("settings.properties")) && !(files[i].getName().equals("index.tmp")) && !(files[i].getName().equals("preview.tmp"))) copyFile(files[i], new File(directory, files[i].getName()));
                        }
                    }
                    FileReader fr = new FileReader(new File(layout.getFile(), "index.tmp"));
                    for (int c; (c = fr.read()) != -1; ) sb_file.append((char) c);
                    fr.close();
                    fr = null;
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                StringBuilder sb_files = new StringBuilder();
                File small = new File(directory, "small");
                File medium = new File(directory, "medium");
                File big = new File(directory, "big");
                try {
                    small.mkdir();
                    if (layout.isMediumCreate()) medium.mkdir();
                    if (layout.isBigCreate()) big.mkdir();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int j = 0;
                int count = 0;
                int page = 0;
                String index = "index." + layout.getPrefix();
                int lastPage = (int) Math.ceil((double) images.length / (double) layout.getMax_pictures_on_site());
                StringBuilder sb_pages = new StringBuilder("");
                for (int i = 0; i < lastPage; i++) {
                    if (i == 0) sb_pages.append("<li id=\"p" + i + "\"><a href=\"index.html\" id=\"ap" + i + "\" class=\"menulink\">" + m.mes.getString("Generator.50") + " " + (i + 1) + "</a></li>\n"); else sb_pages.append("<li id=\"p" + i + "\"><a href=\"index" + i + ".html\" id=\"ap" + i + "\" class=\"menulink\">" + m.mes.getString("Generator.50") + " " + (i + 1) + "</a></li>\n");
                }
                File out_s;
                File out_m;
                File out_b;
                ArrayList<Element> elements = new ArrayList<Element>();
                for (int i = 0; i < images.length; i++) {
                    try {
                        m.jOutputDoc.insertString(m.jOutputDoc.getLength(), m.mes.getString("Generator.10"), m.outputAtr);
                        m.jOutputDoc.insertString(m.jOutputDoc.getLength(), images[i].getName(), m.fileAtr);
                        m.jOutputDoc.insertString(m.jOutputDoc.getLength(), "\t . . ", m.outputAtr);
                        m.text.setCaretPosition(m.jOutputDoc.getLength());
                    } catch (Exception e) {
                        System.out.print(m.mes.getString("Generator.10") + images[i].getName() + "\t . . . ");
                    }
                    j++;
                    count++;
                    out_s = new File(small, images[i].getName());
                    elements.add(new Element(i, images[i], layout.getSmallWidth(), layout.getSmallHeight(), small));
                    try {
                        m.jOutputDoc.insertString(m.jOutputDoc.getLength(), ". ", m.outputAtr);
                        m.text.setCaretPosition(m.jOutputDoc.getLength());
                    } catch (Exception e) {
                        System.out.print(". . .  ");
                    }
                    out_m = new File(medium, images[i].getName());
                    if (layout.isMediumCreate()) {
                        elements.add(new Element(i, images[i], layout.getMediumWidth(), layout.getMediumHeight(), medium));
                    }
                    try {
                        m.jOutputDoc.insertString(m.jOutputDoc.getLength(), ". ", m.outputAtr);
                        m.text.setCaretPosition(m.jOutputDoc.getLength());
                    } catch (Exception e) {
                        System.out.print(". . .  ");
                    }
                    out_b = new File(big, images[i].getName());
                    if (layout.isBigCreate()) {
                        elements.add(new Element(i, images[i], layout.getBigWidth(), layout.getBigHeight(), big));
                    }
                    if (layout.isMediumCreate()) {
                        if (i == 0) {
                            createPreview(i, medium, out_m.getName(), out_b.getName(), false, true);
                        } else if (i == (images.length - 1)) {
                            createPreview(i, medium, out_m.getName(), out_b.getName(), true, false);
                        } else {
                            createPreview(i, medium, out_m.getName(), out_b.getName(), true, true);
                        }
                    }
                    try {
                        m.jOutputDoc.insertString(m.jOutputDoc.getLength(), ".  ", m.outputAtr);
                        m.text.setCaretPosition(m.jOutputDoc.getLength());
                    } catch (Exception e) {
                        System.out.print(". . .  ");
                    }
                    if (layout.getAProperty() == null) layout.setAProperty("");
                    String titel = createTitle(out_s.getName());
                    String listType = "div";
                    if (layout.getListType().equalsIgnoreCase("list")) {
                        listType = "li";
                    }
                    if (layout.getOnclickSmall().length() > 0) {
                        StringBuffer oc = new StringBuffer(layout.getOnclickSmall());
                        if (oc.indexOf("::inum::") > 0) oc.replace(oc.indexOf("::inum::"), oc.indexOf("::inum::") + 8, "" + i);
                        sb_files.append("      <" + listType + " class=\"picture\" id=\"pi" + i + "\"><a href=\"#\" class=\"imagelink\" id=\"api" + i + "\" onClick=\"" + oc.toString() + "\"  " + layout.getAProperty() + " title=\"" + titel + "\"><img src=\"small/" + out_s.getName() + "\" alt=\"" + titel + "\" /></a></" + listType + ">\n");
                    } else {
                        if (layout.isMediumCreate()) sb_files.append("      <" + listType + " class=\"picture\" id=\"pi" + i + "\"><a class=\"imagelink\" id=\"api" + i + "\" href=\"medium/image" + i + ".html\" " + layout.getAProperty() + " title=\"" + titel + "\"><img src=\"small/" + out_s.getName() + "\" alt=\"" + titel + "\" /></a></" + listType + ">\n"); else sb_files.append("      <" + listType + " class=\"picture\" id=\"pi" + i + "\"><a class=\"imagelink\" id=\"api" + i + "\" href=\"big/" + out_b.getName() + "\" " + layout.getAProperty() + " title=\"" + titel + "\"><img src=\"small/" + out_s.getName() + "\" alt=\"" + titel + "\" /></a></" + listType + ">\n");
                    }
                    try {
                        m.jOutputDoc.insertString(m.jOutputDoc.getLength(), m.mes.getString("Generator.40") + ls, m.readyAtr);
                        m.text.setCaretPosition(m.jOutputDoc.getLength());
                    } catch (Exception e) {
                        System.out.println(m.mes.getString("Generator.40"));
                    }
                    if (layout.getType().equalsIgnoreCase("multiple") && layout.getMax_pictures_on_site() == count) {
                        count = 0;
                        while (sb_file.indexOf("::title::") > 0) sb_file.replace(sb_file.indexOf("::title::"), sb_file.indexOf("::title::") + 9, o.getGallerieTitle());
                        if (layout.isSubTitle()) while (sb_file.indexOf("::subTitle::") > 0) sb_file.replace(sb_file.indexOf("::subTitle::"), sb_file.indexOf("::subTitle::") + 12, o.getGallerieSubTitle());
                        if (sb_file.indexOf("::pics::") > 0) sb_file.replace(sb_file.indexOf("::pics::"), sb_file.indexOf("::pics::") + 8, sb_files.toString());
                        if (sb_file.indexOf("::pages::") > 0) sb_file.replace(sb_file.indexOf("::pages::"), sb_file.indexOf("::pages::") + 9, sb_pages.toString());
                        if (sb_file.indexOf("::footer::") > 0) sb_file.replace(sb_file.indexOf("::footer::"), sb_file.indexOf("::footer::") + 10, m.mes.getString("Generator.47") + m.mes.getString("Main.0") + " " + m.mes.getString("AboutBox.22") + " " + m.mes.getString("Version") + "</a><br />" + layout.getFooter());
                        if (page > 0) index = "index" + page + "." + layout.getPrefix();
                        sb_files = new StringBuilder("");
                        page++;
                        try {
                            FileOutputStream fos = new FileOutputStream(new File(directory, index));
                            fos.write(sb_file.toString().getBytes());
                            fos.close();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        sb_file = new StringBuilder("");
                        try {
                            FileReader fr = new FileReader(new File(layout.getFile(), "index.tmp"));
                            for (int c; (c = fr.read()) != -1; ) sb_file.append((char) c);
                            fr.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
                if (count > 0) {
                    while (sb_file.indexOf("::title::") > 0) sb_file.replace(sb_file.indexOf("::title::"), sb_file.indexOf("::title::") + 9, o.getGallerieTitle());
                    if (layout.isSubTitle()) while (sb_file.indexOf("::subTitle::") > 0) sb_file.replace(sb_file.indexOf("::subTitle::"), sb_file.indexOf("::subTitle::") + 12, o.getGallerieSubTitle());
                    if (sb_file.indexOf("::pics::") > 0) sb_file.replace(sb_file.indexOf("::pics::"), sb_file.indexOf("::pics::") + 8, sb_files.toString());
                    if (sb_file.indexOf("::pages::") > 0) sb_file.replace(sb_file.indexOf("::pages::"), sb_file.indexOf("::pages::") + 9, sb_pages.toString());
                    if (sb_file.indexOf("::footer::") > 0) sb_file.replace(sb_file.indexOf("::footer::"), sb_file.indexOf("::footer::") + 10, m.mes.getString("Generator.47") + m.mes.getString("Main.0") + " " + m.mes.getString("AboutBox.22") + " " + m.mes.getString("Version") + "</a><br />" + layout.getFooter());
                    if (page > 0) index = "index" + page + "." + layout.getPrefix() + "";
                    try {
                        FileOutputStream fos = new FileOutputStream(new File(directory, index));
                        fos.write(sb_file.toString().getBytes());
                        fos.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                try {
                    m.jOutputDoc.insertString(m.jOutputDoc.getLength(), ls + images.length + m.mes.getString("Generator.44") + o.getOutput_dir_gallerie() + m.mes.getString("Generator.45") + ls, m.readyAtr);
                    m.text.setCaretPosition(m.jOutputDoc.getLength());
                } catch (Exception e) {
                    System.out.println(ls + images.length + m.mes.getString("Generator.46") + ls);
                }
                Element[] els = new Element[elements.size()];
                Producer producer = new Producer(m, elements.toArray(els), "");
                Thread producerThread = new Thread(producer);
                int cpus = Runtime.getRuntime().availableProcessors();
                Thread consumerThreads[] = new Thread[cpus];
                for (int i = 0; i < cpus; i++) {
                    consumerThreads[i] = new Thread(new Consumer(producer, m, false, null));
                }
                producerThread.start();
                for (int i = 0; i < cpus; i++) {
                    consumerThreads[i].start();
                }
                try {
                    producerThread.join();
                    for (int i = 0; i < cpus; i++) {
                        consumerThreads[i].join();
                    }
                } catch (InterruptedException ignore) {
                }
                m.status.setStatusOff();
                m.p_monitor.close();
            }
