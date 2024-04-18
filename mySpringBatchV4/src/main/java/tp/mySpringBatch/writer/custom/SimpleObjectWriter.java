package tp.mySpringBatch.writer.custom;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class SimpleObjectWriter implements ItemWriter<Object>{
	
	
	public SimpleObjectWriter() {
	}
	
	@Override
	public void write(List<? extends Object> chunk) throws Exception {
		for(Object intObj : chunk) {
			System.out.println("OBLECT="+intObj);
		}
	}

	

	
}
