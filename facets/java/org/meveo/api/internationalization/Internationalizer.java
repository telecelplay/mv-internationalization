package org.meveo.api.internationalization;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.meveo.service.script.Script;
import org.meveo.admin.exception.BusinessException;
import org.meveo.model.billing.Language;
import org.meveo.service.admin.impl.MeveoModuleService;
import org.meveo.service.admin.impl.LanguageService;
import org.meveo.api.rest.technicalservice.EndpointScript;
import org.meveo.model.module.MeveoModule;
import org.meveo.model.module.MeveoModuleItem;
import java.util.stream.Collectors;
import org.meveo.model.customEntities.LocalizedMessage;

import org.meveo.persistence.CrossStorageTransaction;
import org.meveo.api.persistence.CrossStorageApi;
import org.meveo.model.storage.Repository;
import org.meveo.service.storage.RepositoryService;

public class Internationalizer extends Script {
  
    private static final Logger log = LoggerFactory.getLogger(Internationalizer.class);
  	private CrossStorageApi crossStorageApi = getCDIBean(CrossStorageApi.class);
    private RepositoryService repositoryService = getCDIBean(RepositoryService.class);
    private CrossStorageTransaction txService = getCDIBean(CrossStorageTransaction.class);
    private Repository defaultRepo = repositoryService.findDefaultRepository();
    private Script scriptInstance;
    private Map<String, Object> context;
    private List<Language> intendedLanguages;
    private MeveoModule module;
  
	
	@Override
	public void execute(Map<String, Object> parameters) throws BusinessException {
		super.execute(parameters);
        
	}
 	public static Internationalizer getInstance(Script scriptInstance, Map<String, Object> context){
      return new Internationalizer(scriptInstance, context);
    }
	
    
    private Internationalizer(Script scriptInstance, Map<String, Object> context){
      this.scriptInstance = scriptInstance;
      this.context = context;
      this.initialize();
    }
  
    public void initialize(){
      this.setIntendedLanguages();
      this.setModule(); 
    }
   
    public void setModule(){
      MeveoModuleService moduleService = getCDIBean(MeveoModuleService.class);
      String parentClassName = this.scriptInstance.getClass().getSuperclass().getSimpleName();
      	String code = this.scriptInstance.getClass().getName();
        log.info("itemClass= org.meveo.model.scripts.ScriptInstance and code= "+code);
        List<MeveoModuleItem> moduleItems = moduleService.findByCodeAndItemType(code, "org.meveo.model.scripts.ScriptInstance");
      	if(moduleItems.size() > 0){
        	this.module = moduleItems.get(0).getMeveoModule();
            log.info("MeveoModule="+module.getCode());
        }
    }
  
    public void setIntendedLanguages(){
		LanguageService languageService = getCDIBean(LanguageService.class);        
          
		List<Language> preferredLanguages = new ArrayList<>();
        String parentClassName = this.scriptInstance.getClass().getSuperclass().getSimpleName();
        boolean isCallingScriptEndPointOne = parentClassName.equals("EndpointScript");
      
        if(isCallingScriptEndPointOne){          
            List<Locale> locales = ((EndpointScript)  this.scriptInstance).getIntendedLocales();
            Iterator<Locale> localesItr = locales.iterator();
            while(localesItr.hasNext() ){
              	String langCode = localesItr.next().getISO3Language();
                Language language = languageService.findByCode( langCode.toUpperCase() );
                preferredLanguages.add(language);
            }
            log.info("preferredLanguages= "+preferredLanguages.size());
        }else{
            preferredLanguages.add(languageService.findByCode( Locale.getDefault().getISO3Language().toUpperCase() ));
        }
		this.intendedLanguages = preferredLanguages;
	}
  
    public String get(String key){
       return this.get(key, null, key);
    }
  
    public String get(String key, List<Object> parameters){
      return this.get(key, parameters, key);
    }
  
    public String get(String key, String defaultValue){
       return this.get(key, null, defaultValue);
    }
    
    public String get(String key, List<Object> parameters, String defaultValue){
      LocalizedMessage message = this.getLocalizedMesage(key);
       if(message != null){
         String finalMessage = parameters != null ? String.format(message.getValue(), parameters.toArray(new Object[0])) : message.getValue();
         log.info("message = "+finalMessage);
         return finalMessage;
       }
       return defaultValue;
    }
  
    private LocalizedMessage getLocalizedMesage(String key){      
      List<LocalizedMessage> localizedMesages = crossStorageApi.find(defaultRepo, LocalizedMessage.class)
      												 .by("module", this.module)
        											 .by("key", key)
                                                     .getResults();
      
      log.info("Localized message(s) total fetched= "+localizedMesages.size());
      Iterator<Language> itr = this.intendedLanguages.iterator();
      if(localizedMesages != null && localizedMesages.size() > 0 ){
          while(itr.hasNext()){	
              Language intendedLanguage = itr.next();
              LocalizedMessage message = localizedMesages.stream().filter(lm->lm.getLanguage().getId().longValue() == intendedLanguage.getId().longValue()).findFirst().get();
              if(message != null){
                  log.info("Finally Message Fetched = "+message);
                  return message;
              }
	      }      
      }
      return null;
    }
  
    public Map<String, String> getLocalizedMesages(List<String> keys){
      Map<String, String> messages = new HashMap<>();
      List<LocalizedMessage> localizedMesages = crossStorageApi.find(defaultRepo, LocalizedMessage.class)
      												 .by("module", this.module)
        											 .by("inList key", keys)
                                                     .getResults();
      
      log.info("Localized message(s) total fetched= "+localizedMesages.size());
      Iterator<Language> itr = this.intendedLanguages.iterator();
      if(localizedMesages != null && localizedMesages.size() > 0 ){
          while(itr.hasNext()){	
              Language intendedLanguage = itr.next();
              List<LocalizedMessage> localizedMessages = localizedMesages.stream().filter(lm->lm.getLanguage().getId().longValue() == intendedLanguage.getId().longValue()).collect(Collectors.toList());
              if(localizedMessages != null ){
                  localizedMessages.forEach(lm-> {
                    if(messages.get(lm.getKey()) == null){
                      messages.put(lm.getKey(), lm.getValue());
                      log.info("Message added "+lm.getValue());
                    }  
                  });
              }
	      }      
      }
      return messages;
    }
  
}