package fr.triedge.pixis.utils;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Icons {

	public static ImageIcon spriteIcon;
	public static ImageIcon paletteIcon;
	public static ImageIcon layerIcon;
	public static ImageIcon mapIcon;
	public static ImageIcon fileIcon;
	public static ImageIcon projectIcon;
	public static ImageIcon folderClosedIcon;
	public static ImageIcon folderOpenedIcon;
	public static ImageIcon saveIcon;
	public static ImageIcon refreshIcon;
	
	static {
		try {
			spriteIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_male.png")));
			spriteIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_refresh.png")));
			saveIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_male.png")));
			paletteIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_palette.png")));
			layerIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_layer.png")));
			mapIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_map.png")));
			fileIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_file.png")));
			projectIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_project.png")));
			folderClosedIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_folderClosed.png")));
			folderOpenedIcon = new ImageIcon(ImageIO.read(new File("img/icon/o_folderOpened.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
