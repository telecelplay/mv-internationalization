package org.meveo.crudscript;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.google.gson.Gson;

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
	public void prePersist(LocalizedMessage entity){     
    } 

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
       boolean isDuplicate = false;
          
          List<LocalizedMessage> messageList = crossStorageApi.find(repositoryService.findDefaultRepository(), LocalizedMessage.class)
                                       .by("key", entity.getKey() )
                                       .getResults();
          final String moduleCode = entity.getModule();//.getCode();  FIXME: remove ;// after testing issue
          final String languageCode = entity.getLanguage().getCode();
          System.out.println("moduleCode="+moduleCode);
          System.out.println("languageCode="+languageCode);
          //FIX ME remove comments frmo below line also after testing
          messageList.stream().filter(m-> m.getModule()/*.getCode()*/.equals(moduleCode) && m.getLanguage().getCode().equals(languageCode)).forEach(m->{ 
            System.out.println("iterating module = "+ new Gson().toJson(m.getModule())); 
            System.out.println("iterating language = "+ new Gson().toJson(m.getLanguage())); 
          });
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