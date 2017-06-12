import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Search {
	//private static  String START="JAK2";
    //private static String END = "STAT5-P";
    
    private static  String START="Basal";
    static String END = "DesensitisedACh2";
    private static int depth=4;
    
    private static String exclude="";
    String include="";
    int index;
    String end;    
    private List<List<String>> result = new ArrayList<List<String>>();
    private static List<String> excludeList=new ArrayList<String>();
    private static Map<String, Integer> nodeCount = new HashMap();
	
	public static void main(String[] args) throws FileNotFoundException {  
		
		long startTime = System.currentTimeMillis();
		Graph graph=new Graph();
		XMLReader xmlReader=new XMLReader();
		String filePath="E:/testData/";

		File[] all_files=new File(filePath).listFiles();
		
		//Reading all edges file by file
		for(File f: all_files){
			xmlReader.addEdges(f.getName(),filePath);
		}
		
		String s1 = null;
		String s2 = null;
		String s3 = null;
		
		for(LinkedList<String> edge: xmlReader.getEdges()){
			//System.out.println(node);
			for(String e : edge){
				if(s1==null){
					s1=e;
				}
				else{
					s2=e;
					s3=s1;
					s1=null;
				}
			}
			//System.out.println(s3+" "+s2);
			if(s3.equalsIgnoreCase(s2)) {
				continue;
				//System.out.println("same");
			} else{
				graph.addEdge(s3, s2);
				//continue;
			}
			//graph.addEdge(s3, s2);			
		}
		//call connect graph
		graph.connectGraphs();
		
		//LinkedList<String> visited=new LinkedList<String>();
		//visited.add(START);
		
		//excludeList
		StringTokenizer st = new StringTokenizer(exclude,"@");
		while(st.hasMoreTokens()){
			excludeList.add(st.nextToken());
		}
		//System.out.println("Number Of Nodes in graph "+graph.getNodeCount());
		Search srh = new Search();
		LinkedList<String> visited=new LinkedList<String>();
		
		for(String startNode : graph.getStartNodes(START)){
			//System.out.println(startNode);
			if (!visited.isEmpty()) {
				visited.clear();
			}
			visited.add(startNode);
			if(!nodeCount.isEmpty()) {
				nodeCount.clear();
			}
			//graph.checkAdjecent(START);
			srh.depthFirst(graph, visited);
			
		}
		srh.filterResults();
		//srh.depthFirst(graph, visited);
		/*for(String startNode : graph.getStartNodes(START)){
			LinkedList<String> visited=new LinkedList<String>();
			visited.add(startNode);
			srh.depthFirstWithDepth(graph, visited);
		}*/
			
		long endTime = System.currentTimeMillis();
		
		System.out.println("Execution time: " + (endTime - startTime) / 1000.0 + " seconds");
		
		if(srh.getResult().isEmpty()){
			System.out.println("There is no path\n");
		}
		System.out.println("Number Of Paths "+srh.getResult().size());
		
		for(String s10: graph.getMap().keySet()){
			if(s10.contains(START) || s10.contains(END)){
				 //System.out.println(s10);			
			}
		}
		//srh.printResults(srh);
		//System.out.println(graph.getMap());
		
	
		
	}
	private void depthFirst(Graph graph, LinkedList<String> visited) {
		 
    	/*if(visited.isEmpty()){
    		visited.add(START);
    		String firstNode = graph.adjacentNodesFirstTime(visited.getLast());
    		visited.remove();
    		visited.add(firstNode);
    	}*/
    	LinkedList<String> nodes = graph.adjacentNodes(visited.getLast());
    
        // examine adjacent nodes
        for (String node : nodes) {
           int index1=node.indexOf("@");
           String end1=node.substring(0,index1);
            if (visited.contains(node)) {
                continue;
            }         
            if (end1.equalsIgnoreCase(END)) {
            	visited.addLast(node);
                result.add(new ArrayList<String>(visited));
                //printPath(visited);           
                //visited.remove(hops);
                visited.removeLast();
                break;
            }
        }
        for (String node : nodes) {
        	int index2=node.indexOf("@");
        	String end2=node.substring(0,index2);
        	int count;
         	if(nodeCount.get(node)==null){
         		nodeCount.put(node, 1);
         		//System.out.println(nodeCount);
         	}
         	else{
         		count=nodeCount.get(node);
         		count+=1;
         		nodeCount.put(node, count);
         	}
            if (visited.contains(node) || end2.equalsIgnoreCase(END) || nodeCount.get(node)>10000000) {
            	continue;
            }
            visited.addLast(node);
            depthFirst(graph, visited);
            visited.removeLast();  
        }
    }
	
	private void depthFirstWithDepth(Graph graph, LinkedList<String> visited) {
		 
	    	/*if(visited.isEmpty()){
	    		visited.add(START);
	    		 firstNode = graph.adjacentNodesFirstTimeDepth(visited.getLast());
	    		visited.remove();
	    		visited.add(firstNode);
	    	}*/
	    	LinkedList<String> nodes = graph.adjacentNodes(visited.getLast());
	    
	        // examine adjacent nodes
	        for (String node : nodes) {
	           int index1=node.indexOf("@");
	           String end1=node.substring(0,index1);
	            if (visited.contains(node)) {
	                continue;
	            }         
	            if (visited.size()==depth-1) {
	            	visited.addLast(node);
	                result.add(new ArrayList<String>(visited));
	                //printPath(visited);           
	                //visited.remove(hops);
	                visited.removeLast();
	            }
	        }
	        for (String node : nodes) {
	        	int index2=node.indexOf("@");
	        	String end2=node.substring(0,index2);
	        	int count;
	         	if(nodeCount.get(node)==null){
	         		nodeCount.put(node, 1);
	         		//System.out.println(nodeCount);
	         	}
	         	else{
	         		count=nodeCount.get(node);
	         		count+=1;
	         		nodeCount.put(node, count);
	         	}
	            if (visited.contains(node) ||  nodeCount.get(node)>100000 || visited.size()==depth-1) {
	            	continue;
	            }
	            if(visited.size()>=0 || visited.size()<=depth-2 ){
	            	visited.addLast(node);
	  	            depthFirstWithDepth(graph, visited);
	  	            visited.removeLast();  
	            }	          
	        }
	    }
	 
    public List<List<String>> getResult() {
		return result;
	}
    
	private void printPath(LinkedList<String> visited) {
    	//System.out.println(path);
    	//System.out.println(j);
        for (String node : visited) {
            System.out.print(node);
            System.out.print(" ");
        }
        System.out.println();
    }
	
	private void printResults(Search srh){
		for(List<String> it: srh.getResult()) {
			for(String s: it) {
				int index2=s.indexOf("@");
	        	String end=s.substring(0,index2);
				if(end.equalsIgnoreCase(END)){
					System.out.print(s + "");
				}
				else{
					System.out.print(s + "->");
				}				
			}
			System.out.println("");
		}
	}
	
	public void filterResults() {
		int index ;
		String node1,node2;
		int index1,index2;
		int count = 0;
		String node1_withoutFileId,node2_withoutFileId;
	
		
		for(List<String> it: result) {
			//index = result.indexOf(it);
			node1 = it.get(0);
			node2 = it.get(1);
			
			index1=node1.indexOf("@");
			index2=node2.indexOf("@");
			node1_withoutFileId = node1.substring(0,index1);
			node2_withoutFileId = node2.substring(0,index2);
			if(node1_withoutFileId.equalsIgnoreCase(node2_withoutFileId)) {
				//System.out.println(result.indexOf(it));
				//System.out.println(it);
				//result.remove(result.indexOf(it));
				count+=1;
			}
			
			//System.out.println(node1);
		}
		System.out.println(count);
	}
}
	   
	 


