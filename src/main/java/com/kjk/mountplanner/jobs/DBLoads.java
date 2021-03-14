package com.kjk.mountplanner.jobs;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kjk.mountplanner.models.Mount;
import com.kjk.mountplanner.models.AppSettings;
import com.kjk.mountplanner.models.Server;
import com.kjk.mountplanner.repositories.MountRepository;
import com.kjk.mountplanner.repositories.ServerRepository;
import com.kjk.mountplanner.services.BnetService;

@EnableScheduling
@Component
public class DBLoads {

	@Autowired
	MountRepository mountRepo;

	@Autowired
	ServerRepository serverRepo;

	@Autowired
	BnetService bnet;

	@Autowired
	MongoTemplate mongoTemplate;

//	@Scheduled(fixedRate = 604800)
//	public void loadMount() throws Exception {
//		try {
//			mongoTemplate.dropCollection("mount");
//			mongoTemplate.createCollection("mount");
//			System.out.print("LOADING MOUNTS...\n");
//			List<Mount> mountsToSave = bnet.getMountIndex();
//			mountRepo.saveAll(mountsToSave);
//			updateLastLoadTime("MOUNT");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	
//	@Scheduled(fixedRate = 604800)
//	public void loadServer() {
//		try {
//			mongoTemplate.dropCollection("server");
//			mongoTemplate.createCollection("server");
//			System.out.print("LOADING SERVERS...\n");
//			List<Server> serversToSave = bnet.getConnectedServers();
//			serverRepo.saveAll(serversToSave);
//			updateLastLoadTime("SERVER");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
	
	private void updateLastLoadTime(String loadCollection) {
		Query query = new Query();

		List<AppSettings> appSettingsList = mongoTemplate.find(query, AppSettings.class);
		AppSettings appSettings = appSettingsList.get(0);

		if (loadCollection.equalsIgnoreCase("MOUNT")) {
			appSettings.setLastMountLoad(LocalDateTime.now().minus(5, ChronoUnit.HOURS));
		} else if (loadCollection.equalsIgnoreCase("SERVER")) {
			appSettings.setLastServerLoad(LocalDateTime.now().minus(5, ChronoUnit.HOURS));
		}

		mongoTemplate.save(appSettings);
		
	}

}
