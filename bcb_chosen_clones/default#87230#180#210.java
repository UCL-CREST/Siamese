        void createStarImportScope() throws Exception {
            log("createStarImportScope");
            PackageSymbol pkg = new PackageSymbol(names.fromString("pkg"), symtab.rootPackage);
            Method importAll;
            try {
                Class<?> c = Class.forName("com.sun.tools.javac.code.Scope$StarImportScope");
                Constructor ctor = c.getDeclaredConstructor(new Class[] { Symbol.class });
                importAll = c.getDeclaredMethod("importAll", new Class[] { Scope.class });
                starImportScope = (Scope) ctor.newInstance(new Object[] { pkg });
            } catch (ClassNotFoundException e) {
                starImportScope = new ImportScope(pkg);
                importAll = null;
            }
            starImportModel = new Model();
            for (Symbol imp : imports) {
                Scope members = imp.members();
                if (importAll != null) {
                    importAll.invoke(starImportScope, members);
                } else {
                    Scope fromScope = members;
                    Scope toScope = starImportScope;
                    for (Scope.Entry e = fromScope.elems; e != null; e = e.sibling) {
                        if (e.sym.kind == TYP && !toScope.includes(e.sym)) toScope.enter(e.sym, fromScope);
                    }
                }
                for (Scope.Entry e = members.elems; e != null; e = e.sibling) {
                    starImportModel.enter(e.sym);
                }
            }
            starImportModel.check(starImportScope);
        }
