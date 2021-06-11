/**
 * Generate flow diagram with page transitions, buttons and backend services
 * from Appery.io Ionic5 project backup.
 */

import groovy.json.*

class ApperyFlow {

    /** Folder with unzipped Appery Ionic5 project backup */
    String projectFolder;

	/** XML metadata */
	def xml;

    /** List of button assets on the page */
    List buttons = [];

    String startPage;
	def startPageBean;

    String TYPE_BEAN = "50";
    String TYPE_ROUTING = "390";

    String BEAN_BUTTON = "Ionic5ButtonBean";

	void processBackupFolder(String backupFolder, String startPage) {
		projectFolder = new File(backupFolder,'project').path
        xml = new XmlParser().parse(new FileReader(projectFolder + '/metadata.xml'))

		if (startPage == null) {
			startPage = findDefaultRouting()
		}
		println "Start page: " + startPage
		this.startPage = startPage

		startPageBean = xml.asset.find { it.@type == TYPE_BEAN && it.@name == startPage }
        def screenBeans = new JsonSlurper().parseText(new File(projectFolder, startPageBean.@id).text)
        visitButtons(screenBeans.bean.children)
		println "== ${buttons.size()} buttons found"
	}

	void visitButtons(children) {
		if (!children == null) {
			return
		}

		/* Find buttons in this screen
		 */
		def res = children.bean.find { it["@type"] == BEAN_BUTTON }
		if (res != null) {
			println "${startPage} --> ${res.property.componentName}:::_Button"
			buttons << res
		}
		def beans = children.bean
		for (def bean: beans) {
			if (bean.children) {
				visitButtons(bean.children)
			}
		}
	}

	String findDefaultRouting() {
		def routing = xml.asset.find { it.@type == TYPE_ROUTING }
		def json = new JsonSlurper().parseText(new File(projectFolder, routing.@id).text)
		String defaultRoute = json.defaultRoute
		def route = json.routes.find { it.guid == defaultRoute }
		return route.name
	}
}