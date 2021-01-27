package com.dhb.tank.cor;

import com.dhb.tank.comms.ProrertyMgr;
import com.dhb.tank.mode.GameObject;

import java.util.LinkedList;
import java.util.List;

public class ColiderChain implements Colider{

	public ColiderChain() {
		List<String> list  = ProrertyMgr.getList("coliders");
		for(String clazz : list) {
			try {
				this.coliders.add((Colider) Class.forName(clazz).newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private List<Colider> coliders = new LinkedList<>();

	public void add(Colider colider) {
		this.coliders.add(colider);
	}

	@Override
	public boolean colide(GameObject o1, GameObject o2) {
		for(Colider colider : coliders) {
			if(!colider.colide(o1,o2)){
				return true;
			}
		}
		return true;
	}
}
