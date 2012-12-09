package oripa.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * For Multi-input Multi-output processing
 * @author koji
 *
 * @param <Value>
 * @param <Output>
 */
public class MultiInMultiOutProcessor<Value, Output> {

	MultiInProcessFactory<Value, Output> processFactory;

	//private ArrayList<Collection<Value>> valueSets;



	public MultiInMultiOutProcessor(MultiInProcessFactory<Value, Output> processFactory) {
		this.processFactory = processFactory;
	}

	/**
	 * executes given process at construction in parallel by separating values in {@code divNum} blocks.
	 * @param values
	 * @param divNum
	 * @return results of each block
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public ArrayList<Output> execute(final List<Value> values, int divNum) 
			throws IllegalAccessException, InterruptedException, ExecutionException{	

		// separate given array for concurrent processing
		ArrayList<Collection<Value>> valueSets = separate(values, divNum);
		
		return execute(valueSets);
	}

	int divNum = -1;
	ExecutorService executor;
	public ArrayList<Output> execute(final Collection< Collection<Value> > valueSets) 
				{

//		if(valueSets.size() > divNum){
//			executor = Executors.newFixedThreadPool(valueSets.size());			
//		}
		divNum = valueSets.size();
		executor = Executors.newFixedThreadPool(valueSets.size());			
		
		CompletionService<Output> completion = new ExecutorCompletionService<>(executor);

		ArrayList<Output> result = new ArrayList<>(divNum);

		try{
			for(Collection<Value> valueSet : valueSets){
				completion.submit( processFactory.create().getCallable(valueSet) );
			}

			for(int i = 0; i < divNum; i++){
				// take the result of the finished process as soon as possible
				Output processed = completion.take().get();
				result.add(processed);
			}
		}
		catch (Exception e) {

		}

		executor.shutdown();

		return result;

	}	


	/**
	 * Separates the given list into divNum collections if the amount of given values can be divided.
	 * Otherwise the result will be divNum+1 collections.
	 * Each collection of the result keeps the order of original list.
	 * Ex) {1, 2, 3, 4, 5, 6} -> {1, 2}, {3, 4}, {5, 6} 
	 * 
	 * @param values list to be separated
	 * @return a list of value collections as the result of separation.
	 */
	public ArrayList<Collection<Value>> separateKeepingOrder(final List<Value> values, int divNum){

		ArrayList<Collection<Value>> valueSets = new ArrayList<>();
		Iterator<Value> remain_itr = separateEqually(values, valueSets, divNum);

		ArrayList<Value> valueSet = new ArrayList<>();

		final int remain = values.size() % divNum;

		valueSet = new ArrayList<>(remain);
		for(int i = values.size() - remain; i < values.size(); i++){
			valueSet.add(remain_itr.next());
		}
		valueSets.add(valueSet);

		return valueSets;
	}

	/**
	 * Separates the given list into divNum collections.
	 * The order of elements in each collections is not equal to that of the original list.
	 * 
	 * @param values list to be separated
	 * @return a list of value collections as the result of separation.
	 */
	public ArrayList<Collection<Value>> separate(final List<Value> values, int divNum){
		
		ArrayList<Collection<Value>> valueSets = new ArrayList<>();
		Iterator<Value> remain_itr = separateEqually(values, valueSets, divNum);

		final int remain = values.size() % divNum;
				
		for(int i = 0; i < remain; i++){
			if(remain_itr.hasNext() == false){
				throw new RuntimeException();
			}
			valueSets.get(i).add(remain_itr.next());
		}

		return valueSets;

	}

	/**
	 * Separates given list in {@code divNum} collections with equal size as large as possible. 
	 * More formally, size {@code s} of each collection is {@code values.size() / divNum}.
	 * The elements in given values with index larger or equal to {@code s * divNum} will be omitted. 
	 * @param values     a list of values
	 * @param valueSets  a list for the result of separating values
	 * @param divNum     number of division
	 * @return a list of value collections as the result of separation.
	 */
	private Iterator<Value> separateEqually(final List<Value> values, ArrayList<Collection<Value>> valueSets, int divNum){

		valueSets.clear();
		
		if(values.isEmpty()){
			return values.iterator();
		}

		final int setSize = values.size() / divNum;
		int tail = setSize;

		ArrayList<Value> valueSet = new ArrayList<>(setSize);

		Iterator<Value> value_itr = values.iterator();
		for(int index = 0; (index < setSize * divNum) && (index < values.size()); index++){
			// shift to next set
			if(index == tail){
				valueSets.add(valueSet);

				tail = tail + setSize;
				valueSet = new ArrayList<>(setSize);
			}

			// add value into set
			valueSet.add(value_itr.next());
		}
		valueSets.add(valueSet);

		while(valueSets.size() < divNum){
			valueSets.add(new ArrayList<Value>(0));
		}
		
		return value_itr;
	}

}
