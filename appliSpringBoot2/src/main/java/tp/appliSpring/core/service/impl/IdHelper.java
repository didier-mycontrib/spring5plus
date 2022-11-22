package tp.appliSpring.core.service.impl;

public interface IdHelper<DTO,E,ID> {
     ID extractEntityId(E e);
     ID extractDtoId(DTO dto);
     void setDtoId(DTO dto,ID id);
     default void copyEntityIDIntoDto(E e,DTO dto) {
    	 setDtoId(dto,extractEntityId(e));
     }
}

