    public String generateOODocument(Integer formLetterId, Integer libraryId, Hashtable parameters) {
        String generatedFileAbsolutePath = "";
        try {
            boolean flag1 = true;
            int countFile1 = 0;
            String fileValid = "";
            while (flag1) {
                fileValid = NewGenLibRoot.getRoot() + ResourceBundle.getBundle("server").getString("Reports");
                fileValid += "/" + System.currentTimeMillis() + "_" + countFile1 + ".sxw";
                File thisDirec = new File(fileValid);
                if (thisDirec.exists()) {
                    countFile1++;
                    continue;
                } else {
                    flag1 = false;
                    break;
                }
            }
            int formLetterIdInt = formLetterId.intValue();
            String fileName = "";
            switch(formLetterIdInt) {
                case 46:
                    {
                        fileName = NewGenLibRoot.getRoot() + ResourceBundle.getBundle("server").getString("TemplatesPath") + "/LIB_" + libraryId.toString() + "/" + "46_FirmOrderPurchase.sxw";
                        break;
                    }
            }
            BufferedInputStream is = null;
            ZipEntry entry;
            JarFile zipfile = new JarFile(fileName);
            Enumeration e = zipfile.entries();
            String subTempDirectory = "";
            boolean flag = true;
            int countFile = 0;
            while (flag) {
                String direcValid = NewGenLibRoot.getRoot() + ResourceBundle.getBundle("server").getString("NewGenLibFiles") + "/Temp";
                direcValid += "/" + System.currentTimeMillis() + "_" + countFile;
                File thisDirec = new File(direcValid);
                if (thisDirec.exists()) {
                    countFile++;
                    continue;
                } else {
                    subTempDirectory = direcValid;
                    String forpic = direcValid + "/Pictures";
                    direcValid += "/META-INF";
                    File thisDirecWithMeta = new File(direcValid);
                    thisDirecWithMeta.mkdirs();
                    thisDirecWithMeta = new File(forpic);
                    thisDirecWithMeta.mkdirs();
                    flag = false;
                    break;
                }
            }
            while (e.hasMoreElements()) {
                entry = (ZipEntry) e.nextElement();
                is = new BufferedInputStream(zipfile.getInputStream(entry));
                int count;
                byte data[] = new byte[BUFFER];
                File tempDirectory = new File(NewGenLibRoot.getRoot() + ResourceBundle.getBundle("server").getString("NewGenLibFiles") + "/Temp");
                if (!tempDirectory.exists()) tempDirectory.mkdirs();
                FileOutputStream fos = new FileOutputStream(subTempDirectory + "/" + entry.getName());
                BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = is.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
                is.close();
            }
            Object[] obx = new Object[parameters.size()];
            Enumeration enumParam = parameters.keys();
            while (enumParam.hasMoreElements()) {
                String key = enumParam.nextElement().toString();
                Object val = parameters.get(key);
                if (val.getClass().getName().equals("java.util.Hashtable")) {
                    Hashtable htMain = (Hashtable) val;
                    String[] headerValues = null;
                    Integer[] columnSizes = null;
                    Vector vecData = null;
                    if (htMain.get("Header") != null) {
                        headerValues = (String[]) htMain.get("Header");
                    }
                    if (htMain.get("ColumnSizes") != null) {
                        columnSizes = (Integer[]) htMain.get("ColumnSizes");
                    }
                    vecData = (Vector) htMain.get("Data");
                    Namespace ns = Namespace.getNamespace("table", "todelete");
                    Namespace nstext = Namespace.getNamespace("text", "todelete");
                    Element rootele = new Element("table", ns);
                    rootele.setAttribute("name", "newgenlibTable", ns);
                    rootele.setAttribute("style-name", "newgenlibTable", ns);
                    String[] rowdataone = (String[]) vecData.elementAt(0);
                    Element tablecolumn = new Element("table-column", ns);
                    tablecolumn.setAttribute("style-name", "newgenlibTable", ns);
                    tablecolumn.setAttribute("number-columns-repeated", String.valueOf(rowdataone.length), ns);
                    rootele.addContent(tablecolumn);
                    for (int i = 0; i < vecData.size(); i++) {
                        String[] rowdata = (String[]) vecData.elementAt(i);
                        Element tablerow = new Element("table-row", ns);
                        for (int j = 0; j < rowdata.length; j++) {
                            Element tablecell = new Element("table-cell", ns);
                            tablecell.setAttribute("style-name", "newgenlibTable", ns);
                            tablecell.setAttribute("value-type", "string", ns);
                            Element celltext = new Element("p", nstext);
                            celltext.setAttribute("style-name", "Table Contents", nstext);
                            celltext.setText(rowdata[j]);
                            tablecell.addContent(celltext);
                            tablerow.addContent(tablecell);
                        }
                        rootele.addContent(tablerow);
                    }
                    Document doc = new Document(rootele);
                    XMLOutputter xout = new XMLOutputter();
                    xout.setOmitDeclaration(true);
                    xout.setOmitEncoding(true);
                    String finalstr = xout.outputString(doc);
                    finalstr = finalstr.replaceAll("xmlns=todelete", "");
                    obx[Integer.parseInt(key)] = "</text:p>" + finalstr + "<text:p>";
                } else {
                    obx[Integer.parseInt(key)] = val.toString();
                }
            }
            BufferedInputStream origin = null;
            FileOutputStream dest1 = new FileOutputStream(fileValid);
            JarOutputStream out = new JarOutputStream(new BufferedOutputStream(dest1));
            byte data[] = new byte[BUFFER];
            File f = new File(subTempDirectory);
            String files[] = f.list();
            File[] allfiles = f.listFiles();
            Vector vecfiles = new Vector(1, 1);
            for (int i = 0; i < allfiles.length; i++) {
                if (allfiles[i].isDirectory()) {
                    File[] subfiles = allfiles[i].listFiles();
                    for (int j = 0; j < subfiles.length; j++) {
                        vecfiles.addElement(allfiles[i].getName() + "/" + subfiles[j].getName());
                    }
                } else {
                    vecfiles.addElement(allfiles[i].getName());
                }
            }
            files = new String[vecfiles.size()];
            for (int i = 0; i < vecfiles.size(); i++) {
                files[i] = vecfiles.elementAt(i).toString();
            }
            for (int i = 0; i < files.length; i++) {
                File file = new File(subTempDirectory + "/" + files[i]);
                if (!file.isDirectory()) {
                    FileInputStream fi = new FileInputStream(subTempDirectory + "/" + files[i]);
                    String str = "";
                    if (file.getName().equals("content.xml")) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(fi));
                        while (br.ready()) {
                            str += br.readLine();
                        }
                        br.close();
                        MessageFormat mf = new MessageFormat(str);
                        str = mf.format(str, obx);
                        FileOutputStream fo = new FileOutputStream(subTempDirectory + "/" + files[i]);
                        fo.write(str.getBytes());
                    }
                    origin = new BufferedInputStream(fi, BUFFER);
                    if (!fi.getFD().valid()) {
                        fi.close();
                        fi = new FileInputStream(subTempDirectory + "/" + files[i]);
                        origin = new BufferedInputStream(fi, BUFFER);
                    }
                    JarEntry entryx = new JarEntry(files[i]);
                    out.putNextEntry(entryx);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    origin.close();
                }
            }
            out.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return generatedFileAbsolutePath;
    }
