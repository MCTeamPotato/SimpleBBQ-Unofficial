package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.util.RLUtils;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.models.generators.block.BlockStateProvider;
import net.minecraft.data.PackOutput;

public class SimpleBBQBlockStateProvider extends BlockStateProvider {
    public SimpleBBQBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SimpleBBQ.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(SimpleBBQRegistry.GRILL_BLOCK, this.models().getExistingFile(RLUtils.createRL("block/grill")));
        this.simpleBlock(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK,
                this.models().cube("skewering_table",
                        RLUtils.createRL("block/skewering_table_bottom"),
                        RLUtils.createVanillaRL("block/smooth_stone"),
                        RLUtils.createRL("block/skewering_table_front"),
                        RLUtils.createRL("block/skewering_table_front"),
                        RLUtils.createRL("block/skewering_table_side"),
                        RLUtils.createRL("block/skewering_table_side")
                ).texture("particle", RLUtils.createRL("block/skewering_table_front"))
        );
    }
}
