// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.paradice.trees;

import com.google.common.collect.ImmutableMap;
import org.terasology.coreworlds.generator.trees.TreeGenerator;
import org.terasology.coreworlds.generator.trees.TreeGeneratorLSystem;
import org.terasology.engine.math.LSystemRule;
import org.terasology.engine.world.block.BlockUri;

public final class GenericTrees {
    private GenericTrees() {
        // no instances!
    }

    public static TreeGenerator mapleTree() {
        return new TreeGeneratorLSystem(
                "FFFFFA", ImmutableMap.<Character, LSystemRule>builder()
                .put('A', new LSystemRule("[&FFBFA]////[&BFFFA]////[&FBFFA]", 1.0f))
                .put('B', new LSystemRule("[&FFFA]////[&FFFA]////[&FFFA]", 0.8f)).build(),
                4, (float) Math.toRadians(30))
                .setLeafType(new BlockUri("PlantPack:MapleLeaf"))
                .setBarkType(new BlockUri("PlantPack:MapleTrunk"));
    }

    public static TreeGenerator palmTree() {
        return new PalmTreeGenerator().setLeafType("PlantPack:AcaiPalmLeaf").setTrunkType("PlantPack:AcaiPalmTrunk");
    }
}
