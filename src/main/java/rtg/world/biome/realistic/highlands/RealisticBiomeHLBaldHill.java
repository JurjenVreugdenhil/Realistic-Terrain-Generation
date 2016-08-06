package rtg.world.biome.realistic.highlands;

import net.minecraft.world.biome.BiomeGenBase;

import rtg.api.biome.BiomeConfig;
import rtg.world.gen.surface.highlands.SurfaceHLBaldHill;
import rtg.world.gen.terrain.highlands.TerrainHLBaldHill;

public class RealisticBiomeHLBaldHill extends RealisticBiomeHLBase {

    public RealisticBiomeHLBaldHill(BiomeGenBase biome, BiomeConfig config) {

        super(config, biome, BiomeGenBase.river,
            new TerrainHLBaldHill(90f, 180f, 13f, 100f, 38f, 260f, 110f),
            new SurfaceHLBaldHill(config, biome.topBlock, biome.fillerBlock)
        );
    }
}
