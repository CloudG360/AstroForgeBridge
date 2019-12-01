package fun.mooncraftgames.luna.astroforgebridge;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

@Mod(
        modid = AstroForgeBridge.MOD_ID,
        name = AstroForgeBridge.MOD_NAME,
        version = AstroForgeBridge.VERSION,
        acceptableRemoteVersions = "*",
        serverSideOnly = true
)
public class AstroForgeBridge {

    public static final String MOD_ID = "astrofb";
    public static final String MOD_NAME = "AstroForgeBridge";
    public static final String VERSION = "1.0.0";

    public static boolean digBlock(Object p, Object istack, int x, int y, int z){
        try {
            EntityPlayerMP player = (EntityPlayerMP) p;
            ItemStack itemStack = (ItemStack) istack;
            BlockPos loc = new BlockPos(x, y, z);

            IBlockState state = player.getServerWorld().getBlockState(loc);
            boolean hasTileEntity = state.getBlock().hasTileEntity(state);
            if (!canBreakBlock(player, x, y, z)) return false;
            state.getBlock().harvestBlock(player.getServerWorld(), player, new BlockPos(x, y, z), state, hasTileEntity ? player.getServerWorld().getTileEntity(loc) : null, itemStack);
            return true;
            /*if(state.getBlock().canHarvestBlock(player.getServerWorld(), loc, player)) {
                player.getServerWorld().setBlockState(loc, Blocks.AIR.getDefaultState());
                if(hasTileEntity){
                    //player.getServerWorld().getTileEntity(loc)
                    // Drop stuff inside of chest?
                }
                if (state.getBlock().canSilkHarvest(player.getServerWorld(), loc, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) > 0) {
                    java.util.List<ItemStack> items = new java.util.ArrayList<ItemStack>();
                    ItemStack itemstack = getSilkDrop(state);
                    if (!itemstack.isEmpty()) { items.add(itemstack); }
                    for (ItemStack item : items) { Block.spawnAsEntity(player.getServerWorld(), loc, item); }
                } else {
                    //May need harvesters? oh well
                    int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack);
                    dropBlock(player, player.getHeldItemMainhand(), loc.getX(), loc.getY(), loc.getZ());
                }
            }*/
        } catch (Exception err){
            err.printStackTrace();
            return false;
        }
    }

    public static boolean canBreakBlock(Object p, int x, int y, int z){
        EntityPlayerMP player = (EntityPlayerMP) p;
        BlockPos loc = new BlockPos(x, y, z);
        IBlockState state = player.getServerWorld().getBlockState(loc);
        return state.getBlock().canHarvestBlock(player.getServerWorld(), loc, player);
    }

    public static void dropBlock(Object p, Object t, int x, int y, int z){
        try {
            EntityPlayerMP player = (EntityPlayerMP) p;
            ItemStack tool = (ItemStack) t;
            BlockPos pos = new BlockPos(x, y, z);
            IBlockState block = player.getServerWorld().getBlockState(pos);

            if (block.getBlock().canSilkHarvest(player.getServerWorld(), pos, block, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
                java.util.List<ItemStack> items = new java.util.ArrayList<ItemStack>();
                ItemStack itemstack = getSilkDrop(block);
                if (!itemstack.isEmpty()) { items.add(itemstack); }
                for (ItemStack item : items) {
                    Block.spawnAsEntity(player.getServerWorld(), pos, item);
                }
            } else {
                int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
                List<ItemStack> drops = block.getBlock().getDrops(player.getServerWorld(), pos, block, i); // use the old method until it gets removed, for backward compatibility
                for (ItemStack drop : drops)
                {
                    Block.spawnAsEntity(player.getServerWorld(), pos, drop.copy());
                }
            }
        } catch (Exception err){
            err.printStackTrace();
        }
    }

    private static ItemStack getSilkDrop(IBlockState state){
        Item item = Item.getItemFromBlock(state.getBlock());
        int i = 0;
        if (item.getHasSubtypes()) { i = state.getBlock().getMetaFromState(state); }
        return new ItemStack(item, 1, i);
    }

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static AstroForgeBridge INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {

    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {

    }

    /**
     * This is a special class that listens to registry events, to allow creation of mod blocks and items at the proper time.
     */
    @Mod.EventBusSubscriber
    public static class ObjectRegistryHandler {
        /**
         * Listen for the register event for creating custom items
         */
        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
           /*
             event.getRegistry().register(new ItemBlock(Blocks.myBlock).setRegistryName(MOD_ID, "myBlock"));
             event.getRegistry().register(new MySpecialItem().setRegistryName(MOD_ID, "mySpecialItem"));
            */
        }

        /**
         * Listen for the register event for creating custom blocks
         */
        @SubscribeEvent
        public static void addBlocks(RegistryEvent.Register<Block> event) {
           /*
             event.getRegistry().register(new MySpecialBlock().setRegistryName(MOD_ID, "mySpecialBlock"));
            */
        }
    }
    /* EXAMPLE ITEM AND BLOCK - you probably want these in separate files
    public static class MySpecialItem extends Item {

    }

    public static class MySpecialBlock extends Block {

    }
    */
}
