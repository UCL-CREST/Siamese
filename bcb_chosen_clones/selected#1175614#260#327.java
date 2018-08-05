    public static void addImageDB(String pictogramsPath, String pictogramToAddPath, String language, String type, String word) {
        try {
            Class.forName("org.sqlite.JDBC");
            String fileName = pictogramsPath + File.separator + G.databaseName;
            File dataBase = new File(fileName);
            if (!dataBase.exists()) {
                JOptionPane.showMessageDialog(null, "No se encuentra el fichero DB", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int idL = 0, idT = 0;
                G.conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
                Statement stat = G.conn.createStatement();
                ResultSet rs = stat.executeQuery("select id from language where name=\"" + language + "\"");
                while (rs.next()) {
                    idL = rs.getInt("id");
                }
                rs.close();
                stat.close();
                stat = G.conn.createStatement();
                rs = stat.executeQuery("select id from type where name=\"" + type + "\"");
                while (rs.next()) {
                    idT = rs.getInt("id");
                }
                rs.close();
                stat.close();
                String id = pictogramToAddPath.substring(pictogramToAddPath.lastIndexOf(File.separator) + 1, pictogramToAddPath.length());
                String idOrig = id;
                String pathSrc = pictogramToAddPath;
                String pathDst = pictogramsPath + File.separator + id.substring(0, 1).toUpperCase() + File.separator;
                String folder = pictogramsPath + File.separator + id.substring(0, 1).toUpperCase();
                String pathDstTmp = pathDst.concat(id);
                String idTmp = id;
                File testFile = new File(pathDstTmp);
                int cont = 1;
                while (testFile.exists()) {
                    idTmp = id.substring(0, id.lastIndexOf('.')) + '_' + cont + id.substring(id.lastIndexOf('.'), id.length());
                    pathDstTmp = pathDst + idTmp;
                    testFile = new File(pathDstTmp);
                    cont++;
                }
                pathDst = pathDstTmp;
                id = idTmp;
                File newDirectoryFolder = new File(folder);
                if (!newDirectoryFolder.exists()) {
                    newDirectoryFolder.mkdirs();
                }
                try {
                    FileChannel srcChannel = new FileInputStream(pathSrc).getChannel();
                    FileChannel dstChannel = new FileOutputStream(pathDst).getChannel();
                    dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                    srcChannel.close();
                    dstChannel.close();
                } catch (IOException exc) {
                    System.out.println(exc.toString());
                }
                PreparedStatement stmt = G.conn.prepareStatement("INSERT OR IGNORE INTO main (word, idL, idT, name, nameNN) VALUES (?,?,?,?,?)");
                stmt.setString(1, word.toLowerCase());
                stmt.setInt(2, idL);
                stmt.setInt(3, idT);
                stmt.setString(4, id);
                stmt.setString(5, idOrig);
                stmt.executeUpdate();
                stmt.close();
                G.conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
