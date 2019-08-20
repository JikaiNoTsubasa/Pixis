package fr.triedge.pixis.run;

import java.io.IOException;

import fr.triedge.pixis.ctrl.Controller;
import fr.triedge.pixis.ui.MainWindow;

public class RunPixis {

	public static void main(String[] args) {
		Controller ctrl = new Controller();
		try {
			ctrl.init();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		MainWindow win = new MainWindow(ctrl);
		ctrl.setMainWindow(win);
		win.build();
		win.setVisible(true);
	}

}
