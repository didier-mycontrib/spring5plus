package tp.mySpringBatch.writer.custom;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class SimpleCounterWriter implements ItemWriter<Integer>{
	
	
	public SimpleCounterWriter() {
	}
	
	@Override
	public void write(List<? extends Integer> chunk) throws Exception {
		for(Integer intObj : chunk) {
			System.out.println("COUNTER="+intObj);
		}
	}

	

	
}
