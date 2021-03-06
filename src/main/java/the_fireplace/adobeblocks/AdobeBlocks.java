package the_fireplace.adobeblocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import the_fireplace.adobeblocks.blocks.AdobeBlock;
import the_fireplace.adobeblocks.blocks.AdobeBricks;
import the_fireplace.adobeblocks.blocks.AdobeDoor;
import the_fireplace.adobeblocks.blocks.AdobeDoubleSlab;
import the_fireplace.adobeblocks.blocks.AdobeFurnace;
import the_fireplace.adobeblocks.blocks.AdobeGlass;
import the_fireplace.adobeblocks.blocks.AdobeHalfSlab;
import the_fireplace.adobeblocks.blocks.AdobeMixtureBlock;
import the_fireplace.adobeblocks.blocks.AdobePane;
import the_fireplace.adobeblocks.blocks.AdobeStairs;
import the_fireplace.adobeblocks.blocks.AdobeTile;
import the_fireplace.adobeblocks.blocks.AdobeWall;
import the_fireplace.adobeblocks.blocks.Beam;
import the_fireplace.adobeblocks.blocks.ItemBlockAdobeSlab;
import the_fireplace.adobeblocks.entity.projectile.EntityThrowingStone;
import the_fireplace.adobeblocks.entity.tile.TileEntityAdobeFurnace;
import the_fireplace.adobeblocks.handlers.AdobeBlocksGuiHandler;
import the_fireplace.adobeblocks.handlers.DispenseBehaviorThrowingStone;
import the_fireplace.adobeblocks.items.AdobeAxe;
import the_fireplace.adobeblocks.items.AdobeHoe;
import the_fireplace.adobeblocks.items.AdobePickaxe;
import the_fireplace.adobeblocks.items.AdobeShovel;
import the_fireplace.adobeblocks.items.AdobeSword;
import the_fireplace.adobeblocks.items.ItemAdobeBlock;
import the_fireplace.adobeblocks.items.ItemAdobeDoor;
import the_fireplace.adobeblocks.items.ThrowingStone;
import the_fireplace.adobeblocks.proxy.CommonProxy;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber
@Mod(modid = AdobeBlocks.MODID, name = AdobeBlocks.MODNAME, updateJSON = "http://thefireplace.bitnamiapp.com/jsons/adobeblocks.json", acceptedMinecraftVersions = "[1.12,1.13)")
public class AdobeBlocks {
	@Instance(AdobeBlocks.MODID)
	public static AdobeBlocks instance;

	public static final String MODID = "adobeblocks";
	public static final String MODNAME = "Adobe Blocks 2";

	@SidedProxy(clientSide = "the_fireplace.adobeblocks.proxy.ClientProxy", serverSide = "the_fireplace.adobeblocks.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static ToolMaterial adobeTool = EnumHelper.addToolMaterial("adobe", 1, 177, 2.0F, 1.0F, 15);

	public static final CreativeTabs TabAdobeBlocks = new TabAdobeBlocks("adobe_blocks");
	public static final Material adobe = new Material(MapColor.ADOBE);

	public static final Block adobe_mixture_block = new AdobeMixtureBlock();
	public static final Block adobe_tile = new AdobeTile();
	public static final Block adobe_bricks = new AdobeBricks();
	public static final Block adobe_furnace = new AdobeFurnace(false).setUnlocalizedName("adobe_furnace").setCreativeTab(TabAdobeBlocks);
	public static final Block adobe_wall = new AdobeWall();
	public static final Block lit_adobe_furnace = new AdobeFurnace(true).setUnlocalizedName("lit_adobe_furnace").setLightLevel(0.875F);
	public static final Block adobe_stairs = new AdobeStairs();
	public static final AdobeHalfSlab adobe_slab = new AdobeHalfSlab();
	public static final AdobeDoubleSlab adobe_double_slab = new AdobeDoubleSlab();
	public static final Block adobe_glass = new AdobeGlass();
	public static final Block adobe_door_internal = new AdobeDoor();
	public static final Block adobe_glass_pane = new AdobePane().setUnlocalizedName("adobe_glass_pane").setHardness(0.3F);
	public static final Block oak_beam = new Beam(Material.WOOD).setUnlocalizedName("oak_beam").setCreativeTab(AdobeBlocks.TabAdobeBlocks).setHardness(2.0F).setResistance(5.0F);
	public static final Block birch_beam = new Beam(Material.WOOD).setUnlocalizedName("birch_beam").setCreativeTab(AdobeBlocks.TabAdobeBlocks).setHardness(2.0F).setResistance(5.0F);
	public static final Block spruce_beam = new Beam(Material.WOOD).setUnlocalizedName("spruce_beam").setCreativeTab(AdobeBlocks.TabAdobeBlocks).setHardness(2.0F).setResistance(5.0F);
	public static final Block jungle_beam = new Beam(Material.WOOD).setUnlocalizedName("jungle_beam").setCreativeTab(AdobeBlocks.TabAdobeBlocks).setHardness(2.0F).setResistance(5.0F);
	public static final Block dark_oak_beam = new Beam(Material.WOOD).setUnlocalizedName("dark_oak_beam").setCreativeTab(AdobeBlocks.TabAdobeBlocks).setHardness(2.0F).setResistance(5.0F);
	public static final Block acacia_beam = new Beam(Material.WOOD).setUnlocalizedName("acacia_beam").setCreativeTab(AdobeBlocks.TabAdobeBlocks).setHardness(2.0F).setResistance(5.0F);
	public static final Block adobe_block = new AdobeBlock();

	public static final Item adobe_mixture = new Item().setUnlocalizedName("adobe_mixture").setCreativeTab(TabAdobeBlocks);
	public static final Item adobe_brick = new Item().setUnlocalizedName("adobe_brick").setCreativeTab(TabAdobeBlocks);
	public static final Item stone_stick = new Item().setUnlocalizedName("stone_stick").setCreativeTab(TabAdobeBlocks);
	public static final Item adobe_capsule = new Item() {
		@Override
		public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
			RayTraceResult movingobjectposition = this.rayTrace(worldIn, playerIn, true);
			if (movingobjectposition == null) return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(hand));
			else {
				if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK) {
					BlockPos blockpos = movingobjectposition.getBlockPos();
					if (!worldIn.isBlockModifiable(playerIn, blockpos))
						return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(hand));
					if (!playerIn.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, playerIn.getHeldItem(hand)))
						return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(hand));
					IBlockState iblockstate = worldIn.getBlockState(blockpos);
					Material material = iblockstate.getMaterial();
					if (material == Material.WATER && iblockstate.getValue(BlockLiquid.LEVEL) == 0) {
						return new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(filled_adobe_capsule, playerIn.getHeldItem(hand).getCount()));
					}
				}
			}
			return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(hand));
		}
	}.setUnlocalizedName("adobe_capsule").setCreativeTab(TabAdobeBlocks);
	public static final Item filled_adobe_capsule = new Item().setUnlocalizedName("filled_adobe_capsule").setCreativeTab(TabAdobeBlocks);
	public static final Item adobe_door = new ItemAdobeDoor(adobe_door_internal);
	public static final Item adobe_sword = new AdobeSword();
	public static final Item adobe_pickaxe = new AdobePickaxe();
	public static final Item adobe_axe = new AdobeAxe();
	public static final Item adobe_shovel = new AdobeShovel();
	public static final Item adobe_hoe = new AdobeHoe();
	public static final Item throwing_stone = new ThrowingStone();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new AdobeBlocksGuiHandler());
		GameRegistry.registerTileEntity(TileEntityAdobeFurnace.class, "adobe_furnace");

		int eid = -1;
		EntityRegistry.registerModEntity(new ResourceLocation("adobeblocks:adobe_thrown_stone"), EntityThrowingStone.class, "adobe_thrown_stone", ++eid, instance, 64, 10, true);
		proxy.registerRenderers();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(throwing_stone, new DispenseBehaviorThrowingStone());
		GameRegistry.addSmelting(adobe_mixture, new ItemStack(adobe_brick), 0.3F);

		OreDictionary.registerOre("stickStone", stone_stick);
		OreDictionary.registerOre("rodStone", stone_stick);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void modelRegister(ModelRegistryEvent e) {
		registerItemRenders();
	}
	
	private static void registerItemRenders() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_mixture_block), 0, new ModelResourceLocation(MODID + ":adobe_mixture_block", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_tile), 0, new ModelResourceLocation(MODID + ":adobe_tile", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_bricks), 0, new ModelResourceLocation(MODID + ":adobe_bricks", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_furnace), 0, new ModelResourceLocation(MODID + ":adobe_furnace", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(lit_adobe_furnace), 0, new ModelResourceLocation(MODID + ":adobe_furnace", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_wall), 0, new ModelResourceLocation(MODID + ":adobe_wall", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_stairs), 0, new ModelResourceLocation(MODID + ":adobe_stairs", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_slab), 0, new ModelResourceLocation(MODID + ":adobe_slab", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_glass), 0, new ModelResourceLocation(MODID + ":adobe_glass", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_glass_pane), 0, new ModelResourceLocation(MODID + ":adobe_glass_pane", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(oak_beam), 0, new ModelResourceLocation(MODID + ":oak_beam", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(spruce_beam), 0, new ModelResourceLocation(MODID + ":spruce_beam", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(jungle_beam), 0, new ModelResourceLocation(MODID + ":jungle_beam", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(birch_beam), 0, new ModelResourceLocation(MODID + ":birch_beam", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(dark_oak_beam), 0, new ModelResourceLocation(MODID + ":dark_oak_beam", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(acacia_beam), 0, new ModelResourceLocation(MODID + ":acacia_beam", "inventory"));
		ModelLoader.registerItemVariants(Item.getItemFromBlock(adobe_block),
				new ModelResourceLocation(MODID + ":after_effects", "inventory"),
				new ModelResourceLocation(MODID + ":audition", "inventory"),
				new ModelResourceLocation(MODID + ":bridge", "inventory"),
				new ModelResourceLocation(MODID + ":dreamweaver", "inventory"),
				new ModelResourceLocation(MODID + ":encore", "inventory"),
				new ModelResourceLocation(MODID + ":fireworks", "inventory"),
				new ModelResourceLocation(MODID + ":flash", "inventory"),
				new ModelResourceLocation(MODID + ":flash_builder", "inventory"),
				new ModelResourceLocation(MODID + ":illustrator", "inventory"),
				new ModelResourceLocation(MODID + ":indesign", "inventory"),
				new ModelResourceLocation(MODID + ":lightroom", "inventory"),
				new ModelResourceLocation(MODID + ":photoshop", "inventory"),
				new ModelResourceLocation(MODID + ":prelude", "inventory"),
				new ModelResourceLocation(MODID + ":premier_pro", "inventory"),
				new ModelResourceLocation(MODID + ":speedgrade", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_block), 0, new ModelResourceLocation(MODID + ":after_effects", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_block), 1, new ModelResourceLocation(MODID + ":audition", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_block), 2, new ModelResourceLocation(MODID + ":bridge", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_block), 3, new ModelResourceLocation(MODID + ":dreamweaver", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_block), 4, new ModelResourceLocation(MODID + ":encore", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_block), 5, new ModelResourceLocation(MODID + ":fireworks", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_block), 6, new ModelResourceLocation(MODID + ":flash", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_block), 7, new ModelResourceLocation(MODID + ":flash_builder", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_block), 8, new ModelResourceLocation(MODID + ":illustrator", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_block), 9, new ModelResourceLocation(MODID + ":indesign", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_block), 10, new ModelResourceLocation(MODID + ":lightroom", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_block), 11, new ModelResourceLocation(MODID + ":photoshop", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_block), 12, new ModelResourceLocation(MODID + ":prelude", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_block), 13, new ModelResourceLocation(MODID + ":premier_pro", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(adobe_block), 14, new ModelResourceLocation(MODID + ":speedgrade", "inventory"));

		ModelLoader.setCustomModelResourceLocation(adobe_mixture, 0, new ModelResourceLocation(MODID + ":adobe_mixture", "inventory"));
		ModelLoader.setCustomModelResourceLocation(adobe_brick, 0, new ModelResourceLocation(MODID + ":adobe_brick", "inventory"));
		ModelLoader.setCustomModelResourceLocation(adobe_sword, 0, new ModelResourceLocation(MODID + ":adobe_sword", "inventory"));
		ModelLoader.setCustomModelResourceLocation(adobe_pickaxe, 0, new ModelResourceLocation(MODID + ":adobe_pickaxe", "inventory"));
		ModelLoader.setCustomModelResourceLocation(adobe_axe, 0, new ModelResourceLocation(MODID + ":adobe_axe", "inventory"));
		ModelLoader.setCustomModelResourceLocation(adobe_shovel, 0, new ModelResourceLocation(MODID + ":adobe_shovel", "inventory"));
		ModelLoader.setCustomModelResourceLocation(adobe_hoe, 0, new ModelResourceLocation(MODID + ":adobe_hoe", "inventory"));
		ModelLoader.setCustomModelResourceLocation(stone_stick, 0, new ModelResourceLocation(MODID + ":stone_stick", "inventory"));
		ModelLoader.setCustomModelResourceLocation(adobe_door, 0, new ModelResourceLocation(MODID + ":adobe_door", "inventory"));
		ModelLoader.setCustomModelResourceLocation(throwing_stone, 0, new ModelResourceLocation(MODID + ":adobe_throwing_stone", "inventory"));
		ModelLoader.setCustomModelResourceLocation(adobe_capsule, 0, new ModelResourceLocation(MODID + ":adobe_capsule", "inventory"));
		ModelLoader.setCustomModelResourceLocation(filled_adobe_capsule, 0, new ModelResourceLocation(MODID + ":filled_adobe_capsule", "inventory"));
	}

	private static IForgeRegistry<Item> itemRegistry = null;
	private static IForgeRegistry<Block> blockRegistry = null;
	
	public static void registerItems(Item item) {
		itemRegistry.register(item.setRegistryName(item.getUnlocalizedName().substring(5)));
	}

	public static void registerItemForBlock(Block block) {
		itemRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}
	
	
	public static void registerBlock(Block block) {
		blockRegistry.register(block.setRegistryName(block.getUnlocalizedName().substring(5)));
	}
	
	public static void registerItemBlock(ItemBlock itemBlock) {
		itemRegistry.register(itemBlock.setRegistryName(itemBlock.getBlock().getUnlocalizedName().substring(5)));
	}
	
	@SubscribeEvent
	public static void itemRegistry(RegistryEvent.Register<Item> event) {
		itemRegistry = event.getRegistry();
		
		itemRegistry.register(new ItemBlockAdobeSlab(adobe_slab, adobe_slab, adobe_double_slab, false).setRegistryName("adobe_slab"));
		itemRegistry.register(new ItemBlockAdobeSlab(adobe_double_slab, adobe_slab, adobe_double_slab, true).setRegistryName("double_adobe_slab"));
		
		registerItems(adobe_mixture);
		registerItems(adobe_brick);
		registerItems(adobe_sword);
		registerItems(adobe_pickaxe);
		registerItems(adobe_axe);
		registerItems(adobe_shovel);
		registerItems(adobe_capsule);
		registerItems(filled_adobe_capsule);
		registerItems(stone_stick);
		registerItems(adobe_door);
		registerItems(throwing_stone);
		registerItems(adobe_hoe);
		
		registerItemForBlock(adobe_mixture_block);
		registerItemForBlock(adobe_tile);
		registerItemForBlock(adobe_bricks);
		registerItemForBlock(adobe_furnace);
		registerItemForBlock(lit_adobe_furnace);
		registerItemForBlock(adobe_wall);
		registerItemForBlock(adobe_stairs);
		registerItemForBlock(adobe_glass);
		registerItemForBlock(adobe_door_internal);
		registerItemForBlock(adobe_glass_pane);
		registerItemForBlock(oak_beam);
		registerItemForBlock(birch_beam);
		registerItemForBlock(spruce_beam);
		registerItemForBlock(jungle_beam);
		registerItemForBlock(dark_oak_beam);
		registerItemForBlock(acacia_beam);
		registerItemBlock(new ItemAdobeBlock(adobe_block));
		
	}
	
	@SubscribeEvent
	public static void blockRegistry(RegistryEvent.Register<Block> event) {
		blockRegistry = event.getRegistry();
		
		blockRegistry.register(adobe_slab.setRegistryName("adobe_slab"));
		blockRegistry.register(adobe_double_slab.setRegistryName("double_adobe_slab"));
		
		registerBlock(adobe_mixture_block);
		registerBlock(adobe_tile);
		registerBlock(adobe_bricks);
		registerBlock(adobe_furnace);
		registerBlock(lit_adobe_furnace);
		registerBlock(adobe_wall);
		registerBlock(adobe_stairs);
		registerBlock(adobe_glass);
		registerBlock(adobe_door_internal);
		registerBlock(adobe_glass_pane);
		registerBlock(oak_beam);
		registerBlock(birch_beam);
		registerBlock(spruce_beam);
		registerBlock(jungle_beam);
		registerBlock(dark_oak_beam);
		registerBlock(acacia_beam);
		registerBlock(adobe_block);
	}
}
