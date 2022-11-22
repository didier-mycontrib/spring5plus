package tp.appliSpring.core.service;

import java.util.List;

import tp.appliSpring.core.exception.ConflictException;
import tp.appliSpring.core.exception.NotFoundException;

//DTO=DTO class "essential/basic"
//DTOEX= DTO class "extended/full" héritant de DTO
//DTOEX peut eventuellement etre meme valeur que DTO (si pas de version étendue)
public interface GenericServiceWithDto<DTO,DTOEX,ID> {
	DTO searchById(ID id)throws NotFoundException;
	List<DTO> searchAll(); //may return an empty list without exception
	DTO saveOrUpdate(DTO dto);
	DTO saveNew(DTO dto) throws ConflictException;
	DTO updateExisting(DTO dto)throws NotFoundException;
	boolean existsById(ID id);
	void deleteById(ID id)throws NotFoundException;
}
