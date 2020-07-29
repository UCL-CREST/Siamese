    protected void initFile(String id) throws FormatException, IOException {
        if (currentId != null && (id.equals(currentId) || isUsedFile(id))) return;
        status("Finding HTML companion file");
        if (debug) debug("PerkinElmerReader.initFile(" + id + ")");
        if (!id.toLowerCase().endsWith(".htm")) {
            Location parent = new Location(id).getAbsoluteFile().getParentFile();
            String[] ls = parent.list();
            for (int i = 0; i < ls.length; i++) {
                if (ls[i].toLowerCase().endsWith(".htm")) {
                    id = new Location(parent.getAbsolutePath(), ls[i]).getAbsolutePath();
                    break;
                }
            }
        }
        super.initFile(id);
        allFiles = new Vector();
        Location tempFile = new Location(id).getAbsoluteFile();
        Location workingDir = tempFile.getParentFile();
        if (workingDir == null) workingDir = new Location(".");
        String workingDirPath = workingDir.getPath() + File.separator;
        String[] ls = workingDir.list();
        allFiles.add(id);
        status("Searching for all metadata companion files");
        int cfgPos = -1;
        int anoPos = -1;
        int recPos = -1;
        int timPos = -1;
        int csvPos = -1;
        int zpoPos = -1;
        int htmPos = -1;
        int filesPt = 0;
        files = new String[ls.length];
        String tempFileName = tempFile.getName();
        int dot = tempFileName.lastIndexOf(".");
        String check = dot < 0 ? tempFileName : tempFileName.substring(0, dot);
        String prefix = null;
        for (int i = 0; i < ls.length; i++) {
            int d = ls[i].lastIndexOf(".");
            while (d == -1 && i < ls.length - 1) {
                i++;
                d = ls[i].lastIndexOf(".");
            }
            String s = dot < 0 ? ls[i] : ls[i].substring(0, d);
            String filename = ls[i].toLowerCase();
            if (s.startsWith(check) || check.startsWith(s) || ((prefix != null) && (s.startsWith(prefix)))) {
                if (cfgPos == -1 && filename.endsWith(".cfg")) {
                    cfgPos = i;
                    prefix = ls[i].substring(0, d);
                }
                if (anoPos == -1 && filename.endsWith(".ano")) {
                    anoPos = i;
                    prefix = ls[i].substring(0, d);
                }
                if (recPos == -1 && filename.endsWith(".rec")) {
                    recPos = i;
                    prefix = ls[i].substring(0, d);
                }
                if (timPos == -1 && filename.endsWith(".tim")) {
                    timPos = i;
                    prefix = ls[i].substring(0, d);
                }
                if (csvPos == -1 && filename.endsWith(".csv")) {
                    csvPos = i;
                    prefix = ls[i].substring(0, d);
                }
                if (zpoPos == -1 && filename.endsWith(".zpo")) {
                    zpoPos = i;
                    prefix = ls[i].substring(0, d);
                }
                if (htmPos == -1 && filename.endsWith(".htm")) {
                    htmPos = i;
                    prefix = ls[i].substring(0, d);
                }
                if (filename.endsWith(".tif") || filename.endsWith(".tiff")) {
                    files[filesPt] = workingDirPath + ls[i];
                    filesPt++;
                }
                try {
                    String ext = filename.substring(filename.lastIndexOf(".") + 1);
                    Integer.parseInt(ext);
                    isTiff = false;
                    files[filesPt] = workingDirPath + ls[i];
                    filesPt++;
                } catch (NumberFormatException e) {
                    try {
                        String ext = filename.substring(filename.lastIndexOf(".") + 1);
                        Integer.parseInt(ext, 16);
                        isTiff = false;
                        files[filesPt] = workingDirPath + ls[i];
                        filesPt++;
                    } catch (NumberFormatException exc) {
                        if (debug) trace(exc);
                    }
                }
            }
        }
        String[] tempFiles = files;
        files = new String[filesPt];
        status("Finding image files");
        int extCount = 0;
        Vector foundExts = new Vector();
        for (int i = 0; i < filesPt; i++) {
            String ext = tempFiles[i].substring(tempFiles[i].lastIndexOf(".") + 1);
            if (!foundExts.contains(ext)) {
                extCount++;
                foundExts.add(ext);
            }
        }
        for (int i = 0; i < filesPt; i += extCount) {
            Vector extSet = new Vector();
            for (int j = 0; j < extCount; j++) {
                if (extSet.size() == 0) extSet.add(tempFiles[i + j]); else {
                    String ext = tempFiles[i + j].substring(tempFiles[i + j].lastIndexOf(".") + 1);
                    int extNum = Integer.parseInt(ext, 16);
                    int insert = -1;
                    int pos = 0;
                    while (insert == -1 && pos < extSet.size()) {
                        String posString = (String) extSet.get(pos);
                        posString = posString.substring(posString.lastIndexOf(".") + 1);
                        int posNum = Integer.parseInt(posString, 16);
                        if (extNum < posNum) insert = pos;
                        pos++;
                    }
                    if (insert == -1) extSet.add(tempFiles[i + j]); else extSet.add(insert, tempFiles[i + j]);
                }
            }
            for (int j = 0; j < extCount; j++) {
                files[i + j] = (String) extSet.get(j);
            }
        }
        for (int i = 0; i < files.length; i++) allFiles.add(files[i]);
        core.imageCount[0] = files.length;
        RandomAccessStream read;
        byte[] data;
        StringTokenizer t;
        tiff = new TiffReader[core.imageCount[0]];
        for (int i = 0; i < tiff.length; i++) {
            tiff[i] = new TiffReader();
            if (i > 0) tiff[i].setMetadataCollected(false);
        }
        status("Parsing metadata values");
        if (timPos != -1) {
            tempFile = new Location(workingDir, ls[timPos]);
            allFiles.add(tempFile.getAbsolutePath());
            read = new RandomAccessStream(tempFile.getAbsolutePath());
            data = new byte[(int) tempFile.length()];
            read.read(data);
            t = new StringTokenizer(new String(data));
            int tNum = 0;
            String[] hashKeys = { "Number of Wavelengths/Timepoints", "Zero 1", "Zero 2", "Number of slices", "Extra int", "Calibration Unit", "Pixel Size Y", "Pixel Size X", "Image Width", "Image Length", "Origin X", "SubfileType X", "Dimension Label X", "Origin Y", "SubfileType Y", "Dimension Label Y", "Origin Z", "SubfileType Z", "Dimension Label Z" };
            while (t.hasMoreTokens() && tNum < hashKeys.length) {
                String token = t.nextToken();
                while ((tNum == 1 || tNum == 2) && !token.trim().equals("0")) {
                    tNum++;
                }
                if (tNum == 4) {
                    try {
                        Integer.parseInt(token);
                    } catch (NumberFormatException e) {
                        tNum++;
                    }
                }
                addMeta(hashKeys[tNum], token);
                if (hashKeys[tNum].equals("Image Width")) {
                    core.sizeX[0] = Integer.parseInt(token);
                } else if (hashKeys[tNum].equals("Image Length")) {
                    core.sizeY[0] = Integer.parseInt(token);
                } else if (hashKeys[tNum].equals("Number of slices")) {
                    core.sizeZ[0] = Integer.parseInt(token);
                } else if (hashKeys[tNum].equals("Experiment details:")) details = token; else if (hashKeys[tNum].equals("Z slice space")) sliceSpace = token;
                tNum++;
            }
            read.close();
        }
        if (csvPos != -1) {
            tempFile = new Location(workingDir, ls[csvPos]);
            allFiles.add(tempFile.getAbsolutePath());
            read = new RandomAccessStream(tempFile.getAbsolutePath());
            data = new byte[(int) tempFile.length()];
            read.read(data);
            t = new StringTokenizer(new String(data));
            int tNum = 0;
            String[] hashKeys = { "Calibration Unit", "Pixel Size X", "Pixel Size Y", "Z slice space" };
            int pt = 0;
            while (t.hasMoreTokens()) {
                if (tNum < 7) {
                    t.nextToken();
                } else if ((tNum > 7 && tNum < 12) || (tNum > 12 && tNum < 18) || (tNum > 18 && tNum < 22)) {
                    t.nextToken();
                } else if (pt < hashKeys.length) {
                    String token = t.nextToken();
                    addMeta(hashKeys[pt], token);
                    if (hashKeys[pt].equals("Image Width")) {
                        core.sizeX[0] = Integer.parseInt(token);
                    } else if (hashKeys[pt].equals("Image Length")) {
                        core.sizeY[0] = Integer.parseInt(token);
                    } else if (hashKeys[pt].equals("Number of slices")) {
                        core.sizeZ[0] = Integer.parseInt(token);
                    } else if (hashKeys[pt].equals("Experiment details:")) details = token; else if (hashKeys[pt].equals("Z slice space")) sliceSpace = token;
                    pt++;
                } else {
                    String key = t.nextToken() + t.nextToken();
                    String value = t.nextToken();
                    addMeta(key, value);
                    if (key.equals("Image Width")) {
                        core.sizeX[0] = Integer.parseInt(value);
                    } else if (key.equals("Image Length")) {
                        core.sizeY[0] = Integer.parseInt(value);
                    } else if (key.equals("Number of slices")) {
                        core.sizeZ[0] = Integer.parseInt(value);
                    } else if (key.equals("Experiment details:")) details = value; else if (key.equals("Z slice space")) sliceSpace = value;
                }
                tNum++;
            }
            read.close();
        } else if (zpoPos != -1) {
            tempFile = new Location(workingDir, ls[zpoPos]);
            allFiles.add(tempFile.getAbsolutePath());
            read = new RandomAccessStream(tempFile.getAbsolutePath());
            data = new byte[(int) tempFile.length()];
            read.read(data);
            t = new StringTokenizer(new String(data));
            int tNum = 0;
            while (t.hasMoreTokens()) {
                addMeta("Z slice #" + tNum + " position", t.nextToken());
                tNum++;
            }
            read.close();
        }
        if (htmPos != -1) {
            tempFile = new Location(workingDir, ls[htmPos]);
            allFiles.add(tempFile.getAbsolutePath());
            read = new RandomAccessStream(tempFile.getAbsolutePath());
            data = new byte[(int) tempFile.length()];
            read.read(data);
            String regex = "<p>|</p>|<br>|<hr>|<b>|</b>|<HTML>|<HEAD>|</HTML>|" + "</HEAD>|<h1>|</h1>|<HR>|</body>";
            Class c = String.class;
            String[] tokens = new String[0];
            Throwable th = null;
            try {
                Method split = c.getMethod("split", new Class[] { c });
                tokens = (String[]) split.invoke(new String(data), new Object[] { regex });
            } catch (NoSuchMethodException exc) {
                if (debug) trace(exc);
            } catch (IllegalAccessException exc) {
                if (debug) trace(exc);
            } catch (InvocationTargetException exc) {
                if (debug) trace(exc);
            }
            for (int j = 0; j < tokens.length; j++) {
                if (tokens[j].indexOf("<") != -1) tokens[j] = "";
            }
            for (int j = 0; j < tokens.length - 1; j += 2) {
                if (tokens[j].indexOf("Wavelength") != -1) {
                    addMeta("Camera Data " + tokens[j].charAt(13), tokens[j]);
                    j--;
                } else if (!tokens[j].trim().equals("")) {
                    addMeta(tokens[j].trim(), tokens[j + 1].trim());
                    if (tokens[j].trim().equals("Image Width")) {
                        core.sizeX[0] = Integer.parseInt(tokens[j + 1].trim());
                    } else if (tokens[j].trim().equals("Image Length")) {
                        core.sizeY[0] = Integer.parseInt(tokens[j + 1].trim());
                    } else if (tokens[j].trim().equals("Number of slices")) {
                        core.sizeZ[0] = Integer.parseInt(tokens[j + 1].trim());
                    } else if (tokens[j].trim().equals("Experiment details:")) {
                        details = tokens[j + 1].trim();
                    } else if (tokens[j].trim().equals("Z slice space")) {
                        sliceSpace = tokens[j + 1].trim();
                    }
                }
            }
            read.close();
        } else {
            throw new FormatException("Valid header files not found.");
        }
        String wavelengths = "1";
        if (details != null) {
            t = new StringTokenizer(details);
            int tokenNum = 0;
            boolean foundId = false;
            String prevToken = "";
            while (t.hasMoreTokens()) {
                String token = t.nextToken();
                foundId = token.equals("Wavelengths");
                if (foundId) {
                    wavelengths = prevToken;
                }
                tokenNum++;
                prevToken = token;
            }
        }
        status("Populating metadata");
        core.sizeC[0] = Integer.parseInt(wavelengths);
        core.sizeT[0] = getImageCount() / (core.sizeZ[0] * core.sizeC[0]);
        if (isTiff) {
            tiff[0].setId(files[0]);
            core.pixelType[0] = tiff[0].getPixelType();
        } else {
            RandomAccessStream tmp = new RandomAccessStream(files[0]);
            int bpp = (int) (tmp.length() - 6) / (core.sizeX[0] * core.sizeY[0]);
            tmp.close();
            switch(bpp) {
                case 1:
                case 3:
                    core.pixelType[0] = FormatTools.UINT8;
                    break;
                case 2:
                    core.pixelType[0] = FormatTools.UINT16;
                    break;
                case 4:
                    core.pixelType[0] = FormatTools.UINT32;
                    break;
            }
        }
        core.currentOrder[0] = "XYC";
        if (core.sizeZ[0] <= 0) {
            core.sizeZ[0] = 1;
            core.sizeT[0] = getImageCount() / (core.sizeZ[0] * core.sizeC[0]);
        }
        if (core.sizeC[0] <= 0) {
            core.sizeC[0] = 1;
            core.sizeT[0] = getImageCount() / (core.sizeZ[0] * core.sizeC[0]);
        }
        if (core.sizeT[0] <= 0) core.sizeT[0] = 1;
        if (sliceSpace != null) {
            core.currentOrder[0] += "TZ";
        } else core.currentOrder[0] += "ZT";
        core.rgb[0] = isTiff ? tiff[0].isRGB() : false;
        core.interleaved[0] = false;
        core.littleEndian[0] = isTiff ? tiff[0].isLittleEndian() : true;
        core.metadataComplete[0] = true;
        core.indexed[0] = isTiff ? tiff[0].isIndexed() : false;
        core.falseColor[0] = false;
        MetadataStore store = getMetadataStore();
        String pixelSizeX = (String) getMeta("Pixel Size X");
        String pixelSizeY = (String) getMeta("Pixel Size Y");
        store.setDimensions(pixelSizeX == null ? null : new Float(pixelSizeX), pixelSizeY == null ? null : new Float(pixelSizeY), null, null, null, null);
        String time = (String) getMeta("Finish Time:");
        if (time != null) {
            SimpleDateFormat parse = new SimpleDateFormat("HH:mm:ss (MM/dd/yyyy)");
            Date date = parse.parse(time, new ParsePosition(0));
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            time = fmt.format(date);
        }
        store.setImage(currentId, time, null, null);
        FormatTools.populatePixels(store, this);
        String originX = (String) getMeta("Origin X");
        String originY = (String) getMeta("Origin Y");
        String originZ = (String) getMeta("Origin Z");
        try {
            store.setStageLabel(null, originX == null ? null : new Float(originX), originY == null ? null : new Float(originY), originZ == null ? null : new Float(originZ), null);
        } catch (NumberFormatException exc) {
            if (debug) trace(exc);
        }
        for (int i = 0; i < core.sizeC[0]; i++) {
            store.setLogicalChannel(i, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        }
    }
