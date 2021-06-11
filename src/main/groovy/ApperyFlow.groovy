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

    String startPage;
	def startPageBean;

	void processBackupFolder(String backupFolder, String startPage) {

		/* Parse XML metadata
		 */
		projectFolder = new File(backupFolder,'project').path
        xml = new XmlParser().parse(new FileReader(projectFolder + '/metadata.xml'))

        /* If start page not specified, find default routiing,
		   otherwise try to find bean with this name.
		 */
		if (startPage == null) {
			startPage = findDefaultRouting()
		}
		startPageBean = findBean(startPage)
		if (startPageBean == null) {
			println "[ERROR] Page not found: " + startPage
			System.exit(1);
		}
		startPage = startPageBean.@name
		println "Start page: $startPage | ${startPageBean.@id}"
		this.startPage = startPageBean.@name
        def screenBeans = new JsonSlurper().parseText(new File(projectFolder, startPageBean.@id).text)

		new ButtonVisitor(startPage).run(screenBeans.bean.children)
		//new BeanVisitor(startPage).collectTypes(screenBeans.bean.children)
	}

    /**
	 * Find bean with given name in assets,
	 * ignore case.
     */
    def findBean(String name) {
		return xml.asset.find {
			return it.@type == Const.TYPE_BEAN &&
			       it.@name.toLowerCase() == name.toLowerCase()
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