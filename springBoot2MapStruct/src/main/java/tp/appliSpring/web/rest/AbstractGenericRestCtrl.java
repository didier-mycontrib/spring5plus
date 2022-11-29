package tp.appliSpring.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import tp.appliSpring.core.exception.ConflictException;
import tp.appliSpring.core.exception.NotFoundException;
import tp.appliSpring.core.service.GenericServiceWithDto;
import tp.appliSpring.dto.Message;

public abstract class AbstractGenericRestCtrl<DTO,DTOEX,ID>  extends AbstractGenericRestCtrlWithoutMapping<DTO,DTOEX,ID>{
	

	public AbstractGenericRestCtrl(GenericServiceWithDto<DTO,DTOEX,ID> serviceWithDto) {
		super(serviceWithDto);
	}
	
	// URL= http://localhost:8080/appliSpringBoot/bank-api/DTO/1_or_other_id
	@GetMapping("/{id}")
	public DTO getDtoById(@PathVariable("id") ID id) throws NotFoundException {
		return super.getDtoById(id); //may throwing NotFoundException
	}
	
	// URL= http://localhost:8080/appliSpringBoot/bank-api/DTO
	// appelé en mode POST avec le corps de la requête HTTP comportant
	// { "id" : null , "xxx" : "valXxx" , "yyyy" : valYyy }
	@PostMapping("")
	public DTO postDto(@RequestBody DTO dto) throws ConflictException{
		return super.postDto(dto);// may throwing ConflictException
	}

	// URL= http://localhost:8080/appliSpringBoot/bank-api/DTO
	// appelé en mode PUT avec le corps de la requête HTTP comportant
	// { "id" : idQuiVaBien , "xxx" : "valXxxQuiVaEncoreBien" ,  "yyyy" : valYyy  }
	@PutMapping("")
	public DTO putDto(@RequestBody DTO dto) throws NotFoundException {
		return super.putDto(dto);// may throwing NotFoundException
	}
	
	// URL= http://localhost:8080/appliSpringBoot/bank-api/DTO/3_or_other_id
	// appelé en mode DELETE
	@DeleteMapping("/{id}")
	public ResponseEntity<Message> deleteDtoById(@PathVariable("id") ID id) {
		return super.deleteDtoById(id);
	}

}
