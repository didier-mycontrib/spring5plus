package org.mycontrib.appliSpringWeb.converter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Function;

import org.mycontrib.appliSpringWeb.dto.NewsL0;
import org.mycontrib.appliSpringWeb.dto.NewsL0Bis;
import org.mycontrib.appliSpringWeb.entity.News;

public class ConverterWithMethodHandleTestApp {

	public static void main(String[] args) throws Throwable {
		viaMethodHandle();
		viaFunctionReference();
		//viaGenericConverterAndPreRegisteredTransformFunction();//with optional explicit transformFunction preRegistering
		viaGenericConverter();//internal map build at first call (if no explicit preRegistering)
		viaGenericConverter();//use of internal map in other calls
		viaGenericConverterWithDefaultGenericMapper();
		
	}
	
	public static void viaFunctionReference()throws Throwable {
		System.out.println("viaFunctionReference");
		//explicit direct conversion:
		News newsEntity = new News(2L,"newsB");
		NewsL0 newsDtoRecord = DtoConverter.INSTANCE.newsToNewsL0(newsEntity);
		System.out.println("newsDtoRecord="+newsDtoRecord);
		//indirect conversion via FunctionReference:
		Function<News,NewsL0> transformFunction =DtoConverter.INSTANCE::newsToNewsL0;
		NewsL0 newsDtoRecord2 = transformFunction.apply(newsEntity);
		System.out.println("newsDtoRecord2="+newsDtoRecord2);
		
	}
	
	public static void viaGenericConverterAndPreRegisteredTransformFunction()throws Throwable {
		System.out.println("viaGenericConverterAndPreRegisteredTransformFunction");
		//explicit direct conversion:
		News newsEntity = new News(3L,"newsC");
		NewsL0 newsDtoRecord = DtoConverter.INSTANCE.newsToNewsL0(newsEntity);
		System.out.println("newsDtoRecord="+newsDtoRecord);
		//indirect conversion via GenericConverter:
		GenericConverter.CONVERTER.preRegisterTransformFunction(News.class, NewsL0.class, DtoConverter.INSTANCE::newsToNewsL0);
		NewsL0 newsDtoRecord2 = GenericConverter.CONVERTER.map(newsEntity,NewsL0.class);
		System.out.println("newsDtoRecord2="+newsDtoRecord2);	
	}
	
	public static void viaGenericConverter()throws Throwable {
		System.out.println("viaGenericConverter");
		//explicit direct conversion:
		News newsEntity = new News(3L,"newsC");
		NewsL0 newsDtoRecord = DtoConverter.INSTANCE.newsToNewsL0(newsEntity);
		System.out.println("newsDtoRecord="+newsDtoRecord);
		//indirect conversion via GenericConverter:
		NewsL0 newsDtoRecord2 = GenericConverter.CONVERTER.map(newsEntity,NewsL0.class);
		System.out.println("newsDtoRecord2="+newsDtoRecord2);	
	}
	
	public static void viaGenericConverterWithDefaultGenericMapper()throws Throwable {
		System.out.println("viaGenericConverterWithDefaultGenericMapper");
		//NB: NewsL0 exist as JavaBean/POJO (with lombok) (not as record)
		//but DtoConverter.INSTANCE.newsToNewsL0Bis() does not exists
		News newsEntity = new News(3L,"newsC");
		//indirect conversion via GenericConverter:
		NewsL0Bis newsDtoRecord = GenericConverter.CONVERTER.map(newsEntity,NewsL0Bis.class);
		System.out.println("newsDtoRecord(bis version)="+newsDtoRecord);	
	}
	
	public static void viaMethodHandle()throws Throwable {
		System.out.println("viaMethodHandle");
		//explicit direct conversion:
		News newsEntity = new News(1L,"newsA");
		NewsL0 newsDtoRecord = DtoConverter.INSTANCE.newsToNewsL0(newsEntity);
		System.out.println("newsDtoRecord="+newsDtoRecord);
		//indirect conversion with methodHandle:
		MethodType convertNewsToNewsL0MethType = 
				MethodType.methodType(NewsL0.class /*returnType*/, News.class /*paramType*/);
		MethodHandle handle = MethodHandles.publicLookup().findVirtual(DtoConverter.class, "newsToNewsL0", convertNewsToNewsL0MethType);
		NewsL0 newsDtoRecord2 = null;
		newsDtoRecord2=(NewsL0)handle.invoke(DtoConverter.INSTANCE, newsEntity);
		System.out.println("newsDtoRecord2="+newsDtoRecord2);
		//indirect conversion via FunctionReference and MethodHandle:
		Function<News,NewsL0> transformFunction =(News entity)->{
			try {
				return (NewsL0)handle.invoke(DtoConverter.INSTANCE, entity);
			} catch (Throwable e) {
				e.printStackTrace();
				return null;
			}
		};
		NewsL0 newsDtoRecord3 = transformFunction.apply(newsEntity);
		System.out.println("newsDtoRecord3="+newsDtoRecord3);
	}

}
