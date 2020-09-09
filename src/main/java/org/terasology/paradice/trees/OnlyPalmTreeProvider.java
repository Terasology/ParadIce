// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.paradice.trees;

import com.google.common.base.Predicate;
import org.terasology.coreworlds.CoreBiome;
import org.terasology.coreworlds.generator.facetProviders.DefaultTreeProvider;
import org.terasology.coreworlds.generator.facets.BiomeFacet;
import org.terasology.coreworlds.generator.facets.TreeFacet;
import org.terasology.coreworlds.generator.trees.Trees;
import org.terasology.engine.world.generation.Border3D;
import org.terasology.engine.world.generation.Facet;
import org.terasology.engine.world.generation.FacetBorder;
import org.terasology.engine.world.generation.GeneratingRegion;
import org.terasology.engine.world.generation.Produces;
import org.terasology.engine.world.generation.Requires;
import org.terasology.engine.world.generation.facets.SeaLevelFacet;
import org.terasology.engine.world.generation.facets.SurfaceHeightFacet;
import org.terasology.math.geom.Vector3i;

import java.util.List;

@Produces(TreeFacet.class)
@Requires({
        @Facet(value = SeaLevelFacet.class, border = @FacetBorder(sides = 13)),
        @Facet(value = SurfaceHeightFacet.class, border = @FacetBorder(sides = 13 + 1)),
        @Facet(value = BiomeFacet.class, border = @FacetBorder(sides = 13))
})
public class OnlyPalmTreeProvider extends DefaultTreeProvider {

    public OnlyPalmTreeProvider() {
        register(CoreBiome.MOUNTAINS, GenericTrees.palmTree(), 0.02f);
        register(CoreBiome.MOUNTAINS, GenericTrees.palmTree(), 0.02f);
        register(CoreBiome.MOUNTAINS, GenericTrees.palmTree(), 0.02f);

        register(CoreBiome.FOREST, GenericTrees.palmTree(), 0.25f);
        register(CoreBiome.FOREST, GenericTrees.palmTree(), 0.10f);
        register(CoreBiome.FOREST, GenericTrees.palmTree(), 0.10f);
        register(CoreBiome.FOREST, GenericTrees.palmTree(), 0.25f);

        register(CoreBiome.SNOW, GenericTrees.palmTree(), 0.02f);
        register(CoreBiome.SNOW, GenericTrees.palmTree(), 0.02f);
        register(CoreBiome.SNOW, GenericTrees.palmTree(), 0.08f);

        register(CoreBiome.PLAINS, GenericTrees.palmTree(), 0.01f);
        register(CoreBiome.PLAINS, GenericTrees.palmTree(), 0.01f);
        register(CoreBiome.PLAINS, GenericTrees.palmTree(), 0.02f);

        register(CoreBiome.DESERT, Trees.cactus(), 0.04f);
        register(CoreBiome.DESERT, GenericTrees.palmTree(), 0.04f);

        register(CoreBiome.BEACH, GenericTrees.palmTree(), 0.2F);
    }

    @Override
    public void process(GeneratingRegion region) {
        SurfaceHeightFacet surface = region.getRegionFacet(SurfaceHeightFacet.class);
        BiomeFacet biome = region.getRegionFacet(BiomeFacet.class);

        List<Predicate<Vector3i>> filters = getFilters(region);

        // these value are derived from the maximum tree extents as
        // computed by the TreeTests class. Birch is the highest with 32
        // and Pine has 13 radius.
        // These values must be identical in the class annotations.
        int maxRad = 13;
        int maxHeight = 32;
        Border3D borderForTreeFacet = region.getBorderForFacet(TreeFacet.class);
        TreeFacet facet = new TreeFacet(region.getRegion(), borderForTreeFacet.extendBy(1, maxHeight, maxRad));

        populateFacet(facet, surface, biome, filters);

        region.setRegionFacet(TreeFacet.class, facet);
    }
}
