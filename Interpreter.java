import java.io.*;
import java.util.*;

public class interpreter {
	public static void interpreter(String inFile, String outFile){
		try{ 
		
			//inFile = "D:\\Eclipse\\CSE305Interpreter\\src\\Test2\\input\\input15.txt";
			//inFile = "C:\\Users\\patel_000\\Desktop\\Checkpush.txt";
			FileReader Filescanner = new FileReader(inFile);
			FileWriter Filewriter = new FileWriter(outFile);
			BufferedWriter w = new BufferedWriter(Filewriter);
			BufferedReader scan = new BufferedReader(Filescanner);
			String line = "";
			ArrayList<String> theStack = new ArrayList<String>();
		
		while( (line = scan.readLine())!=null){ //advances scanner to next line and return the skipped part
			 theStack.add(line);
			
		}
		scan.close();
		//Done reading so now proceed to writing
		//System.out.print(Compute(theStack));
	
		w.write(Compute(theStack));
		w.close();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public static String Compute(ArrayList<String> list){
		HashMap<String,String> newlist = new HashMap<String,String>();
		HashMap<String,String> bindNames = new HashMap<String,String>();
		Stack<String> newStack = new Stack<String>();
		String retval = "";
		if(list.indexOf("let")>-1 && list.lastIndexOf("end")>0){
			ArrayList<String> LElist = new ArrayList<String>(list.subList(list.indexOf("let"), list.lastIndexOf("end")+1));
			//********************** LOOP 1 STARTS ************************
			for(int i =0; i<list.indexOf("let");i++){
				filterLines(list.get(i),newStack,newlist,bindNames);
			}
			//**********-----     END Of The LOOP 1  -----**********
			letEndScoping(LElist,newStack,newlist,bindNames,0);
			//********************** LOOP 2 STARTS ************************
			for(int i =list.lastIndexOf("end")+1; i<list.size();i++){
				filterLines(list.get(i),newStack,newlist,bindNames);	
			}
			//**********-----     END Of The LOOP 2 -----**********
		}
		else{
			for(int i =0; i<list.size();i++){
				filterLines(list.get(i),newStack,newlist,bindNames);
			}
		}
		retval = outputString(newStack,newlist);
		return retval;
	}
	
	 public static void filterLines(String val,Stack<String> newStack, HashMap<String,String> newlist,HashMap<String,String> bindNames){
		String comm[] = val.split(" ");
		
				//********************** PUSH BEGINS *****************
				if(comm.length > 1){
					if(comm[0].equals("push")){ 
						//String
						String ext =comm[1];
						for(int j =2; j <comm.length; j++){
							ext = ext + " "+ comm[j];
						}
						if(ext.startsWith("\"") && ext.endsWith("\"")){
							newlist.put(ext,"string");
							newStack.add(ext);
						}
						//Boolean
						else if(comm[1].equals(":true:") || comm[1].equals(":false:")){
							newlist.put(comm[1],"boolean");
							newStack.add(comm[1]);
						}
						//Integer
						else if(comm[1].matches("\\d+") || comm[1].matches("-" + "\\d+")){
							if(comm[1].equals("-0")){comm[1] = "0";}
							newlist.put(comm[1],"int");
							newStack.add(comm[1]);
						}
						//Name
						else if(comm[1].matches("^[a-zA-Z]+[a-zA-Z0-9]*$") ){
							newlist.put(comm[1],"name");
							newStack.add(comm[1]);
						}
						else {
							newStack.add(":error:");
						}
					}
					
					}//************   PUSH ENDS    ********************************************
				
				//*****************ARITHIMATIC********************************
					else {  //POP
							if(comm[0].equals("pop")){
								if(newStack.size()>0){
									newStack.pop();
								}
								else {newlist.put(":error:","error"); newStack.add(":error:");}
							}
							//ADD
							else if(comm[0].equals("add")){
								if(newStack.size()>1){
									String x1 = newStack.pop();String x2 = newStack.pop();
									if(checkType(x1,newlist,bindNames) == "int" && checkType(x2,newlist,bindNames)=="int"){
										int temp = Integer.parseInt(getVal(x2,newlist,bindNames)) + Integer.parseInt(getVal(x1,newlist,bindNames));
										newlist.put(""+temp, "int");
										newStack.add(""+temp);
										}
									else {
										newStack.add(x2);newStack.add(x1);
										newStack.add(":error:");
									}	
							}
								else {newlist.put(":error:","error"); newStack.add(":error:");}
							}
							//SUB
							else if(comm[0].equals("sub")){
								if(newStack.size()>1){
									String x1 = newStack.pop();String x2 = newStack.pop();
									if(checkType(x1,newlist,bindNames) == "int" && checkType(x2,newlist,bindNames)=="int"){
										int temp = Integer.parseInt(getVal(x2,newlist,bindNames)) - Integer.parseInt(getVal(x1,newlist,bindNames));
										newlist.put(""+temp, "int");
										newStack.add(""+temp);
										}
									else {
										newStack.add(x2);newStack.add(x1);
										newStack.add(":error:");
									}	
							}
								else {newlist.put(":error:","error"); newStack.add(":error:");}
							}
							//MUL
							else if(comm[0].equals("mul")){
								if(newStack.size()>1){
									String x1 = newStack.pop();String x2 = newStack.pop();
									if(checkType(x1,newlist,bindNames) == "int" && checkType(x2,newlist,bindNames)=="int"){
										int temp = Integer.parseInt(getVal(x2,newlist,bindNames)) * Integer.parseInt(getVal(x1,newlist,bindNames));
										newlist.put(""+temp, "int");
										newStack.add(""+temp);
										}
									else {
										newStack.add(x2);newStack.add(x1);
										newStack.add(":error:");
									}	
							}
								else {newlist.put(":error:","error"); newStack.add(":error:");}
							}
							//DIV
							else if(comm[0].equals("div")){
								if(newStack.size()>1){
									String x1 = newStack.pop();String x2 = newStack.pop();
									if(checkType(x1,newlist,bindNames) == "int" && checkType(x2,newlist,bindNames)=="int" && !getVal(x1,newlist,bindNames).equals("0")){
										int temp = Integer.parseInt(getVal(x2,newlist,bindNames)) / Integer.parseInt(getVal(x1,newlist,bindNames));
										newlist.put(""+temp, "int");
										newStack.add(""+temp);
										}
									else {
										newStack.add(x2);newStack.add(x1);
										newStack.add(":error:");
									}	
							}
								else {newlist.put(":error:","error"); newStack.add(":error:");}
							}
							//REM
							else if(comm[0].equals("rem")){
								if(newStack.size()>1){
									String x1 = newStack.pop();String x2 = newStack.pop();
									if(checkType(x1,newlist,bindNames) == "int" && checkType(x2,newlist,bindNames)=="int" && !getVal(x1,newlist,bindNames).equals("0")){
										int temp = Integer.parseInt(getVal(x2,newlist,bindNames)) % Integer.parseInt(getVal(x1,newlist,bindNames));
										newlist.put(""+temp, "int");
										newStack.add(""+temp);
										}
									else {
										newStack.add(x2);newStack.add(x1);
										newStack.add(":error:");
									}	
							}
								else {newlist.put(":error:","error"); newStack.add(":error:");}
							}
							//NEG
							else if(comm[0].equals("neg")){
								if(newStack.size()>0){
									String x1 = newStack.pop();
									if(checkType(x1,newlist,bindNames) == "int"){
										int temp = -1* Integer.parseInt(getVal(x1,newlist,bindNames));
										newlist.put(""+temp, "int");
										newStack.add(""+temp);
										}
									else {
										newStack.add(x1);
										newStack.add(":error:");
									}	
							}
								else {newStack.add(":error:");}
							}
							//SWAP
							else if(comm[0].equals("swap")){
								if(newStack.size()>1){
									String x1 = newStack.pop();String x2 = newStack.pop();
									String temp = x1;
									x1 = x2;
									x2 = temp;
									newStack.add(x2);newStack.add(x1);
							}
								else {newStack.add(":error:");}
							}
							//CAT
							else if(comm[0].equals("cat")){
								if(newStack.size()>1){
									String x1 = newStack.pop();String x2 = newStack.pop();
									if(checkType(x1,newlist,bindNames) == "string" && checkType(x2,newlist,bindNames)=="string"){
										
										String temp = getVal(x2,newlist,bindNames).substring(1, getVal(x2,newlist,bindNames).length()-1) + "" + getVal(x1,newlist,bindNames).substring(1, getVal(x1,newlist,bindNames).length()-1);
										newlist.put("\""+temp+"\"", "string");
										newStack.add("\""+temp+"\"");
										}
									else {
										newStack.add(x2);newStack.add(x1);
										newStack.add(":error:");
									}	
							}
								else {newStack.add(":error:");}
							}
							//AND
							else if(comm[0].equals("and")){
								if(newStack.size()>1){
									String x1 = newStack.pop();String x2 = newStack.pop();
									if(checkType(x1,newlist,bindNames) == "boolean" && checkType(x2,newlist,bindNames)=="boolean"){
										boolean temp1 = false,temp2 = false;
										if(getVal(x1,newlist,bindNames).equals(":true:")){
											temp1 = true;
										}
										if(getVal(x2,newlist,bindNames).equals(":true:")){
											temp2 = true;
										}
										newlist.put(":"+(temp1&&temp2)+":", "boolean");
										newStack.add(":"+(temp1&&temp2)+":");
										}
									else {
										newStack.add(x2);newStack.add(x1);
										newStack.add(":error:");
									}	
							}
								else {newStack.add(":error:");}
							}
							//OR
							else if(comm[0].equals("or")){
								if(newStack.size()>1){
									String x1 = newStack.pop();String x2 = newStack.pop();
									if(checkType(x1,newlist,bindNames) == "boolean" && checkType(x2,newlist,bindNames)=="boolean"){
										boolean temp1 = false,temp2 = false;
										if(getVal(x1,newlist,bindNames).equals(":true:")){
											temp1 = true;
										}
										if(getVal(x2,newlist,bindNames).equals(":true:")){
											temp2 = true;
										}
										newlist.put(":"+(temp1||temp2)+":", "boolean");
										newStack.add(":"+(temp1||temp2)+":");
										}
									else {
										newStack.add(x2);newStack.add(x1);
										newStack.add(":error:");
									}	
							}
								else {newlist.put(":error:","error"); newStack.add(":error:");}
							}
							//NOT
							else if(comm[0].equals("not")){
								if(newStack.size()>0){
									String x1 = newStack.pop();
									if(checkType(x1,newlist,bindNames) == "boolean"){
										boolean temp1 = false;
										if(getVal(x1,newlist,bindNames).equals(":true:")){
											temp1 = true;
										}
										newlist.put(":"+(!temp1)+":", "boolean");
										newStack.add(":"+(!temp1)+":");
										}
									else {
										newStack.add(x1);
										newStack.add(":error:");
									}	
							}
								else {newlist.put(":error:","error"); newStack.add(":error:");}
							}
							//EQUAL
							else if(comm[0].equals("equal")){
								if(newStack.size()>1){
									String x1 = newStack.pop();String x2 = newStack.pop();
									if(checkType(x1,newlist,bindNames) == "int" && checkType(x2,newlist,bindNames)=="int"){
										boolean temp = Integer.parseInt(getVal(x2,newlist,bindNames)) == Integer.parseInt(getVal(x1,newlist,bindNames));
										newlist.put(":"+temp+":", "boolean");
										newStack.push(":"+temp+":");
										}
									else {
										newStack.add(x2);newStack.add(x1);
										newStack.add(":error:");
									}	
							}
								else {newlist.put(":error:","error"); newStack.add(":error:");}
							}
							//LESS THAN
							else if(comm[0].equals("lessThan")){
								if(newStack.size()>1){
									String x1 = newStack.pop();String x2 = newStack.pop();
									if(checkType(x1,newlist,bindNames) == "int" && checkType(x2,newlist,bindNames)=="int"){
										boolean temp = Integer.parseInt(getVal(x2,newlist,bindNames)) < Integer.parseInt(getVal(x1,newlist,bindNames));
										newlist.put(":"+temp+":", "boolean");
										newStack.add(":"+temp+":");
										}
									else {
										newStack.add(x2);newStack.add(x1);
										newStack.add(":error:");
									}	
							}
								else {newlist.put(":error:","error"); newStack.add(":error:");}
							}
							//BIND
							else if(comm[0].equals("bind")){
								if(newStack.size()>1){ 
									String x1 = newStack.pop();String x2 = newStack.pop();
									if(newlist.get(x2)=="name" && (newlist.get(x1)=="int" || newlist.get(x1)=="string"||newlist.get(x1)=="boolean" ||newlist.get(x1)=="unit"||newlist.get(x1)=="name")){
										if(newlist.get(x1)=="name"){
											if(bindNames.get(x1)!=null){
												bindNames.put(x2,bindNames.get(x1));
												newlist.put(":unit:", "unit");
												newStack.add(":unit:");
											}
											else{
												newStack.add(x2);newStack.add(x1);
												newStack.add(":error:");
											}
										}
										else{
											bindNames.put(x2,x1);
											newlist.put(":unit:", "unit");
											newStack.add(":unit:");
										}	
										}
									else {
										newStack.add(x2);newStack.add(x1);
										newStack.add(":error:");
									}	
							}
								else { newStack.add(":error:");}
							}
							//IF
							else if(comm[0].equals("if")){
								if(newStack.size()>2){
									String x1 = newStack.pop();String x2 = newStack.pop();String x3 = newStack.pop();
									if(checkType(x3,newlist,bindNames)=="boolean"){
										if(getVal(x3,newlist,bindNames).equals(":true:")){
											newStack.add(x2);
										}
										else
										newStack.add(x1);
										}
									else {
										newStack.add(x2);newStack.add(x1);
										newStack.add(":error:");
									}	
							}
								else {newStack.add(":error:");}
							}
							//QUIT
							else if(comm[0].equals("quit")){
								return;
							}
							//ERROR STRINGS
							else{
								newStack.add(":error:");
							}
							
							//**********-----     END Of The ARITHMATIC  -----**********	
					}
	 }
	
	public static String outputString(Stack<String> list, HashMap<String,String> theMap){
		String retval = "";
		while(!list.isEmpty()){
			String woq = list.pop(); //for stack without quotes
			if(theMap.get(woq) == "string"){
				woq = woq.substring(1,woq.length()-1);
			}
			retval = retval + woq + "\n" ;
		}
		return retval;	
	}
	
	public static void letEndScoping(ArrayList<String> LElist,Stack<String> newStack, HashMap<String,String> newlist,HashMap<String,String> bindNames,int rec){
		HashMap<String,String> newbinding = new HashMap<String,String>(bindNames);
		HashMap<String,String> hMap = newlist;
		Stack<String> LEStack = new Stack<String>();
		int parser = 0 + rec;
		
		if(LElist.size()<0){
			return;
		}
		else{
			if(LElist.get(0).equals("let")){
				ArrayList<String> newLE = new ArrayList<String>(LElist.subList(1, LElist.size()));
				
				//*******    LET-LET Nested   **************/
				if(newLE.indexOf("let")<newLE.indexOf("end") && newLE.indexOf("let")>-1 && newLE.indexOf("end")>0){
					for(int i =0; i<newLE.indexOf("let");i++){
						filterLines(newLE.get(i),LEStack,hMap,newbinding);
						}
					//System.out.println("This is LE before "+LEStack.peek());
					letEndScoping(new ArrayList<String>(newLE.subList(newLE.indexOf("let"), newLE.size())),LEStack,newlist,newbinding,rec);
					//System.out.println("This is LE after "+LEStack.peek());
					letEndScoping(new ArrayList<String>(newLE.subList(newLE.indexOf("end"), newLE.size())),LEStack,newlist,bindNames,rec);
					//System.out.println("This is LE later "+LEStack.peek());
					if(!LEStack.isEmpty()){
					String x = LEStack.pop();
					newStack.push(x);newlist.put(x, hMap.get(x));
					}
				return;
				}
				
				//***************LET-END type  ******************/
				else if((newLE.indexOf("end")<newLE.indexOf("let")) || (newLE.indexOf("end")>-1||newLE.indexOf("let")<0)){
				//System.out.println("Here");
					for(int i =0; i<newLE.indexOf("end");i++){
					filterLines(newLE.get(i),LEStack,newlist,newbinding);
				}
					if(!LEStack.isEmpty()){
						String x = LEStack.pop();
						newStack.push(x);newlist.put(x, hMap.get(x));
					}
					//System.out.println(newStack.peek() + " is " + parser+" what let -end and rec is " + rec);
					return;
					//letEndScoping(new ArrayList<String>(newLE.subList(newLE.indexOf("end"), newLE.size())),newStack,newlist,bindNames,rec--);
				}
				
				//**********************************************************/
				else{
					return;
					} 
			}
			else if(LElist.get(0).equals("end")){
				
				if(LElist.size()>1){
					ArrayList<String> newLE = new ArrayList<String>(LElist.subList(1, LElist.size()));
					
					//************* ONLY END - END edges ***************/
					if((newLE.indexOf("end")>-1) && (newLE.indexOf("end")<newLE.indexOf("let") || newLE.indexOf("let")<0)){
						for(int i =0; i<newLE.indexOf("end");i++){
							
						filterLines(newLE.get(i),newStack,newlist,newbinding);
					}
					//letEndScoping(new ArrayList<String>(newLE.subList(newLE.indexOf("end"), newLE.size())),newStack,newlist,bindNames);
					return;
					}
					
					//*************  END - LET edges ***************/
					else if(newLE.indexOf("let") < newLE.indexOf("end") && newLE.indexOf("let")>-1){
						for(int i =0; i<newLE.indexOf("let");i++){
						filterLines(newLE.get(i),newStack,newlist,bindNames);
					}
					letEndScoping(new ArrayList<String>(newLE.subList(newLE.indexOf("let"), newLE.size())),newStack,newlist,bindNames,rec);
					return;
					}
					
					//*********************************************************/
				}
				else{
					return;
				}
				}
				
		}
		
	}
	
	public static String checkType(String str, HashMap<String,String> actual,HashMap<String,String> binder){
		String retval = actual.get(str);
		
		if(actual.get(str) == "name"){
			retval = actual.get(binder.get(str));
		}
		return retval;
	}
	
	public static String getVal(String str, HashMap<String,String> actual,HashMap<String,String> binder){
		String retval = str;
		if(actual.get(str) == "name"){
			retval = binder.get(str);
		}
		return retval;
	}
	public static void letEndScoping(ArrayList<String> LElist,Stack<String> newStack, HashMap<String,String> newlist,HashMap<String,String> bindNames){
		HashMap<String,String> newbinding = new HashMap<String,String>(bindNames);
		HashMap<String,String> hMap = newlist;
		Stack<String> LEStack = new Stack<String>();
	}

}
