package elearning;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

public class Help {
 
	private final Pattern keywordPattern=Pattern.compile("abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|instanceof|int|interface|long|native|new|packages|private|protected|public|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while");
	private final Pattern commentPattern=Pattern.compile("\\/\\/.*");
	//private final Pattern commentPattern=Pattern.compile("\\/\\/.*//\n");
  // private final Pattern literalPattern=Pattern.compile("\\([a-zA-z]\\)");
	//private final Pattern literalPattern=Pattern.compile("(\\p{Punct}| )*"+"test"+ "(\\p{Punct}| )*");
	private final Pattern literalPattern=Pattern.compile("\"[a-z0-9]*\"");
	private String []theWords={"abstract","assert","boolean","break","byte","case","catch","char","class",
			"const","continue","default","do","double","else","enum","extends","final","finally","float",
			"for","goto","if","implements","import","instanceof","int","interface","long","native","new",
			"packages","private","protected","public","return","short","static","strictfp","super","switch",
			"synchronized","this","throw","throws","transient","try","void","volatile","while"};
	
	private Pattern [] holder=new Pattern[reservedWords.length];
	//private static   Matcher matcher;
	public Help(){}
	

	
	public Pattern getPattern(){
		return this.keywordPattern;
	}
	
	public Pattern commentPattern(){
		return this.commentPattern;
	}
	
	public Pattern literalPattern(){
		return this.literalPattern;
	}
	public HashMap <String,Integer> hashJavaWords(String text){
		Scanner scan=new Scanner(text);
		
		HashMap <String,Integer> temp=new HashMap<String,Integer>();
		List <String> tempList=Arrays.asList(theWords);
		while(scan.hasNext()){
			String w=scan.next();
			Matcher matcher=getPattern().matcher(w);
			if(matcher.matches())
		for (int i=0; i<tempList.size(); i++){
			temp.put(tempList.get(i),new Integer(Collections.frequency(tempList, w)));
		}
		}
		return temp;
	}
	public  int countJavaWords(JTextPane editor){
		Scanner scan=new Scanner(editor.getText());
		int javaWords=0;
	
		
		while(scan.hasNext()){
			String temp=scan.next();
			Matcher match=getPattern().matcher(temp);
			//System.out.println(temp);
			if(match.matches()){
				
			   javaWords++;
		      //  System.out.print("MATCHES"); 
			}
			
			
		}
		
		
		//System.out.print("THE WORDS"+"= "+d);
		return javaWords;
	}
	
	private Matcher m(JTextPane e){
		Scanner scan=new Scanner(e.getText());
		//	Matcher m=matcher;
			boolean found=false;
			int i=0;
			Matcher matcher = null;
		//	for (int i=0; i<reservedWords.length; i++)
			 //pattern=Pattern.compile("class|void");//("(\\p{Punct}| )*"+reservedWords[i]+ "(\\p{Punct}| )*");
			Pattern pattern=Pattern.compile("abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|instanceof|int|interface|long|native|new|packages|private|protected|public|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while");
            while(scan.hasNext()){
            	String temp=scan.next();
            	 matcher=pattern.matcher(temp);
            	 System.out.println(temp+" "+matcher.start());
            }
            return matcher;
	}
	
	

	public void commnent(JTextPane e,StyledDocument d){
		Scanner scan=new Scanner(e.getText());
		Pattern pattern=Pattern.compile("\\/\\/.*");
		Pattern multiple = Pattern.compile("\\/\\*.*?\\*\\/",
                Pattern.DOTALL);

		while(scan.hasNext()){
			//if (scan.nextLine().startsWith("\\/\\/.*"));
			String temp=scan.next();
			Matcher m=pattern.matcher(temp);
			
			if(m.matches() ){
		    System.out.print(temp);
		    Scanner in=new Scanner(e.getText());
			int c=0;
		//	Scanner in=new Scanner(e);
			while(in.hasNextLine()){
				String line=in.nextLine();
				c++;
			}
		    //d.setCharacterAttributes(m.start(),m.end(),d.getStyle("comment"),false);
			}
		}
		
		while (scan.hasNextLine()){
			String temp=scan.nextLine();
			Matcher mult=multiple.matcher(temp);
			if(mult.matches()){
				System.out.println("DOUBLE VOMME");
			}
		}
	}
	

	

	
	public static int countLines(String e){
		Scanner s=new Scanner(e);
		int c=0;
		Scanner in=new Scanner(e);
		while(in.hasNextLine()){
			String line=in.nextLine();
			c++;
		}
		return c;
	}
	
	
}
