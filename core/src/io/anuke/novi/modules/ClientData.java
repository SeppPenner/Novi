package io.anuke.novi.modules;

import io.anuke.novi.Novi;
import io.anuke.novi.entities.basic.Player;
import io.anuke.ucore.modules.Module;

//TODO remove this pointless module, why the heck does it exist?
public class ClientData extends Module<Novi>{
	public Player player;

	public ClientData(){
		player = new Player();
		player.client = true;
	}
}
