package tp.appliSpring.core.service.impl;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import tp.appliSpring.converter.MyGenericMapper;
import tp.appliSpring.core.exception.NotFoundException;
import tp.appliSpring.core.service.StdGenericServiceWithDto;

public abstract class AbstractGenericStdServiceWithDto<DTO,ID,E,DAO extends CrudRepository<E, ID>> implements StdGenericServiceWithDto<DTO,ID> {

	private Class<E> entityClass;
	private Class<DTO> dtoClass;
	private DAO dao;
	
	public AbstractGenericStdServiceWithDto(Class<DTO> dtoClass,Class<E> entityClass, DAO dao) {
		this.entityClass=entityClass;
		this.dtoClass=dtoClass;
		this.dao=dao;
	}

	@Override
	public DTO searchById(ID id) {
		DTO dto= null;
		E entity = dao.findById(id).orElse(null);
		if(entity!=null) 
			dto=MyGenericMapper.map(entity, dtoClass);
		return dto;
	}

	@Override
	public List<DTO> searchAll() {
		return MyGenericMapper.map((List<E>)dao.findAll(), dtoClass);
	}

	@Override
	public DTO saveOrUpdate(DTO dto) {
		E entity = MyGenericMapper.map(dto, entityClass);
		dao.save(entity);
		return MyGenericMapper.map(entity, dtoClass);
	}

	@Override
	public void deleteById(ID id) {
		dao.deleteById(id);

	}

	@Override
	public DTO saveNew(DTO dto) {
		return saveOrUpdate(dto);
	}

	@Override
	public DTO updateExisting(DTO dto,ID id) {
		if(!existsById(id)) 
			throw new NotFoundException("no existing " + dto.getClass().getSimpleName() + " for id="+id);
		return saveOrUpdate(dto);
	}

	@Override
	public boolean existsById(ID id) {
		return dao.existsById(id);
	}

}
