package vazkii.arl.block;

import net.minecraft.block.Block;
import vazkii.arl.util.RegistryHelper;

import net.minecraft.block.AbstractBlock.Properties;

public class BasicBlock extends Block {

	public BasicBlock(String regname, Properties properties) {
		super(properties);
		
		RegistryHelper.registerBlock(this, regname);
	}

}
