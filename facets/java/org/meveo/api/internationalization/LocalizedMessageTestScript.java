package org.meveo.api.internationalization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.HashMap;

import org.meveo.api.rest.technicalservice.EndpointScript;
import org.meveo.service.script.Script;
import org.meveo.admin.exception.BusinessException;
//import org.meveo.api.internationalization.Internationalizer;

public class LocalizedMessageTestScript extends Script {
  
	private static final Logger log = LoggerFactory.getLogger(LocalizedMessageTestScript.class);
  
	@Override
	public void execute(Map<String, Object> parameters) throws BusinessException {
		super.execute(parameters);
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
	}
	
}