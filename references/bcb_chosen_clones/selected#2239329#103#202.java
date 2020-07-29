    public SukuData exportGedcom(String db, String path, String langCode, int viewId, int surety, int charsetId, boolean includeImages) {
        this.viewId = viewId;
        this.surety = surety;
        switch(charsetId) {
            case 1:
                thisSet = GedSet.Set_Ascii;
                break;
            case 2:
                thisSet = GedSet.Set_Ansel;
                break;
            case 3:
                thisSet = GedSet.Set_Utf8;
                break;
            case 4:
                thisSet = GedSet.Set_Utf16;
                break;
            default:
                thisSet = GedSet.Set_None;
        }
        this.includeImages = includeImages;
        dbName = db;
        units = new LinkedHashMap<Integer, MinimumIndividual>();
        images = new Vector<MinimumImage>();
        families = new LinkedHashMap<String, MinimumFamily>();
        famById = new HashMap<Integer, MinimumFamily>();
        SukuData result = new SukuData();
        if (path == null || path.lastIndexOf(".") < 1) {
            result.resu = "output filename missing";
            return result;
        }
        try {
            collectIndividuals();
            collectFamilies();
            childRids = new HashMap<Integer, Integer>();
            String sql = "select r.pid,n.tag,r.tag,r.surety from relationnotice as n inner join relation  as r on n.rid=r.rid where r.tag in ('FATH','MOTH')";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                int rid = rs.getInt(1);
                childRids.put(rid, rid);
            }
            rs.close();
            stm.close();
            zipPath = path.substring(0, path.lastIndexOf("."));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(bos);
            String fileName = zipPath + "/" + dbName + ".ged";
            ZipEntry entry = new ZipEntry(fileName);
            zip.putNextEntry(entry);
            writeBom(zip);
            writeHead(zip);
            int allCount = units.size();
            int curreCount = 0;
            Set<Map.Entry<Integer, MinimumIndividual>> unitss = units.entrySet();
            Iterator<Map.Entry<Integer, MinimumIndividual>> eex = unitss.iterator();
            while (eex.hasNext()) {
                Map.Entry<Integer, MinimumIndividual> unitx = eex.next();
                MinimumIndividual pit = unitx.getValue();
                curreCount++;
                PersonUtil u = new PersonUtil(con);
                SukuData fam = u.getFullPerson(pit.pid, langCode);
                PersonShortData shortie = new PersonShortData(fam.persLong);
                writeIndi(zip, fam.persLong);
                double prose = (curreCount * 100) / allCount;
                int intprose = (int) prose;
                StringBuilder sbb = new StringBuilder();
                sbb.append(intprose);
                sbb.append(";");
                sbb.append(shortie.getAlfaName());
                setRunnerValue(sbb.toString());
            }
            Set<Map.Entry<String, MinimumFamily>> fss = families.entrySet();
            Iterator<Map.Entry<String, MinimumFamily>> ffx = fss.iterator();
            while (ffx.hasNext()) {
                Map.Entry<String, MinimumFamily> fx = ffx.next();
                MinimumFamily fix = fx.getValue();
                writeFam(zip, fix, langCode);
            }
            zip.write(gedBytes("0 TRLR\r\n"));
            zip.closeEntry();
            for (int i = 0; i < images.size(); i++) {
                entry = new ZipEntry(zipPath + "/" + images.get(i).getPath());
                zip.putNextEntry(entry);
                zip.write(images.get(i).imageData);
                zip.closeEntry();
            }
            zip.close();
            result.buffer = bos.toByteArray();
        } catch (IOException e) {
            result.resu = e.getMessage();
            logger.log(Level.WARNING, "", e);
        } catch (SQLException e) {
            result.resu = e.getMessage();
            logger.log(Level.WARNING, "", e);
        } catch (SukuException e) {
            result.resu = e.getMessage();
            logger.log(Level.WARNING, "", e);
        }
        return result;
    }
