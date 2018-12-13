package br.com.ritcher.pt2view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ScorePanelKeyListener implements KeyListener {

	private Main main;

	public ScorePanelKeyListener(Main main) {
		this.main = main;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_F1)){
		   main.showHelp();
		}
		if ((e.getKeyCode() == KeyEvent.VK_O) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
			JFileChooser jfc = new JFileChooser(this.main.getInputFile());
			jfc.addChoosableFileFilter(new FileNameExtensionFilter("Power Tab 2 File", "pt2"));

			
			int returnValue = jfc.showOpenDialog(null);
			// int returnValue = jfc.showSaveDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				this.main.openFile(jfc.getSelectedFile());
			}
		}
		if ((e.getKeyCode() == KeyEvent.VK_T) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
			  int spacing = Integer.parseInt(JOptionPane.showInputDialog(this.main.getFrame() ,"Note spacing", Integer.toString(this.main.getPanel().getTimeFactor())));
			  this.main.getPanel().setTimeFactor(spacing);
		}
		if ((e.getKeyCode() == KeyEvent.VK_S) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
			if(this.main.getInputFile() == null){
				main.showInfo("Open a Power Tab file first");
			}
			JFileChooser jfc = new JFileChooser(this.main.getInputFile());
			
			
			jfc.setDialogTitle("Export to png");
			jfc.addChoosableFileFilter(new FileNameExtensionFilter(".png file", "png"));
			
			int returnValue = jfc.showOpenDialog(null);
			
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				main.setOutputFile(jfc.getSelectedFile());
				ImageExport export = new ImageExport(main.getPanel(), jfc.getSelectedFile());
				try {
					export.export();
					main.showInfo("Image saved");
					
				} catch (IOException e1) {
					main.showError("Problemas exportanto imagem ", e1);
				}
			}
			
			
		}
	}

	

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

}
