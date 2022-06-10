package org.meveo.api.internationalization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

import org.meveo.api.rest.technicalservice.EndpointScript;
import org.meveo.service.script.Script;
import org.meveo.admin.exception.BusinessException;

import org.meveo.service.admin.impl.MeveoModuleService;
import org.meveo.model.customEntities.LocalizedMessage;
import org.meveo.model.module.MeveoModule;
import org.meveo.model.billing.Language;

import org.meveo.api.persistence.CrossStorageApi;
import org.meveo.model.storage.Repository;
import org.meveo.service.storage.RepositoryService;
//import org.meveo.api.internationalization.Internationalizer;

import org.meveo.service.admin.impl.LanguageService;

public class LocalizedMessageTestScript extends Script {
  
	private static final Logger log = LoggerFactory.getLogger(LocalizedMessageTestScript.class);
    private CrossStorageApi crossStorageApi = getCDIBean(CrossStorageApi.class);
    private RepositoryService repositoryService = getCDIBean(RepositoryService.class);    
    private Repository defaultRepo = repositoryService.findDefaultRepository();  
    private MeveoModuleService moduleService = getCDIBean(MeveoModuleService.class);
	private LanguageService languageService = getCDIBean(LanguageService.class);
  
	@Override
	public void execute(Map<String, Object> parameters) throws BusinessException {
		super.execute(parameters);
        final String COUNTRY_NAME_KEY_PREFIX = "country.name.";
      
        /*Map<String, Object> context= new HashMap<>();
        Internationalizer internationalizer = Internationalizer.getInstance(this, context);
        
        java.util.List<Object> params = new java.util.ArrayList();
		params.add("Farhan Munir");
		params.add(39);
		params.add(68.25);
		java.time.LocalDate dob = java.time.LocalDate.of(1982, java.time.Month.NOVEMBER, 13);
		java.util.Date dateofBirth = java.util.Date.from(dob.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
		params.add(dateofBirth );
      
        
      
        String messsage = internationalizer.get("user.personal.introduction", params );
        log.info("messsage= "+messsage);
      */
      
        /*List<String> countryNameKeys = Arrays.asList((COUNTRY_NAME_KEY_PREFIX+"PK").toLowerCase(), (COUNTRY_NAME_KEY_PREFIX+"FR").toLowerCase(), (COUNTRY_NAME_KEY_PREFIX+"US").toLowerCase());
        Map<String,String> countryNameMap = internationalizer.getLocalizedMesages(countryNameKeys);
        Iterator<String> cNameKeyItr = countryNameMap.keySet().iterator();
        while(cNameKeyItr.hasNext()){
          String messageKey  = cNameKeyItr.next();          
          String countryName = countryNameMap.get(messageKey);
          System.out.println("Key:Value  =  "+messageKey+":"+countryName);
        }*/
        MeveoModule module = moduleService.findByCodeWithFetchEntities("mv-userprofile");
        Language language = languageService.findByCode("FRA");
          
        LocalizedMessage localizedMessage = new LocalizedMessage();
        localizedMessage.setKey("country.name.fr");
        localizedMessage.setModule(module);
        localizedMessage.setLanguage(language);
        this.isDuplicate(localizedMessage);
      
      
	}
  
       public void isDuplicate(LocalizedMessage localizedMessage){
          boolean isDuplicate = false;
          Repository defaultRepo = repositoryService.findDefaultRepository();
          List<LocalizedMessage> messageList = crossStorageApi.find(defaultRepo, LocalizedMessage.class)
                                       .by("key", localizedMessage.getKey() )
                                       .by("module", localizedMessage.getModule() )
                                       .by("language", localizedMessage.getLanguage() )
                                       .getResults();    
          
          isDuplicate = messageList != null && messageList.size() > 0;
          System.out.println("isDuplicate="+isDuplicate);    
        }
	
}