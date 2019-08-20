package fr.triedge.pixis.utils;

import java.io.File;

import javax.xml.bind.JAXBException;

import fr.triedge.pixis.model.Project;

public class Storage {

	public static void storeProject(Project prj, String path) throws JAXBException {
		XmlHelper.storeXml(prj, new File(path));
	}
	
	public static void storeProjectToDefault(Project prj, String projectName) throws JAXBException {
		storeProject(prj, Const.PROJECTS_LOCATION+File.separator+projectName);
	}
	
	public static Project loadProject(File file) throws JAXBException {
		return XmlHelper.loadXml(Project.class, file);
	}
}
