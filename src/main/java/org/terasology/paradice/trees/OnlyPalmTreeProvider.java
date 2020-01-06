/*
 * Copyright 2019 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.paradice.trees;

import com.google.common.base.Predicate;
import org.terasology.core.world.CoreBiome;
import org.terasology.core.world.generator.facetProviders.DefaultTreeProvider;
import org.terasology.core.world.generator.facets.BiomeFacet;
import org.terasology.core.world.generator.facets.TreeFacet;
import org.terasology.core.world.generator.trees.Trees;
import org.terasology.math.geom.Vector3i;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetBorder;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.paradice.trees.GenericTrees;

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
