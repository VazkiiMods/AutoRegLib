package vazkii.arl.block;

import net.minecraft.block.Block;

public class BasicBlock extends Block {

	public BasicBlock(String regname, Properties properties) {
		super(properties);
		setRegistryName(regname);
	}

}
