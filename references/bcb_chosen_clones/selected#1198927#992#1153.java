    private void processCommandLine(String[] args) throws Exception {
        String xmlFile = null;
        List<File> files = new ArrayList<File>();
        Element root = null;
        Element entryNode = null;
        Document doc = null;
        doc = XmlUtil.makeDocument();
        root = XmlUtil.create(doc, TAG_ENTRIES, null, new String[] {});
        int entryCnt = 0;
        for (int i = 3; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals(CMD_FETCH)) {
                if (i >= args.length - 1) {
                    usage("Bad argument: " + arg);
                }
                if (i >= args.length - 2) {
                    usage("Bad argument: " + arg);
                }
                File f = writeFile(args[i + 1], new File(args[i + 2]));
                System.err.println("Wrote file to:" + f);
                return;
            }
            if (arg.equals(CMD_PRINT)) {
                if (i >= args.length - 1) {
                    usage("Bad argument: " + arg);
                }
                printEntry(args[i + 1]);
                return;
            }
            if (arg.equals(CMD_PRINTXML)) {
                if (i >= args.length - 1) {
                    usage("Bad argument: " + arg);
                }
                printEntryXml(args[i + 1]);
                return;
            }
            if (arg.equals(CMD_IMPORT)) {
                assertArgs(arg, i, args.length, 2);
                importFile(new File(args[i + 1]), args[i + 2]);
                return;
            }
            if (arg.equals(CMD_DEBUG)) {
                System.out.println(XmlUtil.toString(root));
            } else if (arg.equals(CMD_EXIT)) {
                return;
            } else if (arg.equals(CMD_FOLDER)) {
                if (i == args.length) {
                    usage("Bad argument: " + arg);
                }
                entryCnt++;
                String name = args[++i];
                String parentId = args[++i];
                entryNode = makeGroupNode(root, parentId, name);
            } else if (arg.equals(CMD_FILE)) {
                if (i >= args.length - 2) {
                    usage("Bad argument: " + arg);
                }
                String name = args[++i];
                File f = new File(args[++i]);
                if (!f.exists()) {
                    usage("File does not exist:" + f);
                }
                if (name.length() == 0) {
                    name = IOUtil.getFileTail(f.toString());
                }
                String parentId = args[++i];
                entryCnt++;
                entryNode = makeEntryNode(root, name, parentId);
                entryNode.setAttribute(ATTR_FILE, IOUtil.getFileTail(f.toString()));
                files.add(f);
            } else if (arg.equals("-localfile")) {
                if (i == args.length) {
                    usage("Bad argument: " + arg);
                }
                i++;
                File f = new File(args[i]);
                entryCnt++;
                entryNode = makeEntryNode(root, IOUtil.getFileTail(args[i]));
                entryNode.setAttribute(ATTR_LOCALFILE, f.getPath());
            } else if (arg.equals("-localfiletomove")) {
                if (i == args.length) {
                    usage("Bad -localfiletomove argument");
                }
                i++;
                File f = new File(args[i]);
                entryCnt++;
                entryNode = makeEntryNode(root, IOUtil.getFileTail(args[i]));
                entryNode.setAttribute(ATTR_LOCALFILETOMOVE, f.getPath());
            } else if (arg.equals("-description")) {
                checkEntryNode(entryNode, "-description");
                if (i == args.length) {
                    usage("Bad -description argument");
                }
                i++;
                Element descNode = XmlUtil.create(doc, TAG_DESCRIPTION, entryNode);
                descNode.appendChild(XmlUtil.makeCDataNode(doc, args[i], false));
            } else if (arg.equals("-addmetadata")) {
                checkEntryNode(entryNode, "-addmetadata");
                entryNode.setAttribute(ATTR_ADDMETADATA, "true");
            } else if (arg.equals("-addshortmetadata")) {
                checkEntryNode(entryNode, "-addshortmetadata");
                entryNode.setAttribute(ATTR_ADDSHORTMETADATA, "true");
            } else if (arg.equals("-attach")) {
                checkEntryNode(entryNode, "-attach");
                if (i == args.length) {
                    usage("Bad -file argument");
                }
                i++;
                File f = new File(args[i]);
                if (!f.exists()) {
                    usage("File does not exist:" + args[i]);
                }
                if (entryNode == null) {
                    usage("Need to specify a -file first");
                }
                addAttachment(entryNode, IOUtil.getFileTail(f.toString()));
                files.add(f);
            } else {
                if (!new File(args[i]).exists()) {
                    usage("Unknown argument:" + args[i]);
                }
                files.add(new File(args[i]));
            }
        }
        if (entryCnt == 0) {
            usage("");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bos);
        if (root != null) {
            String xml = XmlUtil.toString(root);
            System.out.println(xml);
            zos.putNextEntry(new ZipEntry("entries.xml"));
            byte[] bytes = xml.getBytes();
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
        }
        for (File f : files) {
            zos.putNextEntry(new ZipEntry(IOUtil.getFileTail(f.toString())));
            byte[] bytes = IOUtil.readBytes(new FileInputStream(f));
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
        }
        zos.close();
        bos.close();
        List<HttpFormEntry> postEntries = new ArrayList<HttpFormEntry>();
        addUrlArgs(postEntries);
        postEntries.add(new HttpFormEntry(ARG_FILE, "entries.zip", bos.toByteArray()));
        String[] result = doPost(URL_ENTRY_XMLCREATE, postEntries);
        if (result[0] != null) {
            System.err.println("Error:" + result[0]);
            return;
        }
        System.err.println("result:" + result[1]);
        Element response = XmlUtil.getRoot(result[1]);
        String body = XmlUtil.getChildText(response).trim();
        if (responseOk(response)) {
            System.err.println("OK:" + body);
        } else {
            System.err.println("Error:" + body);
        }
    }
