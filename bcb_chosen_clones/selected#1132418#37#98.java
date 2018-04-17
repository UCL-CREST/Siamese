    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String migration = req.getParameter("migration");
        String direction = req.getParameter("dir");
        String cursor = req.getParameter("cursor");
        String rangestring = req.getParameter("num");
        int range;
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String> res = null;
        if (migration == null || migration.equals("")) {
            resp.setContentType("text/plain");
            resp.getWriter().println("error: no migration class provided");
            return;
        }
        if (direction == null || !direction.equalsIgnoreCase("down")) {
            direction = "up";
        }
        range = setBatch(rangestring);
        Enumeration<?> en = req.getParameterNames();
        while (en.hasMoreElements()) {
            String paramName = (String) en.nextElement();
            if (!paramName.equals("cursor") && !paramName.equals("num") && !paramName.equals("dir") && !paramName.equals("migration")) {
                logger.fine("adding " + paramName + " to params");
                params.put(paramName, req.getParameter(paramName));
            }
        }
        try {
            Class<?>[] classParm = null;
            Object[] objectParm = null;
            Class<?> cl = Class.forName(migration);
            java.lang.reflect.Constructor<?> co = cl.getConstructor(classParm);
            Migration mg = (Migration) co.newInstance(objectParm);
            if (direction.equals("down")) {
                logger.info("migrating down: " + mg);
                res = mg.migrate_down(cursor, range, params);
                cursor = res.get("cursor");
            } else {
                logger.info("migrating up: " + mg);
                res = mg.migrate_up(cursor, range, params);
                cursor = res.get("cursor");
            }
            if (cursor != null) {
                Queue queue = QueueFactory.getDefaultQueue();
                TaskOptions topt = TaskOptions.Builder.withUrl("/migration");
                for (String rkey : res.keySet()) {
                    if (!rkey.equals("cursor")) {
                        topt = topt.param(rkey, "" + res.get(rkey));
                    }
                }
                queue.add(topt.param("cursor", cursor).param("num", "" + range).param("dir", direction).param("migration", migration));
            }
        } catch (ClassNotFoundException e) {
            logger.warning(e.getMessage());
            resp.setContentType("text/plain");
            resp.getWriter().println("error: got a 'class not found' exception for " + migration);
        } catch (DatastoreTimeoutException e) {
            throw e;
        } catch (Exception e) {
            logger.warning(e.getMessage());
            resp.setContentType("text/plain");
            resp.getWriter().println(e.getMessage());
        }
    }
