    public static void main(String args[]) throws Exception {
        File testTorrent = new File("resources/freeculture.Team_6.pdf.torrent");
        TorrentMetainfo meta = TorrentMetainfoImpl.deserialize(new BufferedInputStream(new FileInputStream(testTorrent)));
        if (DEBUG) System.out.println(meta);
        InfoDictionary info = meta.getInfo();
        ByteArrayOutputStream infoBytes = new ByteArrayOutputStream();
        info.serialize(infoBytes);
        byte[] sha1 = Utils.computeHash(infoBytes.toByteArray());
        if (DEBUG) {
            for (byte b : sha1) System.out.print(Integer.toHexString(b) + " ");
            System.out.println();
        }
        File outTorrent = new File("freeculture.tmp.torrent");
        OutputStream out = new FileOutputStream(outTorrent);
        meta.serialize(out);
        out.close();
        TorrentMetainfo meta2 = TorrentMetainfoImpl.deserialize(new ByteArrayInputStream(((ByteArrayOutputStream) meta.serialize(new ByteArrayOutputStream())).toByteArray()));
        if (DEBUG) System.out.println(meta);
        info = meta2.getInfo();
        infoBytes = new ByteArrayOutputStream();
        info.serialize(infoBytes);
        byte[] sha2 = Utils.computeHash(infoBytes.toByteArray());
        if (DEBUG) {
            for (byte b : sha2) System.out.print(Integer.toHexString(b) + " ");
            System.out.println();
        }
        System.out.println("Was the SHA has of the two files the same? " + (ByteBuffer.wrap(sha1).compareTo(ByteBuffer.wrap(sha2)) == 0));
    }
