package com.kjk.mountplanner.controllers;

import com.kjk.mountplanner.constants.*;
import com.kjk.mountplanner.models.AppSettings;
import com.kjk.mountplanner.models.Mount;
import com.kjk.mountplanner.models.Profile;
import com.kjk.mountplanner.models.Server;
import com.kjk.mountplanner.repositories.MountRepository;
import com.kjk.mountplanner.services.BnetService;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import java.net.http.HttpResponse;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class MountPlannerController {

	@Autowired
	BnetService bnet;

	@Autowired
	MountRepository mountRepo;

	@Autowired
	MongoTemplate mongoTemplate;

	@GetMapping("/mount/{characterName}/{server}")
	public ResponseEntity<?> getMountsForCharacter(@PathVariable String characterName, @PathVariable String server)
			throws Exception {
		List<Mount> mounts = new ArrayList<Mount>();
		ResponseEntity<?> response = null;
		try {
			mounts = bnet.getMountsForCharacter(characterName.toLowerCase(), server.toLowerCase());
			response = new ResponseEntity<List<Mount>>(mounts, HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<String>("Failed to get mounts: " + e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;

	}

	@GetMapping("/profile/{characterName}/{server}")
	public ResponseEntity<?> getProfileForCharacter(@PathVariable String characterName, @PathVariable String server)
			throws Exception {
		List<Mount> mounts = new ArrayList<Mount>();
		Profile profile = new Profile();
		ResponseEntity<?> response = null;
		try {
			mounts = bnet.getMountsForCharacter(characterName.toLowerCase(), server.toLowerCase());
			profile = bnet.getCharacterProfileSummary(characterName.toLowerCase(), server.toLowerCase());
			profile.setMounts(mounts);
			response = new ResponseEntity<Profile>(profile, HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<String>(
					"Unable to find this profile. Please make sure the spelling was correct: " + e,
					HttpStatus.NOT_FOUND);
		}

		return response;

	}

	@GetMapping("/status/{characterName}/{server}")
	public ResponseEntity<?> getProfileStatus(@PathVariable String characterName, @PathVariable String server)
			throws Exception {
		String is_valid = "";
		ResponseEntity<?> response = null;
		try {
			is_valid = bnet.getCharacterProfileStatus(characterName.toLowerCase(), server.toLowerCase());
			response = new ResponseEntity<>(is_valid, HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<String>(
					"Unable to find this profile. Please make sure the spelling was correct",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;

	}

	@GetMapping("/mount/total")
	public ResponseEntity<Map<String, Integer>> getTotalMounts() {
		Integer total = 0;
		Map<String, Integer> totalRes = new HashMap<String, Integer>();
		ResponseEntity<Map<String, Integer>> response = null;

		total = (int) mountRepo.count();
		totalRes.put("total", total);
		response = new ResponseEntity<Map<String, Integer>>(totalRes, HttpStatus.OK);

		return response;

	}

	@GetMapping("/servers")
	public ResponseEntity<?> getServerList() throws Exception {
		ResponseEntity<?> response = null;
		List<Server> servers = new ArrayList<Server>();

		try {
			servers = bnet.getConnectedServers();
			response = new ResponseEntity<List<Server>>(servers, HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<String>("Unable to get servers: " + e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;

	}

	@GetMapping("/appStatus")
	public ResponseEntity getAppStatus() throws Exception {
		ResponseEntity<?> response = null;
		Query query = new Query();

		List<AppSettings> appSettingsList = mongoTemplate.find(query, AppSettings.class);
		AppSettings appSettings = appSettingsList.get(0);

		if (appSettings.isAppRunning()) {
			response = new ResponseEntity(HttpStatus.OK);
		} else {
			response = new ResponseEntity(appSettings.getOutageMessage(), HttpStatus.SERVICE_UNAVAILABLE);
		}

		return response;

	}

	@GetMapping("/health")
	public ResponseEntity<?> getAppHealth() throws Exception {
		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<Object>("app is healthy", HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<Object>("app unreachable", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;

	}

}
