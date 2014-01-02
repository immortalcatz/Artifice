package shukaro.artifice.block.decorative;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import shukaro.artifice.ArtificeConfig;
import shukaro.artifice.ArtificeCore;
import shukaro.artifice.block.BlockArtifice;
import shukaro.artifice.gui.ArtificeCreativeTab;
import shukaro.artifice.net.Packets;
import shukaro.artifice.render.IconHandler;
import shukaro.artifice.render.TextureHandler;
import shukaro.artifice.render.connectedtexture.ConnectedTextureBase;
import shukaro.artifice.render.connectedtexture.ConnectedTextures;
import shukaro.artifice.render.connectedtexture.schemes.SolidConnectedTexture;
import shukaro.artifice.util.BlockCoord;
import shukaro.artifice.util.ChunkCoord;
import shukaro.artifice.util.PacketWrapper;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMarble extends BlockArtifice
{
    private Icon[] icons = new Icon[ArtificeCore.rocks.length];
    
    public BlockMarble(int id)
    {
        super(id, Material.rock);
        setCreativeTab(ArtificeCreativeTab.main);
        setHardness(1.5F);
        setResistance(10.0F);
        setUnlocalizedName("artifice.marble");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int i, CreativeTabs tabs, List list)
    {
        for (int j = 0; j < ArtificeCore.rocks.length; j++)
        {
            list.add(new ItemStack(i, 1, j));
        }
    }
    
    @Override
    public int damageDropped(int meta)
    {
        return meta == 0 ? 1 : meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister reg)
    {
    	ArtificeConfig.registerConnectedTextures(reg);
        icons[0] = IconHandler.registerSingle(reg, "marble", "marble");
        icons[1] = IconHandler.registerSingle(reg, "cobblestone", "marble");
        icons[2] = IconHandler.registerSingle(reg, "bricks", "marble");
        icons[5] = IconHandler.registerSingle(reg, "chiseled", "marble");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        if (meta >= ArtificeCore.rocks.length)
            meta = 0;
        if (meta == 3)
        	return ConnectedTextures.MarblePaver.textureList[0];
        if (meta == 4)
        	return ConnectedTextures.MarbleAntipaver.textureList[0];
        else
            return icons[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess access, int x, int y, int z, int side)
    {
        int meta = access.getBlockMetadata(x, y, z);
        if (meta > ArtificeCore.rocks.length)
            meta = 0;
        if (meta == 3 || meta == 4)
        {
        	BlockCoord coord = new BlockCoord(x, y, z);
        	if (!ArtificeCore.textureCache.containsKey(coord))
        		TextureHandler.updateTexture(coord);
        	
        	if (ArtificeCore.textureCache.get(coord) == null)
        		return this.getIcon(side, meta);
        	if (TextureHandler.getConnectedTexture(this.getIcon(side, meta)) != null)
        		return TextureHandler.getConnectedTexture(this.getIcon(side, meta)).textureList[ArtificeCore.textureCache.get(coord)[side]];
        	return this.getIcon(side, meta);
        }
        else
            return icons[meta];
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID)
    {
    	int meta = world.getBlockMetadata(x, y, z);
    	BlockCoord c = new BlockCoord(x, y, z);
    	if (c.getBlock(world) != null && (meta == 3 || meta == 4))
    	{
	    	TextureHandler.updateTexture(c);
	    	for (BlockCoord n : c.getAdjacent())
	    		TextureHandler.updateTexture(n);
    	}
    }
}
