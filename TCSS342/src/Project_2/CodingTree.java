package Project_2;

import java.util.*;

public class CodingTree {

	public Map<Character, String> codes;
	
	public List<Byte> bits;
	
	private final Node myRoot;
	
	public CodingTree(String message){
		
		myRoot = buildTree(countFreq(message));
		codes = new HashMap<Character, String>();
		buildCode(myRoot, "");
		bits = new ArrayList<Byte>();
		encode(message);
		
	}
	
	
	private void buildCode(Node t, String code){
		
		if(t ==null){
		
			return;
		
		}else if(t != null && (t.left == null && t.right == null)){
			
			codes.put(t.charNode, code);
			return;
			
		}
		
		buildCode(t.left, code + "0");
		buildCode(t.right, code + "1");
		
	}
	
	private Map<Character, Integer> countFreq(String msg){
		
		if(msg == null){
			
			return null;
			
		}
		
		Map<Character, Integer> freqData = new HashMap<Character, Integer>();
		
		int msgLength=msg.length();
		char inputChar;
		
		for( int i = 0; i<msgLength; i++){
			
			inputChar = msg.charAt(i);
			
			if(freqData.containsKey(inputChar))  {
			
				freqData.put(inputChar, freqData.get(inputChar)+1);
			
			} else{
				
				freqData.put(inputChar, 1);
			
			}
			
		}
		
		return freqData;
	
	}
	
	private void encode(String msg){
		
		if (msg == null || msg.isEmpty()) {
			return;
		}
		
		final StringBuilder sb = new StringBuilder();
		int code;
		
		for (int i = 0; i < msg.length();) {
			
			sb.append(codes.get(msg.charAt(i++)));
			
			while (sb.length() < 8 && i < msg.length()) {
				
				sb.append(codes.get(msg.charAt(i++)));
			
			}
			
			while (sb.length() >= 8) {
				code = Integer.parseInt(sb.substring(0, 8), 2);
				bits.add((byte) code);
				sb.delete(0, 8);
			}
		}
		
		while (sb.length() != 8) {
			
			sb.append('0');
		
		}
		
		code = Integer.parseInt(sb.substring(0, 8), 2);
		bits.add((byte) code);
	
	}
	
	private Node buildTree(Map<Character, Integer> charFreq){
		
		if(charFreq == null || charFreq.isEmpty()){
			
			return null;
			
		}
		
		Node root, left, right;
		
		Iterator<Character> iter = charFreq.keySet().iterator();
		
		MyPriorityQueue<Node> pq = new MyPriorityQueue<Node>();
		
		char indexChar;
		
		while(iter.hasNext()){
			
			indexChar = iter.next();
			pq.insert(new Node(indexChar, charFreq.get(indexChar)));
			
		}
		
		while(pq.size() != 1){
			
			left = pq.deleteMin();
			right = pq.deleteMin();
			root = new Node('\0', left.charFreq + right.charFreq);
			root.left = left;
			root.right = right;
			pq.insert(root);
		
		}
		
		return pq.deleteMin();
	}
	
	private class Node implements Comparable<Node>{
		
		char charNode;
		
		int charFreq;
		
		Node left;
		
		Node right;
		
		public Node(char inputChar, int inputFreq){
			
			this.charNode = inputChar;
			this.charFreq = inputFreq;
			
			left = null;
			right = null;
			
		}

		@Override
		public int compareTo(Node o) {
			if(o == null){
				throw new NullPointerException();
			}
			return charFreq - o.charFreq;
		}
		
	}
	
}
