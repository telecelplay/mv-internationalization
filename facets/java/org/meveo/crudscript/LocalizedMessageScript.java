package org.meveo.crudscript;

import java.util.Map;

import org.meveo.service.script.Script;
import org.meveo.admin.exception.BusinessException;
import org.meveo.service.admin.impl.MeveoModuleService;
import org.meveo.model.customEntities.LocalizedMessage;
import org.meveo.model.customEntities.CrudEventListenerScript;
import org.meveo.model.module.MeveoModule;

import org.meveo.api.persistence.CrossStorageApi;
import org.meveo.model.storage.Repository;
import org.meveo.service.storage.RepositoryService;

public class LocalizedMessageScript extends Script implements CrudEventListenerScript<LocalizedMessage> {
	
    private MeveoModuleService moduleService = getCDIBean(MeveoModuleService.class);
	private RepositoryService repositoryService = getCDIBean(RepositoryService.class);    
    private Repository defaultRepo = repositoryService.findDefaultRepository();  
    private CrossStorageApi crossStorageApi = getCDIBean(CrossStorageApi.class);
  
	@Override
	public void execute(Map<String, Object> parameters) throws BusinessException {
		super.execute(parameters);
	}
  
  /**
	 * @return the class of the entity
	 */
	public Class<LocalizedMessage> getEntityClass(){
      return LocalizedMessage.class;
    }
  
    /**
	 * Called just before entity persistence
	 * 
	 * @param entity entity being persisted
	 */
	public void prePersist(LocalizedMessage entity){}

	/**
	 * Called just before entity update
	 * 
	 * @param entity entity being updated
	 */
	public void preUpdate(LocalizedMessage entity){
    }

	/**
	 * Called just before entity removal
	 * 
	 * @param entity entity being removed
	 */
	public void preRemove(LocalizedMessage entity){
    }

	/**
	 * Called just after entity persistence
	 * 
	 * @param entity persisted entity
	 */
	public void postPersist(LocalizedMessage entity){
      if(entity.getModule() == null){        
        MeveoModule module = moduleService.findByCodeWithFetchEntities("mv-internationalization");
        entity.setModule(module);        
        try{
          crossStorageApi.createOrUpdate(defaultRepo, entity);
        }catch(Exception e){
          
        } 
      }
    }

	/**
	 * Called just after entity update
	 * 
	 * @param entity updated entity
	 */
	public void postUpdate(LocalizedMessage entity){
    }

	/**
	 * Called just after entity removal
	 * 
	 * @param entity removed entity
	 */
	public void postRemove(LocalizedMessage entity){
    }
	
}