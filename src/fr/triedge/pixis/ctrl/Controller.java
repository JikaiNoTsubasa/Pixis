package fr.triedge.pixis.ctrl;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import fr.triedge.pixis.model.Map;
import fr.triedge.pixis.model.Palette;
import fr.triedge.pixis.model.Project;
import fr.triedge.pixis.model.SpriteSheet;
import fr.triedge.pixis.ui.MainWindow;
import fr.triedge.pixis.ui.Node;
import fr.triedge.pixis.ui.NodeType;
import fr.triedge.pixis.ui.SpriteEditor;
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
						Node prjNode = UI.createProjectNode(prj);
						Node root = (Node)getTreeModel().getRoot();
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
						Node prjNode = UI.createProjectNode(prj);
						Node root = (Node)getMainWindow().getTree().getModel().getRoot();
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

	public void actionNewSprite(Node node) {
		log.debug("ACTION CALLED: New Sprite");
		SpriteSheet sprite = UI.showNewSprite();
		if (sprite != null) {
			// Get current project and it's corresponding node
			Node projectNode = UI.getProjectFromChild(node);
			String projectName = projectNode.getUserObject().toString();
			Project prj = getProjectByName(projectName);
			// Add sprite to project
			prj.getSprites().add(sprite);

			// Create node
			Node spriteNode = new Node(sprite.getName(), NodeType.SPRITE, NodeType.SPRITE);
			placeNodeInTree(projectNode, spriteNode);
		}else {
			log.warn("Sprite is null");
		}
		log.debug("ACTION DONE: New Sprite");
	}

	public void actionNewMap(Node node) {
		log.debug("ACTION CALLED: New Map");
		Map map = UI.showNewMap();
		if (map != null) {
			// Get current project and it's corresponding node
			Node projectNode = UI.getProjectFromChild(node);
			String projectName = projectNode.getUserObject().toString();
			Project prj = getProjectByName(projectName);
			// Add sprite to project
			prj.getMaps().add(map);

			// Create node
			Node mapNode = new Node(map.getName(), NodeType.MAP, NodeType.MAP);
			placeNodeInTree(projectNode, mapNode);
		}else {
			log.warn("Map is null");
		}
		log.debug("ACTION DONE: New Map");
	}

	public void actionNewPalette(Node node) {
		log.debug("ACTION CALLED: New Palette");
		Palette pal = UI.showNewPalette();
		if (pal != null) {
			// Get current project and it's corresponding node
			Node projectNode = UI.getProjectFromChild(node);
			String projectName = projectNode.getUserObject().toString();
			Project prj = getProjectByName(projectName);
			// Add sprite to project
			prj.getPalettes().add(pal);

			// Create node
			Node paletteNode = new Node(pal.getName(), NodeType.PALETTE, NodeType.PALETTE);
			placeNodeInTree(projectNode, paletteNode);
		}else {
			log.warn("Palette is null");
		}
		log.debug("ACTION DONE: New Palette");
	}

	public void actionNewNode(Node node, NodeType type) {
		log.debug("ACTION CALLED: New Node of type: "+type);
		// Get current project and it's corresponding node
		Node projectNode = UI.getProjectFromChild(node);
		String projectName = projectNode.getUserObject().toString();
		Project prj = getProjectByName(projectName);
		Node createdNode = null;
		
		switch(type){
		case PALETTE:
			Palette pal = UI.showNewPalette();
			if (pal == null)
				return;
			prj.getPalettes().add(pal);
			createdNode = new Node(pal.getName(), NodeType.PALETTE, NodeType.PALETTE);
			break;
		case SPRITE:
			SpriteSheet sheet = UI.showNewSprite();
			if (sheet == null)
				return;
			prj.getSprites().add(sheet);
			createdNode = new Node(sheet.getName(), NodeType.SPRITE, NodeType.SPRITE);
			break;
		case MAP:
			Map map = UI.showNewMap();
			if (map == null)
				return;
			prj.getMaps().add(map);
			createdNode = new Node(map.getName(), NodeType.MAP, NodeType.MAP);
			break;
		default:
			break;
			
		}
		try {
			prj.save();
		} catch (JAXBException e) {
			log.error("Cannot save project",e);
		}
		placeNodeInTree(projectNode, createdNode);
		log.debug("ACTION DONE: New Node of type: "+type);
	}

	public void placeNodeInTree(Node projectNode, Node node) {
		UI.queue(new Runnable() {

			@Override
			public void run() {
				Node targetNode = UI.placeNode(projectNode, node);
				getTreeModel().nodeStructureChanged(UI.sortNodes(targetNode));
				getMainWindow().getTree().expandPath(new TreePath(targetNode.getPath()));
			}
		});
	}

	public void actionDisplaySpriteSheet(String projectName, String spriteName) {
		JTabbedPane tabs = getMainWindow().getTabPane();
		// Check if tab already exists
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
		SpriteSheet sp = getSpriteSheetByName(prj, spriteName);
		if (sp == null) {
			log.warn("Tried to open sprite but sprite is null");
			return;
		}
		
		SpriteEditor editor = new SpriteEditor(this,sp,prj);
		editor.build();
		try {
			tabs.addTab(spriteName, new ImageIcon(ImageIO.read(new File("img/icon/o_male.png"))), editor);
			tabs.setSelectedComponent(editor);
		} catch (IOException e) {
			log.error("Cannot load icon",e);
		}
	}
	
	public void actionDisplayPalette(String projectName, String paletteName) {
		Project prj = getProjectByName(projectName);
		Palette pal = getPaletteByName(prj, paletteName);
		Color res = JColorChooser.showDialog(null, "Add color to "+paletteName, Color.white);
		pal.getColors().add(res.getRGB());
		UI.info("Color added to Palette");
		try {
			prj.save();
		} catch (JAXBException e) {
			UI.error("Cannot save project", e);
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
	
	public Palette getPaletteByName(Project project, String name) {
		for(Palette p : project.getPalettes())
			if (p.getName().equals(name))
				return p;
		return null;
	}

	public SpriteSheet getSpriteSheetByName(Project project, String name) {
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
