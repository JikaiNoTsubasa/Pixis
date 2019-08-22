package fr.triedge.pixis.ui;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PixisTreeRenderer extends DefaultTreeCellRenderer{

	private static final long serialVersionUID = -6484838419926434290L;
	private static Logger log = LogManager.getLogger(PixisTreeRenderer.class);

	ImageIcon leafIcon;
	ImageIcon paletteIcon;
	ImageIcon fileIcon;
	ImageIcon mapIcon;
	ImageIcon spriteIcon;
	ImageIcon layerIcon;
	ImageIcon projectIcon;
	ImageIcon folderClosedIcon;
	ImageIcon folderOpenedIcon;
	
	public PixisTreeRenderer() {
		setIconTextGap(5);
		try {
			spriteIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_male.png")));
			paletteIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_palette.png")));
			layerIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_layer.png")));
			mapIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_map.png")));
			fileIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_file.png")));
			projectIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_project.png")));
			folderClosedIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_folderClosed.png")));
			folderOpenedIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_folderOpened.png")));
		} catch (IOException e) {
			log.error("Error when setting renderer icons",e);
		}
	}
	
	@Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean isLeaf, int row, boolean focused) {
        Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, isLeaf, row, focused);
        Node node = (Node)value;
        switch(node.type) {
		case ALL:
			break;
		case FOLDER:
			if (expanded)
				setIcon(folderOpenedIcon);
			else
				setIcon(folderClosedIcon);
			break;
		case MAP:
			setIcon(mapIcon);
			break;
		case PALETTE:
			setIcon(paletteIcon);
			break;
		case PROJECT:
			setIcon(projectIcon);
			break;
		case ROOT:
			if (expanded)
				setIcon(folderOpenedIcon);
			else
				setIcon(folderClosedIcon);
			break;
		case SPRITE:
			setIcon(spriteIcon);
			break;
		default:
			setIcon(fileIcon);
			break;
        
        }
        /*
        if (isLeaf) {
        	if (value.toString().endsWith(Const.EXT_SPRITE))
        		setIcon(spriteIcon);
        	else if (!value.toString().contains("."))
        		setIcon(folderClosedIcon);
        	else
        		setIcon(fileIcon);
        }
        else {
        	if (value.toString().endsWith(Const.EXT_PROJECT)) {
        		setIcon(projectIcon);
        	}else {
        		if(expanded) {
        			setIcon(folderOpenedIcon);
        		}else {
        			setIcon(folderClosedIcon);
        		}
        	}
        }*/
        return c;
    }
}
