class BeanVisitor {

    Set types;

    String startPage;

    BeanVisitor(String startPage) {
        this.startPage = startPage;
    }

    void collectTypes(children) {
        types = new HashSet();
	    visit(children)
		List sortedTypes = new ArrayList(types).sort();

        int k1 = "Ionic5".length();
        int k2 = "Bean".length();
        sortedTypes.each {
            String c = it.substring(k1, it.length() - k2).toUpperCase();
            println "static String BEAN_${c} = \"$it\""
        }
        println "== ${types.size()} types found"
    }

	void visit(children) {
		if (!children == null) {
			return
		}

		children.bean.each {
            types << it["@type"]
        }

		def beans = children.bean
		for (def bean: beans) {
			if (bean.children) {
				visit(bean.children)
			}
		}
	}
}