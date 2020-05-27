package mfAlgnCrvs;

/** ===============================================================================
* MultiFocal_AlignCurvesByLMS Version 0.0.1
* 
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation 
* (http://www.gnu.org/licenses/gpl.txt )
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public
* License along with this program.  If not, see
* <http://www.gnu.org/licenses/gpl-3.0.html>.
*  
* Copyright (C) 2019: Jan N. Hansen
* 
* For any questions please feel free to contact me (jan.hansen@uni-bonn.de).
*
* =============================================================================== */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Plot;
import ij.gui.PlotWindow;
import mfAlgnCrvs.tools.constants;
import mfAlgnCrvs.tools.tools;

public class Main extends javax.swing.JFrame implements ActionListener {
	private static final String VERSION = "0.0.1";
	private static final String NAME = "MultiFocal_AlignCurvesByLMS";
	private static final long serialVersionUID = 1L;	
	
	private static String referenceLine = "This file was generated using " + NAME + " version " 
			+ VERSION
			+ ", a java application by Jan Niklas Hansen (\u00a9 2019, "
			+ "see https://github.com/hansenjn/MultiFocal_AlignCurvesByLMS).";
	
	public static final int ERROR = 0;
	public static final int NOTIF = 1;
	public static final int LOG = 2;
	
	final static int [] red = {254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,252,246,240,234,228,222,216,210,204,198,192,188,182,176,170,164,158,152,146,140,134,128,122,116,110,104,98,92,86,80,74,68,63,57,51,45,39,33,27,21,15,9,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,3,9,15,21,27,33,39,45,51,57,63,68,74,80,86,92,98,104,110,116,122,128,134,140,146,152,158,164,170,176,182,188,192,198,204,210,216,222,228,234,240,246,252,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,25};
	final static int [] green = {7,7,13,19,25,31,37,43,49,55,61,66,72,78,84,90,96,102,108,114,120,126,132,138,144,150,156,162,168,174,180,186,191,196,202,208,214,220,226,232,238,244,250,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,248,242,236,230,224,218,212,206,200,194,190,184,178,172,166,160,154,148,142,136,130,124,118,112,106,100,94,88,82,76,70,64,59,53,47,41,35,29,23,17,11,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
	final static int [] blue = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,11,17,23,29,35,41,47,53,59,64,70,76,82,88,94,100,106,112,118,124,130,136,142,148,154,160,166,172,178,184,190,194,200,206,212,218,224,230,236,242,248,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,254,250,244,238,232,226,220,214,208,202,196,191,186,180,174,168,162,156,150,144,138,132,126,120,114,108,102,96,90,84,78,72,66,61,55,49,43,37,31,25,19,13,13};
		
	static final double threshold = 0.70;
	
	static final SimpleDateFormat NameDateFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
	static final SimpleDateFormat FullDateFormatter = new SimpleDateFormat("yyyy-MM-dd	HH:mm:ss");
			
	LinkedList<File> filesToOpen = new LinkedList<File>();
	boolean done = false, dirSaved = false;
	File savedDir;// = new File(getClass().getResource(".").getFile());
	JMenuBar jMenuBar1;
	JMenu jMenu3, jMenu5;
	JSeparator jSeparator2;
	JPanel bgPanel, topPanel;
	JScrollPane jScrollPane1, logScrollPane;
	JList<Object> Liste1, logList;
	JButton loadButton, removeButton, goButton;
	
	private JProgressBar progressBar = new JProgressBar();
	
	private String [] notifications, log;
	private boolean notificationsAvailable = false, errorsAvailable = false;
	
	String desktopPath = "";
	String newLine = System.getProperty("line.separator");
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Main inst = new Main();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public Main() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			String out = "";
			for(int err = 0; err < e.getStackTrace().length; err++){
				out += " \n " + e.getStackTrace()[err].toString();
			}
			this.logMessage("Exception while setting look and feel:" + out, NOTIF);			
		}
		
		File home = FileSystemView.getFileSystemView().getHomeDirectory(); 
		desktopPath = home.getAbsolutePath();
		
		int prefXSize = 600, prefYSize = 570;
		this.setMinimumSize(new java.awt.Dimension(prefXSize, prefYSize+40));
		this.setSize(prefXSize, prefYSize+40);			
		this.setTitle(NAME + " " + VERSION + " (\u00a9 2019 Jan N. Hansen)");
//		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//Surface
			bgPanel = new JPanel();
			bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));
			bgPanel.setVisible(true);
			bgPanel.setPreferredSize(new java.awt.Dimension(prefXSize,prefYSize-20));
			{
				topPanel = new JPanel();
//				topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
				topPanel.setVisible(true);
				topPanel.setPreferredSize(new java.awt.Dimension(prefXSize,60));
				{
					JTextPane text = new JTextPane();
					text.setText("*** For credits, license information, manual etc.: https://github.com/hansenjn/MultiFocal_AlignCurvesByLMS ***");
					text.setBackground(this.getBackground());
					text.setForeground(Color.DARK_GRAY);
					text.setEditable(false);
					text.setVisible(true);
					topPanel.add(text);//, BorderLayout.NORTH);
				}
				{
					JTextPane text = new JTextPane();
					text.setText("Add plane text files to the list (one per plane) and start analysis.");
					text.setBackground(this.getBackground());
					text.setEditable(false);
					text.setVisible(true);
					topPanel.add(text);
				}
				bgPanel.add(topPanel);
			}
			{
				jScrollPane1 = new JScrollPane();
				jScrollPane1.setHorizontalScrollBarPolicy(30);
				jScrollPane1.setVerticalScrollBarPolicy(20);
				jScrollPane1.setPreferredSize(new java.awt.Dimension(prefXSize-10, 340));
				bgPanel.add(jScrollPane1);
				{
					Liste1 = new JList<Object>();
					jScrollPane1.setViewportView(Liste1);
					Liste1.setModel(new DefaultComboBoxModel(new String[] { "" }));
				}
				{
					JPanel spacer = new JPanel();
					spacer.setMaximumSize(new java.awt.Dimension(prefXSize,10));
					spacer.setVisible(true);
					bgPanel.add(spacer);
				}
				{
					JPanel bottom = new JPanel();
					bottom.setMaximumSize(new java.awt.Dimension(prefXSize,10));
					bottom.setVisible(true);
					bgPanel.add(bottom);
					int locHeight = 30;
					int locWidth3 = prefXSize/4-60;
					{
						loadButton = new JButton();
						loadButton.addActionListener(this);
						loadButton.setText("add plane text file(s) to list");
						loadButton.setMinimumSize(new java.awt.Dimension(locWidth3,locHeight));
						loadButton.setVisible(true);
						loadButton.setVerticalAlignment(SwingConstants.BOTTOM);
						bottom.add(loadButton);
					}
					{
						removeButton = new JButton();
						removeButton.addActionListener(this);
						removeButton.setText("remove file from list");
						removeButton.setMinimumSize(new java.awt.Dimension(locWidth3,locHeight));
						removeButton.setVisible(true);
						removeButton.setVerticalAlignment(SwingConstants.BOTTOM);
						bottom.add(removeButton);
					}	
					{
						goButton = new JButton();
						goButton.addActionListener(this);
						goButton.setText("start alignment");
						goButton.setMinimumSize(new java.awt.Dimension(locWidth3,locHeight));
						goButton.setVisible(true);
						goButton.setVerticalAlignment(SwingConstants.BOTTOM);
						bottom.add(goButton);
					}	
				}	
			}
			{
				progressBar = new JProgressBar();
				progressBar = new JProgressBar(0, 0);
				progressBar.setPreferredSize(new java.awt.Dimension(prefXSize,40));
				progressBar.setStringPainted(true);
				progressBar.setValue(0);
				progressBar.setMaximum(100);
				progressBar.setString("no analysis started!");
				bgPanel.add(progressBar);	
			}
			{
				JPanel spacer = new JPanel();
//				spacer.setBackground(Color.black);
				spacer.setMaximumSize(new java.awt.Dimension(prefXSize,10));
				spacer.setVisible(true);
				bgPanel.add(spacer);
			}
			{
				JPanel imPanel = new JPanel();
				imPanel.setLayout(new BorderLayout());
				imPanel.setVisible(true);
				imPanel.setPreferredSize(new java.awt.Dimension(prefXSize,60));
				{
					JLabel spacer = new JLabel("Log:", SwingConstants.LEFT);
					spacer.setMinimumSize(new java.awt.Dimension(prefXSize,20));
					spacer.setVisible(true);
					imPanel.add(spacer, BorderLayout.NORTH);
				}
				{	
					logScrollPane = new JScrollPane();
					logScrollPane.setHorizontalScrollBarPolicy(30);
					logScrollPane.setVerticalScrollBarPolicy(20);
					logScrollPane.setPreferredSize(new java.awt.Dimension(prefXSize, 40));
					imPanel.add(logScrollPane, BorderLayout.CENTER);
					{
						ListModel ListeModel = new DefaultComboBoxModel(new String[] { "" });
						logList = new JList();
						logScrollPane.setViewportView(logList);
						logList.setModel(ListeModel);
					}
				}
				bgPanel.add(imPanel);
			}
			
			getContentPane().add(bgPanel);		
			
			this.addWindowListener(new java.awt.event.WindowAdapter() {
		        public void windowClosing(WindowEvent winEvt) {
		        	dispose();		        	
		        }
		    });
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object eventQuelle = ae.getSource();
		if (eventQuelle == loadButton){
			JFileChooser chooser = new JFileChooser();
			chooser.setPreferredSize(new Dimension(600,400));
			if(dirSaved){				
				chooser.setCurrentDirectory(savedDir);
			}			
			chooser.setMultiSelectionEnabled(true);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			Component frame = null;
			chooser.showOpenDialog(frame);
			
			File[] files = chooser.getSelectedFiles();
			for(int i = 0; i < files.length; i++){
				filesToOpen.add(files[i]);
				savedDir = files[i];
				dirSaved=true;
			}			
			updateDisplay();
		}
		if (eventQuelle == removeButton){
			int[] indices = Liste1.getSelectedIndices();
			for(int i = indices.length-1; i >=0; i--){
				filesToOpen.remove(indices[i]);
			}
			updateDisplay();
		}
		if (eventQuelle == goButton){
			{
				Thread analyze = new Thread(){
					public void run(){		
						analyze();
						Thread.currentThread().interrupt();
					}
				};
				analyze.start();
			}
			System.gc();
		}		
		
	}
	
	private void analyze(){
		goButton.setEnabled(false);
		removeButton.setEnabled(false);
		loadButton.setEnabled(false);
		progressBar.setString("analyzing");
		bgPanel.updateUI();
		
		this.clearLog();
		//create new file
		Date now = new Date();		
		File outputFolder = new File(desktopPath + System.getProperty("file.separator") 
			+ "MFALGNCRVS " + NameDateFormatter.format(now) + System.getProperty("file.separator"));		

		running: while(true){
			try {
//				if(filesToOpen.size()<2){
//					this.logMessage("Sorry .. more than one plane needed to perform LMS", ERROR);
//					break running;
//				}
				//if file does not exists, then create it
				if (!outputFolder.exists()) {
					outputFolder.mkdir();
				}
				ArrayList<double [][]> planeResults = new ArrayList<double [][]> (filesToOpen.size());
				for(int i = 0; i < filesToOpen.size(); i++){
					planeResults.add(this.convertFileToArray(filesToOpen.get(i).getPath(), true, false));				
					progressBar.setValue((int)Math.round((double)(i+1.0)*(10.0/(double)filesToOpen.size())));
				}
				//save list of results data				
				progressBar.setValue(15);
	//			progressBar.updateUI();
				
				//test data set for potential bias
				if(filesToOpen.size()>1){
					for(int i = 1; i < planeResults.size(); i++){
//						if(planeResults.get(i).length!=planeResults.get(0).length
//								|| planeResults.get(i)[0].length!=planeResults.get(0)[0].length){
						if(planeResults.get(i).length != planeResults.get(0).length){
							this.logMessage("Sorry .. dimensions of individual plane files were different.\n" 
									+ "Files with different matrix sizes cannot be handled.", ERROR);
							for(int j = 0; j < planeResults.size(); j++){
								this.logMessage((j+1) + ": " + planeResults.get(j).length, ERROR);
							}
							break running;
						}
					}					
				}
				
				int maxLength = 0;
				for(int i = 0; i < planeResults.size(); i++){
					if(planeResults.get(i)[0].length > maxLength){
						maxLength = planeResults.get(i)[0].length;
					}
				}
				
				//convert results array list to array
				double inputMatrix [][][] 
						= new double [planeResults.size()][planeResults.get(0).length][maxLength];
				for(int i = 0; i < inputMatrix.length; i++){
					for(int j = 0; j < inputMatrix[i].length; j++){
						for(int k = 0; k < planeResults.get(i)[j].length; k++){
							inputMatrix [i][j][k] = planeResults.get(i)[j][k];
						}
					}
				}
				planeResults = null;
				System.gc();
				progressBar.setValue(30);
				
				//remove empty columns
				inputMatrix = removeEmptyColumnsFromArray(inputMatrix);
				
				//LMS
				double outputMatrix [][][] = alignByLeastMeanSquare(inputMatrix, false, true, outputFolder.getPath());
				progressBar.setValue(70);
				
				//Output
				this.outputResults(outputMatrix, filesToOpen, outputFolder.getPath());			
				progressBar.setValue(100);
				
				//Save log file
				this.logMessage("Processing done. A file (" + outputFolder.getName() + ") was created on the desktop.", LOG);
				this.saveLog(outputFolder.getPath() + System.getProperty("file.separator") + "Log.txt");			
			}catch (Exception e) {
				outputFolder.deleteOnExit();
				progressBar.setString("Sorry .. no file could be generated. An error occured during file reading / writing.");
				progressBar.setValue(100); 		
				progressBar.setStringPainted(true);
				progressBar.setForeground(Color.red);
				String out = "";
				for(int err = 0; err < e.getStackTrace().length; err++){
					out += " \n " + e.getStackTrace()[err].toString();
				}
				this.logMessage("Sorry .. no file could be generated. An error occured during file reading / writing:\n" 
						+ e.getCause() + "\n" + out, ERROR);
			}	
			break running;	
		}
		System.gc();
		if(errorsAvailable){
			progressBar.setString("processing done but some tasks failed (see log)!");
			progressBar.setValue(100); 		
			progressBar.setStringPainted(true);
			progressBar.setForeground(Color.red);
			progressBar.updateUI();
		}else if(notificationsAvailable){
			progressBar.setString("processing done, but some notifications are available (see log)!");
			progressBar.setValue(100); 
			progressBar.setStringPainted(true);
			progressBar.setForeground(new Color(255,130,0));
			progressBar.updateUI();
		}else{				
			progressBar.setString("analysis done!");
			progressBar.setStringPainted(true);
			progressBar.setValue(100); 
			progressBar.setForeground(new Color(0,140,0));
			progressBar.updateUI();
		}
		
		goButton.setEnabled(true);
		removeButton.setEnabled(true);
		loadButton.setEnabled(true);
		bgPanel.updateUI();
	}

	private void updateDisplay(){
		String resultsString [] = new String [filesToOpen.size()];
		for(int i = 0; i < filesToOpen.size(); i++){
			resultsString [i] = (i+1) + ": " + filesToOpen.get(i).getName();
		}
		Liste1.setListData(resultsString);
	}
	
	private void clearLog(){
		log = null;
		if(notifications != null){
			logList.setListData(notifications);
		}else{
			logList.setListData(new String []{""});
		}
		bgPanel.updateUI();
	}
	
	private void saveLog(String path){
		if(log!=null){
			final SimpleDateFormat fullDate = new SimpleDateFormat("yyyy-MM-dd	HH:mm:ss");
			File file = new File(path);
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				PrintWriter pw = new PrintWriter(fw);
				pw.println(referenceLine);
				pw.println("Logfile created on:	" + fullDate.format(new Date()));
				pw.println("");
				for(int i = 0; i < log.length; i++){
					pw.println(log[i]);
				}			
				pw.close();
			}catch (IOException e) {
				String out = "";
				for(int err = 0; err < e.getStackTrace().length; err++){
					out += " \n " + e.getStackTrace()[err].toString();
				}				
				this.logMessage("Error while saving log: " + e.getCause() + out,ERROR);
			}
		}		
	}

	void logMessage(String message, int type) {
		if(type == ERROR){
			errorsAvailable = true;
		}else if(type == NOTIF){
			notificationsAvailable = true;
		}
		
		if(type == ERROR || type == NOTIF){
			if(notifications==null ){
				notifications = new String [1];
				notifications [0] = message;
			}else{
				String [] notificationsCopy = notifications.clone();
				notifications = new String [notifications.length+1];
				for(int j = 0; j < notificationsCopy.length; j++){
					notifications[j+1] = notificationsCopy[j];
				}
				notifications [0] = message;
			}
		}else{
			if(log==null ){
				log = new String [1];
				log [0] = message;
			}else{
				String [] logCopy = log;
				log = new String [log.length+1];
				for(int j = 0; j < logCopy.length; j++){
					log[j+1] = logCopy[j];
				}
				log [0] = message;
			}
		}
		
		if(notifications != null && log == null){
			logList.setListData(notifications);
		}else if(notifications == null && log != null){
			logList.setListData(log);
		}else if(notifications != null && log != null){
			String [] listData = new String [notifications.length + log.length];
			for(int i = 0; i < notifications.length; i++){
				listData [i] = notifications [i];
			}
			for(int i = 0; i < log.length; i++){
				listData [notifications.length + i] = log [i];
			}
			logList.setListData(listData);
		}		
		bgPanel.updateUI();
	}

	double [][] convertFileToArray(String path, boolean logging, boolean logDetails){
		if(logging) this.logMessage("Reading file :" + path, LOG);	
		ArrayList<double[]> readPlane = new ArrayList<double[]>(2000);
		int maxNrOfCols = 0;
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String line = "", helpLine = "";
			int nrOfCols;
			copyPaste: while (true) {
				try {
					line = br.readLine();
					if(line.equals(null))	break copyPaste;
					try {
						//				if(readPlane.size() == 0)
						if(logDetails) this.logMessage("Input:" + line, LOG);	
						if(line.contains(",") && !line.contains("."))	line = line.replaceAll(",", ".");
						{
							nrOfCols = 0;
							helpLine = "" + line;
							while(true){
								if(helpLine.lastIndexOf("	") == helpLine.indexOf("	")){
									nrOfCols+=2;
									break;
								}
								nrOfCols++;
								helpLine = helpLine.substring(0,helpLine.lastIndexOf("	"));
							}
							if(nrOfCols>maxNrOfCols){
								maxNrOfCols = nrOfCols;
							}
						}
						double [] read = new double [nrOfCols];
						for(int i = nrOfCols-1; i >= 1; i--){
							if(line.substring(line.lastIndexOf("	")+1).isEmpty()){
								read [i] = Double.NaN;
							}else{
								read [i] = Double.parseDouble(line.substring(line.lastIndexOf("	")+1));									
							}
							line = line.substring(0,line.lastIndexOf("	"));
						}
						if(line.isEmpty()){
							read [0] = Double.NaN;
						}else{
							read [0] = Double.parseDouble(line);
						}
							
						helpLine = "";
						for(int i = 0; i < read.length; i++){
							helpLine += constants.df6US.format(read[i]);
							helpLine += "	";
						}	
						helpLine.substring(0,helpLine.lastIndexOf("	"));
						if(logDetails) this.logMessage("Coverted:" + helpLine, LOG);
						readPlane.add(read);
					}catch(Exception e){
						String out = "";
						for(int err = 0; err < e.getStackTrace().length; err++){
							out += " \n " + e.getStackTrace()[err].toString();
						}
						this.logMessage("Error in reading \n Error message:\n" 
								+ e.getCause() + "\n" + out, ERROR);
						break copyPaste;
					}
				} catch (Exception e) {
					break copyPaste;
				}
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
		
		double output [][] = new double [maxNrOfCols][readPlane.size()];
		for(int i = 0; i < readPlane.size(); i++){
			for(int j = 0; j < readPlane.get(i).length; j++){
				output [j][i] = readPlane.get(i)[j];
			}
			
		}
		
		return output;
	}
		
	private double [][][] alignByLeastMeanSquare(double inArray [][][], boolean logging, boolean savePlots, String savePath){	//slice,i,t
		int nSlices = inArray.length;
		int trackSize = inArray[0].length;
		int nFrames = inArray[0][0].length;
		int shiftrange = ((int)Math.round(nFrames/2)-1);
		double input [][][] = new double [nSlices][trackSize][nFrames];
		
		double maxDiv [] = new double [trackSize];
		Arrays.fill(maxDiv, Double.NEGATIVE_INFINITY);
		double minDiv [] = new double [trackSize];
		Arrays.fill(minDiv, Double.POSITIVE_INFINITY);
		
		for(int s = 0; s < nSlices; s++){
			save2DArrayPlot(inArray[s], "file " + (s+1) + " before processing", "x", "y", savePath + System.getProperty("file.separator") + (s+1) + "_before", false);
		}
				
		for(int s = 0; s < nSlices; s++){
			for(int i = 0; i < trackSize; i++){
				//Smooth curve
				input[s][i] = smoothCurve(inArray[s][i]);
				for(int t = 0; t < nFrames; t++){
					if(input [s][i][t] != 0.0 && !Double.isNaN(input [s][i][t])){
						if(input [s][i][t] < minDiv [i])	minDiv [i] = input [s][i][t];
						if(input [s][i][t] > maxDiv [i])	maxDiv [i] = input [s][i][t];
					}
				}	
			}
		}
			
		for(int s = 0; s < nSlices; s++){
			save2DArrayPlot(input[s], "file " + (s+1) + " after smoothing", "x", "y", savePath + System.getProperty("file.separator") +  (s+1) + "_smoothed", false);
		}
		
		if (logging) this.logMessage("LMS: Initialized variables", LOG);
		
		//filter out high values
		double max = tools.getMedian(maxDiv), min = tools.getMedian(minDiv);
		double threshold = min+(max-min)/3.0*2.0;
//		double threshold = maxRadius * xyCal / 5.0 * 2.0;
		this.logMessage("Threshold:	" + threshold, LOG);
		for(int s = 0; s < nSlices; s++){
			for(int i = 0; i < trackSize; i++){
				for(int t = 0; t < nFrames; t++){
					if(input [s][i][t] > threshold){
						input [s][i][t] = Double.NaN;
//					}else{
//						input [s][i][t] = (input [s][i][t]-min)/(max-min)*1000;	//TODO new from 02.05.2019
					}
				}	
			}
		}
		
		for(int s = 0; s < nSlices; s++){
			save2DArrayPlot(input[s], "file " + (s+1) + " thresholded (<= " + constants.df6US.format(threshold) + ")", "x", "y", savePath + System.getProperty("file.separator") + (s+1) + "_thresholded", false);
		}
		
		//keep only largest continuous stretch
		{
			int start = -1, count, bestStart = -1, bestCount;
			//				double threshold = maxRadius * xyCal / 5.0 * 2.0;
			for(int s = 0; s < nSlices; s++){
				for(int i = 0; i < trackSize; i++){
					bestCount = 0;
					count = 0;
					for(int t = 0; t < nFrames; t++){
						if(!Double.isNaN(input [s][i][t])||input [s][i][t]>0.0){
							if(count == 0){
								start = t;
							}
							count++;
						}else if(count!=0){
							if(count>bestCount){
								//delete old stretch
								if(bestStart != -1){
									for(int it = 0; it < bestCount-1; it++){
										input [s][i][bestStart+it] = Double.NaN;
									}
								}
								//reset best
								bestStart = start;
								bestCount = count;
							}else{
								//delete stretch
								for(int it = 0; it < count-1; it++){
									input [s][i][start+it] = Double.NaN;
								}
							}
							count = 0;
						}
					}					
				}
			}
		}
		
		for(int s = 0; s < nSlices; s++){
			save2DArrayPlot(input[s], "file " + (s+1) + " post short stretch removal", "x", "y", savePath + System.getProperty("file.separator") + (s+1) + "_bestStretches", false);
		}			
		
		double output [][][] = new double [nSlices][trackSize][nFrames+4*shiftrange+1];
		int shifts [][] = new int [trackSize][trackSize]; //[alignmentSeq][shifts]
		double lms [][] = new double [trackSize][trackSize];
		double minLMS, minLMSCounts, lowestTotalLMS = Double.POSITIVE_INFINITY, totalLMS = 0.0; 
		int minLMSShift, lowestTotalLMSIndex = 0;
		double LMSResult [];
		double LMSResultTemp [];
		
		
		
		for(int i = 0; i < trackSize; i++){
			for(int s = 0; s < nSlices; s++){
				Arrays.fill(output[s][i], Double.NaN);
			}
			Arrays.fill(lms[i], 0.0);
			Arrays.fill(shifts[i], 0);
		}
		
		if (logging) this.logMessage("LMS: Initialized variables - II", LOG);
		
		{			
			lowestTotalLMS = Double.POSITIVE_INFINITY;
			for(int a = 0; a < trackSize; a++){
				for(int b = 0; b < trackSize; b++){
					if (logging) this.logMessage("LMS: testing for track " + (b+1) + " aligned to " + (a+1), LOG);
					this.progressBar.setValue(30 + (int)Math.round(40*(a*trackSize+b)/(double)(trackSize*trackSize)));
					
//					if(b==a) continue;
					minLMS = Double.POSITIVE_INFINITY;
					minLMSShift = 0;
					minLMSCounts = 0.0;
					for(int shift = -1*shiftrange; shift <= shiftrange; shift ++){
						LMSResult = new double [] {0.0, 0.0};
						for(int s = 0; s < nSlices; s++){
							LMSResultTemp = leastMeanSquare(input[s][a], input[s][b], shift);
							if(LMSResultTemp [1] != 0){
								LMSResult [0] += LMSResultTemp [0];
							}	
							LMSResult [1] += LMSResultTemp [1];
						}
						LMSResult [1] /= (double) nSlices;
//						if (logging && LMSResult [1] > 0.0) this.logMessage("LMS: testing for track " + (b+1) + " aligned to " + (a+1) 
//								+ ": LMS " + LMSResult [0] + " counts " + LMSResult [1], LOG);
						
//						if (b==a && shift == 0) IJ.log(a + "=" + b + ":" + LMSResult [0] + " - " + LMSResult [1]);
						if(LMSResult [0] < minLMS 
								&& LMSResult [1] >= 0.5){
							minLMS = LMSResult [0];
							minLMSShift = shift;
							minLMSCounts = LMSResult [1];
						}
					}
					
					if (logging) this.logMessage("LMS: testing for track " + (b+1) + " aligned to " + (a+1) 
							+ ": min LMS " + minLMS + " (counts " + minLMSCounts + " shift " + minLMSShift + ")", LOG);
					
					shifts[a][b] = minLMSShift;
					if(minLMS != Double.POSITIVE_INFINITY){
						lms[a][b] = minLMS;
					}else{
						lms [a][b] = 0.0;
						for(int s = 0; s < nSlices; s++){
							lms[a][b] += sum(input[s][a]);
						}						
					}
				}				
				totalLMS = sum(lms[a]);
				if(totalLMS < lowestTotalLMS){
					lowestTotalLMS = totalLMS;
					lowestTotalLMSIndex = a;
				}
			}
			this.logMessage("lowest LMS:	" + lowestTotalLMS,LOG);
			this.logMessage("best LMS index:	" + lowestTotalLMSIndex,LOG);
			int start = shiftrange * 2;
			if(lowestTotalLMS != Double.POSITIVE_INFINITY){
				for(int a = 0; a < trackSize; a++){
					this.logMessage("shift " + a + ": " + shifts[lowestTotalLMSIndex][a], LOG);
					for(int s = 0; s < nSlices; s++){
						for(int i = 0; i < nFrames; i ++){
							output[s][a][start + i + shifts[lowestTotalLMSIndex][a]] = inArray[s][a][i];
						}
					}					
				} 
			}
			
			{
				int minT = nFrames, maxT = 0;				
					for(int s = 0; s < output.length; s++){
						for(int a = 0; a < output[0].length; a++){
							for(int i = 0; i < output[0][0].length; i ++){
							if(output[s][a][i]!=0.0 && !Double.isNaN(output[s][a][i])){							
								if(i < minT) minT = i;
								if(i > maxT) maxT = i;
							}
						}
					}
				}
				this.logMessage("minT:	" + minT,LOG);
				this.logMessage("maxT:	" + maxT,LOG);
				
				double [][][] cropped = cropArrayIn3rdDim(output, minT, maxT);
				for(int s = 0; s < nSlices; s++){
					save2DArrayPlot(cropped [s], "file " + (s+1) + " LMS aligned (reference column " + constants.df0.format(lowestTotalLMSIndex+1) + ")", "x", "y", savePath + System.getProperty("file.separator") + (s+1) + "_aligned", false);
				}
				cropped = null;
				System.gc();
			}
			
		}
		return output;
	}
	
	private static double [] leastMeanSquare (double f1 [], double f2 [], int indexShiftF2){
		double lms = 0.0;
		int counter = 0;
		int counterF1 = 0, counterF2 = 0;
		for(int i = 0; i < f1.length; i++){
			if(f1[i]!=0.0 && !Double.isNaN(f1[i])){
				counterF1++;
			}
		}
		for(int i = 0; i < f2.length; i++){
			if(f2[i]!=0.0 && !Double.isNaN(f2[i])){
				counterF2++;
			}
		}
		
		for(int i = 0; i < f1.length; i++){
			if(i-indexShiftF2 < 0 || i-indexShiftF2 >= f2.length){
				continue;
			}
//			if(f1[i]!=0.0 && !Double.isNaN(f1[i])){
//				counterF1++;
//			}
//			if(f2[i-indexShiftF2]!=0.0 && !Double.isNaN(f2[i-indexShiftF2])){
//				counterF2++;
//			}
			if(f1[i]==0.0 || f2[i-indexShiftF2] == 0.0 
					|| Double.isNaN(f1[i]) || Double.isNaN(f2[i-indexShiftF2])){
				continue;
			}else{
				lms += Math.pow(f1[i]-f2[i-indexShiftF2],2.0);
				counter ++;
			}
		}
		double output [] = {lms,counter};
		if(counter == 0){
			output [0] = Double.NEGATIVE_INFINITY;
			return output;
		}
		if(counterF2<counterF1){
			output[1] /= (double) counterF2;
		}else{
			output[1] /= (double) counterF1;
		}		
		return output;
	}
	
	private static double sum (double data[]){
		double sum = 0.0;
		for(int i = 0; i < data.length; i++){
			sum += data[i];
		}
		return sum;
	}
	
	private double [] smoothCurve(double [] input){
		double [] output = new double [input.length];
		double newIntensity;
		int counter;
		for(int n = 0; n < input.length; n++){
			newIntensity = input[n];								
			if(n==0){
				if(input.length>1){
					if(input[n+1] != 0.0){
						newIntensity += input[n+1];
						newIntensity /= 2.0;
					}					
				}						
			}else if(n==input.length-1){
				if(input[n-1] != 0.0){
					newIntensity += input[n-1];
					newIntensity /= 2.0;
				}				
			}else{
				counter = 1;
				if(input[n+1]!=0.0){
					newIntensity += input[n+1];
					counter++;
				}
				if(input[n-1]!=0.0){
					newIntensity += input[n-1];
					counter++;
				}
				newIntensity /= (double)counter;
			}
			output[n] = newIntensity;	
		}
		return output;
	}
	
	private void outputResults(double LMSAligned [][][], LinkedList<File> filesToOpen, String savePath){
		int LMSAlignedPMin = -1, LMSAlignedPMax = LMSAligned[0][0].length;
		for(int t = 0; t < LMSAligned[0][0].length; t++){
			searching: for(int s = 0; s < LMSAligned.length; s++){			
				for(int i = 0; i < LMSAligned[0].length; i++){
					if(!Double.isNaN(LMSAligned [s][i][t])&& LMSAligned [s][i][t] != 0.0){
						if(LMSAlignedPMin == -1){
							LMSAlignedPMin = t;
						}
						LMSAlignedPMax = t;
						break searching;
					}
				}
			}						
		}
		
		String appText = "";

		File fileRes;
		FileWriter fw;
		BufferedWriter bw;
		fileRes = new File(savePath + System.getProperty("file.separator") + "LMS.txt");
		
		try {
			if (!fileRes.exists()) {
				fileRes.createNewFile();
			}	
			fw = new FileWriter(fileRes.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			
			bw.write(referenceLine + newLine);
			bw.write("Saving date:	" + constants.dateTab.format(new Date()) + newLine);
			bw.write("Loaded files:	Name	Parent" + newLine);
			for(int i = 0; i < filesToOpen.size(); i++){
				bw.write("	" + filesToOpen.get(i).getName() + "	" + filesToOpen.get(i).getParent() + newLine);
			}
			
			bw.write(newLine);
			
			bw.write("Pos	LMS aligned radius results for file:" + newLine);
			
			appText = "	";
			for(int s = 0; s < LMSAligned.length; s++){
				appText +=	"Plane " + constants.df0.format(s+1);
				if(s==LMSAligned.length-1){
					break;
				}
				for(int i = 0; i < LMSAligned[0].length; i++){
					appText += "	";
				}
			}
			bw.write(appText + newLine);
			
			appText = "";
			for(int s = 0; s < LMSAligned.length; s++){
				for(int i = 0; i < LMSAligned[0].length; i++){
					appText += "	" + constants.df0.format(i+1);
				}
			}
			bw.write(appText + newLine);
			
			for(int p = LMSAlignedPMin; p <= LMSAlignedPMax; p++){
				appText = constants.df0.format(p);
				for(int s = 0; s < LMSAligned.length; s++){
					for(int i = 0; i < LMSAligned[0].length; i++){
						appText += "	";
						if(!Double.isNaN(LMSAligned [s][i][p]) && LMSAligned [s][i][p] != 0.0){
							appText += constants.df6US.format(LMSAligned [s][i][p]);
						}									
					}
				}							
				bw.write(appText + newLine);
			}
			
			bw.close();
			fw.close();	
		} catch (IOException e) {
			String out = "";
			for(int err = 0; err < e.getStackTrace().length; err++){
				out += " \n " + e.getStackTrace()[err].toString();
			}
			this.logMessage("no results list generated - IOException:\n" + out, NOTIF);
			System.out.println(out);
		}		
	}
	
	/**
	 * 1st dimension > different graphs
	 * 2nd dimension > y points
	 * */
	private static void save2DArrayPlot(double [][] array, String label, String xLabel, String yLabel, String savePath, boolean logarithmic){
		double [] xValues = new double [array[0].length];
		for(int i = 0; i < xValues.length; i++)	xValues [i] = i;
		double yMax = Double.NEGATIVE_INFINITY;
		double max;
		for(int i = 0; i < array.length; i++){
			max = tools.getMaximum(array[i]);
			if(yMax < max) yMax = max;
		}
		Color c;
		Plot p;
		ImagePlus pImp;
		String legend = "";
		PlotWindow.noGridLines = true;
		
		p = new Plot(label, xLabel, yLabel);
		p.addLabel(0, 1, label);
		p.setAxisYLog(logarithmic);
		p.setSize(600, 400);
		p.setLimits(0, xValues.length-1, 0.0, yMax);		
		for(int i = 0; i < array.length; i++){
			c = new Color(red[(int)(i/((double)array.length+1)*red.length)], green[(int)(i/((double)array.length+1)*green.length)], blue[(int)(i/((double)array.length+1)*blue.length)]);
			p.setColor(c);
			p.addPoints(xValues,array[i],PlotWindow.LINE);
			legend += "" + i;
			legend += "\n";
		}
		p.addLegend(legend);
		p.setLimitsToFit(true);
		pImp = p.makeHighResolution("plot",1,true,false);
		IJ.saveAs(pImp,"PNG",savePath + ".png");
		pImp.changes = false;
		pImp.close();
		p.dispose();			
	  	System.gc();
	}
	
	private static double [][][] cropArrayIn3rdDim(double [][][] array, int min, int max){
		double [][][] out = new double [array.length][array[0].length][max-min+1];
		for(int i = 0; i < array.length; i++){
			for(int j = 0; j < array[0].length; j++){
				for(int k = min; k <= max; k++){
					out [i][j][k-min] = array [i][j][k]; 
				}
			}
		}
		return out;
	}
	
	private static double [][][] removeEmptyColumnsFromArray(double array [][][]){
		boolean empty = true;
		boolean notKeep [] = new boolean [array[0].length];
		int nr = 0;
		
		for(int j = 0; j < array[0].length; j++){
			empty = true;
				search: for(int i = 0; i < array.length; i++){
					for(int k = 0; k < array[0][0].length; k++){
					if(array[i][j][k] != 0.0 && !Double.isNaN(array[i][j][k])){
						empty = false;
						nr++;
						break search;
					}					
				}				
			}
			notKeep [j] = empty;
		}
		
		double out [][][] = new double [array.length][nr][array[0][0].length];
		int index = 0;
		for(int j = 0; j < array[0].length; j++){
			if(notKeep [j]){
				continue;				
			}
			for(int i = 0; i < array.length; i++){
				for(int k = 0; k < array[0][0].length; k++){
					out [i][index][k] = array[i][j][k];
				}
			}
			index ++;
		}
		return out;
	}
}
