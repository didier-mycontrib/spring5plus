package tp.appliSpring.core.service.impl;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import tp.appliSpring.converter.MyGenericMapper;
import tp.appliSpring.core.exception.ConflictException;
import tp.appliSpring.core.exception.NotFoundException;
import tp.appliSpring.core.service.GenericServiceWithDto;

public abstract class AbstractGenericServiceWithDto<DTO,DTOEX,ID,E,DAO extends CrudRepository<E, ID>> implements GenericServiceWithDto<DTO,DTOEX,ID> {

	private Class<E> entityClass;
	private Class<DTO> dtoClass;
	private Class<DTOEX> dtoExClass;
	private IdHelper<DTO,E,ID> idHelper;
	private DAO dao;
	
	public AbstractGenericServiceWithDto(Class<DTO> dtoClass,Class<DTOEX> dtoExClass,Class<E> entityClass, DAO dao, IdHelper<DTO,E,ID> idHelper) {
		this.entityClass=entityClass;
		this.dtoClass=dtoClass;
		this.dtoExClass=dtoExClass;
		this.idHelper=idHelper;
		this.dao=dao;
	}

	@Override
	public DTO searchById(ID id) throws NotFoundException {
		E entity = dao.findById(id).orElse(null);
		if(entity!=null) 
			return MyGenericMapper.map(entity, dtoClass);
		else 
			throw new NotFoundException("no existing " + dtoClass.getSimpleName() + " for id="+id);
	}

	@Override
	public List<DTO> searchAll() {
		return MyGenericMapper.map((List<E>)dao.findAll(), dtoClass);
	}

	@Override
	public DTO saveOrUpdate(DTO dto) {
		E entity = MyGenericMapper.map(dto, entityClass);
		dao.save(entity);
		//return MyGenericMapper.map(entity, dtoClass);
		idHelper.copyEntityIDIntoDto(entity, dto);
		return dto;
	}

	@Override
	public void deleteById(ID id) throws NotFoundException{
		if(!existsById(id)) 
			throw new NotFoundException("no existing " + dtoClass.getSimpleName() + " to delete for id="+id);
		dao.deleteById(id);
	}

	@Override
	public DTO saveNew(DTO dto) throws ConflictException{
		ID id = this.idHelper.extractDtoId(dto);
		if(id != null) {
			if(dao.existsById(id))
				throw new ConflictException("already existing " + dtoClass.getSimpleName() + " with id="+id);
		}
		return saveOrUpdate(dto);
	}

	@Override
	public DTO updateExisting(DTO dto) {
		ID id = this.idHelper.extractDtoId(dto);
		if(!existsById(id)) 
			throw new NotFoundException("no existing " + dtoClass.getSimpleName() + " to update for id="+id);
		return saveOrUpdate(dto);
	}

	@Override
	public boolean existsById(ID id) {
		return dao.existsById(id);
	}

}
