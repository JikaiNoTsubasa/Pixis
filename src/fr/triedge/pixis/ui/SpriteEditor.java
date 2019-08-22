package fr.triedge.pixis.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.xml.bind.JAXBException;

import fr.triedge.pixis.ctrl.Controller;
import fr.triedge.pixis.model.Palette;
import fr.triedge.pixis.model.Project;
import fr.triedge.pixis.model.Sprite;
import fr.triedge.pixis.model.SpriteSheet;
import fr.triedge.pixis.utils.Utils;

public class SpriteEditor extends JPanel implements MouseWheelListener{
	//https://stackoverflow.com/questions/2900801/wanting-a-type-of-grid-for-a-pixel-editor/2901472#2901472
	//https://stackoverflow.com/questions/34344309/create-a-swing-gui-to-manipulate-png-pixel-by-pixel
	private static final long serialVersionUID = 6821220201585858378L;

	private Controller controller;
	private Project project;
	private SpriteSheet spriteSheet;
	private Sprite currentSprite;
	private Palette currentPalette;
	private double zoomFactor = 1, zoomIncrement = 0.5;
	private SpriteDisplayer spriteDisplayer;
	private PaletteDisplayer paletteDisplayer;
	private int currentColor = Color.green.getRGB();

	private JToolBar toolBar;
	private JLabel labelStatus;
	private JPanel statusBar;
	private JButton btnSave,btnZoomFactor,btnRefreshCombo;
	private JComboBox<Palette> comboPalettes;


	BufferedImage img;

	public SpriteEditor(Controller controller, SpriteSheet spriteSheet, Project project) {
		setController(controller);
		setSpriteSheet(spriteSheet);
		setProject(project);
	}

	public void build() {
		setBackground(Color.DARK_GRAY);
		setLayout(new BorderLayout());
		//setPreferredSize(new Dimension(500, 500));
		setToolBar(new JToolBar());
		getToolBar().setFloatable(false);
		setBtnZoomFactor(new JButton());
		updateZoomDisplay();
		setupPalettes();
		try {
			setBtnSave(new JButton(new ImageIcon(ImageIO.read(new File("img/icon/o_save.png")))));
			setBtnRefreshCombo(new JButton(new ImageIcon(ImageIO.read(new File("img/icon/o_refresh.png")))));
		} catch (IOException e1) {
			UI.error("Cannot load button image", e1);
		}

		setStatusBar(new JPanel(new FlowLayout(FlowLayout.LEFT)));
		getStatusBar().setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY),new EmptyBorder(4, 4, 4, 4)));
		setLabelStatus(new JLabel("Ready"));
		getStatusBar().add(getLabelStatus());
		getBtnSave().setToolTipText("Save");
		getToolBar().add(getBtnSave());
		getToolBar().add(getBtnRefreshCombo());
		getToolBar().add(getComboPalettes());
		getToolBar().add(getBtnZoomFactor());

		getBtnZoomFactor().addActionListener(e -> resetZoomFactor());
		getBtnSave().addActionListener(e -> saveProject());
		getBtnRefreshCombo().addActionListener(e -> updatePalettes());
		
		getComboPalettes().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});

		// Setup displayers
		setSpriteDisplayer(new SpriteDisplayer());
		setPaletteDisplayer(new PaletteDisplayer());

		add(getToolBar(), BorderLayout.NORTH);
		add(new JScrollPane(getSpriteDisplayer()), BorderLayout.CENTER);
		add(new JScrollPane(getPaletteDisplayer()), BorderLayout.EAST);
		add(getStatusBar(), BorderLayout.SOUTH);

		addMouseWheelListener(this);
		setCurrentSprite(getSpriteSheet().getLayers().get(0).getSprites().get(0));
		try {
			img = UI.bytesToImage(getCurrentSprite().getImageData());
		} catch (IOException e) {
			UI.error("Cannot load image from bytes", e);
		}
	}
	
	public void setupPalettes() {
		int numberOfPalettes = getProject().getPalettes().size();
		Palette[] pals = new Palette[numberOfPalettes];
		for (int i = 0;i < numberOfPalettes; ++i) {
			pals[i] = getProject().getPalettes().get(i);
		}
		setComboPalettes(new JComboBox<Palette>(pals));
		revalidate();
		repaint();
	}
	
	public void updatePalettes() {
		UI.queue(new Runnable() {
			
			@Override
			public void run() {
				DefaultComboBoxModel<Palette> model = (DefaultComboBoxModel<Palette>)getComboPalettes().getModel();
				model.removeAllElements();
				for (Palette p : getProject().getPalettes()) {
					model.addElement(p);
				}
				revalidate();
				repaint();
			}
		});
	}
	
	private class PaletteDisplayer extends JPanel implements MouseListener{
		private static final long serialVersionUID = 7714820717792969665L;
		private int colorSize = 20, colorColumns = 6, colorRows = 20;
		int selectedX = 0;
		int selectedY = 0;
		
		public PaletteDisplayer() {
			setBackground(Color.ORANGE);
			setLayout(new BorderLayout());
			setPreferredSize(new Dimension(colorSize*colorColumns, colorSize*colorRows));
			addMouseListener(this);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			Palette pal = (Palette)getComboPalettes().getSelectedItem();
			
			for (int i = 0; i < pal.getColors().size(); ++i) {
				int rgba = pal.getColors().get(i);
				int x = (i * colorSize)%(colorColumns*colorSize);
				int y = Math.floorDiv(i*colorSize,colorSize*colorColumns)*colorSize;
				g.setColor(new Color(rgba, true));
				g.fillRect(x, y, colorSize, colorSize);
			}
			g.setColor(Color.red);
			g.drawRect(0, 0, getWidth(),getHeight());
			g.setColor(Color.red);
			g.drawRect(selectedX * colorSize, selectedY * colorSize, colorSize, colorSize);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			int cellx = (int) Math.floor(e.getX()/colorSize);
			int celly = (int) Math.floor(e.getY()/colorSize);
			int index = (celly*colorColumns)+cellx;
			Palette pal = (Palette)getComboPalettes().getSelectedItem();
			if (pal.getColors().size()>index) {
				Integer color = pal.getColors().get(index);
				setCurrentColor(color);
				selectedX = cellx;
				selectedY = celly;
				redraw();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}
	}

	private class SpriteDisplayer extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener{
		private static final long serialVersionUID = 6297012885468227631L;

		private int spHeight = getSpriteSheet().getCharacterHeight();
		private int spWidth = getSpriteSheet().getCharacterWidth();

		public SpriteDisplayer() {
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
			setSize(spWidth*10, spHeight*10);
			setPreferredSize(new Dimension(spWidth*10, spHeight*10));
		}

		@Override
		protected void paintComponent(Graphics g) {
			g.clearRect(0, 0, getWidth(), getHeight());
			g.drawImage(img,0,0,(int)(spWidth*zoomFactor),(int)(spHeight*zoomFactor),null);
			g.setColor(Color.red);
			g.drawRect(0, 0, (int)(img.getWidth()*zoomFactor),(int)(img.getHeight()*zoomFactor));
		}
		
		private void updatePanelSize() {
			setSize((int)(spWidth*getZoomFactor()), (int)(spHeight*getZoomFactor()));
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			int x = Utils.roundDown(e.getX()/getZoomFactor());
			int y = Utils.roundDown(e.getY()/getZoomFactor());
			drawColorAt(getCurrentColor(), x, y);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		public void drawColorAt(int color, int x, int y) {
			if (x >= 0 && x < spWidth && y >= 0 && y < spHeight) {
				img.setRGB(x, y, color);
				repaint();
			}
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getWheelRotation() < 0) {
				// Zoom in
				setZoomFactor(getZoomFactor() + getZoomIncrement());
			}else {
				// Zoom out
				setZoomFactor(getZoomFactor() - getZoomIncrement());
			}
			if (getZoomFactor() < 1)
				setZoomFactor(1);
			updateZoomDisplay();
			updatePanelSize();
			this.repaint();
		}
	}
	
	public void redraw() {
		this.repaint();
	}

	private void saveProject() {
		try {
			getCurrentSprite().setImageData(UI.imageToBytes(img));
			getProject().save();
			updateStatusDisplay("Project saved");
		} catch (JAXBException e) {
			UI.error("Cannot save project", e);
		} catch (IOException e) {
			UI.error("Cannot convert image to bytes", e);
		}
	}

	private void resetZoomFactor() {
		setZoomFactor(1);
		updateZoomDisplay();
		repaint();
	}

	private void updateZoomDisplay() {
		getBtnZoomFactor().setText("Zoom x"+getZoomFactor());
	}

	private void updateStatusDisplay(String text) {
		getLabelStatus().setText(text);
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public SpriteSheet getSpriteSheet() {
		return spriteSheet;
	}

	public void setSpriteSheet(SpriteSheet sprite) {
		this.spriteSheet = sprite;
	}

	public double getZoomFactor() {
		return zoomFactor;
	}

	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	public Sprite getCurrentSprite() {
		return currentSprite;
	}

	public void setCurrentSprite(Sprite currentSprite) {
		this.currentSprite = currentSprite;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}

	public double getZoomIncrement() {
		return zoomIncrement;
	}

	public void setZoomIncrement(double zoomIncrement) {
		this.zoomIncrement = zoomIncrement;
	}

	public JToolBar getToolBar() {
		return toolBar;
	}

	public void setToolBar(JToolBar toolBar) {
		this.toolBar = toolBar;
	}

	public SpriteDisplayer getSpriteDisplayer() {
		return spriteDisplayer;
	}

	public void setSpriteDisplayer(SpriteDisplayer spriteDisplayer) {
		this.spriteDisplayer = spriteDisplayer;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public JButton getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(JButton btnSave) {
		this.btnSave = btnSave;
	}

	public JButton getBtnZoomFactor() {
		return btnZoomFactor;
	}

	public void setBtnZoomFactor(JButton btnZoomFactor) {
		this.btnZoomFactor = btnZoomFactor;
	}

	public JPanel getStatusBar() {
		return statusBar;
	}

	public void setStatusBar(JPanel statusBar) {
		this.statusBar = statusBar;
	}

	public JLabel getLabelStatus() {
		return labelStatus;
	}

	public void setLabelStatus(JLabel labelStatus) {
		this.labelStatus = labelStatus;
	}

	public int getCurrentColor() {
		return currentColor;
	}

	public void setCurrentColor(int selectedColor) {
		this.currentColor = selectedColor;
	}

	public PaletteDisplayer getPaletteDisplayer() {
		return paletteDisplayer;
	}

	public void setPaletteDisplayer(PaletteDisplayer paletteDisplayer) {
		this.paletteDisplayer = paletteDisplayer;
	}

	public Palette getCurrentPalette() {
		return currentPalette;
	}

	public void setCurrentPalette(Palette currentPalette) {
		this.currentPalette = currentPalette;
	}

	public JComboBox<Palette> getComboPalettes() {
		return comboPalettes;
	}

	public void setComboPalettes(JComboBox<Palette> comboPalettes) {
		this.comboPalettes = comboPalettes;
	}

	public JButton getBtnRefreshCombo() {
		return btnRefreshCombo;
	}

	public void setBtnRefreshCombo(JButton btnRefreshCombo) {
		this.btnRefreshCombo = btnRefreshCombo;
	}
}
