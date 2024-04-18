package tp.mySpringBatch.job.xml;

import org.springframework.core.io.WritableResource;

public class MyWritableResourceFactoryBean extends MyResourceFactoryBean{

	@Override
	public Class getObjectType() {
		return WritableResource.class;
	}

}
