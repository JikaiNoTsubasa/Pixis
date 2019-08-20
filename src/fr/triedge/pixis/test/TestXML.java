package fr.triedge.pixis.test;

import java.io.File;

import javax.xml.bind.JAXBException;

import fr.triedge.pixis.model.Project;
import fr.triedge.pixis.utils.XmlHelper;

public class TestXML {

	public static void main(String[] args) {
		Project prj = new Project();
		prj.setName("Dummy");
		
		try {
			XmlHelper.storeXml(prj, new File("test/prj.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
