package org.meveo.api.internationalization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

import org.meveo.api.rest.technicalservice.EndpointScript;
import org.meveo.service.script.Script;
import org.meveo.admin.exception.BusinessException;
//import org.meveo.api.internationalization.Internationalizer;

public class LocalizedMessageTestScript extends Script {
  
	private static final Logger log = LoggerFactory.getLogger(LocalizedMessageTestScript.class);
  
	@Override
	public void execute(Map<String, Object> parameters) throws BusinessException {
		super.execute(parameters);
        final String COUNTRY_NAME_KEY_PREFIX = "country.name.";
      
        Map<String, Object> context= new HashMap<>();
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
      
        /*List<String> countryNameKeys = Arrays.asList((COUNTRY_NAME_KEY_PREFIX+"PK").toLowerCase(), (COUNTRY_NAME_KEY_PREFIX+"FR").toLowerCase(), (COUNTRY_NAME_KEY_PREFIX+"US").toLowerCase());
        Map<String,String> countryNameMap = internationalizer.getLocalizedMesages(countryNameKeys);
        Iterator<String> cNameKeyItr = countryNameMap.keySet().iterator();
        while(cNameKeyItr.hasNext()){
          String messageKey  = cNameKeyItr.next();          
          String countryName = countryNameMap.get(messageKey);
          System.out.println("Key:Value  =  "+messageKey+":"+countryName);
        }*/
        
	}
	
}