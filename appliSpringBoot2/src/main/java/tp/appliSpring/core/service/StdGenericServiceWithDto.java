package tp.appliSpring.core.service;

import java.util.List;

public interface StdGenericServiceWithDto<DTO,ID> {
	DTO searchById(ID id);
	List<DTO> searchAll();
	DTO saveOrUpdate(DTO dto);
	DTO saveNew(DTO dto);
	DTO updateExisting(DTO dto,ID id);
	boolean existsById(ID id);
	void deleteById(ID id);
}
