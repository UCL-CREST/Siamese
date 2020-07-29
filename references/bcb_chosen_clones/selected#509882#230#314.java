    public XmldbURI createFile(String newName, InputStream is, Long length, String contentType) throws IOException, PermissionDeniedException, CollectionDoesNotExistException {
        if (LOG.isDebugEnabled()) LOG.debug("Create '" + newName + "' in '" + xmldbUri + "'");
        XmldbURI newNameUri = XmldbURI.create(newName);
        MimeType mime = MimeTable.getInstance().getContentTypeFor(newName);
        if (mime == null) {
            mime = MimeType.BINARY_TYPE;
        }
        DBBroker broker = null;
        Collection collection = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        VirtualTempFile vtf = new VirtualTempFile();
        BufferedOutputStream bos = new BufferedOutputStream(vtf);
        IOUtils.copy(bis, bos);
        bis.close();
        bos.close();
        vtf.close();
        if (mime.isXMLType() && vtf.length() == 0L) {
            if (LOG.isDebugEnabled()) LOG.debug("Creating dummy XML file for null resource lock '" + newNameUri + "'");
            vtf = new VirtualTempFile();
            IOUtils.write("<null_resource/>", vtf);
            vtf.close();
        }
        TransactionManager transact = brokerPool.getTransactionManager();
        Txn txn = transact.beginTransaction();
        try {
            broker = brokerPool.get(subject);
            collection = broker.openCollection(xmldbUri, Lock.WRITE_LOCK);
            if (collection == null) {
                LOG.debug("Collection " + xmldbUri + " does not exist");
                transact.abort(txn);
                throw new CollectionDoesNotExistException(xmldbUri + "");
            }
            if (mime.isXMLType()) {
                if (LOG.isDebugEnabled()) LOG.debug("Inserting XML document '" + mime.getName() + "'");
                VirtualTempFileInputSource vtfis = new VirtualTempFileInputSource(vtf);
                IndexInfo info = collection.validateXMLResource(txn, broker, newNameUri, vtfis);
                DocumentImpl doc = info.getDocument();
                doc.getMetadata().setMimeType(mime.getName());
                collection.store(txn, broker, info, vtfis, false);
            } else {
                if (LOG.isDebugEnabled()) LOG.debug("Inserting BINARY document '" + mime.getName() + "'");
                InputStream fis = vtf.getByteStream();
                bis = new BufferedInputStream(fis);
                DocumentImpl doc = collection.addBinaryResource(txn, broker, newNameUri, bis, mime.getName(), length.longValue());
                bis.close();
            }
            transact.commit(txn);
            if (LOG.isDebugEnabled()) LOG.debug("Document created sucessfully");
        } catch (EXistException e) {
            LOG.error(e);
            transact.abort(txn);
            throw new IOException(e);
        } catch (TriggerException e) {
            LOG.error(e);
            transact.abort(txn);
            throw new IOException(e);
        } catch (SAXException e) {
            LOG.error(e);
            transact.abort(txn);
            throw new IOException(e);
        } catch (LockException e) {
            LOG.error(e);
            transact.abort(txn);
            throw new PermissionDeniedException(xmldbUri + "");
        } catch (IOException e) {
            LOG.error(e);
            transact.abort(txn);
            throw e;
        } catch (PermissionDeniedException e) {
            LOG.error(e);
            transact.abort(txn);
            throw e;
        } finally {
            if (vtf != null) {
                vtf.delete();
            }
            if (collection != null) {
                collection.release(Lock.WRITE_LOCK);
            }
            brokerPool.release(broker);
            if (LOG.isDebugEnabled()) LOG.debug("Finished creation");
        }
        XmldbURI newResource = xmldbUri.append(newName);
        return newResource;
    }
