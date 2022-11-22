package tp.appliSpring.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import tp.appliSpring.core.exception.ConflictException;
import tp.appliSpring.core.exception.NotFoundException;
import tp.appliSpring.core.service.GenericServiceWithDto;
import tp.appliSpring.dto.Message;

public abstract class AbstractGenericRestCtrlWithoutMapping<DTO,DTOEX,ID> {
	
	/*
	 NB: au sein de cette classe abstraite , tous les mappings sont volontairement
	     désactivés (en commentaire) , pour que l'on puisse les redéfinir dans une sous classe
	     (avec par exemple @PreAuthorize("hasRole('ROLE_ADMIN')") en plus)
	     et cela sans engendrer des ambiguités de mapping
	 */
	
	private GenericServiceWithDto<DTO,DTOEX,ID> serviceWithDto;
	
	public AbstractGenericRestCtrlWithoutMapping(GenericServiceWithDto<DTO,DTOEX,ID> serviceWithDto) {
		this.serviceWithDto=serviceWithDto;
	}
	
	// URL= http://localhost:8080/appliSpringBoot/bank-api/DTO/1_or_other_id
	//@GetMapping("/{id}")
	public DTO getDtoById(/*@PathVariable("id")*/ ID id) throws NotFoundException {
		return serviceWithDto.searchById(id); //may throwing NotFoundException
	}
	
	// URL= http://localhost:8080/appliSpringBoot/bank-api/DTO
	// appelé en mode POST avec le corps de la requête HTTP comportant
	// { "id" : null , "xxx" : "valXxx" , "yyyy" : valYyy }
	//@PostMapping("")
	public DTO postDto(/*@RequestBody*/ DTO dto) throws ConflictException{
		return serviceWithDto.saveNew(dto);// may throwing ConflictException
	}

	// URL= http://localhost:8080/appliSpringBoot/bank-api/DTO
	// appelé en mode PUT avec le corps de la requête HTTP comportant
	// { "id" : idQuiVaBien , "xxx" : "valXxxQuiVaEncoreBien" ,  "yyyy" : valYyy  }
	//@PutMapping("")
	public DTO putDto(/*@RequestBody*/ DTO dto) throws NotFoundException {
		return serviceWithDto.updateExisting(dto);// may throwing NotFoundException
	}
	
	// URL= http://localhost:8080/appliSpringBoot/bank-api/DTO/3_or_other_id
	// appelé en mode DELETE
	//@DeleteMapping("/{id}")
	public ResponseEntity<Message> deleteDtoById(/*@PathVariable("id")*/ ID id) {
		try {
			serviceWithDto.deleteById(id);
			return new ResponseEntity<Message>(new Message("suppression bien effectué"), HttpStatus.OK); // OK:200
			// return new ResponseEntity<Message>( HttpStatus.NO_CONTENT); //NO_CONTENT:204
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Message>(new Message("suppression impossible"), HttpStatus.NOT_FOUND);
		}
	}

}
