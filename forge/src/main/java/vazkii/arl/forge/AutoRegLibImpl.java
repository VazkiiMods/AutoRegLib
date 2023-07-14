package vazkii.arl.forge;

import vazkii.arl.AutoRegLib;
import net.minecraftforge.fml.common.Mod;

@Mod(AutoRegLib.MOD_ID)
public class AutoRegLibImpl {
    public AutoRegLibImpl() {
        AutoRegLib.init();
    }
}