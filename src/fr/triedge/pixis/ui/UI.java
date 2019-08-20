package fr.triedge.pixis.ui;

import java.awt.GridLayout;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.triedge.pixis.model.Project;
import fr.triedge.pixis.model.SpriteSheet;
import fr.triedge.pixis.utils.Const;

public class UI {

	public static void queue(Runnable runnable) {
		SwingUtilities.invokeLater(runnable);
	}
	
	public static Project showNewProject() {
		Project project = null;
		JTextField projectName = new JTextField("project1");
		projectName.requestFocus();

		JPanel panel = new JPanel(new GridLayout(0, 2));
		panel.add(new JLabel("Project Name:"));
		panel.add(projectName);

		int result = JOptionPane.showConfirmDialog(null, panel, "New Project",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			project = new Project();
			project.setName(projectName.getText().replace(" ", "_")+Const.EXT_PROJECT);
		}
		return project;
	}
	
	public static SpriteSheet showNewSprite() {
		SpriteSheet sprite = null;
		JTextField textName = new JTextField("sprite1");
		JTextField numCharacterHeight = new JTextField("16");
		JTextField numCharacterWidth = new JTextField("16");

		JPanel panel = new JPanel(new GridLayout(0, 2));
		panel.add(new JLabel("Sprite Name:"));
		panel.add(textName);
		panel.add(new JLabel("Character Height(px):"));
		panel.add(numCharacterHeight);
		panel.add(new JLabel("Character Width(px):"));
		panel.add(numCharacterWidth);

		int result = JOptionPane.showConfirmDialog(null, panel, "New Sprite",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			sprite = new SpriteSheet();
			sprite.setName(textName.getText().replace(" ", "_")+Const.EXT_SPRITE);
			sprite.setCharacterHeight(Integer.valueOf(numCharacterHeight.getText()));
			sprite.setCharacterWidth(Integer.valueOf(numCharacterWidth.getText()));
		}
		return sprite;
	}
	
	public static File showProjectChooser() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter prjFilter = new FileNameExtensionFilter("Project files", "prj");
		chooser.setFileFilter(prjFilter);
		chooser.setCurrentDirectory(new File(Const.PROJECTS_LOCATION));
		int res = chooser.showDialog(null, "Open Project");
		if (res == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		}
		return null;
	}
	
	public static DefaultMutableTreeNode getChildByName(DefaultMutableTreeNode projectNode, String name) {
		int count = projectNode.getChildCount();
		for (int i = 0 ; i < count ; ++i) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)projectNode.getChildAt(i);
			if (node.getUserObject().toString().equals(name))
				return node;
		}
		return null;
	}
	
	public static DefaultMutableTreeNode getProjectFromChild(DefaultMutableTreeNode childNode) {
		if (childNode.getUserObject().toString().endsWith(Const.EXT_PROJECT))
			return childNode;
		else {
			if (childNode.getParent() == null)
				return null;
			else {
				return getProjectFromChild((DefaultMutableTreeNode)childNode.getParent());
			}
		}
	}
	
	public static DefaultMutableTreeNode sortNodes(DefaultMutableTreeNode node) {
	    //sort alphabetically
	    for(int i = 0; i < node.getChildCount() - 1; i++) {
	        DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
	        String nt = child.getUserObject().toString();

	        for(int j = i + 1; j <= node.getChildCount() - 1; j++) {
	            DefaultMutableTreeNode prevNode = (DefaultMutableTreeNode) node.getChildAt(j);
	            String np = prevNode.getUserObject().toString();

	            if(nt.compareToIgnoreCase(np) > 0) {
	                node.insert(child, j);
	                node.insert(prevNode, i);
	            }
	        }
	        if(child.getChildCount() > 0) {
	            sortNodes(child);
	        }
	    }

	    //put folders first - normal on Windows and some flavors of Linux but not on Mac OS X.
	    for(int i = 0; i < node.getChildCount() - 1; i++) {
	        DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
	        for(int j = i + 1; j <= node.getChildCount() - 1; j++) {
	            DefaultMutableTreeNode prevNode = (DefaultMutableTreeNode) node.getChildAt(j);

	            if(!prevNode.isLeaf() && child.isLeaf()) {
	                node.insert(child, j);
	                node.insert(prevNode, i);
	            }
	        }
	    }
	    return node;
	}
}
