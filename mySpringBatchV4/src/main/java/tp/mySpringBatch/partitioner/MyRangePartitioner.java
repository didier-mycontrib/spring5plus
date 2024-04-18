package tp.mySpringBatch.partitioner;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

public class MyRangePartitioner implements Partitioner {
	
	private static Logger logger = LoggerFactory.getLogger(MyRangePartitioner.class);
	
	//range (as rangeSize) is the number of entries that will be managed 
	//by a thread/partition_executionContext
	private Integer range=10; //default value
	
	public MyRangePartitioner(Integer range) {
		super();
		this.range = range;
	}
	
	
	public MyRangePartitioner() {
		super();
	}


	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		//NB: grid size will be the number of threads (in //)
		
		Map<String, ExecutionContext> partitionMap = new HashMap<String, ExecutionContext>();

		int fromId = 1;
		int toId = range; 

		for (int i = 1; i <= gridSize; i++) {
			//NB: a springBatch ExcecutionContext is a sort of Map of any key/value
			//that will be used by a executionThread
			ExecutionContext partitionExecutionContext = new ExecutionContext();
			
			partitionExecutionContext.putString("name", "partition_" + i);
			partitionExecutionContext.putInt("fromId", fromId);//first id value in table to be managed by this thread/partition
			partitionExecutionContext.putInt("toId", toId);//last id value in table to be managed by this thread/partition
			logger.debug(partitionExecutionContext.getString("name") + " managed by a specific thread" 
			+ " will be use to manage data whith id between " + partitionExecutionContext.getInt("fromId")
            + "and " + partitionExecutionContext.getInt("toId"));

			partitionMap.put(partitionExecutionContext.getString("name"), partitionExecutionContext);

			fromId = toId + 1;
			toId += range;
		}

		return partitionMap;
	}
	
	public Integer getRange() {
		return range;
	}

	public void setRange(Integer range) {
		this.range = range;
	}

}
