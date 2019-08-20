package fr.triedge.pixis.ui;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.triedge.pixis.ctrl.Controller;
import fr.triedge.pixis.utils.Const;

public class MainWindow extends JFrame{

	private static final long serialVersionUID = -2521639473225704163L;
	private static Logger log = LogManager.getLogger(MainWindow.class);

	private JMenuBar bar;
	private JMenu menuFile;
	private JMenuItem itemQuit, itemNewProject, itemOpenProject, itemNewSprite;
	private JSplitPane splitPane;
	private JTree tree;
	private JTabbedPane tabPane;
	private JPopupMenu treePopup;

	private Controller controller;

	public MainWindow(Controller controller) {
		setController(controller);
	}

	public void build() {
		setTitle("Pixis");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSplitPane(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT));
		//getSplitPane().setDividerSize(2);
		getSplitPane().setResizeWeight(0.2);
		setContentPane(getSplitPane());
		
		// Setup tree
		setupTree();
		
		// Setup popup
		setupTreePopup();

		setTabPane(new JTabbedPane());
		getSplitPane().add(new JScrollPane(getTree()));
		getSplitPane().add(getTabPane());

		// Setup menu
		setBar(new JMenuBar());

		setMenuFile(new JMenu("File"));

		setItemQuit(new JMenuItem("Quit"));
		getItemQuit().addActionListener(e -> getController().actionQuit());
		setItemNewProject(new JMenuItem("New Project"));
		getItemNewProject().addActionListener(e -> getController().actionNewProject());
		setItemOpenProject(new JMenuItem("Open Project"));
		getItemOpenProject().addActionListener(e -> getController().actionOpenProject());

		getMenuFile().add(getItemNewProject());
		getMenuFile().add(new JSeparator());
		getMenuFile().add(getItemQuit());
		getBar().add(getMenuFile());

		setJMenuBar(getBar());

		// Windows closing
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				getController().actionQuit();
			}
		});
		log.debug("Windows built");
	}
	
	private void setupTreePopup() {
		setTreePopup(new JPopupMenu("Project Menu"));
		JMenuItem itemNewSprite = new JMenuItem("New Sprite");
		getTreePopup().add(itemNewSprite);
	}
	
	public void openTreeMenu(Component c, int x, int y) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)getTree().getLastSelectedPathComponent();
		String name = node.getUserObject().toString();
		// Create dynamic menu
		setTreePopup(new JPopupMenu("Project Menu"));
		if (name.equals(Const.PROJECTS)) {
			JMenuItem itemNewP = new JMenuItem("New Project");
			itemNewP.addActionListener(e -> getController().actionNewProject());
			JMenuItem itemOpenP = new JMenuItem("Open Project");
			itemOpenP.addActionListener(e -> getController().actionOpenProject());
			getTreePopup().add(itemNewP);
			getTreePopup().add(itemOpenP);
		}
		if (name.equals(Const.SPRITES) || name.endsWith(Const.EXT_PROJECT)) {
			JMenuItem itemNewS = new JMenuItem("New Sprite");
			itemNewS.addActionListener(e -> getController().actionNewSprite(node));
			getTreePopup().add(itemNewS);
		}
		getTreePopup().show(c, x, y);
	}

	private void setupTree() {
		// Setup tree http://www.informit.com/articles/article.aspx?p=26327
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(Const.PROJECTS);
		setTree(new JTree(root));
		getController().setTreeModel((DefaultTreeModel)getTree().getModel());
		getTree().setShowsRootHandles(true);
		getTree().setCellRenderer(new PixisTreeRenderer());
		getTree().addMouseListener(new PixisTreeListener(this,getTree(),getController()));
	}
	
	public void doubleClicked() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)getTree().getLastSelectedPathComponent();
		String name = node.getUserObject().toString();
		if (name.endsWith(Const.EXT_SPRITE)) {
			DefaultMutableTreeNode prjNode = UI.getProjectFromChild(node);
			getController().actionDisplaySprite(prjNode.getUserObject().toString(), name);
		}
	}

	public JMenuBar getBar() {
		return bar;
	}

	public void setBar(JMenuBar bar) {
		this.bar = bar;
	}

	public JMenu getMenuFile() {
		return menuFile;
	}

	public void setMenuFile(JMenu menuFile) {
		this.menuFile = menuFile;
	}

	public JMenuItem getItemQuit() {
		return itemQuit;
	}

	public void setItemQuit(JMenuItem itemQuit) {
		this.itemQuit = itemQuit;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public JSplitPane getSplitPane() {
		return splitPane;
	}

	public void setSplitPane(JSplitPane splitPane) {
		this.splitPane = splitPane;
	}

	public JMenuItem getItemNewProject() {
		return itemNewProject;
	}

	public void setItemNewProject(JMenuItem itemNewProject) {
		this.itemNewProject = itemNewProject;
	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}

	public JTabbedPane getTabPane() {
		return tabPane;
	}

	public void setTabPane(JTabbedPane tabPane) {
		this.tabPane = tabPane;
	}

	public JMenuItem getItemOpenProject() {
		return itemOpenProject;
	}

	public void setItemOpenProject(JMenuItem itemOpenProject) {
		this.itemOpenProject = itemOpenProject;
	}

	public JMenuItem getItemNewSprite() {
		return itemNewSprite;
	}

	public void setItemNewSprite(JMenuItem itemNewSprite) {
		this.itemNewSprite = itemNewSprite;
	}

	public JPopupMenu getTreePopup() {
		return treePopup;
	}

	public void setTreePopup(JPopupMenu treePopup) {
		this.treePopup = treePopup;
	}

}
