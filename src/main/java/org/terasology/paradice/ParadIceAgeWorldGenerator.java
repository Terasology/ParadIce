// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.paradice;

import org.terasology.coreworlds.generator.facetProviders.BiomeProvider;
import org.terasology.coreworlds.generator.facetProviders.DefaultFloraProvider;
import org.terasology.coreworlds.generator.facetProviders.PerlinBaseSurfaceProvider;
import org.terasology.coreworlds.generator.facetProviders.PerlinHillsAndMountainsProvider;
import org.terasology.coreworlds.generator.facetProviders.PerlinHumidityProvider;
import org.terasology.coreworlds.generator.facetProviders.PerlinOceanProvider;
import org.terasology.coreworlds.generator.facetProviders.PerlinRiverProvider;
import org.terasology.coreworlds.generator.facetProviders.PlateauProvider;
import org.terasology.coreworlds.generator.facetProviders.SeaLevelProvider;
import org.terasology.coreworlds.generator.facetProviders.SurfaceToDensityProvider;
import org.terasology.engine.core.SimpleUri;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.logic.spawner.FixedSpawner;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.generation.BaseFacetedWorldGenerator;
import org.terasology.engine.world.generation.WorldBuilder;
import org.terasology.engine.world.generator.RegisterWorldGenerator;
import org.terasology.engine.world.generator.plugin.WorldGeneratorPluginLibrary;
import org.terasology.math.geom.ImmutableVector2i;
import org.terasology.math.geom.Vector3f;
import org.terasology.paradice.trees.OnlyPalmTreeProvider;
import org.terasology.paradice.trees.PalmTreeRasterizer;

/**
 *
 */
@RegisterWorldGenerator(id = "paradice", displayName = "ParadIce", description = "Chilly palms")
public class ParadIceAgeWorldGenerator extends BaseFacetedWorldGenerator {

    private final FixedSpawner spawner = new FixedSpawner(0, 0);

    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;

    public ParadIceAgeWorldGenerator(SimpleUri uri) {
        super(uri);
    }

    @Override
    public Vector3f getSpawnPosition(EntityRef entity) {
        return spawner.getSpawnPosition(getWorld(), entity);
    }

    @Override
    protected WorldBuilder createWorld() {
        int seaLevel = 64;
        ImmutableVector2i spawnPos = new ImmutableVector2i(0, 0); // as used by the spawner

        return new WorldBuilder(worldGeneratorPluginLibrary)
                .setSeaLevel(seaLevel)
                .addProvider(new SeaLevelProvider(seaLevel))
                .addProvider(new PerlinHumidityProvider())
                .addProvider(new FrozenSurfaceTemperatureProvider())
                .addProvider(new PerlinBaseSurfaceProvider())
                .addProvider(new PerlinRiverProvider())
                .addProvider(new PerlinOceanProvider())
                .addProvider(new PerlinHillsAndMountainsProvider())
                .addProvider(new BiomeProvider())
                .addProvider(new SurfaceToDensityProvider())
                .addProvider(new DefaultFloraProvider())
                .addProvider(new OnlyPalmTreeProvider())
                .addProvider(new PlateauProvider(spawnPos, seaLevel + 4, 10, 30))
                //.addRasterizer(new GroundRasterizer(blockManager))
                .addRasterizer(new ParadIceRasterizer())
                .addPlugins()
                .addRasterizer(new PalmTreeRasterizer());
    }
}
