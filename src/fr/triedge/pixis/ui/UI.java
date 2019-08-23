package fr.triedge.pixis.ui;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import fr.triedge.pixis.model.Map;
import fr.triedge.pixis.model.Palette;
import fr.triedge.pixis.model.Project;
import fr.triedge.pixis.model.Sprite;
import fr.triedge.pixis.model.SpriteLayer;
import fr.triedge.pixis.model.SpriteSheet;
import fr.triedge.pixis.utils.Const;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

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
		SpriteSheet spriteSheet = null;
		JTextField textName = new JTextField("sprite1");
		JTextField numCharacterHeight = new JTextField("32");
		JTextField numCharacterWidth = new JTextField("16");
		ComboImageType[] comboTypes = new ComboImageType[4];
		comboTypes[0] = new ComboImageType("TYPE_INT_RGB", BufferedImage.TYPE_INT_RGB);
		comboTypes[1] = new ComboImageType("TYPE_INT_ARGB", BufferedImage.TYPE_INT_ARGB);
		comboTypes[2] = new ComboImageType("TYPE_4BYTE_ABGR", BufferedImage.TYPE_4BYTE_ABGR);
		comboTypes[3] = new ComboImageType("TYPE_BYTE_GRAY", BufferedImage.TYPE_BYTE_GRAY);
		JComboBox<ComboImageType> comboImageType = new JComboBox<>(comboTypes);
		comboImageType.setSelectedIndex(2);

		JPanel panel = new JPanel(new GridLayout(0, 2));
		panel.add(new JLabel("Sprite Name:"));
		panel.add(textName);
		panel.add(new JLabel("Character Height(px):"));
		panel.add(numCharacterHeight);
		panel.add(new JLabel("Character Width(px):"));
		panel.add(numCharacterWidth);
		panel.add(new JLabel("Image Type:"));
		panel.add(comboImageType);

		int result = JOptionPane.showConfirmDialog(null, panel, "New Sprite",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			spriteSheet = new SpriteSheet();
			spriteSheet.setName(textName.getText().replace(" ", "_")+Const.EXT_SPRITE);
			spriteSheet.setCharacterHeight(Integer.valueOf(numCharacterHeight.getText()));
			spriteSheet.setCharacterWidth(Integer.valueOf(numCharacterWidth.getText()));
			spriteSheet.setImageType(((ComboImageType)comboImageType.getSelectedItem()).value);
			// Create first sprite
			spriteSheet.getLayers().add(new SpriteLayer());
			BufferedImage img = new BufferedImage(spriteSheet.getCharacterWidth(), spriteSheet.getCharacterHeight(), spriteSheet.getImageType());
			Sprite sp = new Sprite();
			spriteSheet.getLayers().get(0).getSprites().add(sp);
			sp.setPosXonSheet(0);
			sp.setPosYonSheet(0);
			try {
				sp.setImageData(UI.imageToBytes(img));
			} catch (IOException e) {
				error("Cannot convert default image to bytes", e);
				return null;
			}
		}
		return spriteSheet;
	}
	
	public static Map showNewMap() {
		Map map = null;
		JTextField textName = new JTextField("map1");
		JTextField numCharacterHeight = new JTextField("50");
		JTextField numCharacterWidth = new JTextField("50");

		JPanel panel = new JPanel(new GridLayout(0, 2));
		panel.add(new JLabel("Map Name:"));
		panel.add(textName);
		panel.add(new JLabel("Map Height(blocs):"));
		panel.add(numCharacterHeight);
		panel.add(new JLabel("Map Width(blocs):"));
		panel.add(numCharacterWidth);

		int result = JOptionPane.showConfirmDialog(null, panel, "New Map",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			map = new Map();
			map.setName(textName.getText().replace(" ", "_")+Const.EXT_MAP);
			map.setHeight(Integer.valueOf(numCharacterHeight.getText()));
			map.setWidth(Integer.valueOf(numCharacterWidth.getText()));
		}
		return map;
	}
	
	public static Palette showNewPalette() {
		Palette palette = null;
		JTextField textName = new JTextField("palette1");

		JPanel panel = new JPanel(new GridLayout(0, 2));
		panel.add(new JLabel("Palette Name:"));
		panel.add(textName);

		int result = JOptionPane.showConfirmDialog(null, panel, "New Palette",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			palette = new Palette();
			palette.setName(textName.getText().replace(" ", "_")+Const.EXT_PALETTE);
		}
		return palette;
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
	
	public static Node getChildByName(Node projectNode, String name) {
		int count = projectNode.getChildCount();
		for (int i = 0 ; i < count ; ++i) {
			Node node = (Node)projectNode.getChildAt(i);
			if (node.getUserObject().toString().equals(name))
				return node;
		}
		return null;
	}
	
	public static Node getChildByAllowedType(Node projectNode, NodeType type) {
		int count = projectNode.getChildCount();
		for (int i = 0 ; i < count ; ++i) {
			Node node = (Node)projectNode.getChildAt(i);
			if (node.allowedChildrenType == type)
				return node;
		}
		return null;
	}
	
	public static Node getProjectFromChild(Node childNode) {
		if (childNode.getUserObject().toString().endsWith(Const.EXT_PROJECT))
			return childNode;
		else {
			if (childNode.getParent() == null)
				return null;
			else {
				return getProjectFromChild((Node)childNode.getParent());
			}
		}
	}
	
	public static Node sortNodes(Node node) {
	    //sort alphabetically
	    for(int i = 0; i < node.getChildCount() - 1; i++) {
	        Node child = (Node) node.getChildAt(i);
	        String nt = child.getUserObject().toString();

	        for(int j = i + 1; j <= node.getChildCount() - 1; j++) {
	            Node prevNode = (Node) node.getChildAt(j);
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
	        Node child = (Node) node.getChildAt(i);
	        for(int j = i + 1; j <= node.getChildCount() - 1; j++) {
	            Node prevNode = (Node) node.getChildAt(j);

	            if(!prevNode.isLeaf() && child.isLeaf()) {
	                node.insert(child, j);
	                node.insert(prevNode, i);
	            }
	        }
	    }
	    return node;
	}
	
	public static Node createProjectNode(Project prj) {
		if (prj == null)
			return null;
		Node prjNode = new Node(prj.getName(), NodeType.PROJECT, NodeType.ALL);
		Node spritesNode = new Node(Const.SPRITES, NodeType.FOLDER, NodeType.SPRITE);
		Node palettesNode = new Node(Const.PALETTES, NodeType.FOLDER, NodeType.PALETTE);
		Node mapsNode = new Node(Const.MAPS, NodeType.FOLDER, NodeType.MAP);
		for (SpriteSheet sp : prj.getSprites()) {
			spritesNode.add(new Node(sp.getName(), NodeType.SPRITE, NodeType.SPRITE));
		}
		for (Map sp : prj.getMaps()) {
			mapsNode.add(new Node(sp.getName(), NodeType.MAP, NodeType.MAP));
		}
		for (Palette sp : prj.getPalettes()) {
			palettesNode.add(new Node(sp.getName(), NodeType.PALETTE, NodeType.PALETTE));
		}
		prjNode.add(spritesNode);
		prjNode.add(palettesNode);
		prjNode.add(mapsNode);
		return prjNode;
	}

	public static Node placeNode(Node projectNode, Node node) {
		Node targetNode = getChildByAllowedType(projectNode, node.type);
		targetNode.add(node);
		return targetNode;
	}
	
	public static void error(String content) {
		JOptionPane.showMessageDialog(null,
			    content,
			    "ERROR",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	public static void info(String content) {
		JOptionPane.showMessageDialog(null,
			    content,
			    "INFO",
			    JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void error(String content, Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText("");
		alert.setContentText("Error message: "+e.getMessage());

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();


		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(textArea, 0, 0);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
	
	public static byte[] imageToBytes(BufferedImage img) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( img, "png", baos );
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
	}
	
	public static BufferedImage bytesToImage(byte[] imageData) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
		return ImageIO.read(bais);
	}
}
