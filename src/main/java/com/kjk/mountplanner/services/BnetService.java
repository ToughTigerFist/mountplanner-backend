package com.kjk.mountplanner.services;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.Base64;

import com.kjk.mountplanner.models.Mount;
import com.kjk.mountplanner.models.Profile;
import com.kjk.mountplanner.models.Server;

@Service
public class BnetService {

	private String getBnetToken() throws Exception {
		JSONObject object = null;
		try {
			OkHttpClient client = new OkHttpClient();
			String clientId = System.getenv("CLIENT_ID");
			String clientSecret = System.getenv("CLIENT_SECRET");
			String encodedAuth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
			RequestBody body = new MultipartBuilder().type(MultipartBuilder.FORM)
					.addFormDataPart("grant_type", "client_credentials").addFormDataPart("client_id", clientId)
					.addFormDataPart("client_secret", clientSecret).build();
			Request request = new Request.Builder().url("https://us.battle.net/oauth/token").method("POST", body)
					.addHeader("Authorization", "Basic " + encodedAuth).build();
			Response response = client.newCall(request).execute();

			String jsonString = response.body().string();
			object = new JSONObject(jsonString);
		} catch (Exception e) {
			throw (new Exception("Unable to get battle.net token: " + e));
		}

		return (String) object.get("access_token");

	}

	public List<Mount> getMountIndex() throws Exception {
		List<Mount> mounts = new ArrayList<Mount>();
		String access_token = getBnetToken();
		Response response = null;
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url("https://us.api.blizzard.com/data/wow/mount/index?namespace=static-us&locale=en_US")
				.method("GET", null).addHeader("Authorization", "Bearer " + access_token).build();
		try {
			response = client.newCall(request).execute();
			JSONObject jsonObject = new JSONObject(response.body().string());
			JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("mounts"));

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				Mount newMount = new Mount();
				newMount.setName(obj.getString("name"));
				newMount.setId(String.valueOf(obj.getInt("id")));
				mounts.add(newMount);
			}

		} catch (Exception e) {
			throw (new Exception("Unable to get mount index: " + e));
		}

		return mounts;
	}

	public List<Mount> getMountsForCharacter(String charName, String server) throws Exception {
		List<Mount> mounts = new ArrayList<Mount>();
		String access_token = getBnetToken();
		Response response = null;
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url("https://us.api.blizzard.com/profile/wow/character/" + server + "/" + charName
						+ "/collections/mounts?namespace=profile-us&locale=en_US")
				.method("GET", null).addHeader("Authorization", "Bearer " + access_token).build();
		try {
			response = client.newCall(request).execute();
			if (response.code() == 200) {
				JSONObject jsonObject = new JSONObject(response.body().string());
				JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("mounts"));
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					if (obj.getBoolean("is_useable")) {
						Mount newMount = new Mount();
						newMount.setUseable(obj.getBoolean("is_useable"));
						JSONObject innerObj = obj.getJSONObject("mount");
						newMount.setName(innerObj.getString("name"));
						newMount.setId(String.valueOf(innerObj.getInt("id")));
						mounts.add(newMount);
					}

				}
			} else {
				throw new Exception("[" + response.code() + "] Failed to get mounts for character");
			}

		} catch (Exception e) {
			throw new Exception(e);
		}

		return mounts;
	}

	public List<Server> getConnectedServers() throws Exception {
		List<Server> servers = new ArrayList<Server>();
		String access_token = getBnetToken();
		Response response = null;
		OkHttpClient client = new OkHttpClient();
		// https://us.api.blizzard.com/data/wow/realm/index?namespace=dynamic-us&locale=en_US
		Request request = new Request.Builder()
				.url("https://us.api.blizzard.com/data/wow/realm/index?namespace=dynamic-us&locale=en_US")
				.method("GET", null).addHeader("Authorization", "Bearer " + access_token).build();
		try {
			response = client.newCall(request).execute();
			JSONObject jsonObject = new JSONObject(response.body().string());
			JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("realms"));

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				Server newServer = new Server();
				newServer.setName(obj.getString("name"));
				newServer.setSlug(obj.getString("slug"));
				servers.add(newServer);
			}

		} catch (Exception e) {
			throw (new Exception("Unable to get battle.net servers: " + e));
		}

		return servers;
	}

	public Profile getCharacterProfileSummary(String charName, String server) throws Exception {
		Profile profile = new Profile();
		String access_token = getBnetToken();
		Response response = null;
		OkHttpClient client = new OkHttpClient();
		// https://us.api.blizzard.com/profile/wow/character/area-52/lokhano?namespace=profile-us&locale=en_US
		Request request = new Request.Builder()
				.url("https://us.api.blizzard.com/profile/wow/character/" + server + "/" + charName
						+ "?namespace=profile-us&locale=en_US")
				.method("GET", null).addHeader("Authorization", "Bearer " + access_token).build();
		try {
			response = client.newCall(request).execute();
			JSONObject jsonObject = new JSONObject(response.body().string());
			profile.setName(jsonObject.getString("name"));
			profile.setFaction(jsonObject.getJSONObject("faction").getString("name"));
			profile.setRace(jsonObject.getJSONObject("race").getString("name"));
			profile.setCharacter_class(jsonObject.getJSONObject("character_class").getString("name"));
			profile.setActive_spec(jsonObject.getJSONObject("active_spec").getString("name"));
			profile.setRealmName(jsonObject.getJSONObject("realm").getString("name"));
			profile.setRealmSlug(jsonObject.getJSONObject("realm").getString("slug"));
			profile.setGuild(jsonObject.getJSONObject("guild").getString("name"));
			profile.setLevel(jsonObject.getInt("level"));
			profile.setTitle(jsonObject.getJSONObject("active_title").getString("name"));
			profile.setCovenant(
					jsonObject.getJSONObject("covenant_progress").getJSONObject("chosen_covenant").getString("name"));
			profile.setRenown(jsonObject.getJSONObject("covenant_progress").getInt("renown_level"));

		} catch (Exception e) {
			throw (new Exception("Unable to get profile summary " + e));
		}

		return profile;
	}
	public String getCharacterProfileStatus(String charName, String server) throws Exception {
		// https://us.api.blizzard.com/profile/wow/character/area-52/lokhano/status?namespace=profile-us
		// https://us.api.blizzard.com/profile/wow/character/" + server + "/" + charName
		// + "status?namespace=profile-us
		String is_valid = "";
		String access_token = getBnetToken();
		Response response = null;
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url("https://us.api.blizzard.com/profile/wow/character/" + server + "/" + charName
						+ "/status?namespace=profile-us")
				.method("GET", null).addHeader("Authorization", "Bearer " + access_token).build();

		try {
			response = client.newCall(request).execute();
			JSONObject jsonObject = new JSONObject(response.body().string());
			is_valid = String.valueOf(jsonObject.getBoolean("is_valid"));
		} catch (Exception e) {
			throw (new Exception("Unable to get profile status " + e));
		}

		return is_valid;
	}

}
