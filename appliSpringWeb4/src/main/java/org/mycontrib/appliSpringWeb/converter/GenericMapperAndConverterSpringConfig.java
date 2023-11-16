package org.mycontrib.appliSpringWeb.converter;

import org.mycontrib.util.generic.converter.UltraBasicGenericMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenericMapperAndConverterSpringConfig {
	
	@Bean
	public UltraBasicGenericMapper genericMapper() {
		return UltraBasicGenericMapper.MAPPER;
	}
	
	@Bean
	public GenericConverter genericConverter() {
		return GenericConverter.CONVERTER;
	}
	
	/*
	 pour pouvoir faire ceci dans classes de type @Service ou @RestController :
	 
	 @Autowired 
	 private GenericMapper genericMapper;
	 
	 @Autowired 
	 private GenericConverter genericMapper;
	 */

}
