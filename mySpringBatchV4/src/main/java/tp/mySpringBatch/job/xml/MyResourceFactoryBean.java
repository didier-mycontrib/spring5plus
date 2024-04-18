package tp.mySpringBatch.job.xml;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class MyResourceFactoryBean implements FactoryBean{

	private String path ; //relativePathNameOfFile

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public Object getObject() throws Exception {
		Resource res=  new FileSystemResource(path);
		//System.out.println("MyResourceFactoryBean returning resource for path="+path);
		return res;
	}

	@Override
	public Class getObjectType() {
		return Resource.class;
	}

}
