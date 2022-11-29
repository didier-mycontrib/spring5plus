package tp.appliSpring.converter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

public class MyGenericMapper {
	
	static MyMapper mapper = MyMapper.INSTANCE;
	
	static String withFirstLowerCase(String s){
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}

	@SuppressWarnings("unchecked")
	public static <S,D> D map(S source,Class<D> destinationClass) {
		D destination = null;
		try {
			
			//With mapStruct style Mapper:
			String convertMethodName=withFirstLowerCase(source.getClass().getSimpleName() + "To" + destinationClass.getSimpleName());
			//System.out.println("convertMethodName="+convertMethodName);
			Method convertMethod=mapper.getClass().getDeclaredMethod(convertMethodName,source.getClass());
			if(convertMethod!=null) {
				destination = (D) convertMethod.invoke(mapper, source);
			}else {
				//without mapStruct mapper (as fault back)
				destination = destinationClass.getDeclaredConstructor().newInstance();
				BeanUtils.copyProperties(source, destination);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return destination;
	}
	
	
	public static <S,D> List<D> map(List<S> sourceList , Class<D> destinationClass){
		return  sourceList.stream()
			   .map((source)->map(source,destinationClass))
			   .collect(Collectors.toList());
	}

}
