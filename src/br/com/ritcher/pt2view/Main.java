package br.com.ritcher.pt2view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.codehaus.jackson.JsonParseException;

import br.com.ritcher.pt2view.data.Score;

public class Main {
	    private ScorePanel panel;
	    
	    private File inputFile;
	    
	    private File outputFile;
	    
	    public File getOutputFile() {
			return outputFile;
		}

		public void setOutputFile(File outputFile) {
			this.outputFile = outputFile;
		}

		private PT2Loader loader;

		private JScrollPane scrollPane;

		private JFrame frame;
	    
	    public ScorePanel getPanel() {
			return panel;
		}

		public void setPanel(ScorePanel panel) {
			this.panel = panel;
		}

		public File getInputFile() {
			return inputFile;
		}

		public void setInputFile(File file) {
			this.inputFile = file;
		}

		public PT2Loader getLoader() {
			return loader;
		}

		public void setLoader(PT2Loader loader) {
			this.loader = loader;
		}

		public JFrame getFrame() {
			return frame;
		}

		public void setFrame(JFrame frame) {
			this.frame = frame;
		}

		public Main() {
			loader = new PT2Loader();
		}

		private void createAndShowGUI() {
	        //Create and set up the window.
	        frame = new JFrame("Score View");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        panel = new ScorePanel();
	        /*
	        ScorePattern pattern = new ScorePattern();
	        pattern.setScore(panel.score);
	        pattern.process();
	         */
	        
	        scrollPane = new JScrollPane(panel);
	        scrollPane.setPreferredSize(new Dimension(800,600));

	        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
	        
	        frame.addKeyListener(new ScorePanelKeyListener(this));
	 
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	        
	        //openFile(new File("C:\\Users\\talvares\\Downloads\\Metallica - Nothing Else Matters (2).pt2"));
	        showHelp();
	    }
	 
	    public static void main(String[] args) {
	        //Schedule a job for the event-dispatching thread:
	        //creating and showing this application's GUI.
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                new Main().createAndShowGUI();
	            }
	        });
	    }

		public void openFile(File selectedFile) {
			
			try {
				this.inputFile = selectedFile;
				Score score = loader.loadFile(selectedFile);
				panel.setScore(score);
				frame.pack();
				
			} catch (JsonParseException e) {
				showError("Problems parsing the input file", e);
			} catch (FileNotFoundException e) {
				showError("File not found", e);
				e.printStackTrace();
			} catch (IOException e) {
				showError("Input error", e);
				e.printStackTrace();
			}
			
		}

		public void showError(String string, IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, string + ":" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		public void showInfo(String string) {
			JOptionPane.showMessageDialog(frame, string, "Mensagem", JOptionPane.INFORMATION_MESSAGE);
		}

		public void showHelp() {
			 JOptionPane.showMessageDialog(frame, 
			    		"CTRL-o: Opens a file from the file system. \n"+
			    		"CTRL-s: Save an png file of the score. \n"+
			    		"CTRL-t: Change the note spacing factor. \n"
			    	,"Ajuda",  JOptionPane.INFORMATION_MESSAGE);

		}
}
