// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.paradice;

import org.terasology.engine.utilities.procedural.BrownianNoise;
import org.terasology.engine.utilities.procedural.PerlinNoise;
import org.terasology.engine.utilities.procedural.SubSampledNoise;
import org.terasology.engine.world.generation.FacetProvider;
import org.terasology.engine.world.generation.GeneratingRegion;
import org.terasology.engine.world.generation.Produces;
import org.terasology.engine.world.generation.facets.SurfaceTemperatureFacet;
import org.terasology.math.geom.Vector2f;

@Produces(SurfaceTemperatureFacet.class)
public class FrozenSurfaceTemperatureProvider implements FacetProvider {
    private static final int SAMPLE_RATE = 4;

    private SubSampledNoise temperatureNoise;

    @Override
    public void setSeed(long seed) {
        temperatureNoise = new SubSampledNoise(new BrownianNoise(new PerlinNoise(seed + 5), 8), new Vector2f(0.0005f,
                0.0005f), SAMPLE_RATE);
    }

    @Override
    public void process(GeneratingRegion region) {
        SurfaceTemperatureFacet facet = new SurfaceTemperatureFacet(region.getRegion(),
                region.getBorderForFacet(SurfaceTemperatureFacet.class));
        float[] noise = this.temperatureNoise.noise(facet.getWorldRegion());

        for (int i = 0; i < noise.length; ++i) {
            noise[i] = 0;
        }

        facet.set(noise);
        region.setRegionFacet(SurfaceTemperatureFacet.class, facet);
    }
}
