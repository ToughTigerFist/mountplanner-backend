package com.kjk.mountplanner.models;

import java.util.List;

import org.springframework.data.annotation.Id;

public class Profile {
	@Id
	public String id;
	public String name;
	public String faction;
	public String race;
	public String character_class;
	public String active_spec;
	public String realmName;
	public String realmSlug;
	public String guild;
	public Integer level;
	public String title;
	public String covenant;
	public Integer renown;
	public List<Mount> mounts;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFaction() {
		return faction;
	}
	public void setFaction(String faction) {
		this.faction = faction;
	}
	public String getRace() {
		return race;
	}
	public void setRace(String race) {
		this.race = race;
	}
	public String getCharacter_class() {
		return character_class;
	}
	public void setCharacter_class(String character_class) {
		this.character_class = character_class;
	}
	public String getActive_spec() {
		return active_spec;
	}
	public void setActive_spec(String active_spec) {
		this.active_spec = active_spec;
	}
	public String getRealmName() {
		return realmName;
	}
	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}
	public String getRealmSlug() {
		return realmSlug;
	}
	public void setRealmSlug(String realmSlug) {
		this.realmSlug = realmSlug;
	}
	public String getGuild() {
		return guild;
	}
	public void setGuild(String guild) {
		this.guild = guild;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCovenant() {
		return covenant;
	}
	public void setCovenant(String covenant) {
		this.covenant = covenant;
	}
	public Integer getRenown() {
		return renown;
	}
	public void setRenown(Integer renown) {
		this.renown = renown;
	}
	public List<Mount> getMounts() {
		return mounts;
	}
	public void setMounts(List<Mount> mounts) {
		this.mounts = mounts;
	}
	
	
	
	
	
	
	
}
