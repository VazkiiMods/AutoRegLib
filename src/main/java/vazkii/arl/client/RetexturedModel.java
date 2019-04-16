package vazkii.arl.client;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.IExtendedBlockState;
import vazkii.arl.interf.IStateMapperProvider;

import javax.annotation.Nonnull;
import java.util.List;

public class RetexturedModel extends BakedModelWrapper<IBakedModel> {

	private IModel model;
	private final VertexFormat format;
	private final String textureKey;
	public RetexturedModel(IBakedModel originalModel, IModel model, VertexFormat format, String textureKey) {
		super(originalModel);
		this.model = model;
		this.format = format;
		this.textureKey = textureKey;
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		IBakedModel bakedModel = this.originalModel;
		if(state instanceof IExtendedBlockState) {
			IExtendedBlockState extendedState = (IExtendedBlockState) state;
			String texture = extendedState.getValue(IStateMapperProvider.TEXTURE);
			if(texture != null) {
				IModel retextured = model.retexture(ImmutableMap.of(textureKey, texture));
				bakedModel = retextured.bake(retextured.getDefaultState(), format, ModelLoader.defaultTextureGetter());
			}
		}
		return bakedModel.getQuads(state, side, rand);
	}
}
