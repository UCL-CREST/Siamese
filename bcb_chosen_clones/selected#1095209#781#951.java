    private void dumpPageStore(String fileName) {
        setDatabaseName(fileName.substring(0, fileName.length() - Constants.SUFFIX_PAGE_FILE.length()));
        PrintWriter writer = null;
        int[] pageTypeCount = new int[Page.TYPE_STREAM_DATA + 2];
        int emptyPages = 0;
        pageDataEmpty = 0;
        pageDataRows = 0;
        pageDataHead = 0;
        try {
            writer = getWriter(fileName, ".sql");
            writer.println("CREATE ALIAS IF NOT EXISTS READ_CLOB FOR \"" + this.getClass().getName() + ".readClob\";");
            writer.println("CREATE ALIAS IF NOT EXISTS READ_BLOB FOR \"" + this.getClass().getName() + ".readBlob\";");
            resetSchema();
            store = FileStore.open(null, fileName, remove ? "rw" : "r");
            long length = store.length();
            try {
                store.init();
            } catch (Exception e) {
                writeError(writer, e);
            }
            Data s = Data.create(this, 128);
            store.seek(0);
            store.readFully(s.getBytes(), 0, 128);
            s.setPos(48);
            pageSize = s.readInt();
            int writeVersion = s.readByte();
            int readVersion = s.readByte();
            writer.println("-- pageSize: " + pageSize + " writeVersion: " + writeVersion + " readVersion: " + readVersion);
            if (pageSize < PageStore.PAGE_SIZE_MIN || pageSize > PageStore.PAGE_SIZE_MAX) {
                pageSize = PageStore.PAGE_SIZE_DEFAULT;
                writer.println("-- ERROR: page size; using " + pageSize);
            }
            int pageCount = (int) (length / pageSize);
            parents = new int[pageCount];
            s = Data.create(this, pageSize);
            for (int i = 3; i < pageCount; i++) {
                s.reset();
                store.seek(i * pageSize);
                store.readFully(s.getBytes(), 0, 32);
                s.readByte();
                s.readShortInt();
                parents[i] = s.readInt();
            }
            int logKey = 0, logFirstTrunkPage = 0, logFirstDataPage = 0;
            for (int i = 1; ; i++) {
                if (i == 3) {
                    break;
                }
                s.reset();
                store.seek(i * pageSize);
                store.readFully(s.getBytes(), 0, pageSize);
                CRC32 crc = new CRC32();
                crc.update(s.getBytes(), 4, pageSize - 4);
                int expected = (int) crc.getValue();
                int got = s.readInt();
                long writeCounter = s.readLong();
                int key = s.readInt();
                int firstTrunkPage = s.readInt();
                int firstDataPage = s.readInt();
                if (expected == got) {
                    logKey = key;
                    logFirstTrunkPage = firstTrunkPage;
                    logFirstDataPage = firstDataPage;
                }
                writer.println("-- head " + i + ": writeCounter: " + writeCounter + " log key: " + key + " trunk: " + firstTrunkPage + "/" + firstDataPage + " crc expected " + expected + " got " + got + " (" + (expected == got ? "ok" : "different") + ")");
            }
            writer.println("-- firstTrunkPage: " + logFirstTrunkPage + " firstDataPage: " + logFirstDataPage);
            s = Data.create(this, pageSize);
            int free = 0;
            for (int page = 3; page < pageCount; page++) {
                s = Data.create(this, pageSize);
                store.seek(page * pageSize);
                store.readFully(s.getBytes(), 0, pageSize);
                int type = s.readByte();
                switch(type) {
                    case Page.TYPE_EMPTY:
                        pageTypeCount[type]++;
                        emptyPages++;
                        continue;
                }
                boolean last = (type & Page.FLAG_LAST) != 0;
                type &= ~Page.FLAG_LAST;
                if (!PageStore.checksumTest(s.getBytes(), page, pageSize)) {
                    writer.println("-- ERROR: page " + page + " checksum mismatch type: " + type);
                }
                s.readShortInt();
                switch(type) {
                    case Page.TYPE_DATA_LEAF:
                        {
                            pageTypeCount[type]++;
                            int parentPageId = s.readInt();
                            setStorage(s.readVarInt());
                            int columnCount = s.readVarInt();
                            int entries = s.readShortInt();
                            writer.println("-- page " + page + ": data leaf " + (last ? "(last)" : "") + " parent: " + parentPageId + " table: " + storageId + " entries: " + entries + " columns: " + columnCount);
                            dumpPageDataLeaf(writer, s, last, page, columnCount, entries);
                            break;
                        }
                    case Page.TYPE_DATA_NODE:
                        {
                            pageTypeCount[type]++;
                            int parentPageId = s.readInt();
                            setStorage(s.readVarInt());
                            s.readInt();
                            int entries = s.readShortInt();
                            writer.println("-- page " + page + ": data node " + (last ? "(last)" : "") + " parent: " + parentPageId + " entries: " + entries);
                            dumpPageDataNode(writer, s, page, entries);
                            break;
                        }
                    case Page.TYPE_DATA_OVERFLOW:
                        pageTypeCount[type]++;
                        writer.println("-- page " + page + ": data overflow " + (last ? "(last)" : ""));
                        break;
                    case Page.TYPE_BTREE_LEAF:
                        {
                            pageTypeCount[type]++;
                            int parentPageId = s.readInt();
                            setStorage(s.readVarInt());
                            int entries = s.readShortInt();
                            writer.println("-- page " + page + ": b-tree leaf " + (last ? "(last)" : "") + " parent: " + parentPageId + " index: " + storageId + " entries: " + entries);
                            if (trace) {
                                dumpPageBtreeLeaf(writer, s, entries, !last);
                            }
                            break;
                        }
                    case Page.TYPE_BTREE_NODE:
                        pageTypeCount[type]++;
                        int parentPageId = s.readInt();
                        setStorage(s.readVarInt());
                        writer.println("-- page " + page + ": b-tree node" + (last ? "(last)" : "") + " parent: " + parentPageId + " index: " + storageId);
                        dumpPageBtreeNode(writer, s, page, !last);
                        break;
                    case Page.TYPE_FREE_LIST:
                        pageTypeCount[type]++;
                        writer.println("-- page " + page + ": free list " + (last ? "(last)" : ""));
                        free += dumpPageFreeList(writer, s, page, pageCount);
                        break;
                    case Page.TYPE_STREAM_TRUNK:
                        pageTypeCount[type]++;
                        writer.println("-- page " + page + ": log trunk");
                        break;
                    case Page.TYPE_STREAM_DATA:
                        pageTypeCount[type]++;
                        writer.println("-- page " + page + ": log data");
                        break;
                    default:
                        writer.println("-- ERROR page " + page + " unknown type " + type);
                        break;
                }
            }
            writeSchema(writer);
            try {
                dumpPageLogStream(writer, logKey, logFirstTrunkPage, logFirstDataPage);
            } catch (EOFException e) {
            }
            writer.println("-- page count: " + pageCount + " empty: " + emptyPages + " free: " + free);
            writer.println("-- page data head: " + pageDataHead + " empty: " + pageDataEmpty + " rows: " + pageDataRows);
            for (int i = 0; i < pageTypeCount.length; i++) {
                int count = pageTypeCount[i];
                if (count > 0) {
                    writer.println("-- page count type: " + i + " " + (100 * count / pageCount) + "% count: " + count);
                }
            }
            writer.close();
        } catch (Throwable e) {
            writeError(writer, e);
        } finally {
            IOUtils.closeSilently(writer);
            closeSilently(store);
        }
    }
