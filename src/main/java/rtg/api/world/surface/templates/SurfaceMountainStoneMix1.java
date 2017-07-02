package rtg.api.world.surface.templates;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import rtg.api.config.BiomeConfig;
import rtg.api.util.CliffCalculator;
import rtg.api.util.noise.OpenSimplexNoise;
import rtg.api.world.IRTGWorld;
import rtg.api.world.surface.SurfaceBase;

public class SurfaceMountainStoneMix1 extends SurfaceBase {

    private float min;

    private float sCliff = 1.5f;
    private float sHeight = 60f;
    private float sStrength = 65f;
    private float cCliff = 1.5f;

    private IBlockState mix;
    private float mixHeight;

    public SurfaceMountainStoneMix1(BiomeConfig config, IBlockState top, IBlockState fill, float minCliff, float stoneCliff, float stoneHeight, float stoneStrength, float clayCliff, IBlockState mixBlock, float mixSize) {

        super(config, top, fill);
        min = minCliff;

        sCliff = stoneCliff;
        sHeight = stoneHeight;
        sStrength = stoneStrength;
        cCliff = clayCliff;

        mix = mixBlock;
        mixHeight = mixSize;
    }

    @Override
    public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int z, int depth, IRTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

        Random rand = rtgWorld.rand();
        OpenSimplexNoise simplex = rtgWorld.simplex();
        float c = CliffCalculator.calc(x, z, noise);
        int cliff = 0;
        boolean m = false;

        Block b;
        for (int k = 255; k > -1; k--) {
            b = primer.getBlockState(x, k, z).getBlock();
            if (b == Blocks.AIR) {
                depth = -1;
            }
            else if (b == Blocks.STONE) {
                depth++;

                if (depth == 0) {

                    float p = simplex.noise3(i / 8f, j / 8f, k / 8f) * 0.5f;
                    if (c > min && c > sCliff - ((k - sHeight) / sStrength) + p) {
                        cliff = 1;
                    }
                    if (c > cCliff) {
                        cliff = 2;
                    }

                    if (cliff == 1) {
                        if (rand.nextInt(3) == 0) {

                            primer.setBlockState(x, k, z, hcCobble(rtgWorld, i, j, x, z, k));
                        }
                        else {

                            primer.setBlockState(x, k, z, hcStone(rtgWorld, i, j, x, z, k));
                        }
                    }
                    else if (cliff == 2) {
                        primer.setBlockState(x, k, z, getShadowStoneBlock(rtgWorld, i, j, x, z, k));
                    }
                    else if (k < 63) {
                        if (k < 62) {
                            primer.setBlockState(x, k, z, fillerBlock);
                        }
                        else {
                            primer.setBlockState(x, k, z, topBlock);
                        }
                    }
                    else if (simplex.noise2(i / 12f, j / 12f) > mixHeight) {
                        primer.setBlockState(x, k, z, mix);
                        m = true;
                    }
                    else {
                        primer.setBlockState(x, k, z, topBlock);
                    }
                }
                else if (depth < 6) {
                    if (cliff == 1) {
                        primer.setBlockState(x, k, z, hcStone(rtgWorld, i, j, x, z, k));
                    }
                    else if (cliff == 2) {
                        primer.setBlockState(x, k, z, getShadowStoneBlock(rtgWorld, i, j, x, z, k));
                    }
                    else {
                        primer.setBlockState(x, k, z, fillerBlock);
                    }
                }
            }
        }
    }
}