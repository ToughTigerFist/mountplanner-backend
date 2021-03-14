package com.kjk.mountplanner.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.kjk.mountplanner.models.Mount;
import com.kjk.mountplanner.repositories.MountRepository;
import com.kjk.mountplanner.services.BnetService;

public class MountDao {

	@Autowired
	BnetService bnet;

	@Autowired
	MountRepository mountRepo;

	public List<Mount> getUnobtainedMounts(String charName, String server) {
		List<Mount> mountsObtained = new ArrayList();
		List<Mount> missingMounts = new ArrayList();
		List<String> obtainedMountNames = new ArrayList();

		try {
			mountsObtained = bnet.getMountsForCharacter(charName, server);
			for (int i = 0; i < mountsObtained.size(); i++) {
				obtainedMountNames.add(mountsObtained.get(i).getName());
			}
			missingMounts = mountRepo.findByNameNotIn(obtainedMountNames);
		} catch (Exception e) {

		}

		return missingMounts;

	}

}
