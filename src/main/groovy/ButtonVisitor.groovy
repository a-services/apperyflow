/**
 * Find buttons in page beans
 */
class ButtonVisitor {

    String startPage;

    /** List of button assets on the page */
    List buttons;

    ButtonVisitor(String startPage) {
        this.startPage = startPage;
    }

    void run(children) {
        buttons = []
	    visit(children)
		println "== ${buttons.size()} buttons found"
    }

    /**
	 * Recursive function to find buttons in asset's children beans.
	 */
	void visit(children) {
		if (!children == null) {
			return
		}

		/* Find buttons in this screen
		 */
		def res = children.bean.find { it["@type"] == Const.BEAN_BUTTON }
		if (res != null) {
			println "${startPage} --> ${res.property.componentName}:::_Button"
			buttons << res
		}
		def beans = children.bean
		for (def bean: beans) {
			if (bean.children) {
				visit(bean.children)
			}
		}
	}

}