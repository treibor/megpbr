package com.megpbr.audit;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.megpbr.data.entity.AuditTrail;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.pbr.Markets;
import com.megpbr.data.entity.pbr.Scapes;
import com.megpbr.data.entity.villages.VillageDetails;
import com.megpbr.data.service.AuditService;
import com.megpbr.data.service.UserService;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;

@Service
public class Audit {
	private static final long serialVersionUID = 1L;
	private AuditService auditservice;
	private UserService uservice;
	AuditTrail audit;
	
	public Audit(AuditService auditservice, UserService uservice) {
		//super();
		this.auditservice = auditservice;
		this.uservice = uservice;
		//ipAddress= getRealClientIp();
	}
	public String getRealClientIp() {
	    VaadinRequest request = VaadinRequest.getCurrent();
	    String xForwardedForHeader = request.getHeader("X-Forwarded-For");
	    if (xForwardedForHeader == null || xForwardedForHeader.isEmpty()) {
	        return request.getRemoteAddr();
	    } else {
	        return xForwardedForHeader.split(",")[0].trim();
	    }
	}

	public void saveAudit(Crops crop, String action) {
		audit=new AuditTrail();
		audit.setAction(action);
		audit.setActionBy(uservice.getLoggedUserName());
		audit.setActionOn(LocalDateTime.now());
		audit.setIpAddress(getRealClientIp());
		String villageText=crop.getVillage() == null ? "Master Data" : crop.getVillage().getVillageName();
		audit.setDetails(crop.getId()+"-" +crop.getFormat().getFormatName() +", S. Name-"+crop.getScientificName()+",Village-"+ villageText+", Previous User-"+crop.getEnteredBy().getUserName()+", Previous Entry Date-"+crop.getEnteredOn());
		auditservice.updateAudit(audit);
	}
	public void saveAudit(Markets market, String action) {
		audit=new AuditTrail();
		audit.setAction(action);
		audit.setActionBy(uservice.getLoggedUserName());
		audit.setActionOn(LocalDateTime.now());
		audit.setIpAddress(getRealClientIp());
		String villageText=market.getVillage() == null ? "Master Data" : market.getVillage().getVillageName();
		audit.setDetails(market.getId()+"-" +market.getFormat().getFormatName() +", S. Name-"+market.getName()+",Village-"+ villageText+", Previous User-"+market.getEnteredBy().getUserName()+", Previous Entry Date-"+market.getEnteredOn());
		auditservice.updateAudit(audit);
	}
	public void saveAudit(Scapes scape, String action) {
		audit=new AuditTrail();
		audit.setAction(action);
		audit.setActionBy(uservice.getLoggedUserName());
		audit.setActionOn(LocalDateTime.now());
		audit.setIpAddress(getRealClientIp());
		String villageText=scape.getVillage() == null ? "Master Data" : scape.getVillage().getVillageName();
		audit.setDetails(scape.getId()+"-" +scape.getFormat().getFormatName() +", S. Name-"+scape.getFaunaPopulation()+",Village-"+ villageText+", Previous User-"+scape.getEnteredBy().getUserName()+", Previous Entry Date-"+scape.getEnteredOn());
		auditservice.updateAudit(audit);
	}
	public void saveAudit(VillageDetails vill, String action) {
		audit=new AuditTrail();
		audit.setAction(action);
		audit.setActionBy(uservice.getLoggedUserName());
		audit.setActionOn(LocalDateTime.now());
		audit.setIpAddress(getRealClientIp());
		//String villageText="";
		audit.setDetails(vill.getId()+"-"+vill.getVillage().getVillageName()+",Habitat-"+vill.getHabitat()+", Previous User-"+vill.getEnteredBy().getUserName()+", Previous Entry Date-"+vill.getEnteredOn());
		auditservice.updateAudit(audit);
	}
	public void saveAudit(String action, String details) {
		audit=new AuditTrail();
		audit.setAction(action);
		audit.setActionBy(uservice.getLoggedUserName());
		audit.setActionOn(LocalDateTime.now());
		audit.setDetails(details);
		audit.setIpAddress(getRealClientIp());
		auditservice.updateAudit(audit);
	}
	public void saveLoginAudit(String action, String details) {
		audit=new AuditTrail();
		audit.setAction(action);
		audit.setActionOn(LocalDateTime.now());
		audit.setDetails(details);
		audit.setIpAddress(getRealClientIp());
		auditservice.updateAudit(audit);
	}
}
