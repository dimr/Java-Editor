package elearning;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.Doc;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class EEFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1709009137090877913L;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	private EEMenuBar menuBar;
	private EETextPane editor;
	private EEConsole console;
	private EEStatusBar statusBar;
	private File file;
	private Words wordsCounter;
	//private Lines linesCounter;
	private Help helper=new Help();
	private String temp;
	private StyledDocument doc;
	
	public static final String NL = System.getProperty("line.separator"); 
	//CONSTRUCTOR  ****SUPER CLASS
	public EEFrame() throws HeadlessException {
		super("Elearn Editor");
		
		JScrollPane scrollPane;
		
		layout = new GridBagLayout();
		setLayout(layout);
		
		constraints = new GridBagConstraints();
		
		menuBar = new EEMenuBar();
		setJMenuBar(menuBar);
		console=new EEConsole();
		editor = new EETextPane();
	//	applyPaneStyles();
		scrollPane = new JScrollPane(editor);
		scrollPane.setBorder(new TitledBorder("Editor"));
		
		setConstraints(1, 100, GridBagConstraints.BOTH);
		addComponent(scrollPane, 0, 0, 1, 1);
		
		console = new EEConsole();

		
		scrollPane = new JScrollPane(console);
		scrollPane.setBorder(new TitledBorder("Console"));
		
		setConstraints(1, 40, GridBagConstraints.BOTH);
		addComponent(scrollPane, 1 ,0 ,1, 1);
		
		statusBar = new EEStatusBar();
		setConstraints(1, 0, GridBagConstraints.BOTH);
		addComponent(statusBar, 2, 0, 1, 1);
	 
		file = null;

		
	}
	public JTextPane getConsolePane(){
		return this.console;
	}
	
	public JTextPane getTextPane() {
		return this.editor;
	}

	public void setLines(int lines) {
		this.statusBar.setLines(lines);
	}
	
	public void setWords(int words) {
		this.statusBar.setJavaWords(words);
	}

	public Words Words(){
		return new Words();
	}


	
	
	
	
	private void setConstraints(int weightx, int weighty, int fill) {
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.fill = fill;
	}
	
	private void addComponent(Component component, int row, int column, int width, int height) {
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		layout.setConstraints(component, constraints);
		add(component);
	}
	
	
	/**
	 * INNER CLASSES
	 *------------------------------------------------------------------------------------------------------------------------------------
	 * 
	 */
	
	private class EEMenuBar extends JMenuBar {
		
		
		private static final long serialVersionUID = -2176624051362992835L;
		private JMenu fileMenu, compilationMenu;
		private JMenuItem newItem, openItem, saveItem, saveAsItem, compileItem;
		private JFileChooser fileChooser,newChooser;
		private FileNameExtensionFilter filter=new FileNameExtensionFilter("*.java","java");
		File openFile;//,newFile;
		File newFile;
		int theValue;
		String absolutePath;
	    //JavaCompiler compiler,nextCompiler;
	   
	    FileWriter writer;
		public EEMenuBar() {
			super();
			
			fileMenu = new JMenu("File");
			
			newItem = new JMenuItem("New");
			
			newItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					newItemAction(arg0);
				}
			});
			
			fileMenu.add(newItem);
			
			openItem = new JMenuItem("Open");
			
			openItem.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					openItemAction(arg0);
				}
				
			});
			
			fileMenu.add(openItem);
			
			saveItem = new JMenuItem("Save");
			saveItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent ee) {
					saveItemAction(ee);
				}
			});
			
			fileMenu.add(saveItem);
			
			saveAsItem = new JMenuItem("Save As");
			
			saveAsItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					saveAsItemAction(e);
				}
				
			});
			
			
			fileMenu.add(saveAsItem);
			
			add(fileMenu);
			
			compilationMenu = new JMenu("Compilation");
			
			compileItem = new JMenuItem("Compile");
			
			compileItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					compilationItemAction(e);
					//System.out.println(Compiler.class.getCanonicalName());
				}
				
			});
			
			compilationMenu.add(compileItem);
			
			add(compilationMenu);
			
		}
		private void newItemAction(ActionEvent e){
			newChooser=new JFileChooser("new");
			newChooser.setFileFilter(filter);
			theValue=newChooser.showDialog(this, "Create new..");
			
			if(theValue==newChooser.APPROVE_OPTION){
				try{
					editor.setText("public class "+newChooser.getSelectedFile().getName()+" {"+"\n\n\n\n\n}");  //.getName()
					
					 writer=new FileWriter(new File(newChooser.getSelectedFile().getAbsolutePath())+".java");
					newFile=newChooser.getSelectedFile();
					//File file=new File(newFile);
				}catch(IOException ed){}
					
				
			}
			 
		}
		
		private void openItemAction(ActionEvent e){
			fileChooser=new JFileChooser();
			fileChooser.setFileFilter(filter);
			//System.out.print(editor.getLineCount());
			theValue=fileChooser.showOpenDialog(this);
			//EEStatusBar.this.lines.setText(Help.countLines(Integer.toString((editor.getText()));
			if(theValue==fileChooser.APPROVE_OPTION){
				openFile=fileChooser.getSelectedFile();
			try{
				
				editor.read(new FileReader(openFile.getAbsolutePath()), null);
				
				
		        // fileChooser.setFileFilter(filter);
				
			}catch(IOException ef){
			}
			}else if(theValue==fileChooser.CANCEL_OPTION){
				fileChooser.cancelSelection();
			}
			
			
			}
		
		private void saveItemAction(ActionEvent e){
           String command=((AbstractButton) e.getSource()).getText();//e.getSource().toString();
			
			
			
			try {
				//if(command.equals(newItem.getValue(NAME))){
				if(theValue==fileChooser.APPROVE_OPTION ){
					if(command.equals("Open") |command.equals("Save")){
	
						 writer=new FileWriter(new File(fileChooser.getSelectedFile().getAbsolutePath()));
				                 writer.write(editor.getText());
				                 writer.close();
					//}
				}else if(command.equals("New")){
					
					writer=new FileWriter(absolutePath);
					
					writer.write(editor.getText());
					writer.close();
				}
					
				}
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
		}
		
		private void saveAsItemAction(ActionEvent e){
			com.itextpdf.text.Document d=new com.itextpdf.text.Document();
			String command=((AbstractButton) e.getSource()).getText();
			if (command == "Open" | command.equals("new"));

//			
			
			fileChooser=new JFileChooser();
			fileChooser.setDialogTitle("Save As");
			//fileChooser.setCurrentDirectory(fileChoose);	
			fileChooser.setFileFilter(filter);
			theValue=fileChooser.showSaveDialog(this);
			
			if(theValue==fileChooser.APPROVE_OPTION){
				try{
					FileWriter writer=new FileWriter(new File(fileChooser.getSelectedFile().getAbsolutePath()));//
					writer.write(editor.getText());
					writer.close();
					PdfWriter.getInstance(d, new FileOutputStream(fileChooser.getSelectedFile().getAbsolutePath()));
					String t=getTextPane().getText();
					
					
					d.open();
					d.add(new Paragraph(t));
					d.close();
				}catch (Exception ee){
					
				}
					
				}
			}
		
		
		private void compilationItemAction(ActionEvent e) {
		
			
		Style success=EEFrame.this.getConsolePane().getStyle("success");
		Style er=EEFrame.this.getConsolePane().getStyle("error");
			
		
               File file;
               FileReader f;
               BufferedReader br;
               OutputStream error;
               List <String>s;
				 if(newChooser==null && fileChooser!=null){
					 try {
					 JavaCompiler compiler=ToolProvider.getSystemJavaCompiler();
				   
				     
			           error=new FileOutputStream("text.txt");
			           int result= compiler.run(null, null, error,"-verbose" ,fileChooser.getSelectedFile().getAbsolutePath());
						if(result==0){
					    
					        file=new File("text.txt");
					         f=new FileReader(file);
					         br=new BufferedReader(f);
					      s=new ArrayList<String>();
					      // String temp=br.readLine();
					         while((temp=br.readLine())!= null){
					        	 s.add(temp);
					        	// System.out.println(sss);
					         }
					         String text=s.toString();
					         StyledDocument cons=EEFrame.this.getConsolePane().getStyledDocument();
					         System.out.print(result);
					            cons.setCharacterAttributes(0, 100, success, true);
					        	 EEFrame.this.console.compilerText(text +" \n\n\nCOMPILATION SUCCESFUL");
					        	 System.out.print(text);
					         
					         file.delete();
					     }else{
					    	 file=new File("text.txt");
					    	 f=new FileReader(file);
					    	 br=new BufferedReader(f);
					    	 int r=compiler.run(null, null,error,"-verbose",fileChooser.getSelectedFile().getAbsolutePath());
					    	 error.close();
					    	 s=new ArrayList<String>();
					    	 while((temp=br.readLine())!=null){
					    		 s.add(temp);
					    	 }
					    	 EEFrame.this.console.compilerText(s.toString()+"\n\n\nCOMPILATION FAILED");
					    	 file.delete();
					     }
					//	file.delete();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				     
				     
				     
				 }
				 
				 if( newChooser!=null &&fileChooser==null){
					 try {
						// compiler.run(null, null, null ,newChooser.getSelectedFile().getAbsolutePath()+".java");
						 JavaCompiler compiler=ToolProvider.getSystemJavaCompiler();
						// compiler.run(null, null, null ,newChooser.getSelectedFile().getAbsolutePath()+".java");
						error=new FileOutputStream("text.txt");
						int r=compiler.run(null,null,error,"-verbose", newChooser.getSelectedFile().getAbsolutePath()+".java");
						System.out.print(r);
						if (r==0){
							file=new File("text.txt");
					         f=new FileReader(file);
					         br=new BufferedReader(f);
					         s=new ArrayList<String>();
					     
					      // String temp=br.readLine();
					         while((temp=br.readLine())!= null){
					        	 s.add(temp);
					        	// System.out.println(temp);
					        	// System.out.println(sss);
					         }
					        String text=s.toString();
					         StyledDocument cons=EEFrame.this.getConsolePane().getStyledDocument();
					       StyledDocument c=EEFrame.this.getConsolePane().getStyledDocument();
					            c.setCharacterAttributes(0, 10, success, true);
					        	 EEFrame.this.console.compilerText(text+"\n\n\nCOMPILATION SUCCESFUL");
					        	// System.out.print(result);
					        	 file.delete();
						} else{
							file=new File("text.txt");
					    	 f=new FileReader(file);
					    	 br=new BufferedReader(f);
					    	 int rr=compiler.run(null, null,error,"-verbose",newChooser.getSelectedFile().getAbsolutePath()+".java");
					    	 error.close();
					    	 s=new ArrayList<String>();
					    	 while((temp=br.readLine())!=null){
					    		 s.add(temp);
					    	 }
					    	 EEFrame.this.console.compilerText(s.toString()+"\n\n\nCOMPILATION FAILED");
					    	 file.delete();
						}
						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// compiler.run(null, null, null ,newChooser.getSelectedFile().getAbsolutePath());
					
				 }
					

		}
		
		
	}//  INNER CLASSIS
	
	
	private class EETextPane extends JTextPane {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -7437561302249475096L;

		public EETextPane() {
			super();
		//	doc=editor.getStyledDocument();
			Style def = StyleContext.getDefaultStyleContext().getStyle( StyleContext.DEFAULT_STYLE );
			StyleConstants.setForeground(def, Color.BLACK);
			StyleConstants.setFontFamily(def, "Courier");
			StyleConstants.setFontSize(def, 12);
			 
			Style keyword = addStyle("keyword", def);
			StyleConstants.setForeground(keyword, Color.BLUE);
			
			Style literal = addStyle("literal", def);
			StyleConstants.setForeground(literal, Color.ORANGE);
			
			Style comment = addStyle("comment", def);
			StyleConstants.setForeground(comment, Color.GREEN);
			
		}
		


		
		public void compilerText(String t){
			this.setText(t);
		}
			
		}



	
	private class EEConsole extends JTextPane {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -5968199559991291905L;

		public EEConsole() {
			super();
			
			//add styles to fdocument
			Style def = StyleContext.getDefaultStyleContext().getStyle( StyleContext.DEFAULT_STYLE );
			StyleConstants.setForeground(def, Color.BLACK);
			StyleConstants.setFontFamily(def, "Courier");
			StyleConstants.setFontSize(def, 12);
			 
			Style error = addStyle("error", def);
			StyleConstants.setForeground(error, Color.RED);
			
			Style success = addStyle("success", def);
			StyleConstants.setForeground(success, Color.GREEN);
			//setText(new Integer((EEFrame.this.menuBar.k())).toString());
			
			
			
		}
		
		public void compilerText(String t){
			 this.setText(t);
		}
		
		
	}
	
	private class EEStatusBar extends JPanel {

	
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 185007911993347696L;
		private BoxLayout layout;
		private JLabel linesLabel, lines, wordsLabel, words;
		
		public EEStatusBar() {
			super();
			
			layout = new BoxLayout(this, BoxLayout.X_AXIS);
			setLayout(layout);
			
			linesLabel = new JLabel("Lines : ");
			linesLabel.setAlignmentX(LEFT_ALIGNMENT);
			add(linesLabel);
			//words.setText(Integer.toString(Help.count(editor.getText())));  //edo nullPointerEception
			lines = new JLabel("");
			lines.setAlignmentX(LEFT_ALIGNMENT);
			add(lines);
			
			
			//lines.setText(Help.countLines(editor.getText()));
			add(Box.createRigidArea(new Dimension(10,10)));
			
			wordsLabel = new JLabel("Java Words : ");
			wordsLabel.setAlignmentX(LEFT_ALIGNMENT);
			add(wordsLabel);
			
			 
			words = new JLabel("");
			words.setAlignmentX(RIGHT_ALIGNMENT);
			add(words);
			
	
		
		}
		
		public void setLines(int lines) {
			this.lines.setText(String.valueOf(lines));
			
		}
		
		
		public void setJavaWords(int words) {	
			this.words.setText(String.valueOf(words));//(Help.countWords(getTextPane().getText())));
			
		}
		
		
		
	}//  EEStatusBar
	
	
	private class Words implements Runnable{
        

		
		public Words(){
		
		}
		@Override
		public void run()
		    {
		      Style keywords = EEFrame.this.getTextPane().getStyle("keyword");
		       Style comment = EEFrame.this.getTextPane().getStyle("comment");
		       Style literal = EEFrame.this.getTextPane().getStyle("literal");
		       while (true) {
		         StyledDocument doc = EEFrame.this.getTextPane().getStyledDocument();
		         String text = EEFrame.this.getTextPane().getText();
		         StringTokenizer st = new StringTokenizer(text);
		         int currentPos = 0;
		         int jWords = 0;
		         StringBuffer b = new StringBuffer();
		         Matcher m = EEFrame.this.helper.getPattern().matcher(b.append(text));
		         while (st.hasMoreTokens()) {
		           String nextToken = st.nextToken();
		           if (nextToken.matches(EEFrame.this.helper.getPattern().toString()))
		           {
		             doc.setCharacterAttributes(currentPos, nextToken.length(), keywords, true);
		 
		             jWords++;
		           }
		 
		           currentPos += nextToken.length() + 1;
		           EEFrame.this.setWords(jWords);
		         }
		 
		         StringTokenizer s = new StringTokenizer(text, EEFrame.NL);
		         int pos = 0;
		         int lines = 1;
		 
		         while (s.hasMoreTokens()) {
		           String token = s.nextToken();
		 
		           if (token.matches(EEFrame.this.helper.commentPattern().toString()))
		           {
		             doc.setCharacterAttributes(pos, token.length(), comment, true);
		           }
		 
		           pos += token.length() + 1;
		           EEFrame.this.setLines(lines++);
		         }
		 
		         StringTokenizer f = new StringTokenizer(text);
		         int p = 0;
		         while (f.hasMoreTokens()) {
		           String next = f.nextToken();
		           if (next.matches(EEFrame.this.helper.literalPattern().toString()))
		           {
		             doc.setCharacterAttributes(p, next.length(), literal, true);
		           }
		           p += next.length() + 1;
		         }
		         try {
		           Thread.sleep(250L);
		         }
		         catch (InterruptedException localInterruptedException)
		         {
		         }
		       }
		     }
		   }
		 }
