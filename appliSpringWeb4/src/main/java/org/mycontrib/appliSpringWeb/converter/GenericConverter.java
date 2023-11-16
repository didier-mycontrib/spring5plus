package org.mycontrib.appliSpringWeb.converter;

import org.mycontrib.util.generic.converter.AbstractGenericConverter;

/*
 * Version plus sophistiquee de GenericMapper
 * deleguant des conversions spécifique à DtoConverter
 */

public class GenericConverter extends AbstractGenericConverter {

	public static GenericConverter CONVERTER = new GenericConverter();

	@Override
	public Object getDtoConverter() {
		return DtoConverter.INSTANCE;
	}

	
}
