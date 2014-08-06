package shukaro.artifice.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import shukaro.artifice.ArtificeConfig;
import shukaro.artifice.ArtificeRegistry;

import java.util.List;

public class ArtificeEventHandler
{
    private List<Integer> dimBlacklist;

    @SubscribeEvent
    public void chunkLoad(ChunkDataEvent.Load e)
    {
        if (!ArtificeConfig.enableWorldGen.getBoolean(true))
            return;

        int dim = e.world.provider.dimensionId;
        if (dimBlacklist == null)
            dimBlacklist = ArtificeRegistry.getDimensionBlacklist();
        if (dimBlacklist.contains(dim))
            return;


    }
}
