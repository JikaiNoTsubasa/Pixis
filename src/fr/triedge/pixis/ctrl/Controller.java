package fr.triedge.pixis.ctrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import fr.triedge.pixis.model.Project;
import fr.triedge.pixis.model.SpriteSheet;
import fr.triedge.pixis.ui.MainWindow;
import fr.triedge.pixis.ui.PixelEditor;
import fr.triedge.pixis.ui.PixisTabListener;
import fr.triedge.pixis.ui.UI;
import fr.triedge.pixis.utils.Const;
import fr.triedge.pixis.utils.Storage;

public class Controller {

	private MainWindow mainWindow;
	private DefaultTreeModel treeModel;
	private static Logger log;
	private ArrayList<Project> projects = new ArrayList<>();
	
	public void init() throws FileNotFoundException, IOException {
		// Configure logging
		configureLogging();
		
		log.debug("Starting initialization...");
		
		
		log.debug("Initialization completed");
	}
	
	public void actionQuit() {
		log.debug("ACTION CALLED: Quit");
		UI.queue(new Runnable() {
			
			@Override
			public void run() {
				getMainWindow().dispose();
			}
		});
		log.debug("ACTION DONE: Quit");
	}
	
	public void actionNewProject() {
		log.debug("ACTION CALLED: New Project");
		Project prj = UI.showNewProject();
		if (prj != null) {
			getProjects().add(prj);
			try {
				prj.save();
				UI.queue(new Runnable() {
					
					@Override
					public void run() {
						DefaultMutableTreeNode prjNode = new DefaultMutableTreeNode(prj.getName());
						DefaultMutableTreeNode spritesNode = new DefaultMutableTreeNode("Sprites");
						prjNode.add(spritesNode);
						DefaultMutableTreeNode root = (DefaultMutableTreeNode)getTreeModel().getRoot();
						root.add(prjNode);
						getTreeModel().nodeStructureChanged(UI.sortNodes(root));
					}
				});
				log.debug("Project created: "+ prj.getName());
			} catch (JAXBException e) {
				log.error("Cannot save project "+prj.getName(),e);
			}
		}else {
			log.warn("Project is null");
		}
		log.debug("ACTION DONE: New Project");
	}
	
	public void actionOpenProject() {
		log.debug("ACTION CALLED: Open Project");
		File file = UI.showProjectChooser();
		if (file.getName().endsWith(Const.EXT_PROJECT)) {
			try {
				Project prj = Storage.loadProject(file);
				getProjects().add(prj);
				UI.queue(new Runnable() {
					
					@Override
					public void run() {
						DefaultMutableTreeNode prjNode = new DefaultMutableTreeNode(prj.getName());
						DefaultMutableTreeNode spNode = new DefaultMutableTreeNode("Sprites");
						prjNode.add(spNode);
						for (SpriteSheet sp : prj.getSprites()) {
							spNode.add(new DefaultMutableTreeNode(sp.getName()));
						}
						DefaultMutableTreeNode root = (DefaultMutableTreeNode)getMainWindow().getTree().getModel().getRoot();
						root.add(prjNode);
						getTreeModel().nodeStructureChanged(UI.sortNodes(root));
					}
				});
			} catch (JAXBException e) {
				log.error("Cannot load project",e);
			}
		}
		log.debug("ACTION DONE: Open Project");
	}
	
	public void actionNewSprite(DefaultMutableTreeNode node) {
		log.debug("ACTION CALLED: New Sprite");
		SpriteSheet sprite = UI.showNewSprite();
		if (sprite != null) {
			DefaultMutableTreeNode projectNode = UI.getProjectFromChild(node);
			DefaultMutableTreeNode targetNode = UI.getChildByName(projectNode, Const.SPRITES);
			String projectName = projectNode.getUserObject().toString();
			Project prj = getProjectByName(projectName);
			prj.getSprites().add(sprite);
			try {
				prj.save();
				UI.queue(new Runnable() {
					
					@Override
					public void run() {
						
						targetNode.add(new DefaultMutableTreeNode(sprite.getName()));
						getTreeModel().nodeStructureChanged(UI.sortNodes(targetNode));
						getMainWindow().getTree().expandPath(new TreePath(targetNode.getPath()));
					}
				});
				log.debug("sprite created: "+ sprite.getName());
			} catch (JAXBException e) {
				log.error("Cannot save project "+prj.getName(),e);
			}
		}else {
			log.warn("Sprite is null");
		}
		log.debug("ACTION DONE: New Sprite");
	}
	
	public void actionDisplaySprite(String projectName, String spriteName) {
		JTabbedPane tabs = getMainWindow().getTabPane();
		int tabCount = tabs.getTabCount();
		for (int i = 0; i < tabCount; ++i) {
			String title = tabs.getTitleAt(i);
			if (title.equals(spriteName)) {
				tabs.setSelectedIndex(i);
				return;
			}
		}
		Project prj = getProjectByName(projectName);
		if (prj == null) {
			log.warn("Tried to open sprite but project is null");
			return;
		}
		SpriteSheet sp = getSpriteByName(prj, spriteName);
		if (sp == null) {
			log.warn("Tried to open sprite but sprite is null");
			return;
		}
		
		PixelEditor editor = new PixelEditor(this,sp);
		editor.build();
		try {
			tabs.addTab(spriteName, new ImageIcon(ImageIO.read(new File("img/icon/o_sprite.png"))), editor);
			tabs.setSelectedComponent(editor);
			tabs.addMouseListener(new PixisTabListener(tabs));
		} catch (IOException e) {
			log.error("Cannot load icon",e);
		}
	}
	
	private void configureLogging() throws FileNotFoundException, IOException {
		// Set configuration file for log4j2
		ConfigurationSource source = new ConfigurationSource(new FileInputStream(Const.CONFIG_LOG_LOCATION));
		Configurator.initialize(null, source);
		log = LogManager.getLogger(Controller.class);
	}
	
	public Project getProjectByName(String name) {
		for(Project p : getProjects())
			if (p.getName().equals(name))
				return p;
		return null;
	}
	
	public SpriteSheet getSpriteByName(Project project, String name) {
		for (SpriteSheet s : project.getSprites())
			if (s.getName().equals(name))
				return s;
		return null;
	}

	public MainWindow getMainWindow() {
		return mainWindow;
	}
	
	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void setTreeModel(DefaultTreeModel model) {
		this.treeModel = model;
	}
	
	public DefaultTreeModel getTreeModel() {
		return this.treeModel;
	}

	public ArrayList<Project> getProjects() {
		return projects;
	}

	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}


}
