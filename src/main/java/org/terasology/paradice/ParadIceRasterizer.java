// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.paradice;

import org.terasology.biomesAPI.Biome;
import org.terasology.biomesAPI.BiomeRegistry;
import org.terasology.coreworlds.generator.facets.BiomeFacet;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.chunks.ChunkConstants;
import org.terasology.engine.world.chunks.CoreChunk;
import org.terasology.engine.world.generation.Region;
import org.terasology.engine.world.generation.WorldRasterizer;
import org.terasology.engine.world.generation.facets.DensityFacet;
import org.terasology.engine.world.generation.facets.SeaLevelFacet;
import org.terasology.engine.world.generation.facets.SurfaceDepthFacet;
import org.terasology.engine.world.generation.facets.SurfaceHeightFacet;
import org.terasology.engine.world.generation.facets.SurfaceTemperatureFacet;
import org.terasology.math.TeraMath;
import org.terasology.math.geom.Vector2i;
import org.terasology.math.geom.Vector3i;

public class ParadIceRasterizer implements WorldRasterizer {

    private Block water;
    private Block ice;
    private Block stone;
    private Block snow;
    private Block dirt;
    private BiomeRegistry biomeRegistry;

    @Override
    public void initialize() {
        BlockManager blockManager = CoreRegistry.get(BlockManager.class);
        biomeRegistry = CoreRegistry.get(BiomeRegistry.class);
        stone = blockManager.getBlock("CoreAssets:Stone");
        water = blockManager.getBlock("CoreAssets:Water");
        ice = blockManager.getBlock("CoreAssets:Ice");
        snow = blockManager.getBlock("CoreAssets:Snow");
        dirt = blockManager.getBlock("CoreAssets:Dirt");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        DensityFacet solidityFacet = chunkRegion.getFacet(DensityFacet.class);
        SurfaceHeightFacet surfaceFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
        SurfaceDepthFacet surfaceDepthFacet = chunkRegion.getFacet(SurfaceDepthFacet.class);
        BiomeFacet biomeFacet = chunkRegion.getFacet(BiomeFacet.class);
        SeaLevelFacet seaLevelFacet = chunkRegion.getFacet(SeaLevelFacet.class);
        SurfaceTemperatureFacet surfaceTemperatureFacet = chunkRegion.getFacet(SurfaceTemperatureFacet.class);
        int seaLevel = seaLevelFacet.getSeaLevel();

        Vector2i pos2d = new Vector2i();
        for (Vector3i pos : ChunkConstants.CHUNK_REGION) {
            pos2d.set(pos.x, pos.z);
            int posY = pos.y + chunk.getChunkWorldOffsetY();

            // Check for an optional depth for this layer - if defined stop generating below that level
            if (surfaceDepthFacet != null && posY < surfaceDepthFacet.get(pos2d)) {
                continue;
            }

            Biome biome = biomeFacet.get(pos2d);
            biomeRegistry.setBiome(biome, chunk, pos.x, pos.y, pos.z);

            float density = solidityFacet.get(pos);

            if (density >= 32) {
                chunk.setBlock(pos, stone);
            } else if (density >= 0) {
                int depth = TeraMath.floorToInt(surfaceFacet.get(pos2d)) - posY;
                Block block = getSurfaceBlock(depth, posY,
                        biome,
                        seaLevel);
                chunk.setBlock(pos, block);
            } else {
                // fill up terrain up to sealevel height with water or ice
                if (posY == seaLevel && surfaceTemperatureFacet.get(pos.x, pos.y) <= 0.2F) {
                    chunk.setBlock(pos, ice);
                } else if (posY <= seaLevel) {         // either OCEAN or SNOW
                    chunk.setBlock(pos, water);
//                }
                }
            }
        }
    }

    private Block getSurfaceBlock(int depth, int height, Biome type, int seaLevel) {
        if (depth == 0 && height > seaLevel) {
            // Snow on top
            return snow;
        } else if (depth > 32) {
            // Stone
            return stone;
        } else {
            // Dirt
            return dirt;
        }
    }
}
