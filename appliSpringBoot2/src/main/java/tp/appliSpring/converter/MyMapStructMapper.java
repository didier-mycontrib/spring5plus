package tp.appliSpring.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MyMapStructMapper {
	
	
	MyMapStructMapper INSTANCE = Mappers.getMapper( MyMapStructMapper.class );
	
	public RecordXy classXyToRecordXy(ClassXy xy);
}
