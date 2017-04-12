package Project_2;

import java.util.Arrays;

public class MyPriorityQueue<Type extends Comparable<Type>>{

	private int size;
	
	private Type [] items;
	
	private static final int INTIAL_SIZE = 11;
	
	@SuppressWarnings("unchecked")
	public MyPriorityQueue(){
		
		items = (Type[]) new Comparable[INTIAL_SIZE];
		size = 0;
		
	}
	
	public int size(){
		
		return size;
	
	}
	
	public boolean isEmpty(){
		
		if(size == 0){
			return true;
		}
		else{
			return false;
		}
		
	}
	
	public String toString(){
		
		return Arrays.toString(items);
	
	}
	
	private int percolateUp(int hole, Type insertItem){
		
		while(hole > 1 && insertItem.compareTo(items[hole/2]) < 0){
			items[hole] = items[hole/2];
			hole = hole / 2;
		}
		
		return hole;
	}
	
	private int percolateDown(int hole, Type value){
		
		int target;
		
		while(hole *2 <= size){
			
			int leftIndex = hole *2;
			int rightIndex = leftIndex + 1;
			if(rightIndex > size || items[leftIndex].compareTo(items[rightIndex]) < 0){
				
				target = leftIndex;
			
			}else{
				target = rightIndex;
			}
			
			if(value.compareTo(items[target]) >= 0){
				items[hole] = items[target];
				hole = target;
			}else{
				break;
			}
			
		}
		return hole;
	}
	
	public void insert(Type insertItem){

		if(size == items.length -1){
			items = Arrays.copyOf(items, items.length * 2);
		}
		
		size++;
		
		int index = percolateUp(size, insertItem);
		items[index] = insertItem;
		
	}
	
	public Type deleteMin(){
		
		if(isEmpty()){
			return null;
		}
		
		Type gotItem = items[1];
		
		int index = percolateDown(1, items[size]);
		
		items[index] = items[size];
		
		size--;
		
		return gotItem;
	}
	
}
