package vazkii.arl.block;

import net.minecraft.world.level.block.Block;
import vazkii.arl.util.RegistryHelper;

public class BasicBlock extends Block {

	public BasicBlock(String regname, Properties properties) {
		super(properties);
		
		RegistryHelper.registerBlock(this, regname);
	}

}
